package BasedRNGGod;

import Main.GameBoard;

public class RNGGod extends Thread {
	private GameBoard gb;
	private BasedRNGGod bgod;
	private int happiness, step;
	private static int totalWorkers;
	
	private String[] luxuries = { "yachts", "caviar", "an acting career", "harvard tuition", "42 virgins", "implants" };
	private final String tip1 = "Tip: Keep RNG God happy to get better RNG! Bad RNG makes\nhim unhappy :(.\n";
	private final String tip2 = "Tip: Better prayers are more likely to please RNG God!\n";
	private final String tip3 = "Tip: Repairing costs half the price of the building.\n";
	private final String tip4 = "Tip: More workers means more productivity. Build bases\nand don't let too many die!\n";
	private final String tip5 = "Tip: More mines adjacent to minerals increases productivity!\n";
	private final String tip6 = "Tip: Workers will mine the closest mineral connected by roads\nto their home base.\n";
	private final String tip7 = "Tip: Each factory next to a mine converts more money per\nmineral produced at that mine.\n";
	private final String tip8 = "Tip: More shrines give you better RNG and don't require connecting roads.\n";
	private final String tip9 = "Tip: As the difficulty increases RNG god will be more active.\n";
	private final String tip10 = "Tip: Destroyed tiles might make good RNG fail and\nmakes bad RNG more likely to hit you!\n";
	private final String tip11 = "Tip: You'll receive a free mineral every 15 turns.\nGive each mineral a base if possible!\n";
	private final String tip12 = "Tip: Each worker produces 1 money and 1 score per cycle.\n";
	private final String tip13 = "Tip: Connecting bases with roads allows breeding to increase\npopulation. Each base connects only once.\n";
	
	public RNGGod(GameBoard gb, BasedRNGGod bgod) {
		this.gb = gb;
		this.bgod = bgod;
		happiness = 5;
		step = 0;
		totalWorkers = 10;
	}

	public void run() {
		while (!bgod.getLost()) {
			randomAction();
			bgod.redrawMap();
			try {
				Thread.sleep((11 - bgod.getDifficulty()) * 1000);
			} catch (InterruptedException e) {

			}
			if (totalWorkers <= 0) {
				bgod.setLost(true);
			}
		}
	}

	public void randomAction() {
		int act = ((int) (Math.random() * 100) + 1) * happiness
				+ (int) (happiness * ((double) bgod.getShrines() / 2));
		if (step % 4 == 1 && bgod.getDifficulty() < 6)
			showTip();
	
		int target = (int) (Math.random() * 200);

		// one mineral and one base to start the game
		if (step == 0) {
			gb.getBlocks()[105].setMineral(true);
			gb.getBlocks()[105].setEmpty(false);

			if (target == 105)
				target++;
			gb.getBlocks()[target].setBase(true);
			gb.getBlocks()[target].setBase(new Base(bgod, target, gb));
			gb.getBlocks()[target].setEmpty(false);

			BasedRNGGodControlPanel
					.setText("RNG God has gifted you your first base and a mineral!\n");
		} else if (step % 15 == 0) {
			while (!gb.getBlocks()[target].isEmpty())
				target = (int) (Math.random() * 200);
			gb.getBlocks()[target].setMineral(true);
			gb.getBlocks()[target].setEmpty(false);
			BasedRNGGodControlPanel
					.setText("RNGGod gave you a mineral on tile #" + target
							+ "!\n");
		} else {
			// give base
			if (act > 950) {
				while (!gb.getBlocks()[target].isEmpty())
					target = (int) (Math.random() * 200);
				gb.getBlocks()[target].setBase(true);
				gb.getBlocks()[target].setBase(new Base(bgod, target, gb));
				gb.getBlocks()[target].setEmpty(false);
				totalWorkers += 10;
				BasedRNGGodControlPanel
						.setText("RNGGod gave you a base on tile #" + target
								+ "!\n");
				happiness++;
				// give factory
			} else if (act > 900) {
				while (!gb.getBlocks()[target].isEmpty())
					target = (int) (Math.random() * 200);
				gb.getBlocks()[target].setFactory(true);
				gb.getBlocks()[target].setEmpty(false);
				BasedRNGGodControlPanel
						.setText("RNGGod gave you a factory on tile #" + target
								+ "!\n");
				happiness++;
				// give shrine
			} else if (act > 750) {
				while (!gb.getBlocks()[target].isEmpty())
					target = (int) (Math.random() * 200);
				gb.getBlocks()[target].setShrine(true);
				gb.getBlocks()[target].setEmpty(false);
				BasedRNGGodControlPanel
						.setText("RNGGod gave you a shrine on tile #" + target
								+ "! Narcissitic...\n");

				happiness++;
				// give money
			} else if (act > 700) {
				bgod.addMoney(200);
				BasedRNGGodControlPanel.setText("RNGGod gave you $200!\n");
				happiness++;
			} else if (act > 500) {
				BasedRNGGodControlPanel.setText("RNGGod is watching you...\n");
				happiness--;
			}
			// break tile
			else if (act > 400) {
				while (gb.getBlocks()[target].isBlackHole()
						|| gb.getBlocks()[target].isDestroyed()
						|| gb.getBlocks()[target].isEmpty()
						|| gb.getBlocks()[target].isMineral())
					target = (int) (Math.random() * 200);
				happiness--;
				String targetString = targetString(target);
				BasedRNGGodControlPanel
						.setText("RNGGod broke the " + targetString + " at tile #"
								+ target + "!\n");
				gb.getBlocks()[target].setBroken(true);
			}
			// erase tile
			else if (act > 300) {
				while (gb.getBlocks()[target].isBlackHole()
						|| gb.getBlocks()[target].isDestroyed()
						|| gb.getBlocks()[target].isEmpty()
						|| gb.getBlocks()[target].isMineral())
					target = (int) (Math.random() * 200);
				happiness--;
				String targetString = targetString(target);
				BasedRNGGodControlPanel.setText("RNGGod deleted the " + targetString + " at tile #"
						+ target + "!\n");
				gb.getBlocks()[target].reset();
			}
			// destroy tile
			else if (act > 200) {
				while (gb.getBlocks()[target].isBlackHole()
						|| gb.getBlocks()[target].isDestroyed())
					target = (int) (Math.random() * 200);
				gb.getBlocks()[target].reset();
				gb.getBlocks()[target].setDestroyed(true);
				gb.getBlocks()[target].setEmpty(false);
				happiness--;
				BasedRNGGodControlPanel.setText("RNGGod destroyed tile #"
						+ target + "!\n");
				//
			} else if (act > 100) {
				bgod.setMoney(0);
				BasedRNGGodControlPanel
						.setText("RNGGod took all your money to buy " + luxuries[(int) (Math.random() * luxuries.length)] + "!\n");
				happiness--;
				// black hole
			} else if (act > 50) {
				happiness--;
				blackHole(target);
				BasedRNGGodControlPanel
						.setText("RNGGod made a black hole on tile #" + target
								+ "!\n");
				// plague
			} else if (act > 1) {
				happiness--;
				int counter = 0;
				for (int i = 0; i < 200; i++) {
					if (gb.getBlocks()[i].isBase()) {
						int x = gb.getBlocks()[i].getBase().plague();
						totalWorkers -= x;
						counter += x;
					}		
				}
				BasedRNGGodControlPanel
				.setText("RNGGod has plagued your people! "
						+ counter + " deaths reported!\n");
			} else if (act == 1) {
				bgod.setLost(true);
			}

			if (happiness == 0) {
				happiness = 1;
			}
			if (happiness > 10) {
				happiness = 10;
			}
		}
		BasedRNGGodControlPanel.happiness(happiness);
		step++;
	}

	public static void newBase() {
		totalWorkers += 10;
	}

	public static void removeBase(Base base) {
		totalWorkers -= base.getWorkers();
	}

	public static void addWorkers(int w){
		totalWorkers += w;
	}
	
	public static int getWorkers() {
		return totalWorkers;
	}

	public void acceptPrayer(int recieve) {
		if (recieve == 1) {
			happiness += 3;
			if (happiness > 10)
				happiness = 10;
			totalWorkers--;
			BasedRNGGodControlPanel
					.setText("Delicious! RNGGod is most pleased.\n");
		}
		if (recieve == 50)
			if (Math.random() * recieve > 20) {
				BasedRNGGodControlPanel
						.setText("Sufficient... I guess that'll do.\n");
				happiness++;
			} else {
				BasedRNGGodControlPanel
						.setText("I am a diety you know. That donation was kind of offensive.\n");
				happiness--;
			}
		if (recieve == 500)
			if (Math.random() * recieve > 10) {
				BasedRNGGodControlPanel
						.setText("Marvelous! I think I'll buy some "
								+ luxuries[(int) (Math.random() * luxuries.length)]
								+ " with that!\n");
				happiness++;
			} else {
				BasedRNGGodControlPanel
						.setText("I'm feeling like a regular arsehole today. I require more!\n");
				happiness--;
			}

		BasedRNGGodControlPanel.happiness(happiness);
	}

	public void showTip() {
		int act = (int) (Math.random() * 13) + 1;
		switch (act) {
		case 1:
			BasedRNGGodControlPanel.setText(tip1);
			break;
		case 2:
			BasedRNGGodControlPanel.setText(tip2);
			break;
		case 3:
			BasedRNGGodControlPanel.setText(tip3);
			break;
		case 4:
			BasedRNGGodControlPanel.setText(tip4);
			break;
		case 5:
			BasedRNGGodControlPanel.setText(tip5);
			break;
		case 6:
			BasedRNGGodControlPanel.setText(tip6);
			break;
		case 7:
			BasedRNGGodControlPanel.setText(tip7);
			break;
		case 8:
			BasedRNGGodControlPanel.setText(tip8);
			break;
		case 9:
			BasedRNGGodControlPanel.setText(tip9);
			break;
		case 10:
			BasedRNGGodControlPanel.setText(tip10);
			break;
		case 11:
			BasedRNGGodControlPanel.setText(tip11);
			break;
		case 12:
			BasedRNGGodControlPanel.setText(tip12);
			break;
		case 13:
			BasedRNGGodControlPanel.setText(tip13);
			break;
		}
	}

	public void blackHole(int target) {
		gb.getBlocks()[target].reset();
		gb.getBlocks()[target].setBlackHole(true);
		gb.getBlocks()[target].setEmpty(false);

		if (target > 10) {
			gb.getBlocks()[target - 10].reset();
			gb.getBlocks()[target - 10].setDestroyed(true);
			gb.getBlocks()[target - 10].setEmpty(false);
		}

		if (target < 190) {
			gb.getBlocks()[target + 10].reset();
			gb.getBlocks()[target + 10].setDestroyed(true);
			gb.getBlocks()[target + 10].setEmpty(false);
		}

		if (target % 10 != 0) {
			gb.getBlocks()[target - 1].reset();
			gb.getBlocks()[target - 1].setDestroyed(true);
			gb.getBlocks()[target - 1].setEmpty(false);
		}

		if (target % 10 != 9) {
			gb.getBlocks()[target + 1].reset();
			gb.getBlocks()[target + 1].setDestroyed(true);
			gb.getBlocks()[target + 1].setEmpty(false);
		}
	}
	
	public String targetString(int target){
		String s = "";
		if(gb.getBlocks()[target].isBase()){
			s = "base";
		}
		else if(gb.getBlocks()[target].isShrine()){
			s = "shrine";
		}
		else if(gb.getBlocks()[target].isFactory()){
			s = "factory";
		}
		else if(gb.getBlocks()[target].isRoad()){
			s = "road";
		}
		else
			s="mine";
		
		return s;
	}
}
