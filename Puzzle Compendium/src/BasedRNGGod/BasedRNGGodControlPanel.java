package BasedRNGGod;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class BasedRNGGodControlPanel extends JPanel {

	private BasedRNGGod game;
	private static JLabel score, difficulty, minerals, money, consolel, distributionl;
	private static JButton pray, road, shrine, mine, base, factory, demolish, repair, cancel;
	private static JTextArea console;
	private static JSlider distribution;
	private ButtonHandler bl = new ButtonHandler();
	private static boolean flag = false;

	public BasedRNGGodControlPanel(BasedRNGGod g) {
		game = g;

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		setLayout(new GridLayout(2, 1));
		panel1.setLayout(new GridLayout(7, 2));
		panel2.setLayout(new BorderLayout());
		panel3.setLayout(new BorderLayout());
		distributionl = new JLabel("Mine     -     Distribution of Labor     -     Convert");
		distribution = new JSlider();
		distribution.setMajorTickSpacing(20);
		distribution.setMinorTickSpacing(10);
		distribution.setPaintTicks(true);
		distribution.setSnapToTicks(true);
        distributionl.setFont(new Font("Serif", Font.PLAIN, 10));
        
		consolel = new JLabel("Log:");
		console = new JTextArea("");
		
		difficulty = new JLabel("Difficulty: ");
		score = new JLabel("Score: ");
		minerals = new JLabel("Minerals: ");
		money = new JLabel("Money: $");
		pray = new JButton("1. Pray to RNG God");
		road = new JButton("2. Build road. $5");
		shrine = new JButton("3. Build shrine. $50");
		mine = new JButton("4. Build mine. $25");
		base = new JButton("5. Build base. $500");
		factory = new JButton("6. Build factory. $200");
		repair = new JButton("8. Repair");
		demolish = new JButton("7. Demolish. $5");
		cancel = new JButton("9. Cancel");
		pray.setEnabled(false);
		pray.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		cancel.setEnabled(false);
		
		pray.addActionListener(bl);
		road.addActionListener(bl);
		shrine.addActionListener(bl);
		mine.addActionListener(bl);
		base.addActionListener(bl);
		factory.addActionListener(bl);
		demolish.addActionListener(bl);
		repair.addActionListener(bl);
		cancel.addActionListener(bl);

		panel1.add(score);
		panel1.add(difficulty);
		panel1.add(minerals);
		panel1.add(money);
		panel1.add(pray);
		panel1.add(road);
		panel1.add(shrine);
		panel1.add(mine);
		panel1.add(base);
		panel1.add(factory);
		panel1.add(demolish);
		panel1.add(repair);
		panel1.add(cancel);
		panel3.add(distributionl, BorderLayout.NORTH);
		panel3.add(distribution, BorderLayout.CENTER);
		panel1.add(panel3);
		panel2.add(consolel, BorderLayout.NORTH);
		panel2.add(console, BorderLayout.CENTER);
		
		add(panel1);
		add(panel2);
		
		ActionMap actionMap = this.getActionMap();
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "q");
		actionMap.put("q", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.setLost(true);
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "1");
		actionMap.put("1", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				pray.doClick();
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "2");
		actionMap.put("2", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				road.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "3");
		actionMap.put("3", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				shrine.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0), "6");
		actionMap.put("6", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				factory.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "4");
		actionMap.put("4", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mine.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), "5");
		actionMap.put("5", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				base.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0), "8");
		actionMap.put("8", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				repair.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_9, 0), "9");
		actionMap.put("9", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cancel.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), "7");
		actionMap.put("7", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				demolish.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), "0");
		actionMap.put("0", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				buying();
				game.setBuildFlag(8);			}
		});

		JOptionPane.showMessageDialog(null,
				"Rules: RNGGod doesn't like you. Good luck.\n(Pay attention for gameplay tips in the log!)\nControls:\nNum keys: fast click button\n0: Query base tile's stats\nQ: quit");
		game.getNewGame().execute();
	}
	
	public static void setFlag(boolean f){
		flag = f;
		repair.setEnabled(true);
		cancel.setEnabled(false);
	}
	
	public static void setText(String text){
		if(console.getLineCount() >= 15){
			console.setText("");
		}
		console.append(text);
		console.repaint();
	}
	
	public static int getDistribution(){
		return distribution.getValue();
	}
	
	public static void setScore(long totalscore) {
		score.setText("   Score: " + Long.toString(totalscore));
	}

	public static void setDifficulty(int dif) {
		difficulty.setText("   Difficulty: " + Integer.toString(dif)
				+ "            ");
	}

	public static void updateMinerals(int min) {
		minerals.setText("   Minerals: " + Integer.toString(min)
				+ "            ");
	}

	public static void updateMoney(int mon) {
		money.setText("   Money: $" + Integer.toString(mon) + "            ");

		if (!flag) {
			if (mon < 5) {
				road.setEnabled(false);
				shrine.setEnabled(false);
				mine.setEnabled(false);
				base.setEnabled(false);
				factory.setEnabled(false);
				demolish.setEnabled(false);
			} else{
				road.setEnabled(true);
				demolish.setEnabled(true);
			}
			
			if (mon < 25) {
				shrine.setEnabled(false);
				mine.setEnabled(false);
				base.setEnabled(false);
				factory.setEnabled(false);
			} else
				mine.setEnabled(true);

			if (mon < 50) {
				shrine.setEnabled(false);
				factory.setEnabled(false);
				base.setEnabled(false);
			} else{
				shrine.setEnabled(true);
			}
			
			if (mon < 200) {
				factory.setEnabled(false);
				base.setEnabled(false);
			} else{
				factory.setEnabled(true);
			}
			
			if (mon < 500) {
				base.setEnabled(false);
			} else
				base.setEnabled(true);
		}
	}

	public void buying() {
		flag = true;
		road.setEnabled(false);
		shrine.setEnabled(false);
		mine.setEnabled(false);
		base.setEnabled(false);
		factory.setEnabled(false);
		repair.setEnabled(false);
		demolish.setEnabled(false);
		cancel.setEnabled(true);
	}

	public static void happiness(int happy){
		if(happy == 10){
			pray.setEnabled(false);
		}
		else
		{
			pray.setEnabled(true);
			pray.setBorder(BorderFactory.createLineBorder(Color.RED, 1+(18-happy*2)));
		}
	}
	
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("1. Pray to RNG God")) {
				game.sendPrayer();
			}

			if (command.equals("2. Build road. $5")) {
				buying();
				game.setBuildFlag(1);
				game.setMoney(game.getMoney() - 5);
			}

			if (command.equals("3. Build shrine. $50")) {
				buying();
				game.setBuildFlag(2);
				game.setMoney(game.getMoney() - 50);
			}

			if (command.equals("4. Build mine. $25")) {
				buying();
				game.setBuildFlag(3);
				game.setMoney(game.getMoney() - 25);
			}

			if (command.equals("5. Build base. $500")) {
				buying();
				game.setBuildFlag(4);
				game.setMoney(game.getMoney() - 500);
			}
			
			if (command.equals("7. Demolish. $5")) {
				buying();
				game.setBuildFlag(5);
				game.setMoney(game.getMoney() - 5);
			}
			
			if (command.equals("8. Repair")) {
				buying();
				game.setBuildFlag(6);
			}

			if (command.equals("6. Build factory. $200")) {
				buying();
				game.setBuildFlag(7);
				game.setMoney(game.getMoney() - 200);
			}
			
			if (command.equals("9. Cancel")) {
				game.refund();
				cancel.setEnabled(false);
				flag = false;
				road.setEnabled(true);
				shrine.setEnabled(true);
				mine.setEnabled(true);
				base.setEnabled(true);
				factory.setEnabled(true);
				demolish.setEnabled(true);
				cancel.setEnabled(false);
			}
		}
	}
}
