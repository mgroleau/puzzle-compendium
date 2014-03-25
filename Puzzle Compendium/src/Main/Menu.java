package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Menu extends JFrame {

	private final int WINDOW_WIDTH = 850;
	private final int WINDOW_HEIGHT = 510;
	private JLabel top, bottom;
	private ButtonHandler bl = new ButtonHandler(); 
	
	public Menu(){
		setLayout(new BorderLayout());
		top = new JLabel("Another Generic Block Game                                                                                       Miner"); 
		bottom = new JLabel("ManPac                                                                                                                             Based RNG God");
		JPanel games = new JPanel();
		ImageIcon agbgicon = new ImageIcon("res/AGBG.jpg");
		ImageIcon mineicon = new ImageIcon("res/mine.jpg");
		ImageIcon manpacicon = new ImageIcon("res/manpac.jpg");
		ImageIcon rngicon = new ImageIcon("res/rng.jpg");
		JButton agbg = new JButton("agbg");
		JButton mine = new JButton("mine");
		JButton manpac = new JButton("manpac");
		JButton rng = new JButton("rng");
		agbg.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
		mine.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
		manpac.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
		rng.setBorder(BorderFactory.createLineBorder(Color.MAGENTA, 2));
		agbg.setIcon(agbgicon);
		mine.setIcon(mineicon);
		manpac.setIcon(manpacicon);
		rng.setIcon(rngicon);
		agbg.addActionListener(bl);
		mine.addActionListener(bl);
		manpac.addActionListener(bl);
		rng.addActionListener(bl);
		
		setTitle("Choose a Game!");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		games.setLayout(new GridLayout(2,2));
		setResizable(false);
		add(top, BorderLayout.NORTH);
		add(bottom, BorderLayout.SOUTH);
		games.add(agbg);
		games.add(mine);
		games.add(manpac);
		games.add(rng);
		add(games, BorderLayout.CENTER);
		setLocation(50,50);
		setVisible(true);
	}
	
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command=="agbg"){
				Highscores.read(0);
				dispose();
				Frame f = new Frame(0);		
			}
			
			if(command=="mine"){
				Highscores.read(1);
				dispose();
				Frame f = new Frame(1);
			}
			
			if(command=="manpac"){
				Highscores.read(2);
				dispose();
				Frame f = new Frame(2);
			}
			
			if(command=="rng"){
				Highscores.read(3);
				dispose();
				Frame f = new Frame(3);
			}
		}
	}
}
