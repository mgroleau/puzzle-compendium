package BasedRNGGod;

import java.awt.Color;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import Main.Block;
import Main.GameBoard;
import Main.Highscores;
import Main.Menu;

public class BasedRNGGod {

	private SwingWorker<Boolean, Void> newGame;
	private GameBoard gb;
	private int difficulty, score, minerals, money;
	private int buildFlag = 0;
	private boolean lost;
	private int numShrines;
	private RNGGod rgod;
	private Semaphore semaphore = new Semaphore(1);

	public SwingWorker<Boolean, Void> getNewGame() {
		return newGame;
	}

	public BasedRNGGod(GameBoard gameboard) {
		gb = gameboard;
		rgod = new RNGGod(gb, this);
		difficulty = 1;
		score = 0;
		money = 200;
		lost = false;

		newGame = new SwingWorker<Boolean, Void>() {
			public Boolean doInBackground() {
				rgod.start();
				gb.enable();

				while (!lost) {
					score += rgod.getWorkers();
					money += rgod.getWorkers();
					if (score > difficulty * 5000 && difficulty <= 10)
						difficulty++;
					setDifficulty();
					updateScore();
					updateMinerals();
					updateMoney();

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}
					productivity();

					if (lost) {
						Highscores.save(score, 3);
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

	public void reset() {
		difficulty = 1;
		score = 0;
		money = 200;
		lost = false;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty() {
		BasedRNGGodControlPanel.setDifficulty(difficulty);
	}

	public void updateMinerals() {
		BasedRNGGodControlPanel.updateMinerals(minerals);
	}

	public void updateMoney() {
		BasedRNGGodControlPanel.updateMoney(money);
	}

	public void updateScore() {
		BasedRNGGodControlPanel.setScore(score);
	}

	public int getMinerals() {
		return minerals;
	}

	public void addMinerals(int minerals) {
		this.minerals += minerals;
	}

	public void setMinerals(int minerals) {
		this.minerals = minerals;
	}

	public void addMoney(int money) {
		this.money += money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getMoney() {
		return money;
	}

	public void setLost(boolean lose) {
		lost = lose;
	}

	public boolean getLost() {
		return lost;
	}

	public void sendPrayer() {
		String[] options = new String[] { "$50. Might work...",
				"$500. Will probably work.", "Human sacrifice. Will work!" };
		int i = JOptionPane.showOptionDialog(null, "Offer to the RNG God!",
				"What will you give?", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (i == 0) {
			if (money < 50)
				BasedRNGGodControlPanel.setText("You can't afford that!\n");
			else {
				money -= 50;
				rgod.acceptPrayer(50);
			}
		}
		if (i == 1) {
			if (money < 500)
				BasedRNGGodControlPanel.setText("You can't afford that!\n");
			else {
				money -= 500;
				rgod.acceptPrayer(500);
			}
		}
		if (i == 2) {
			boolean success = false;
			for (int j = 0; j < 200 && success == false; j++) {
				if (gb.getBlocks()[j].isBase()) {
					int x = gb.getBlocks()[j].getBase().sacrifice();
					if (x == 0) {
						gb.getBlocks()[j].reset();
						redrawMap();
					}

					success = true;
				}
			}
			if (success) {
				rgod.acceptPrayer(1);
			} else
				BasedRNGGodControlPanel
						.setText("You have no workers to sacrifice!\n");
		}

	}

	public void setBuildFlag(int flag) {
		buildFlag = flag;
	}

	public void productivity() {
		try {
			semaphore.acquire();
			for (int i = 0; i < 200; i++) {
				if (gb.getBlocks()[i].isBase() && !gb.getBlocks()[i].isBroken()) {
					int p = gb.getBlocks()[i].getBase().productivity();
					Thread.sleep(300);
					gb.getBlocks()[i].getBase().convert();
				}
			}
		} catch (InterruptedException e) {

		}
		semaphore.release();
	}

	public void redrawMap() {
		try {
			semaphore.acquire();

			String road = "";
			numShrines = 0;
			int numFilled = 0;

			for (int i = 0; i < 200; i++) {
				gb.getBlocks()[i].setText("");

				if (!gb.getBlocks()[i].isEmpty()) {
					numFilled++;
				}
				if (gb.getBlocks()[i].isRoad()) {
					road = "";
					if (i - 10 < 0)
						road += "n";
					else if (gb.getBlocks()[i - 10].isRoad())
						road += "y";
					else
						road += "n";

					if (i + 10 > 199)
						road += "n";
					else if (gb.getBlocks()[i + 10].isRoad())
						road += "y";
					else
						road += "n";

					if (i % 10 == 0)
						road += "n";
					else if (gb.getBlocks()[i - 1].isRoad())
						road += "y";
					else
						road += "n";

					if (i % 10 == 9)
						road += "n";
					else if (gb.getBlocks()[i + 1].isRoad())
						road += "y";
					else
						road += "n";

					road += ".png";

					gb.getBlocks()[i]
							.setIcon(new ImageIcon("res/roads/" + road));
				}

				if (gb.getBlocks()[i].isShrine()) {
					gb.getBlocks()[i].setIcon(new ImageIcon("res/shrine.png"));
					if (!gb.getBlocks()[i].isBroken()) {
						numShrines++;
					}
				}

				if (gb.getBlocks()[i].isMine()) {
					gb.getBlocks()[i].setIcon(new ImageIcon("res/mine.png"));
				}

				if (gb.getBlocks()[i].isBase()) {
					gb.getBlocks()[i].setIcon(new ImageIcon("res/base.png"));
				}

				if (gb.getBlocks()[i].isDestroyed()) {
					gb.getBlocks()[i]
							.setIcon(new ImageIcon("res/destroyed.png"));
				}

				if (gb.getBlocks()[i].isBlackHole()) {
					gb.getBlocks()[i]
							.setIcon(new ImageIcon("res/blackhole.png"));
				}

				if (gb.getBlocks()[i].isMineral()) {
					gb.getBlocks()[i].setIcon(new ImageIcon("res/mineral.png"));
				}

				if (gb.getBlocks()[i].isFactory()) {
					gb.getBlocks()[i].setIcon(new ImageIcon("res/factory.png"));
				}
				if (gb.getBlocks()[i].isEmpty()) {
					gb.getBlocks()[i].setForeground(Color.WHITE);
					gb.getBlocks()[i].setText(i + "");
				}
				if (gb.getBlocks()[i].isBroken()) {
					gb.getBlocks()[i].setIcon(new ImageIcon("res/broken.png"));
				}
				if (numFilled == 198)
					gb.getBlocks()[199].reset();
			}
		} catch (InterruptedException e) {

		}
		semaphore.release();
	}

	public void handleButton(Block b) {
		int num = b.getId();
		if (gb.getBlocks()[num].isDestroyed()
				|| gb.getBlocks()[num].isBlackHole()) {
			BasedRNGGodControlPanel.setText("It's just a wasteland!\n");
			refund();
		} else if (gb.getBlocks()[num].isEmpty() == false && buildFlag != 6
				&& buildFlag != 5 && buildFlag != 8) {
			BasedRNGGodControlPanel
					.setText("There's something already built there!\n");
			refund();
		} else {
			switch (buildFlag) {
			case 0:
				break;
			// road
			case 1:
				gb.getBlocks()[num].setRoad(true);
				gb.getBlocks()[num].setEmpty(false);
				break;
			// shrine
			case 2:
				gb.getBlocks()[num].setShrine(true);
				gb.getBlocks()[num].setEmpty(false);
				break;
			// mine
			case 3:
				gb.getBlocks()[num].setMine(true);
				gb.getBlocks()[num].setEmpty(false);
				break;
			// base
			case 4:
				gb.getBlocks()[num].setBase(true);
				gb.getBlocks()[num].setBase(new Base(this, num, gb));
				RNGGod.newBase();
				gb.getBlocks()[num].setEmpty(false);
				break;
			// demolish
			case 5:
				gb.getBlocks()[num].reset();
				gb.getBlocks()[num].setEmpty(true);
				break;
			// repair
			case 6:
				repair(num);
				break;
			// factory
			case 7:
				gb.getBlocks()[num].setFactory(true);
				gb.getBlocks()[num].setEmpty(false);
				break;
			// query
			case 8:
				if (gb.getBlocks()[num].isBase()){
					BasedRNGGodControlPanel.setText("Workers at base #" + num
							+ ": " + gb.getBlocks()[num].getBase().getWorkers()
							+ "\n");
					int base = gb.getBlocks()[num].getBase().getConnectedTo();
					if(base == -1)
						BasedRNGGodControlPanel.setText("This base has not made a connection\n");
					else
						BasedRNGGodControlPanel.setText("This base is connected to base#" + gb.getBlocks()[num].getBase().getConnectedTo() + "\n");
					BasedRNGGodControlPanel.setText(gb.getBlocks()[num].getBase().output());
				}
				break;
			}
		}

		redrawMap();
		BasedRNGGodControlPanel.setFlag(false);
		buildFlag = 0;
	}

	public void refund() {
		switch (buildFlag) {
		case 0:
			break;
		case 1:
			money += 5;
			break;
		case 2:
			money += 50;
			break;
		case 3:
			money += 25;
			break;
		case 4:
			money += 500;
			break;
		case 5:
			money += 5;
			break;
		case 6:
			break;
		case 7:
			money += 200;
			break;
		}
		buildFlag = 0;
	}

	public void repair(int num) {
		int owed = 0;
		if (gb.getBlocks()[num].isBroken() == true) {
			if (gb.getBlocks()[num].isRoad())
				owed = 3;
			if (gb.getBlocks()[num].isShrine())
				owed = 25;
			if (gb.getBlocks()[num].isFactory())
				owed = 100;
			if (gb.getBlocks()[num].isMine())
				owed = 13;
			if (gb.getBlocks()[num].isBase())
				owed = 250;
		}

		if (owed == 0)
			BasedRNGGodControlPanel.setText("Nothing to repair!\n");
		else if (money > owed) {
			money -= owed;
			gb.getBlocks()[num].setBroken(false);
		} else
			BasedRNGGodControlPanel
					.setText("You can't afford to repair that!\n");
	}

	public int getShrines() {
		return numShrines;
	}
}
