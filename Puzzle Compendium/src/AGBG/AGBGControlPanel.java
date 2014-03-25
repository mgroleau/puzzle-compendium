package AGBG;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class AGBGControlPanel extends JPanel {
	private AGBG game;
	private JButton challenge, pause, swap, speedup;
	private static JLabel score, difficulty, image1, image2;
	private ButtonHandler bl = new ButtonHandler();

	public AGBGControlPanel(AGBG g) {
		game = g;
		setLayout(new GridLayout(4, 2));
		challenge = new JButton("Challenge Mode");
		challenge.addActionListener(bl);
		pause = new JButton("Pause");
		pause.addActionListener(bl);
		swap = new JButton("Clear Mode");
		swap.addActionListener(bl);
		speedup = new JButton("Speedup");
		speedup.addActionListener(bl);
		score = new JLabel("Score: " + Integer.toString(0));
		difficulty = new JLabel("Difficulty: " + Integer.toString(0));
		image1 = new JLabel(new ImageIcon("res/photo2.jpg"));
		image2 = new JLabel(new ImageIcon("res/photo3.jpg"));

		add(score);
		add(challenge);
		add(difficulty);
		add(pause);
		add(image1);
		add(swap);
		add(image2);
		add(speedup);

		ActionMap actionMap = this.getActionMap();
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "e");
		actionMap.put("e", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				swap.doClick();
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "q");
		actionMap.put("q", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				speedup.doClick();
			}
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "d");
		actionMap.put("d", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.swap(0);
			}
		});

		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "w");
		actionMap.put("w", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.swap(1);
			}
		});

		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a");
		actionMap.put("a", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.swap(2);
			}
		});

		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s");
		actionMap.put("s", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.swap(3);
			}
		});


		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "p");
		actionMap.put("P", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				pause.doClick();
			}
		});
		JOptionPane
				.showMessageDialog(
						null,
						"Rules: Aim for the highest score!\n\nWhen clear mode is active, click areas of 4 or more connected same color blocks to erase them and score.\nThe bigger the area the more points you get. When swap mode is active, click a block you want to move and \npress a directional key to swap colors. The current mode is displayed on a button on the left hand side. As \nthe difficulty increases, the blocks will fall faster and you will have more colors to deal with. Click the challenge \nmode button for a true challenge!\n\nHotkeys:\np: Pause/Resume\ne: Switch modes\nq: Speedup\nw/a/s/d: Directional input");
		game.getNewGame().execute();
	}

	public static void setScore(long totalscore) {
		score.setText("Score: " + Long.toString(totalscore));
	}

	public static void setDifficulty(int dif) {
		difficulty.setText("Difficulty: " + Integer.toString(dif));
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("Challenge Mode")) {
				game.reset();
				game.setChallenge(true);
			}
			if (command.equals("Clear Mode")) {
				game.setSwap(true);
				swap.setText("Swap Mode");
			}
			if (command.equals("Swap Mode")) {
				game.setSwap(false);
				swap.setText("Clear Mode");
			}
			if (command.equals("Pause")) {
				game.setPause(true);
				pause.setText("Resume");
			}
			if (command.equals("Resume")) {
				game.setPause(false);
				pause.setText("Pause");
			}
			if (command.equals("Speedup"))
				game.setSpeedup(30 - 2 * Integer.parseInt(difficulty.getText()
						.substring(12)));
		}
	}
}
