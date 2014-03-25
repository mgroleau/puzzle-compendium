package BasedRNGGod;

import Main.GameBoard;

public class Base {
	private int workers;
	private BasedRNGGod g;
	private int position;
	private GameBoard gb;
	private int[] marks = new int[200];
	private int factories;
	private boolean connected;
	private int connectedTo = -1;
	private int mines;

	public Base(BasedRNGGod game, int p, GameBoard gameboard) {
		workers = 10;
		g = game;
		position = p;
		gb = gameboard;
	}

	public int sacrifice() {
		workers--;
		if (workers == 0) {
			BasedRNGGodControlPanel
					.setText("A base was abandoned because no workers are left.\n");
			return 0;
		}

		return 1;
	}

	public int plague() {
		int x = workers / 2;
		workers = x;
		return x;
	}

	public int productivity() {
		if(!connected){
			int[] b = breadthFirst(true); 
			if(b[0] != -1){
				connected = true;
				gb.getBlocks()[b[0]].getBase().setConnected(true);
				int temp = workers / 2;
				workers += temp;
				gb.getBlocks()[b[0]].getBase().addWorkers(temp);
				RNGGod.addWorkers(temp*2);
				connectedTo = b[0];
				gb.getBlocks()[b[0]].getBase().setConnectedTo(position);
			}
		}
		
		int[] m = breadthFirst(false);
		mines = 0;
		factories = 0;

		if (m[0] != -1) {
			if (m[0] < 200 && gb.getBlocks()[m[0] + 10].isMine()) {
				mines++;
				factories += getFactories(m[0] + 10);
			}
			if (m[0] > 9 && gb.getBlocks()[m[0] - 10].isMine()) {
				mines++;
				factories += getFactories(m[0] - 10);
			}
			if (m[0] % 10 != 0 && gb.getBlocks()[m[0] - 1].isMine()) {
				mines++;
				factories += getFactories(m[0] - 1);
			}
			if (m[0] % 10 != 9 && gb.getBlocks()[m[0] + 1].isMine()) {
				mines++;
				factories += getFactories(m[0] + 1);
			}

			mines *= (int) (workers
					* (100 - BasedRNGGodControlPanel.getDistribution()) / 100.0);
			mines /= m[1];

			g.addMinerals(mines);
			g.updateMinerals();
		}

		return 0;
	}

	public void setConnected(boolean b) {
		connected = b;
	}
	
	public void setConnectedTo(int num) {
		connectedTo = num;
	}

	public int getConnectedTo() {
		return connectedTo;
	}
	
	public String output(){
		return "This base mines " + mines + " minerals and converts 1 mineral ->" + factories/2 + " money.\n";
	}
	
	public int[] breadthFirst(boolean base) {
		int[] m = { -1, -1 };

		for (int i = 0; i < 200; i++) {
			if ((gb.getBlocks()[i].isRoad() && !gb.getBlocks()[i].isBroken())
					|| gb.getBlocks()[i].isMineral() || gb.getBlocks()[i].isBase())
				marks[i] = -1;
			else
				marks[i] = 999;
		}

		marks[position] = 0;

		int i = 0;
		int counter = 1;
		boolean flag = false;
		while (counter > 0) {
			i++;
			counter = 0;
			for (int j = 0; j < 200; j++) {
				if (marks[j] == i - 1) {
					if (j % 10 != 9) {
						if (marks[j + 1] == -1) {
							marks[j + 1] = i;
							counter++;
							if (base) {
								if (gb.getBlocks()[j + 1].isBase()) {
									m[0] = j + 1;
									m[1] = i;
									counter = -999;
								}
							} else {
								if (gb.getBlocks()[j + 1].isMineral()) {
									m[0] = j + 1;
									m[1] = i;
									counter = -999;
								}
							}
						}
					}
					if (j < 190) {
						if (marks[j + 10] == -1) {
							marks[j + 10] = i;
							counter++;
							if (base) {
								if (gb.getBlocks()[j + 10].isBase()) {
									m[0] = j + 10;
									m[1] = i;
									counter = -999;
								}
							} else {
								if (gb.getBlocks()[j + 10].isMineral()) {
									m[0] = j + 10;
									m[1] = i;
									counter = -999;
								}
							}
						}
					}
					if (j % 10 != 0) {
						if (marks[j - 1] == -1) {
							marks[j - 1] = i;
							counter++;
							if (base) {
								if (gb.getBlocks()[j - 1].isBase()) {
									m[0] = j - 1;
									m[1] = i;
									counter = -999;
								}
							} else {
								if (gb.getBlocks()[j - 1].isMineral()) {
									m[0] = j - 1;
									m[1] = i;
									counter = -999;
								}
							}
						}
					}
					if (j > 9) {
						if (marks[j - 10] == -1) {
							marks[j - 10] = i;
							counter++;
							if (base) {
								if (gb.getBlocks()[j - 10].isBase()) {
									m[0] = j - 10;
									m[1] = i;
									counter = -999;
								}
							} else {
								if (gb.getBlocks()[j - 10].isMineral()) {
									m[0] = j - 10;
									m[1] = i;
									counter = -999;
								}
							}
						}
					}
				}
			}
		}

		return m;
	}

	public void convert() {
		if (g.getMinerals() != 0) {
			int temp = (int) (workers
					* BasedRNGGodControlPanel.getDistribution() / 100.0);
			
			if (g.getMinerals() < temp) {
				g.addMoney(g.getMinerals() * factories / 2);
				g.setMinerals(0);
			} else {
				g.addMinerals(temp * (-1));
				g.addMoney(temp * factories / 2);
			}
		}
	}

	public int getWorkers() {
		return workers;
	}

	public void addWorkers(int w) {
		workers += w;
	}
	
	public int getFactories(int mine) {
		int factories = 0;
		if (mine < 200 && gb.getBlocks()[mine + 10].isFactory()) {
			factories++;
		}
		if (mine > 9 && gb.getBlocks()[mine - 10].isFactory()) {
			factories++;
		}
		if (mine % 10 != 0 && gb.getBlocks()[mine - 1].isFactory()) {
			factories++;
		}
		if (mine % 10 != 9 && gb.getBlocks()[mine + 1].isFactory()) {
			factories++;
		}

		return factories;
	}
}
