package Game;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class WindowConnect {
	
	private JFrame connect_frame;
	private JTextField address_field;
	private JTextField name_field;
	private JLabel address_label;
	private JLabel error_label;
	private JLabel name_label;
	private JButton enter; 
	
	private Game game;
	
	public WindowConnect(Game game) {
		connect_frame = new JFrame("Connection");
		connect_frame.setLayout(null);
		connect_frame.setBounds(100, 200, 500, 150);
		
		this.game = game;
		final Game selfGame = game;
		Font f = new Font("Times New Romans", Font.BOLD, 13);
		
		address_field = new JTextField("localhost");
		address_field.setBounds(160, 30, 100, 25);
		address_field.setFont(f);
		name_field = new JTextField("Wolf");
		name_field.setBounds(160, 60, 100, 25);
		name_field.setFont(f);
		name_field.setEnabled(false);
		address_label = new JLabel("Enter ip-address:");
		address_label.setBounds(30, 25, 120, 30);
		address_label.setFont(f);
		name_label = new JLabel("Name: ");
		name_label.setBounds(30, 55, 120, 30);
		name_label.setFont(f);
		error_label = new JLabel("-");
		error_label.setBounds(30, 5, 300, 30);
		error_label.setFont(f);
		
		enter = new JButton("Connect!");
		enter.setBounds(280, 30, 100, 20);
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				error_label.setText("Соединение...");
				Thread checking = new Thread( new Runnable() {
					@Override
					public void run() {
						if (address_field.getText().isEmpty())
							error_label.setText("Не написан адрес сервера!");
						else
						{
							selfGame.connect(address_field.getText());
						}
					}
				});
				checking.start();
			}
		});

		JButton exit = new JButton("Exit");
		exit.setBounds(400, 80, 80, 20);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connect_frame.dispose();
				selfGame.stop();
			}
		});
		
		JButton set_name = new JButton("Set name");
		set_name.setBounds(280, 60, 100, 20);
		set_name.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selfGame.sendName(name_field.getText());
			}
		});
				
		connect_frame.add(address_label);
		connect_frame.add(address_field);
		connect_frame.add(name_field);
		connect_frame.add(name_label);
		connect_frame.add(enter);
		connect_frame.add(exit);
		connect_frame.add(set_name);
		connect_frame.add(error_label);
		connect_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connect_frame.setVisible(true);
	}
	
	public void connectSet()
	{
		error_label.setText("Соединение установлено. Введите имя");
		name_field.setEnabled(true);
	}
	
	public void setLabelText(String str) {
		error_label.setText(str);
	}
	
	public void setShow(boolean set) {
		connect_frame.setVisible(set);
	}
}
