package Game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import other.Message;
import other.SPlayer;


public class Window implements Runnable {
	// Отрисовывается все тут
	private JFrame main_frame;
	private Canvas canvas;
	private JTextArea text_area;
	private JLabel msg;
	private JMenuBar menu;
	private JSplitPane split_pane;
	
	/*
	 * Point задает смещение, а offset - сколько прибавлять к смещение на каждой итерации
	 */
	private Point offset = new Point(500, 0);
	private int offset_x = 0;
	private int offset_y = 0;
	private float scale_map = 1f;
	private float scale_inc = 0f;
	
	// Поток, в котором крутиться window.
	private Thread game_thread;
	
	// флаг на работу
	private boolean running;
	
	// флаг, если надо изменить вывод OpenGL
	private boolean need_update_viewport = false;
	
	// Массив к текстурными координатами.
	private HashMap<Integer , int[]> coord_tex = new HashMap<Integer, int[]>(70);
	private HashMap<Integer , int[]> coord_tex_ship = new HashMap<Integer, int[]>(50);

	// текстуры земли и всего вокруг 
	private Texture sprites;
	private Texture texture_ship;
	byte[][] map;
	
	private volatile HashMap<String, Player> objects = new HashMap<String, Player>();
	private String namePlayer;
	private Game game;
	
	public Window(Game g) {
		game = g;
		// Установка вида GUI под системный тип
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			if (Game.isError) ex.printStackTrace();
		}
		
		// Canvas будет контейнером для OpenGL
		canvas = new Canvas() {
			private static final long serialVersionUID = -1069002023468669595L;
			public void removeNotify() {
				//stopOpenGL();
			}
		};
		canvas.setIgnoreRepaint(true);
		canvas.setBounds(0, 0, 800, 600);
		canvas.setPreferredSize(new Dimension(800, 600));
		canvas.setMinimumSize(new Dimension(320, 240));
		canvas.setVisible(true);
		
		setMainFrame();
		setMenu();
		setListeners();
		setOther();
		System.out.println("Create Window : ");
	}
	
	private void setMainFrame()	{
		// Создание frame для держание всего.
		main_frame = new JFrame();
		main_frame.setTitle("Swing + LWJGL");
		main_frame.setLayout(null);
		main_frame.setBounds(0, 0, 1280, 768);
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Текстовое окно, в которое будут вводить алгоритм
		text_area = new JTextArea();
		text_area.setColumns(35);
		text_area.setLineWrap(true);
		text_area.setFont(new Font("Arial", Font.BOLD, 20));
		text_area.setBounds(0, 0, 300, 200);
		
		// Разделяет frame на две части. В одной будет текст, в другой canvas
		split_pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		split_pane.setBounds(0,0, main_frame.getWidth(), main_frame.getHeight()-50-80);
				
		// Поле для вывода ошибок и сообщений пользователю
		msg = new JLabel("No errors");
		msg.setBounds(0, split_pane.getHeight(), 500, 30);
		msg.setForeground(java.awt.Color.RED);
		msg.setBorder(BorderFactory.createLineBorder(java.awt.Color.black));
		
		JPanel EditPanel = new JPanel();
		EditPanel.setBounds(0, 0, 100, 100);
		
		// Группа из кнопок и текстового поля
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBounds(0, 0, 300, main_frame.getHeight());
		leftPanel.add(text_area, BorderLayout.CENTER);
		
		split_pane.setRightComponent(canvas);
		split_pane.setLeftComponent(leftPanel);
		split_pane.setVisible(true);
		
		main_frame.add(split_pane);
		main_frame.add(msg);
		main_frame.setVisible(true);
		main_frame.setEnabled(true);
	}
	
	private void setListeners()	{
		//При изменении основного окна, меняем и размеры компоненты, который разделяет все.
		main_frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				split_pane.setBounds(0,0, main_frame.getWidth(), main_frame.getHeight()-menu.getHeight()-80);
				msg.setBounds(10, split_pane.getHeight()+5, 500, 30);
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				split_pane.setBounds(0,0, main_frame.getWidth(), main_frame.getHeight()-menu.getHeight()-80);
				msg.setBounds(10, split_pane.getHeight()+5, 500, 30);
			}
		});
		main_frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				game.stop();
				System.out.println("windowClosing");
			}
		});
		
		// Навешиваем слушателей, чтобы openGL изменял свой вид, при изменения окна.
		canvas.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {
				setNeedValidation();
			}
			public void componentResized(ComponentEvent e) {
				setNeedValidation();
			}
			public void componentMoved(ComponentEvent e) {
				setNeedValidation();
			}
			public void componentHidden(ComponentEvent e) {
				setNeedValidation();
			}
		});
	}
	
	private void setOther()	{
		coord_tex.put(0, new int[]{5,0}); // grass
		coord_tex.put(1, new int[]{0,0}); // deep
		coord_tex.put(2, new int[]{1,4}); // beach
		coord_tex.put(3, new int[]{2,3}); // shallow
		coord_tex.put(4, new int[]{0,3}); // grass-beach-in-1
		coord_tex.put(5, new int[]{4,3}); // grass-beach-in-2
		coord_tex.put(6, new int[]{1,3}); // grass-beach-in-3
		coord_tex.put(7, new int[]{5,2}); // grass-beach-in-4
		coord_tex.put(8, new int[]{5,4}); // grass-beach-out-1
		coord_tex.put(9, new int[]{5,3}); // grass-beach-out-2
		coord_tex.put(10, new int[]{7,2}); // grass-beach-out-3
		coord_tex.put(11, new int[]{3,4}); // grass-beach-out-4
		coord_tex.put(12, new int[]{6,2}); // grass-beach-1
		coord_tex.put(13, new int[]{6,3}); // grass-beach-2
		coord_tex.put(14, new int[]{7,4}); // grass-beach-3
		coord_tex.put(15, new int[]{3,3}); // grass-beach-4
		coord_tex.put(16, new int[]{2,0}); // beach-shallow-in-1
		coord_tex.put(17, new int[]{7,0}); // beach-shallow-in-2
		coord_tex.put(18, new int[]{6,0}); // beach-shallow-in-3
		coord_tex.put(19, new int[]{2,4}); // beach-shallow-in-4
		coord_tex.put(20, new int[]{0,4}); // beach-shallow-out-1
		coord_tex.put(21, new int[]{0,1}); // beach-shallow-out-2
		coord_tex.put(22, new int[]{6,4}); // beach-shallow-out-3
		coord_tex.put(23, new int[]{4,0}); // beach-shallow-out-4
		coord_tex.put(24, new int[]{1,0}); // shallow-deep-in-1
		coord_tex.put(25, new int[]{0,2}); // shallow-deep-in-2
		coord_tex.put(26, new int[]{2,1}); // shallow-deep-in-3
		coord_tex.put(27, new int[]{1,1}); // shallow-deep-in-4
		coord_tex.put(28, new int[]{7,1}); // shallow-deep-out-1
		coord_tex.put(29, new int[]{1,2}); // shallow-deep-out-2
		coord_tex.put(30, new int[]{2,2}); // shallow-deep-out-3
		coord_tex.put(31, new int[]{6,1}); // shallow-deep-out-4
		coord_tex.put(32, new int[]{4,1}); // shallow-deep-1
		coord_tex.put(33, new int[]{5,1}); // shallow-deep-2
		coord_tex.put(34, new int[]{3,1}); // shallow-deep-3
		coord_tex.put(35, new int[]{3,0}); // shallow-deep-4
		coord_tex.put(36, new int[]{7,3}); // beach-shallow-1
		coord_tex.put(37, new int[]{3,2}); // beach-shallow-2
		coord_tex.put(38, new int[]{4,2}); // beach-shallow-3
		coord_tex.put(39, new int[]{4,4}); // beach-shallow-4
		coord_tex.put(40, new int[]{2,3}); // ship который shallow
		coord_tex.put(41, new int[]{0,5}); // Jewel
		
		coord_tex_ship.put(0,new int[]{1,1});
		coord_tex_ship.put(1,new int[]{0,0});
		coord_tex_ship.put(2,new int[]{0,1});
		coord_tex_ship.put(3,new int[]{1,0});
	}
	
	
	private void setMenu()	{
		menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu prog = new JMenu("Programm");
		JMenu help = new JMenu("Help");
		
		//JMenuItem load_map = new JMenuItem("Load map...");
		JMenuItem save_programm = new JMenuItem("Save programm...");
		JMenuItem load_programm = new JMenuItem("Load programm...");
		JMenuItem play = new JMenuItem("Play");
		JMenuItem check_programm = new JMenuItem("Check programm");
		JMenuItem about = new JMenuItem("About");
		JMenuItem help1 = new JMenuItem("Help contents...");
		
		main_frame.setJMenuBar(menu);
		menu.setVisible(true);
		menu.add(file);
		menu.add(prog);
		menu.add(help);
		file.add(load_programm);
		file.add(save_programm);
		
		prog.add(play);
		prog.add(check_programm);
		help.add(help1);
		help.add(about);
		
		save_programm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser(new File("."));
				int ret = fileopen.showSaveDialog(null);
				
				if (ret == JFileChooser.APPROVE_OPTION)	{
					File file = fileopen.getSelectedFile();
					try {
						
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						
						String text = "@ProgramExecutor\n" + text_area.getText();
						
						bw.write(text);
						bw.close();
					}
					catch (IOException ex) {
						if (Game.isError) ex.printStackTrace();
					}
				}
			}
		});
		
		load_programm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser(new File("."));
				int ret = fileopen.showSaveDialog(null);
				if (ret == JFileChooser.APPROVE_OPTION) {
					File file = fileopen.getSelectedFile();
					try	{	
						BufferedReader rw = new BufferedReader(new FileReader(file));
						String line = rw.readLine();
						if (line == null || line.equals("~ProgramExecutor"))
							setMsg("Неверный файл");
						else {
							StringBuffer text = new StringBuffer();
							while (rw.ready())
								text.append(rw.readLine()).append("\n");
							text_area.setText(text.toString());
						}
						rw.close();
					}
					catch (IOException ex) {
						if (Game.isError) ex.printStackTrace();
					}
				}
			}
		});
		
		help1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Help syntax");
				frame.setBounds( frame.getX()+200, frame.getY()+200, 410, 500);
				frame.setLayout(null);
				final JTextPane ta = new JTextPane();
				ta.setBounds(0, 0, 410, 400);
				//ta.setContentType("text/html");
				ta.setText(Compiller.getSyntax());
				ta.setVisible(true);
				ta.setEditable(false);
				
				JButton reset = new JButton("Reset");
				reset.setBounds(10, 420, 80, 20);
				reset.setVisible(true);
				reset.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Compiller.setCommands("Executer.ini");
						ta.setText(Compiller.getSyntax());
					}
				});
				frame.add(reset);
				frame.add(ta);
				frame.setVisible(true);
			}
		});
		
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String programm = text_area.getText();
				game.fromPlayer(new Message(namePlayer, "programm", programm));
			}
		});
		
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame us = new JFrame("About");
				
				us.setBounds( main_frame.getX()+200, main_frame.getY()+200, 600, 400);
				us.setLayout(null);
				
				JButton btn1 = new JButton("OK");
				JButton btn2 = new JButton("Don't OK");
				
				JTextArea ta = new JTextArea("Был использован OpenGL, Slick2D. \n\n" +
								"Спрайты использованы с этого адреса:\nhttp://opengameart.org/content/unknown-horizons-tileset \n"+
								"Создатели спрайтов:\nCredit me as either Daniel Stephens or Scribe and a link back to Unknown Horizons and\nOGA would be appreciated!\n\n"+
								"Разработчик: Волков Данил\n"+
								"e-mail: Linad29@mail.ru\n\n"
								+ "Данная работа была сделана в рамках защиты димлома в ПГГПУ. 2016г.");
				ta.setEditable(false);
				ta.setBackground( us.getBackground() );
				
				ta.setFont(new Font("Times New Roman", Font.TRUETYPE_FONT , 16));
				ta.setBounds(10, 0, 550, 300);
				btn1.setBounds(40, us.getHeight()-100, 50, 30);
				btn2.setBounds(100, us.getHeight()-100, 90, 30);
				
				ActionListener al = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e)	{
						JButton btn = (JButton) e.getSource();
						// достаем этот Frame, т.к. он локальный и я хз как его достать еще :DD
						JFrame f = (JFrame) btn.getParent().getParent().getParent().getParent();
						f.dispose();
					}
				};
				
				btn1.addActionListener(al);
				btn2.addActionListener(al);
				us.add(ta);
				us.add(btn1);
				us.add(btn2);
				us.setVisible(true);
				ta.setVisible(true);
			}
		});
	}
	
	
	public void setMsg(String text) {
		msg.setText(text);
	}
	
	private void setNeedValidation() {
		need_update_viewport = true;
	}
	
	public void setMap(byte[][] m) {
		map = m;
	}
	
	public String getName() {
		return namePlayer;
	}
	
	public void setRender(String name, boolean set) {
		if (objects.containsKey(name))
			objects.get(name).setRender(set);
	}
	
	public void setAllVisible() {
		for (String name : objects.keySet())
			objects.get(name).setRender(true);
	}
	
	private void setOpenGL() {
		try {
			// Создание и настройки OpenGL
			Display.create();
			Display.setParent(canvas);
			
			Rectangle rect = canvas.getBounds();
			int w = (int) rect.getWidth();
			int h = (int) rect.getHeight();
			
			GL11.glClearColor(0f, 0f, 0f, 1);
			
			// Указывание, сколько мерная текстура
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			// Смешение цветов.
			GL11.glEnable(GL11.GL_BLEND);
			// Проверка альфы
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			// Если меньше этого значения, то пиксель отбрасывается
			GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.8f);
			
			// Проверка глубины
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, w, h, 0, -1, 1);
			
			GL11.glViewport(0, 0, w, h);
			
			try {
				sprites = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Spritesheet.png"));
				texture_ship = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/ship1.png"));
			}
			catch (Exception ex) {
				if (Game.isError) ex.printStackTrace();
			}
		}
		catch (LWJGLException ex) {
			if (Game.isError) ex.printStackTrace();
		}
	}
	
	public void run() {
		setOpenGL();
		running = true;
		while (running) {
			updateGL();
			keyLoop();
		}
			
		if (Display.isCreated()) {
			Display.destroy();
		}
	}
	
	
	public void addPlayer(Player p) {
		if (namePlayer == null)
			namePlayer = p.getName();
		if (!objects.containsKey(p.getName()) || objects.get(p.getName()) != null)
			objects.put(p.getName() , p);
	}
	
	public void deletePlayer(SPlayer p) {
		if (objects.containsKey(p.name))
			objects.remove(p.name);
	}
	
	public void updatePlayers(List<SPlayer> list, int size) {
		for (int i=0; i<size; i++) {
			SPlayer p = list.get(i);
			if (objects.containsKey(p.name) && objects.get(p.name) != null) {
				Player player = objects.get(p.name);
				player.setDirection(p.direction);
				player.setPosition(p.x, p.y);
				player.setRotation(p.rotation);
			}
			else {
				addPlayer(new Player(p));
			}
		}
	}
	
	private void keyLoop() {
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		    	switch (Keyboard.getEventKey()) {
		    	case Keyboard.KEY_A:
		    		offset_x = 2;
		    		break;
		    	case Keyboard.KEY_D:
		    		offset_x = -2;
		    		break;
		    	case Keyboard.KEY_W:
		    		offset_y = 2;
		    		break;
		    	case Keyboard.KEY_S:
		    		offset_y = -2;
		    		break;
		    	case Keyboard.KEY_Q:
		    		scale_inc = +0.05f;
		    		break;
		    	case Keyboard.KEY_E:
		    		scale_inc = -0.05f;
		    		break;
		    	}
		    }
		    else {
		    	switch (Keyboard.getEventKey()) {
		    	case Keyboard.KEY_A:
		    		offset_x = 0;
		    		break;
		    	case Keyboard.KEY_D:
		    		offset_x = 0;
		    		break;
		    	case Keyboard.KEY_W:
		    		offset_y = 0;
		    		break;
		    	case Keyboard.KEY_S:
		    		offset_y = 0;
		    		break;
		    	case Keyboard.KEY_Q:
		    		scale_inc = 0;
		    		break;
		    	case Keyboard.KEY_E:
		    		scale_inc = 0;
		    		break;
		    	}
		    }
		}
		
		offset.x += offset_x;
		offset.y += offset_y;
	}
		
	private void drawMap() {
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glTranslatef(offset.x, offset.y, 0);
		GL11.glScalef(scale_map, scale_map , 0);
		
		// Подключаем текстуру.
		sprites.bind();
		
		int[] t = new int[2];
		int width = 64;
		int height = 32;
		for (int x=0; x<map.length; x++)
			for (int y=0; y<map[x].length; y++) {
				float sx = x*width/2;
				float sy = y*height;
				
				float _sx = sx - sy;
				float _sy = (sx+sy)/2; 
								
				t = coord_tex.get((int)map[x][y]);
				if (t == null) System.out.println(map[x][y]);
				drawTexture(_sx, _sy, 0, t[0], t[1]);
			}
		GL11.glPopMatrix();
	}
	
	private void drawTexture(float px, float py, float pz, float tx, float ty) {
		GL11.glPushMatrix();
		GL11.glTranslatef(px, py, pz);
		
		float tex_width = 1f/8f;
		float tex_height = 1f/8f;
		
		//GL11.glRotatef(rot, 0f, 0f, 1f);
		
		Color.white.bind();
		GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(tx*tex_width, ty*tex_height);
            GL11.glVertex2f(-32,-32);
            
            GL11.glTexCoord2f(tx*tex_width+tex_width, ty*tex_height);
            GL11.glVertex2f(32, -32);
            
            GL11.glTexCoord2f(tx*tex_width+tex_width, ty*tex_height+tex_height);
            GL11.glVertex2f(32, 32);
           
            GL11.glTexCoord2f(tx*tex_width, ty*tex_height+tex_height);
            GL11.glVertex2f(-32, 32);
        GL11.glEnd();
       
        GL11.glPopMatrix();
	}
	
	private void drawShip(Player ship) {
		if (ship != null) {
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			float tex_width = 1f/2f;
			float tex_height = 1f/2f;
			
			float x = ship.getPosition().x;
	    	float y = ship.getPosition().y;
	    	
	    	float sx = x*64/2;
			float sy = y*32;
			
			float _sx = sx - sy;
			float _sy = (sx+sy)/2;
			
			int t[] = coord_tex_ship.get(ship.getDirection());
	    				
	    	//font.drawString(0, 0, "X: "+x+" Y: "+y+" x_p: "+_sx+ " y_p: "+_sy);
	    	texture_ship.bind();
	    	GL11.glTranslatef(offset.x, offset.y, 0);
	    	GL11.glTranslatef(_sx,_sy, 0);
	    	GL11.glScalef( scale_map, scale_map, 0);
	    	GL11.glBegin(GL11.GL_QUADS);
	    	GL11.glTexCoord2f(t[0]*tex_width, t[1]*tex_height);
			GL11.glVertex2f(-32,-32);
			
			GL11.glTexCoord2f(t[0]*tex_width+tex_width, t[1]*tex_height);
			GL11.glVertex2f(32, -32);
			
			GL11.glTexCoord2f(t[0]*tex_width+tex_width, t[1]*tex_height+tex_height);
			GL11.glVertex2f(32, 32);
			
			GL11.glTexCoord2f(t[0]*tex_width, t[1]*tex_height+tex_height);
			GL11.glVertex2f(-32, 32);
			GL11.glEnd();
			
			// Рамка для обозначения игрока
			if (!namePlayer.isEmpty() && ship.getName().equals(namePlayer)) {
				sprites.bind();
				float coord[] = {1,5};
				tex_width = 1f/8f;
				tex_height = 1f/8f;
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(coord[0]*tex_width, coord[1]*tex_height);
				GL11.glVertex2f(-32,-32);
				
				GL11.glTexCoord2f(coord[0]*tex_width+tex_width, coord[1]*tex_height);
				GL11.glVertex2f(32, -32);
				
				GL11.glTexCoord2f(coord[0]*tex_width+tex_width, coord[1]*tex_height+tex_height);
				GL11.glVertex2f(32, 32);
				
				GL11.glTexCoord2f(coord[0]*tex_width, coord[1]*tex_height+tex_height);
				GL11.glVertex2f(-32, 32);
				GL11.glEnd();
			}
			GL11.glPopMatrix();
	    }
	}
	
	public void updateGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		render();
		
		if (need_update_viewport) {
			need_update_viewport = false;
			
			Rectangle rect = canvas.getBounds();
			int w = (int) rect.getWidth();
			int h = (int) rect.getHeight();
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, w, h, 0, -1, 1);
			GL11.glViewport(0, 0, w, h);
		}
		
		Display.update();
		Display.sync(60);
	}
	
	public void closeServer() {
		running = false;
	}
	
	private void render() {
		if (map != null) {
			drawMap();
			for (String name : objects.keySet()) {
				Player p = objects.get(name);
				if (p.isRender())
					drawShip(p);
			}
		}
		else
			setMsg("No map");
	}
	
	public void start() {	
		running = true;
		game_thread = new Thread(this);
		game_thread.start();
	}
}
