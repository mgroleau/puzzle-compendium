package ManPac;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.*;
import Main.*;

public class ManPac {
	private GameBoard gb;
	private ManPac manpac;
	private SwingWorker<Boolean, Void> newGame;
	private Boolean dead = false, levelend = false;
	private int orientation = 0, position = 95, score = 0, difficulty = 1,
			flag = 0, killed = 0;
	private int[] map = { 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0,
			0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0,
			1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0,
			0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1,
			0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1,
			0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1,
			1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1,
			0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1,
			0, 0, 1, 1, 0, 1 };
	private ArrayList<Integer> topwarps = new ArrayList<Integer>();
	private ArrayList<Integer> leftwarps = new ArrayList<Integer>();
	private ArrayList<Integer> rightwarps = new ArrayList<Integer>();
	private ArrayList<Integer> botwarps = new ArrayList<Integer>();

	private final int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;

	private ImageIcon manImage0, manImage1, manImage2, manImage3;
	private Seeker[] seekers;
	public SwingWorker<Boolean, Void> getNewGame() {
		return newGame;
	}

	public ManPac(GameBoard gameboard) {
		gb = gameboard;
		manpac = this;
		manImage0 = new ImageIcon("res/manpacdown.png");
		manImage1 = new ImageIcon("res/manpacleft.png");
		manImage2 = new ImageIcon("res/manpacright.png");
		manImage3 = new ImageIcon("res/manpacup.png");

		topwarps.add(1);
		topwarps.add(4);
		topwarps.add(5);
		topwarps.add(8);
		leftwarps.add(60);
		leftwarps.add(90);
		leftwarps.add(100);
		leftwarps.add(130);
		rightwarps.add(69);
		rightwarps.add(99);
		rightwarps.add(109);
		rightwarps.add(139);
		botwarps.add(191);
		botwarps.add(194);
		botwarps.add(195);
		botwarps.add(198);

		for (int i = 0; i < 200; i++) {
			if (map[i] == 1)
				gb.getBlocks()[i].setWall(true);
			else
				gb.getBlocks()[i].setBackground(Color.blue);
			gb.getBlocks()[i].setEnabled(true);
		}

		newGame = new SwingWorker<Boolean, Void>() {
			public Boolean doInBackground() {
				while (!dead) {
					if (difficulty == 17) {
						dead = true;
						Highscores.save(score, 2);
						int reply = JOptionPane.showConfirmDialog(null,
								"Play again?", "You win!",
								JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION)
							reset(1);

						levelend = true;
					} else {
						position = 115;
						gb.getBlocks()[position].setIcon(manImage0);
						levelend = false;
						setDifficulty();
						seekers = new Seeker[difficulty];
						for (int i = 0; i < difficulty; i++) {
							if (i % 4 == 0)
								seekers[i] = new Seeker(gb, manpac, topwarps.get(i / 4) + 10);
							else if (i % 4 == 1)
								seekers[i]= new Seeker(gb, manpac, leftwarps.get(i / 4) + 1);
							else if (i % 4 == 2)
								seekers[i] = new Seeker(gb, manpac,
										rightwarps.get(i / 4) - 1);
							else
								seekers[i] = new Seeker(gb, manpac, botwarps.get(i / 4) - 10);

							seekers[i].start();
						}
					}

					while (!levelend) {
						updateScore();

						if (killed == difficulty)
							levelend = true;

						if (levelend) {
							JOptionPane.showMessageDialog(null, "Success!");
							score += difficulty * 1000;
							difficulty++;
							reset(0);
						}
						
						if(position % 10 != 9)
							if(gb.getBlocks()[position+1].getS()!=null){
								gb.getBlocks()[position+1].getS().setWin(true);
								dead=true;
							}
						if(position % 10 != 0)
							if(gb.getBlocks()[position-1].getS()!=null){
								gb.getBlocks()[position-1].getS().setWin(true);
								dead=true;
							}
						if(position<190)
							if(gb.getBlocks()[position+10].getS()!=null){
								gb.getBlocks()[position+10].getS().setWin(true);
								dead=true;
							}
						if(position>10)
							if(gb.getBlocks()[position-10].getS()!=null){
								gb.getBlocks()[position-10].getS().setWin(true);
								dead=true;
							}
						
						if (dead) {
							Highscores.save(score, 2);
							int reply = JOptionPane.showConfirmDialog(null,
									"Play again?", "Game Over!",
									JOptionPane.YES_NO_OPTION);
							if (reply == JOptionPane.YES_OPTION)
								reset(1);

							levelend = true;
						}
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
		ManPacControlPanel.setDifficulty(difficulty);
	}

	public void updateScore() {
		ManPacControlPanel.setScore(score);
	}

	public void reset(int strength) {
		if (strength == 1) {
			score = 0;
			difficulty = 1;
			dead = false;
		}

		killed = 0;

		for (int i = 0; i < 200; i++) {
			gb.getBlocks()[i].reset();
			if (map[i] == 1)
				gb.getBlocks()[i].setWall(true);
			else
				gb.getBlocks()[i].setBackground(Color.blue);
		}
	}

	public void move() {
		if (topwarps.contains(position) && orientation == UP) {
			gb.getBlocks()[position].setIcon(null);
			position += 190;
			flag = 1;
			gb.getBlocks()[position].setIcon(manImage0);
		} else if (leftwarps.contains(position) && orientation == LEFT) {
			gb.getBlocks()[position].setIcon(null);
			position += 9;
			flag = 1;
			gb.getBlocks()[position].setIcon(manImage1);
		} else if (rightwarps.contains(position) && orientation == RIGHT) {
			gb.getBlocks()[position].setIcon(null);
			position -= 9;
			flag = 1;
			gb.getBlocks()[position].setIcon(manImage2);
		} else if (botwarps.contains(position) && orientation == DOWN) {
			gb.getBlocks()[position].setIcon(null);
			position -= 190;
			flag = 1;
			gb.getBlocks()[position].setIcon(manImage3);
		} else {
			switch (orientation) {

			case DOWN:
				if (gb.getBlocks()[position + 10].isWall() == false) {
					gb.getBlocks()[position].setIcon(null);
					position += 10;
					flag = 1;
					gb.getBlocks()[position].setIcon(manImage0);
				}
				break;
			case LEFT:
				if (gb.getBlocks()[position - 1].isWall() == false) {
					gb.getBlocks()[position].setIcon(null);
					position -= 1;
					flag = 1;
					gb.getBlocks()[position].setIcon(manImage1);
				}
				break;
			case RIGHT:
				if (gb.getBlocks()[position + 1].isWall() == false) {
					gb.getBlocks()[position].setIcon(null);
					position += 1;
					flag = 1;
					gb.getBlocks()[position].setIcon(manImage2);
				}
				break;
			case UP:
				if (gb.getBlocks()[position - 10].isWall() == false) {
					gb.getBlocks()[position].setIcon(null);
					position -= 10;
					flag = 1;
					gb.getBlocks()[position].setIcon(manImage3);
				}
				break;
			}
		}

		if (flag == 1) {
			if (gb.getBlocks()[position].isEaten() == false) {
				score += 10 * difficulty;
				gb.getBlocks()[position].setEaten(true);
			}

			flag = 0;
		}

		
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
		move();
	}

	public int getPosition() {
		return position;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void setKilled() {
		killed++;
		score += 500 * difficulty;
	}
	
	public void lose(){
		for(int i=0;i<difficulty;i++)
			seekers[i].setWin(true);
	}

	public void handleButton(Block b) {
		int num = b.getId();

		int position2 = position;
		while (gb.getBlocks()[position2].isWall() == false) {
			if (botwarps.contains(position2)) {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 -= 190;
			} else {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 += 10;
			}
		}
		position2 = position;
		while (gb.getBlocks()[position2].isWall() == false) {
			if (rightwarps.contains(position2)) {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 -= 9;
			} else {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 += 1;
			}
		}
		position2 = position;
		while (gb.getBlocks()[position2].isWall() == false) {
			if (leftwarps.contains(position2)) {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 += 9;
			} else {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 -= 1;
			}
		}
		position2 = position;
		while (gb.getBlocks()[position2].isWall() == false) {
			if (topwarps.contains(position2)) {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 += 190;
			} else {
				if (gb.getBlocks()[position2].getS() != null && num==position2) {
					gb.getBlocks()[position2].getS().setDead(true);
					gb.getBlocks()[position2].setS(null);
				}
				position2 -= 10;
			}
		}
	}
}
