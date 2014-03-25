package ManPac;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ManPacControlPanel extends JPanel {

	private ManPac game;
	private static JLabel score, difficulty;

	public ManPacControlPanel(ManPac g) {
		game = g;
		setLayout(new GridLayout(2, 1));

		difficulty = new JLabel("   Difficulty: " + Integer.toString(0) + "            ");
		score = new JLabel("   Score: " + Integer.toString(0));

		add(score);
		add(difficulty);

		ActionMap actionMap = this.getActionMap();
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s");
		actionMap.put("s", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.setOrientation(0);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a");
		actionMap.put("a", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.setOrientation(1);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "d");
		actionMap.put("d", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.setOrientation(2);
			}
		});

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "w");
		actionMap.put("w", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.setOrientation(3);
			}
		});

		JOptionPane
				.showMessageDialog(
						null,
						"Rules: Run over blue squares to score points, but avoid the seekers!\nIf they touch you your game will end, but if you kill them all you\ncan advance to the next level. To kill a seeker click them when\nManPac has them in his line of sight.\n\nHotkeys:\nw/a/s/d: Directional input");
		game.getNewGame().execute();
	}

	public static void setScore(long totalscore) {
		score.setText("   Score: " + Long.toString(totalscore));
	}

	public static void setDifficulty(int dif) {
		difficulty.setText("   Difficulty: " + Integer.toString(dif) + "            ");
	}
}
