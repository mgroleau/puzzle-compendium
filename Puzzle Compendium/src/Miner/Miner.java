package Miner;

import java.awt.Color;
import javax.swing.*;
import Main.*;

public class Miner {

	private SwingWorker<Boolean, Void> newGame;
	private boolean lost = false, endReached = false, warned=false;
	private boolean dig = false, shop = false, usePowerUp = false,
			pause = false;
	private int difficulty = 1, O2 = 20, maxO2 = 20, position = 14, gold = 0,
			score = 0, orientation = 0, powerUp = 0, end = 200;
	private ImageIcon minerImage0, minerImage1, minerImage2;
	private GameBoard gb;
	private Miner miner;

	static int flashlights = 0, dynamite = 0, upgrades = 0;

	public SwingWorker<Boolean, Void> getNewGame() {
		return newGame;
	}

	public Miner(GameBoard gameboard) {
		gb = gameboard;
		minerImage0 = new ImageIcon("res/minerimage.jpg");
		minerImage1 = new ImageIcon("res/minerleft.jpg");
		minerImage2 = new ImageIcon("res/minerright.jpg");
		miner = this;

		newGame = new SwingWorker<Boolean, Void>() {
			public Boolean doInBackground() {
				while (!lost) {
					endReached = false;
					position = 14;
					randomizeLevel();
					setDifficulty();
					while (!endReached) {
						// Keep refreshing miner's position
						switch (orientation) {
						case 0:
							gb.getBlocks()[position].setIcon(minerImage0);
							break;
						case 1:
							gb.getBlocks()[position].setIcon(minerImage1);
							break;
						case 2:
							gb.getBlocks()[position].setIcon(minerImage2);
							break;
						}
						gb.getBlocks()[position].setEnabled(true);

						displayVision(0);

						// Check if a button was clicked
						if (dig) {
							gb.getBlocks()[position].setIcon(null);
							gb.getBlocks()[position].setEnabled(false);
							displayVision(1);
							move(0);
							if (gb.getBlocks()[position].isPath() == false) {
								mine();
								move(1);
							}
							O2--;

							dig = false;
						}
						if (shop) {
							Shop frame = new Shop(miner);
							shop = false;
						}
						if (usePowerUp) {
							pause = true;
							gb.enableInner();
							while (pause) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									Thread.currentThread().interrupt();
								}
							}
							gb.disable();

							usePowerUp = false;
						}

						updateScore();
						setO2();
						setGold();

						// Check if the end was reached
						if (position == end) {
							int reply = JOptionPane.showConfirmDialog(null,
									"Next Level?", "Success!",
									JOptionPane.YES_NO_OPTION);
							if (reply == JOptionPane.YES_OPTION) {
								endReached = true;
								score += gold * difficulty + difficulty * 100;
								difficulty++;
								gold = 0;
								reset(0);
							} else
								position -= 10;
						}
						
						if(O2==1 && warned == false){
							JOptionPane.showMessageDialog(null, "You are almost out of O2!");
							warned=true;
						}
						if(O2 != 1)
							warned=false;
						
						// Check if game was lost
						if (O2 == 0) {
							endReached = true;
							lost = true;
						}
					}
					if (lost) {
						Highscores.save(score, 1);
						int reply = JOptionPane.showConfirmDialog(null,
								"Play again?", "Game Over!",
								JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION)
							reset(1);

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
		MinerControlPanel.setDifficulty(difficulty);
	}

	public void updateScore() {
		MinerControlPanel.setScore(score);
	}

	public void setO2() {
		MinerControlPanel.setO2(O2, maxO2);
	}

	public void setGold() {
		MinerControlPanel.setGold(gold);
	}

	public void reset(int strength) {
		if (strength == 1) {
			score = 0;
			difficulty = 1;
			O2 = 20;
			maxO2 = 20;
			gold = 0;
			powerUp = 0;
			upgrades = 0;
			lost = false;
		}

		flashlights = 0;
		dynamite = 0;
		powerUp = 0;

		for (int i = 0; i < 200; i++) {
			gb.getBlocks()[i].reset();

		}
	}

	public void mine() {
		if (gb.getBlocks()[position].isGold() == true) {
			gb.getBlocks()[position].setBackground(new Color(255, 205, 0));
			int temp = orientation;
			if (!gb.getBlocks()[position].isLit()) {
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
				}
			}
			orientation=temp;
			gold += 10 * difficulty;
			gb.getBlocks()[position].setText("");
			gb.getBlocks()[position].setGold(false);
			gb.getBlocks()[position].setLit(false);
			gb.getBlocks()[position].setPath(true);
		}
		if (gb.getBlocks()[position].isRock() == true) {
			gb.getBlocks()[position].setLit(true);
			gb.getBlocks()[position]
					.setHits(gb.getBlocks()[position].getHits() - 1);
			gb.getBlocks()[position].setText("Rock +"
					+ gb.getBlocks()[position].getHits());

			if (gb.getBlocks()[position].getHits() == 0) {
				gb.getBlocks()[position].setText("");
				gb.getBlocks()[position].setRock(false);
				gb.getBlocks()[position].setPath(true);
				gb.getBlocks()[position].setLit(false);
				score += 10 * difficulty;
			}
		}
		if (gb.getBlocks()[position].isMetal() == true) {
			gb.getBlocks()[position].setLit(true);
			gb.getBlocks()[position]
					.setHits(gb.getBlocks()[position].getHits() - 1);
			gb.getBlocks()[position].setText("Metal +"
					+ gb.getBlocks()[position].getHits());

			if (gb.getBlocks()[position].getHits() == 0) {
				gb.getBlocks()[position].setText("");
				gb.getBlocks()[position].setMetal(false);
				gb.getBlocks()[position].setPath(true);
				gb.getBlocks()[position].setLit(false);
				score += 20 * difficulty;
			}
		}

		if (gb.getBlocks()[position].isDiamond() == true) {
			gb.getBlocks()[position].setLit(true);
			gb.getBlocks()[position]
					.setHits(gb.getBlocks()[position].getHits() - 1);
			gb.getBlocks()[position].setText("Diamond +"
					+ gb.getBlocks()[position].getHits());

			if (gb.getBlocks()[position].getHits() == 0) {
				gb.getBlocks()[position].setText("");
				gb.getBlocks()[position].setDiamond(false);
				gb.getBlocks()[position].setPath(true);
				gb.getBlocks()[position].setLit(false);
				score += 40 * difficulty;
			}
		}
	}

	public void move(int flag) {
		if (flag == 0) {
			switch (orientation) {
			case 0:
				if (position < 190)
					position += 10;
				break;
			case 1:
				if (position % 10 != 0)
					position -= 1;
				break;
			case 2:
				if (position % 10 != 9)
					position += 1;
				break;
			}
		} else {
			switch (orientation) {
			case 0:
				position -= 10;
				break;
			case 1:
				position += 1;
				break;
			case 2:
				position -= 1;
				break;
			}
		}
	}

	public void randomizeLevel() {
		int random = 0;

		gb.getBlocks()[position].setPath(true);

		for (int i = 0; i < 10; i++) {
			gb.getBlocks()[i].setText("XXX");
			gb.getBlocks()[i].setBackground(Color.green);
		}

		while (position < 190) {
			random = (int) (Math.random() * 3);
			switch (random) {
			case 0:
				position += 10;
				gb.getBlocks()[position].setPath(true);
				break;
			case 1:
				if (position % 10 != 0) {
					position--;
					gb.getBlocks()[position].setPath(true);
				}
				break;
			case 2:
				if (position % 10 != 9) {
					position++;
					gb.getBlocks()[position].setPath(true);
				}
				break;
			}
		}
		end = position;
		gb.getBlocks()[position].setEnabled(true);
		position = 10;
		for (int i = position; i < 200; i++) {
			if (gb.getBlocks()[i].isPath() == false) {
				random = (int) (Math.random() * (difficulty + 1));
				if(random == 0)
					gb.getBlocks()[i].setGold(true);
				else if (random % 5 == 0)
					gb.getBlocks()[i].setDiamond(true);
				else if (random % 4 == 0)
					gb.getBlocks()[i].setMetal(true);
				else if (random % 3 == 0)
					gb.getBlocks()[i].setGold(true);
				else
					gb.getBlocks()[i].setRock(true);
			}
		}
		position = 14;
	}

	public void displayVision(int flag) {
		if (position < 190) {
			gb.getBlocks()[position + 10].minerDisplay(flag, end);
			if (position % 10 != 0)
				gb.getBlocks()[position + 9].minerDisplay(flag, end);
			if (position % 10 != 9)
				gb.getBlocks()[position + 11].minerDisplay(flag, end);
		}
		if (orientation == 2)
			gb.getBlocks()[position - 1].minerDisplay(1, end);
		else if (position % 10 != 0)
			gb.getBlocks()[position - 1].minerDisplay(flag, end);
		if (orientation == 1)
			gb.getBlocks()[position + 1].minerDisplay(1, end);
		else if (position % 10 != 9)
			gb.getBlocks()[position + 1].minerDisplay(flag, end);
	}

	public void handleButton(Block b) {
		int num = b.getId();
		if (num != position && num != end) {
			switch (powerUp) {
			case 0:
				JOptionPane.showMessageDialog(null, "No Power Up Purchased!");
				break;
			case 1:
				gb.getBlocks()[num].minerDisplay(0, end);
				gb.getBlocks()[num].setLit(true);
				gb.getBlocks()[num - 1].minerDisplay(0, end);
				gb.getBlocks()[num - 1].setLit(true);
				gb.getBlocks()[num + 1].minerDisplay(0, end);
				gb.getBlocks()[num + 1].setLit(true);
				gb.getBlocks()[num - 10].minerDisplay(0, end);
				gb.getBlocks()[num - 10].setLit(true);
				gb.getBlocks()[num + 10].minerDisplay(0, end);
				gb.getBlocks()[num + 10].setLit(true);
				gb.getBlocks()[num - 11].minerDisplay(0, end);
				gb.getBlocks()[num - 11].setLit(true);
				gb.getBlocks()[num - 9].minerDisplay(0, end);
				gb.getBlocks()[num - 9].setLit(true);
				gb.getBlocks()[num + 9].minerDisplay(0, end);
				gb.getBlocks()[num + 9].setLit(true);
				gb.getBlocks()[num + 11].minerDisplay(0, end);
				gb.getBlocks()[num + 11].setLit(true);
				break;
			case 2:
				int demo = demolition(num);
				gb.getBlocks()[num].reset();
				gb.getBlocks()[num].setPath(true);
				gb.getBlocks()[num - 1].reset();
				gb.getBlocks()[num - 1].setPath(true);
				gb.getBlocks()[num + 1].reset();
				gb.getBlocks()[num + 1].setPath(true);
				gb.getBlocks()[num - 10].reset();
				gb.getBlocks()[num - 10].setPath(true);
				gb.getBlocks()[num + 10].reset();
				gb.getBlocks()[num + 10].setPath(true);
				gb.getBlocks()[num - 11].reset();
				gb.getBlocks()[num - 11].setPath(true);
				gb.getBlocks()[num - 9].reset();
				gb.getBlocks()[num - 9].setPath(true);
				gb.getBlocks()[num + 9].reset();
				gb.getBlocks()[num + 9].setPath(true);
				gb.getBlocks()[num + 11].reset();
				gb.getBlocks()[num + 11].setPath(true);
				JOptionPane.showMessageDialog(null, "Score increased by "
						+ demo + " for demolition!");
				score += demo;
				break;
			}
			powerUp = 0;
			pause = false;
			MinerControlPanel.powerup.setIcon(new ImageIcon("res/X.jpg"));
		}
	}

	public int demolition(int num) {
		int score = 0;
		if (gb.getBlocks()[num].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num - 1].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num - 1].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num - 1].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num + 1].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num + 1].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num + 1].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num + 10].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num + 10].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num + 10].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num - 10].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num - 10].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num - 10].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num + 11].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num + 11].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num + 11].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num - 11].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num - 11].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num - 11].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num + 9].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num + 9].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num + 9].isDiamond())
			score += 40 * difficulty;
		if (gb.getBlocks()[num - 9].isRock())
			score += 10 * difficulty;
		if (gb.getBlocks()[num - 9].isMetal())
			score += 20 * difficulty;
		if (gb.getBlocks()[num - 9].isDiamond())
			score += 40 * difficulty;
		return score;
	}

	public void setDig(boolean dig) {
		this.dig = dig;
	}

	public void setShop(boolean shop) {
		this.shop = shop;
	}

	public void setUsePowerUp(boolean usePowerUp) {
		this.usePowerUp = usePowerUp;
	}

	public boolean isPause() {
		return pause;
	}

	public void setOrientation(int i) {
		orientation = i;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int getO2() {
		return O2;
	}

	public int getMaxO2() {
		return maxO2;
	}

	public void setO2(int o2) {
		O2 = o2;
	}

	public void setMaxO2(int maxO2) {
		this.maxO2 = maxO2;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getPowerUp() {
		return powerUp;
	}

	public void setPowerUp(int powerUp) {
		this.powerUp = powerUp;
	}
}
