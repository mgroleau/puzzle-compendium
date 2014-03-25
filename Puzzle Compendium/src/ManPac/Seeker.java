package ManPac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.ImageIcon;
import Main.*;

public class Seeker extends Thread {

	private GameBoard gb;
	private ManPac manpac;
	private int position;
	private boolean dead = false, win = false;
	private int[] marks = new int[200];
	private int[] warps = { 1, 4, 5, 8, 60, 90, 100, 130, 69, 99, 109, 139,
			191, 194, 195, 198 };
	private int manPacLoc;
	private int temp;
	private final int DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3;

	private ImageIcon seekImage0, seekImage1, seekImage2, seekImage3;

	public Seeker(GameBoard gameboard, ManPac game, int pos) {
		gb = gameboard;
		position = pos;
		manpac = game;

		seekImage0 = new ImageIcon("res/seekerdown.png");
		seekImage1 = new ImageIcon("res/seekerleft.png");
		seekImage2 = new ImageIcon("res/seekerright.png");
		seekImage3 = new ImageIcon("res/seekerup.png");
	}

	public void run() {
		gb.getBlocks()[position].setIcon(seekImage0);
		while (!dead && !win) {
			gb.getBlocks()[position].setS(this);
			move();
		}

		gb.getBlocks()[position].setIcon(null);
		gb.getBlocks()[position].setS(null);

		if (!win)
			manpac.setKilled();
		else
			manpac.lose();
	}

	public void move() {
		manPacLoc = manpac.getPosition();

		for (int i = 0; i < 200; i++) {
			if (gb.getBlocks()[i].isWall())
				marks[i] = 999;
			else
				marks[i] = -1;
		}
		for (int i = 0; i < warps.length; i++) {
			marks[warps[i]] = 999;
		}

		marks[position] = 0;
		int dir = -1;

		if (!checkInArray(warps)) {
			breadthFirst();
			dir = retrace();
		}

		if (dir != -1) {
			updateDisplay(dir);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {

			}
		}
	}
	
	public boolean checkInArray(int[] myArray) {
		    int i = 0;
		
		    for (; i < warps.length; i++) {
		        if(warps[i] == manPacLoc)
		           break;
		    }		
		    return i != myArray.length;
		}


	public void breadthFirst() {
		int i = 0;

		while (true) {
			i++;
			for (int j = 11; j < 189; j++) {
				if (marks[j] == i - 1) {
					if (marks[j + 1] == -1) {
						marks[j + 1] = i;
						if ((j + 1) == manPacLoc)
							return;
					}
					if (marks[j + 10] == -1) {
						marks[j + 10] = i;
						if ((j + 10) == manPacLoc)
							return;
					}
					if (marks[j - 1] == -1) {
						marks[j - 1] = i;
						if ((j - 1) == manPacLoc)
							return;
					}
					if (marks[j - 10] == -1) {
						marks[j - 10] = i;
						if ((j - 10) == manPacLoc)
							return;
					}
				}
			}
		}
	}

	public int retrace() {
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(manPacLoc);
		int i = marks[manPacLoc];
		int temp = manPacLoc;
		while (i > 1) {
			if (marks[temp + 1] == i - 1) {
				path.add(temp + 1);
				temp += 1;
			} else if (marks[temp + 10] == i - 1) {
				path.add(temp + 10);
				temp += 10;
			} else if (marks[temp - 1] == i - 1) {
				path.add(temp - 1);
				temp -= 1;
			} else if (marks[temp - 10] == i - 1) {
				path.add(temp - 10);
				temp -= 10;
			}

			i--;
		}

		Collections.reverse(path);

		int dir = -1;
		temp = position;

		if (path.get(0) == temp + 1) {
			dir = RIGHT;
			temp += 1;
		}
		if (path.get(0) == temp + 10) {
			dir = DOWN;
			temp += 10;
		}
		if (path.get(0) == temp - 1) {
			dir = LEFT;
			temp -= 1;
		}
		if (path.get(0) == temp - 10) {
			dir = UP;
			temp -= 10;
		}

		return dir;
	}

	public int updateDisplay(int temp) {
		gb.getBlocks()[position].setIcon(null);
		gb.getBlocks()[position].setS(null);
		switch (temp) {
		case DOWN:
			position += 10;
			gb.getBlocks()[position].setIcon(seekImage0);
			gb.getBlocks()[position].setS(this);
			break;
		case LEFT:
			position -= 1;
			gb.getBlocks()[position].setIcon(seekImage1);
			gb.getBlocks()[position].setS(this);
			break;
		case RIGHT:
			position += 1;
			gb.getBlocks()[position].setIcon(seekImage2);
			gb.getBlocks()[position].setS(this);
			break;
		case UP:
			position -= 10;
			gb.getBlocks()[position].setIcon(seekImage3);
			gb.getBlocks()[position].setS(this);
			break;
		}
		return 1;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public void setWin(boolean win) {
		this.win = win;
	}
}
