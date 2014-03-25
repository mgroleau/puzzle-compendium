package Miner;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

public class MinerControlPanel extends JPanel {

	private Miner game;
	private static JLabel gold, difficulty, score, O2;
	static JLabel powerup;

	public MinerControlPanel(Miner g) {
		game = g;
		setLayout(new GridLayout(5, 1));

		O2 = new JLabel("   O2: 10/10   ");
		gold = new JLabel("   Gold: " + Integer.toString(0) + "  ");
		difficulty = new JLabel("   Difficulty: " + Integer.toString(0) + "  ");
		score = new JLabel("   Score: " + Integer.toString(0) + "  ");	
		powerup = new JLabel(new ImageIcon("res/X.jpg"));
		
		add(O2);
		add(gold);
		add(difficulty);
		add(score);
		add(powerup);

		ActionMap actionMap = this.getActionMap();
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "d");
		actionMap.put("d", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isPause() == false)
					game.setDig(true);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s");
		actionMap.put("s", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isPause() == false)
					game.setShop(true);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a");
		actionMap.put("a", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isPause() == false)
					game.setUsePowerUp(true);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		actionMap.put("down", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isPause() == false)
					game.setOrientation(0);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		actionMap.put("left", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isPause() == false)
					game.setOrientation(1);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		actionMap.put("right", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (game.isPause() == false)
					game.setOrientation(2);
			}
		});

		JOptionPane
				.showMessageDialog(
						null,
						"Rules: Try to reach the end of the maze at the bottom of the mine scoring points as\nyou go. Be careful not to run out of Oxygen! Buy flashlights to permanently\nreveal a 3x3 area. Buy dynamite to destroy a 3x3 area making a walkable path\nand scoring points for non-gold blocks that are destroyed. Upgrades cost\nexponentially more the more you buy per level. As the difficulty increases\nthe percentage of gold in the mine decreases and the cost of upgrades increases!\nAt the end of each level your gold is converted into score and Oxygen is not refilled.\n\nHotkeys:\nD: Dig\nS: Open Shop\nA: Use Power Up\nArrow Keys: Change Orientation");
		game.getNewGame().execute();
	}

	public static void setScore(long totalscore) {
		score.setText("   Score: " + Long.toString(totalscore) + "  ");
	}

	public static void setDifficulty(int dif) {
		difficulty.setText("   Difficulty: " + Integer.toString(dif) + "  ");
	}

	public static void setO2(int Oh2, int maxO2) {
		if (Oh2 <= 5)
			O2.setForeground(Color.red);
		else
			O2.setForeground(Color.black);

		int num = Integer.toString(Oh2).length()
				+ Integer.toString(maxO2).length() + 1;

		O2.setText("   O2: " + Integer.toString(Oh2) + "/"
				+ Integer.toString(maxO2) + "  ");

		for (int i = 0; i < 5 - num; i++)
			O2.setText(O2.getText() + " ");

	}

	public static void setGold(int g) {
		gold.setText("   Gold: " + Integer.toString(g) + "  ");
	}
}
