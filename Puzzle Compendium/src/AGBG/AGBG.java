package AGBG;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import Main.*;

public class AGBG {

	private SwingWorker<Boolean, Void> newGame;
	private int difficulty = 1;
	private long score;
	private boolean pause = false, swap = false, challenge = false;
	private int swap1 = 200, swap2 = 200, speedup = 0;
	private GameBoard gb;

	public SwingWorker<Boolean, Void> getNewGame() {
		return newGame;
	}

	public AGBG(GameBoard gameboard) {
		gb = gameboard;
		newGame = new SwingWorker<Boolean, Void>() {
			public Boolean doInBackground() {
				int counter = 0;
				score = 1;
				for (int i = 200; i < 210; i++) {
					gb.getBlocks()[i] = new Block(i);
					gb.getBlocks()[i].setLanded();
				}
				while (checkLoss(counter)) {
					if (challenge) {
						difficulty = 10;
					} else {
						setDifficulty();
					}
					updateScore();
					if (pause) {
						pause(0);
						while (pause) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
						pause(1);
					}

					gb.getBlocks()[counter].setCreated(difficulty);
					counter++;
					if (counter == 10) {
						counter = 0;
					}
					if (speedup == 0) {
						try {
							Thread.sleep(1000 - difficulty * 75);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					} else {
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						speedup--;
					}
					fall();
					if (!checkLoss(counter)) {
						Highscores.save(score, 0);
						int reply = JOptionPane.showConfirmDialog(null,
								"Play again?", "Game Over!",
								JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION) 
							reset();
					}
				}
				return true;
			}

			public void done() {
				Menu m = new Menu();
				gb.end();
			}
		};

	}

	public void setDifficulty() {
		difficulty = (int) Math.min(10, Math.log10(score));
		AGBGControlPanel.setDifficulty(difficulty);
	}

	public void updateScore() {
		AGBGControlPanel.setScore(score);
	}

	public void setChallenge(boolean challenge) {
		this.challenge = challenge;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}

	public void setSpeedup(int speedup) {
		this.speedup = speedup;
	}

	public void setSwap(boolean swap) {
		this.swap = swap;
	}

	public void swap(int flag) {
		if (swap1 != 200) {
			switch (flag) {
			case 0:
				if ((swap1 + 1) % 10 != 0
						&& gb.getBlocks()[swap1 + 1].isEnabled())
					swap2 = swap1 + 1;
				break;
			case 1:
				if ((swap1 - 10) >= 0 && gb.getBlocks()[swap1 - 10].isEnabled())
					swap2 = swap1 - 10;
				break;
			case 2:
				if ((swap1 - 1) % 10 != 9
						&& gb.getBlocks()[swap1 - 1].isEnabled())
					swap2 = swap1 - 1;
				break;
			case 3:
				if ((swap1 + 10) < 200)
					swap2 = swap1 + 10;
				break;
			}
		}
		if (swap2 != 200) {
			Color temp = gb.getBlocks()[swap1].getBackground();
			gb.getBlocks()[swap1].setBackground(gb.getBlocks()[swap2]
					.getBackground());
			gb.getBlocks()[swap2].setBackground(temp);
			gb.getBlocks()[swap2].setBorder(BorderFactory.createLineBorder(
					Color.white, 4));
			gb.getBlocks()[swap1].setBorder(UIManager
					.getBorder("Button.border"));
			swap1 = swap2;
			swap2 = 200;
		}
	}

	public void pause(int flag) {
		if (flag == 0) {
			for (int i = 0; i < 200; i++) {
				gb.getBlocks()[i].setEnabled(false);
			}
		} else {
			for (int i = 0; i < 200; i++) {
				if (gb.getBlocks()[i].isLanded()) {
					gb.getBlocks()[i].setEnabled(true);
				}
			}
		}
	}

	public int calcscore(int size) {
		int points = 0;
		if (challenge) {
			points += size;
		} else {
			for (int i = 4; i <= size; i++) {
				points += i;
			}
			points *= Math.max(Math.pow(10, difficulty - 2), .1);
		}
		return points;
	}

	public void reset() {
		score = 1;
		difficulty = 1;
		challenge = false;
		
		for (int i = 0; i < 200; i++) {
			gb.getBlocks()[i].reset();
			
		}
	}

	public void fall() {
		for (int i = 199; i >= 0; i--) {
			if (gb.getBlocks()[i + 10].isLanded()
					&& gb.getBlocks()[i].isFalling()) {
				gb.getBlocks()[i].setLanded();
			}
			if (gb.getBlocks()[i].isFalling()) {
				gb.getBlocks()[i + 10].setBackground(gb.getBlocks()[i]
						.getBackground());
				gb.getBlocks()[i + 10].setFalling();
				gb.getBlocks()[i].reset();
			}
		}
	}

	public boolean checkLoss(int counter) {
		if (gb.getBlocks()[counter].getBackground() != Color.black) {
			return false;
		}

		return true;
	}

	public void handleButton(Block b) {
		int num = b.getId();
		int counter = 0;

		if (swap == true) {
			gb.getBlocks()[swap1].setBorder(UIManager
					.getBorder("Button.border"));
			gb.getBlocks()[swap1].setEnabled(true);
			swap1 = num;
			gb.getBlocks()[swap1].setBorder(BorderFactory.createLineBorder(
					Color.white, 4));
		} else {
			gb.getBlocks()[swap1].setBorder(UIManager
					.getBorder("Button.border"));
			swap1 = 200;

			ArrayList<Integer> solution = new ArrayList<Integer>();

			solution.add(num);

			while (solution.size() > counter) {
				num = solution.get(counter);
				if ((num > 9)
						&& (solution.contains(num - 10) == false)
						&& (gb.getBlocks()[num - 10].getBackground() == gb
								.getBlocks()[num].getBackground())) {
					solution.add(num - 10);

				}
				if ((num < 190)
						&& (solution.contains(num + 10) == false)
						&& (gb.getBlocks()[num + 10].getBackground() == gb
								.getBlocks()[num].getBackground())) {
					solution.add(num + 10);

				}
				if ((num % 10 != 9)
						&& (solution.contains(num + 1) == false)
						&& (gb.getBlocks()[num + 1].getBackground() == gb
								.getBlocks()[num].getBackground())) {
					solution.add(num + 1);

				}
				if ((num % 10 != 0)
						&& (solution.contains(num - 1) == false)
						&& (gb.getBlocks()[num - 1].getBackground() == gb
								.getBlocks()[num].getBackground())) {
					solution.add(num - 1);

				}
				counter++;
			}

			if (solution.size() > 3) {
				int points = calcscore(solution.size());
				score += points;
				for (int i = 0; i < solution.size(); i++) {
					int j = solution.get(i);
					gb.getBlocks()[j].reset();
					while (j > 9) {
						j -= 10;
						if (gb.getBlocks()[j].isLanded()) {
							gb.getBlocks()[j].setFalling();
						}
					}
				}
			}
		}
	}
}
