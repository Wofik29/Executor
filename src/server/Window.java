package server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class Window extends JFrame {
	private static final long serialVersionUID = -1052791113101528590L;
	private int step = 20;
	private int offset_x = 0;
	private int offset_y = 0;
	private Game game;
	private Image image;
	
	public Window(Game g) {
		this.setTitle("Map");
		this.setLayout(null);
		this.setVisible(true);
		setMenu();
		this.setSize(500, 500);
		resetMap();
		game = g;
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'a': offset_x-=10; break;
				case 'd': offset_x+=10; break;
				case 'w': offset_y-=10; break;
				case 's': offset_y+=10; break;
				}
				draw();
			}
		});
	}
	
	private void setMenu() {
		JButton load = new JButton("Load map");
		load.setBounds(0, 0, 100, 20);
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileopen = new JFileChooser(new File("."));
				fileopen.setFileFilter(new FileFilter() {
					@Override
					public String getDescription() {return null;}
					private String getExtension(File pathname) {
					    String filename = pathname.getPath();
					    int i = filename.lastIndexOf('.');
					    if ( i>0 && i<filename.length()-1 ) {
					        return filename.substring(i+1).toLowerCase();
					    }
					    return "";
					}
					
					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) return true;
						else if	("map".equals(getExtension(f.getAbsoluteFile()))) return true;
						else return false;
					}
				});
				int ret = fileopen.showSaveDialog(null);
				if (ret == JFileChooser.APPROVE_OPTION) {
					File file = fileopen.getSelectedFile();
					game.loadMap(file.getAbsolutePath());
					resetMap();
					draw();
				}
			}
		});
		load.setVisible(true);
		this.add(load);
	}
	
	public void draw() {
		update(this.getGraphics());
	}
	
	private void resetMap() {
		if (World.map != null)
			image = this.createImage(World.map.length*step, World.map[0].length*step);
		else
			image = this.createImage(700, 700);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (image == null) return;
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		image.flush();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		byte[][] map = World.map;
		if (map != null) {
			for (int i=0; i<map.length; i++)
				for (int j=0; j<map[i].length; j++) {
					switch (map[i][j]) {
					case Map.SHALLOW:
						g2.setColor(new Color(0,192,255));
						break;
					case Map.SHIP:
						g2.setColor(Color.BLACK);
						break;
					case Map.JEWEL:
						g2.setColor(Color.red);
						break;
					default:	
						g2.setColor(new Color(255,217,00));
						break;
					}
					g2.fillRect(i*step+offset_x, j*step+offset_y, step, step);
				}
		}
		
		g.drawImage(image, 10, 100, null);
	}
}