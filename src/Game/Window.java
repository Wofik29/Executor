package Game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
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
	private JSplitPane splitPane;
	private JTextArea textArea;
	private JPanel leftPanel;
	private JPanel panel;
	private JButton start;
	private JButton stop;
	
	// Работает все тут
	private Thread gameThread;
	
	// флаг на работу
	private boolean running;
	
	// флаг, если надо изменить вывод OpenGL
	private boolean needUpdateViewport = false;
	
	// Массив к текстурными координатами.
	HashMap<Integer , int[]> coordTex = new HashMap<Integer, int[]>(70);
	HashMap<Integer , int[]> coordTexShip = new HashMap<Integer, int[]>(50);

	// текстуры земли и всего вокруг 
	Texture sprites;
	Texture ship;
	
	byte[][] map;
	
	GameObject player;
	
	// объект для вывода текста на OpenGL
	TrueTypeFont font;
	
	private Controller controller;
	
	public Window(Controller c)
	{
		
		controller = c;
		
		// Создание frame для держание всего.
		frame = new JFrame();
		frame.setTitle("Swing + LWJGL");
		frame.setLayout(null);
		frame.setBounds(100, 100, 1024, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textArea = new JTextArea();
		//textArea.setBounds(0, 0, 100, frame.getHeight()-60);
		textArea.setMinimumSize(new Dimension(320, 240));
		
		leftPanel = new JPanel();
		
		
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setBounds(0, 0, 320,  frame.getHeight());
		panel = new JPanel();
		
		
		start = new JButton("Start");
		start.setMinimumSize(new Dimension(100,50));
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("pressed start");
				String text = textArea.getText();
				controller.setProgramm(text);
			}
		});
		
		stop = new JButton("Stop");
		start.setMinimumSize(new Dimension(100,50));
		
		panel.add(start);
		panel.add(stop);
		
		leftPanel.add(panel, BorderLayout.NORTH);
		leftPanel.add(textArea, BorderLayout.CENTER);
		
		
		// Разделяет frame на две части. В одной будет текст, в другой canvas
		splitPane = new JSplitPane();
		splitPane.setBounds(0,0, frame.getWidth(), frame.getHeight());
		
		frame.add(splitPane);
				
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
		
		splitPane.setRightComponent(canvas);
		splitPane.setLeftComponent(leftPanel);
		//splitPane.setLeftComponent(textArea);
		
		// Делаем все видимое, ясно.
		frame.setVisible(true);
		splitPane.setVisible(true);
		canvas.setVisible(true);
		start.setVisible(true);
		
		//При изменении основного окна, меняем и размеры компоненты, который разделяет все.
		frame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {}
			
			@Override
			public void componentResized(ComponentEvent arg0) 
			{
				splitPane.setBounds(0,0, frame.getWidth(), frame.getHeight());
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
		
		KeyListener listener = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("typed");
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("released");
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0.getExtendedKeyCode());
				System.out.println(arg0.getKeyChar());
				System.out.println("pressed");
			}
		};
		
		
		frame.addKeyListener(listener);
		//canvas.addKeyListener(listener);
		splitPane.addKeyListener(listener);
		frame.setFocusable(true);
		
		frame.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				//frame.setFocusable(true);
				System.out.println("Frame Lost");
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Frame Gained");
				
			}
		});
		
		textArea.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				frame.setFocusable(true);
				System.out.println("Lost");
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Gained");
				
			}
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
		
		coordTexShip.put(0,new int[]{0,0});
		coordTexShip.put(1,new int[]{1,1});
		coordTexShip.put(2,new int[]{1,0});
		coordTexShip.put(3,new int[]{0,1});
		
		System.out.println("Create Window");
	}
	
	private void setNeedValidation() 
	{
		needUpdateViewport = true;
	}
	
	public void setPlayer(GameObject obj)
	{
		player = obj;
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
			
			GL11.glShadeModel(GL11.GL_SMOOTH);
			
			// Проверка альфы
			GL11.glEnable(GL11.GL_ALPHA_TEST);

			// Если меньше этого значения, то пиксель отбрасывается
			GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.8f);
			
			// Проверка глубины
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			// Смешение цветов.
			GL11.glEnable(GL11.GL_BLEND);
			
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
		}
			
		if (Display.isCreated()) 
		{
			Display.destroy();
		}
	}
	
	private void drawMap()
	{
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 0);
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
				
				drawTexture(Xo, Yo, 0, t[0], t[1]);	
			}
			
			
		}
	    if (player != null)
	    {
	    	float y = player.x;
	    	float x = player.y;
	    	Xc = C - (width / 2 * y);
	    	Xo = Xc + (x * (width / 2));
	    	Yo = (height / 2) * y + player.y*(height/2);
	    	
	    	
	    	font.drawString(0, 0, "X: "+x+" Y: "+y);
			drawShip(Xo,Yo);
		}
		//drawShip();
		GL11.glPopMatrix(); 
	}
	
	private void drawShip(float x, float y)
	{
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		float tex_width = 1f/2f;
		float tex_height = 1f/2f;
		int[] t = coordTexShip.get(player.direction);
		

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
	
	public void render()
	{
		drawMap();
	}
	
	void drawTexture(float px, float py, float pz, float tx, float ty)
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
