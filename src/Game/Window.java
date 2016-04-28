package Game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Window implements Runnable
{
	// Отрисовывается все тут
	private JFrame frame;
	private Canvas canvas;
	private JTextArea textArea;
	private JLabel msg;
	private JMenuBar menu;
	private JSplitPane splitPane;
	
	// Поток, в котором крутиться window.
	private Thread gameThread;
	
	// флаг на работу
	private boolean running;
	
	// флаг, если надо изменить вывод OpenGL
	private boolean needUpdateViewport = false;
	
	// Массив к текстурными координатами.
	private HashMap<Integer , int[]> coordTex = new HashMap<Integer, int[]>(70);
	private HashMap<Integer , int[]> coordTexShip = new HashMap<Integer, int[]>(50);

	// текстуры земли и всего вокруг 
	private Texture sprites;
	private Texture ship;
	
	byte[][] map;
	
	private GameObject player;
	private GameObject another_ship;
	//private volatile List<GameObject> objects = new ArrayList<>();
	
	// объект для вывода текста на OpenGL
	private TrueTypeFont font;
	
	private Controller controller;
	
	public Window(Controller c)
	{
		
		controller = c;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Создание frame для держание всего.
		frame = new JFrame();
		frame.setTitle("Swing + LWJGL");
		frame.setLayout(null);
		frame.setBounds(0, 0, 1280, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		setMenu();
		
		
		JPanel leftPanel;
		JPanel EditPanel;
		JPanel panel;
		
		textArea = new JTextArea();
		textArea.setColumns(35);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Arial", Font.BOLD, 20));
		textArea.setBounds(0, 0, 100, 200);
		
		EditPanel = new JPanel();
		EditPanel.setBounds(0, 0, 100, 100);
		// Можно как то поиграться, чтоб была подсветка текста
		//AttributeSet mySet = StyleContext.getDefaultStyleContext().addAttribute(old, name, value)
		
		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBounds(0, 0, 120, frame.getHeight());
		panel = new JPanel();
		
		JButton start;
		JButton stop;
		start = new JButton("Start");
		start.setMinimumSize(new Dimension(100,50));
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				System.out.println("pressed start");
				String text = textArea.getText();
				controller.setProgramm(text);
			}
		});
		
		stop = new JButton("Stop");
		
		
		panel.add(start);
		panel.add(stop);
		
				
		//msg.setFont(new );
		
		
		leftPanel.add(panel, BorderLayout.NORTH);
		leftPanel.add(textArea, BorderLayout.CENTER);
		//leftPanel.add(msg, BorderLayout.SOUTH);
		
		
		// Разделяет frame на две части. В одной будет текст, в другой canvas
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		splitPane.setBounds(0,0, frame.getWidth(), frame.getHeight()-menu.getHeight()-80);
		
		msg = new JLabel(frame.getHeight()+ "");
		msg.setBounds(0, splitPane.getHeight(), 500, 30);
		Font f = new Font("Arial", Font.BOLD, 14);
		msg.setForeground(java.awt.Color.RED);
		
		
		frame.add(splitPane);
		frame.add(msg);
				
		// Canvas будет контейнером для OpenGL
		canvas = new Canvas() {
			private static final long serialVersionUID = -1069002023468669595L;
			public void removeNotify() 
			{
				//stopOpenGL();
			}
		};
		
		canvas.setIgnoreRepaint(true);
		canvas.setBounds(0, 0, 800, 600);
		canvas.setPreferredSize(new Dimension(800, 600));
		canvas.setMinimumSize(new Dimension(320, 240));
		
		// Навешиваем слушателей, чтобы openGL изменял свой вид, при изменения окна.
		canvas.addComponentListener(new ComponentListener() 
		{
			public void componentShown(ComponentEvent e) 
			{
				setNeedValidation();
			}
			public void componentResized(ComponentEvent e) 
			{
				setNeedValidation();
			}
			public void componentMoved(ComponentEvent e) 
			{
				setNeedValidation();
			}
			public void componentHidden(ComponentEvent e) 
			{
				setNeedValidation();
			}
		});
		
		//splitPane.setLayout(null);
		splitPane.setRightComponent(canvas);
		splitPane.setLeftComponent(leftPanel);
		
		//splitPane.setLeftComponent(textArea);
		
		// Делаем все видимое, ясно.
		frame.setVisible(true);
		splitPane.setVisible(true);
		EditPanel.setVisible(true);
		canvas.setVisible(true);
		start.setVisible(true);
		msg.setVisible(true);
		
		//При изменении основного окна, меняем и размеры компоненты, который разделяет все.
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) 
			{
				splitPane.setBounds(0,0, frame.getWidth(), frame.getHeight()-menu.getHeight()-80);
				msg.setBounds(0, splitPane.getHeight(), 500, 30);
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) 
			{
				splitPane.setBounds(0,0, frame.getWidth(), frame.getHeight()-menu.getHeight()-80);
				msg.setBounds(0, splitPane.getHeight(), 500, 30);
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		
		coordTex.put(0, new int[]{5,0}); // grass
		coordTex.put(1, new int[]{0,0}); // deep
		coordTex.put(2, new int[]{1,4}); // beach
		coordTex.put(3, new int[]{2,3}); // shallow
		coordTex.put(4, new int[]{0,3}); // grass-beach-in-1
		coordTex.put(5, new int[]{4,3}); // grass-beach-in-2
		coordTex.put(6, new int[]{1,3}); // grass-beach-in-3
		coordTex.put(7, new int[]{5,2}); // grass-beach-in-4
		coordTex.put(8, new int[]{5,4}); // grass-beach-out-1
		coordTex.put(9, new int[]{5,3}); // grass-beach-out-2
		coordTex.put(10, new int[]{7,2}); // grass-beach-out-3
		coordTex.put(11, new int[]{3,4}); // grass-beach-out-4
		coordTex.put(12, new int[]{6,2}); // grass-beach-1
		coordTex.put(13, new int[]{6,3}); // grass-beach-2
		coordTex.put(14, new int[]{7,4}); // grass-beach-3
		coordTex.put(15, new int[]{3,3}); // grass-beach-4
		coordTex.put(16, new int[]{2,0}); // beach-shallow-in-1
		coordTex.put(17, new int[]{2,4}); // beach-shallow-in-2
		coordTex.put(18, new int[]{6,0}); // beach-shallow-in-3
		coordTex.put(19, new int[]{7,0}); // beach-shallow-in-4
		coordTex.put(20, new int[]{0,4}); // beach-shallow-out-1
		coordTex.put(21, new int[]{4,0}); // beach-shallow-out-2
		coordTex.put(22, new int[]{6,4}); // beach-shallow-out-3
		coordTex.put(23, new int[]{0,1}); // beach-shallow-out-4
		coordTex.put(24, new int[]{1,0}); // shallow-deep-in-1
		coordTex.put(25, new int[]{0,2}); // shallow-deep-in-2
		coordTex.put(26, new int[]{2,1}); // shallow-deep-in-3
		coordTex.put(27, new int[]{1,1}); // shallow-deep-in-4
		coordTex.put(28, new int[]{7,1}); // shallow-deep-out-1
		coordTex.put(29, new int[]{1,2}); // shallow-deep-out-2
		coordTex.put(30, new int[]{2,2}); // shallow-deep-out-3
		coordTex.put(31, new int[]{6,1}); // shallow-deep-out-4
		coordTex.put(32, new int[]{4,1}); // shallow-deep-1
		coordTex.put(33, new int[]{5,1}); // shallow-deep-2
		coordTex.put(34, new int[]{3,1}); // shallow-deep-3
		coordTex.put(35, new int[]{3,0}); // shallow-deep-4
		coordTex.put(36, new int[]{3,2}); // beach-shallow-1
		coordTex.put(37, new int[]{7,3}); // beach-shallow-2
		coordTex.put(38, new int[]{4,4}); // beach-shallow-3
		coordTex.put(39, new int[]{4,2}); // beach-shallow-4
		coordTex.put(41, new int[]{0,5}); // Jewel
		
		coordTexShip.put(0,new int[]{0,0});
		coordTexShip.put(1,new int[]{1,1});
		coordTexShip.put(2,new int[]{1,0});
		coordTexShip.put(3,new int[]{0,1});
		
		System.out.println("Create Window : ");
	}
	
	private void setMenu()
	{
		menu = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu prog = new JMenu("Programm");
		JMenu help = new JMenu("Help");
		
		JMenuItem load_map = new JMenuItem("Load map...");
		JMenuItem save_programm = new JMenuItem("Save programm...");
		JMenuItem load_programm = new JMenuItem("Load programm...");
		JMenuItem play = new JMenuItem("Play");
		JMenuItem about = new JMenuItem("About");
		JMenuItem help1 = new JMenuItem("Help contents...");
		
		frame.setJMenuBar(menu);
		menu.add(file);
		menu.add(prog);
		menu.add(help);
		
		file.add(load_programm);
		file.add(save_programm);
		file.add(load_map);
		
		prog.add(play);
		
		help.add(help1);
		help.add(about);
		
		load_map.addActionListener(new ActionListener() 
		{			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser fileopen = new JFileChooser(new File("."));
				int ret = fileopen.showOpenDialog(null);
				
				if (ret == JFileChooser.APPROVE_OPTION)
				{
					String name = fileopen.getSelectedFile().getAbsolutePath();
					controller.loadMap(name);
				}
			}
		});
		
		save_programm.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fileopen = new JFileChooser(new File("."));
				int ret = fileopen.showSaveDialog(null);
				
				if (ret == JFileChooser.APPROVE_OPTION)
				{
					File file = fileopen.getSelectedFile();
					
					try 
					{
						
						BufferedWriter bw = new BufferedWriter(new FileWriter(file));
						
						String text = "@ProgramExecutor\n" + textArea.getText();
						
						bw.write(text);
						bw.close();
					}
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
			}
		});
		
		load_programm.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fileopen = new JFileChooser(new File("."));
				int ret = fileopen.showSaveDialog(null);
				
				if (ret == JFileChooser.APPROVE_OPTION)
				{
					File file = fileopen.getSelectedFile();
					
					try
					{	
						BufferedReader rw = new BufferedReader(new FileReader(file));
						String line = rw.readLine();
						if (line == null || line.equals("~ProgramExecutor"))
						{
							setMsg("Неверный файл");
						}
						else
						{
							StringBuffer text = new StringBuffer();
							while (rw.ready())
								text.append(rw.readLine()).append("\n");
							
							textArea.setText(text.toString());
						}
						
						rw.close();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
				
			}
		});
		
		help1.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFrame frame = new JFrame("Help syntax");
				frame.setBounds( frame.getX()+200, frame.getY()+200, 400, 400);
				frame.setLayout(null);
				
				JTextPane ta = new JTextPane();
				ta.setBounds(0, 0, 400, 400);
				//ta.setContentType("text/html");
				ta.setText(Compiller.getSyntax());
				
				ta.setVisible(true);
				ta.setEditable(false);
				
				frame.add(ta);
				frame.setVisible(true);
			}
		});
		
		about.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				JFrame us = new JFrame("About");
				
				us.setBounds( frame.getX()+200, frame.getY()+200, 400, 200);
				us.setLayout(null);
				
				JButton btn1 = new JButton("OK");
				JButton btn2 = new JButton("Don't OK");
				
				JTextArea ta = new JTextArea("Программу создал Волков Данил. \t\n" +
								"Был использован OpenGL, Slick2D. \n\n" +
								"Спрайты были взяты отсюда: http://opengameart.org/content/unknown-horizons-tileset\n "+
								"Credit me as either Daniel Stephens or Scribe and a link back to Unknown Horizons and\nOGA would be appreciated!");
				ta.setEditable(false);
				//ta.setForeground(java.awt.Color.red);
				
				
				ta.setBounds(50, 0, 300, 100);
				btn1.setBounds(40, 130, 50, 30);
				btn2.setBounds(100, 130, 90, 30);
				
				ActionListener al = new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						JButton btn = (JButton) e.getSource();
						JFrame f = (JFrame) btn.getParent().getParent().getParent().getParent(); // достаем этот Frame, т.к. он локальный и я хз как его достать еще :DD
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
	
	
	public void setMsg(String text)
	{
		msg.setText(text);
	}
	
	private void setNeedValidation() 
	{
		needUpdateViewport = true;
	}
	
	public void setPlayer(GameObject obj)
	{
		player = obj;
	}
	public void setShip(GameObject obj)
	{
		another_ship = obj;
	}
	
	public void setMap(byte[][] m)
	{
		map = m;
	}
	
	private void stopOpenGL()
	{
		System.out.println("StopOpenGL");
		
		running = false;
		try 
		{
			gameThread.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		controller.stop();
	}
	
	public void run()
	{
		try 
		{
			// Создание и настройки OpenGL
			Display.create();
			Display.setParent(canvas);
			
			Rectangle rect = canvas.getBounds();
			int w = (int) rect.getWidth();
			int h = (int) rect.getHeight();
			
			Font awtFont = new Font("Times new Roman", Font.BOLD, 25);
			font = new TrueTypeFont(awtFont, false);
			
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
			
			try
			{
								
				sprites = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Spritesheet.png"));
				ship = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/ship1.png"));

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
			running = true;
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
		
		while (running) 
		{
			updateGL();
			keyLoop();
		}
			
		if (Display.isCreated()) 
		{
			Display.destroy();
		}
	}
	
	private void keyLoop()
	{
		while (Keyboard.next()) 
		{
		    if (Keyboard.getEventKeyState()) 
		    {
		    	controller.pressedKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
		    }
		    else
		    {
		    	controller.relessedKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
		    }
		}
	}
	
	
	private void drawMap()
	{
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 0);//player.getLocation().x, player.getLocation().y, 0);
		GL11.glScalef(1f,1f, 0);
		
		// Подключаем текстуру.
		sprites.bind();
		
		// Координаты середины текстурного квадрата
		float Xo = 0;
	    float Yo = 0;
	    
	    // его видимые размеры
	    int height = 32;
	    int width = 64;
	    
	    int[] t = new int[2];
	    
	    // Середина по ширине
	    float C =(float) Math.floor(Display.getWidth()/ 2);
	    
	    //
	    float Xc = 0;
	    
	    for (int y=0; y<map.length; y++)
		{
			// Здесь высчитывается, на какой высоте должна начинаться отрисовка 
            Yo = (height / 2) * y;
            Xc = C - (width / 2 * y);
			
			for (int x=0; x<map[y].length; x++)
			{
				Xo = Xc + (x * (width / 2));
				Yo += height / 2;
				
				t = coordTex.get((int)map[y][x]);
				if (t == null) System.out.println(map[y][x]);
				drawTexture(Xo, Yo, 0, t[0], t[1]);
			}
		}
	    if (player != null)
	    {
	    	
	    	float y = player.getLocation().x+1;
	    	float x = player.getLocation().y+1;
	    	Xc = C - (width / 2 * y);
	    	Xo = Xc + (x * (width / 2));
	    	Yo = (height / 2) * y + (x-1)*(height/2);
	    	
	    	
	    	font.drawString(0, 0, "X: "+x+" Y: "+y);
			drawShip(Xo,Yo, coordTexShip.get(player.direction));
		}
	    if (another_ship != null)
	    {
	    	float y = another_ship.getLocation().x+1;
	    	float x = another_ship.getLocation().y+1;
	    	Xc = C - (width / 2 * y);
	    	Xo = Xc + (x * (width / 2));
	    	Yo = (height / 2) * y + (x-1)*(height/2);
	    	
	    	drawShip(Xo,Yo, coordTexShip.get(another_ship.direction));
	    }
		//drawShip();
		GL11.glPopMatrix(); 
	}
	
	private void drawShip(float x, float y, int[] t)
	{
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		float tex_width = 1f/2f;
		float tex_height = 1f/2f;
		//int[] t = coordTexShip.get(player.direction);
		

		GL11.glTranslatef(x,y, 0);
		//GL11.glScalef(scal,scal, 0);
		ship.bind();
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
		
		/*
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glColor3f(1, 0, 0);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(-10, -10);
			GL11.glVertex2d(-10, 10);
			GL11.glVertex2d(10, 10);
			GL11.glVertex2d(10, -10);
		GL11.glEnd();
		*/
		GL11.glPopMatrix();
		sprites.bind();
	}
	
	public void updateGL()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		render();
		
		if (needUpdateViewport) 
		{
			needUpdateViewport = false;
			
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
	
	private void render()
	{
		drawMap();
	}
	
	private void drawTexture(float px, float py, float pz, float tx, float ty)
	{
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
	
	public void start()
	{
		
		gameThread = new Thread(this);
		gameThread.start();
	}
}
