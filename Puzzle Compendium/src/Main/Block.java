package Main;

import java.awt.*;

import javax.swing.*;

import BasedRNGGod.Base;
import BasedRNGGod.RNGGod;
import ManPac.Seeker;

public class Block extends JButton {

	// universal id
	private int id;

	// agbg variables
	private boolean created;
	private boolean falling;
	private boolean landed;

	// miner variables
	private boolean isPath;
	private boolean isGold;
	private boolean isRock;
	private boolean isMetal;
	private boolean isDiamond;
	private boolean isLit;
	int hits;
	
	// manPac variables
	private boolean isWall;
	private boolean isEaten;
	private Seeker s;

	//rngGod variables
	private boolean isRoad;
	private boolean isShrine;
	private boolean isMine;
	private boolean isBase;
	private boolean isBlackHole;
	private boolean isDestroyed;
	private boolean isMineral;
	private boolean isBroken;
	private boolean isEmpty;
	private boolean isFactory;
	private Base base;
	
	public Block(int id) {
		this.falling = false;
		this.landed = false;
		this.created = false;

		this.isPath = false;
		this.isGold = false;
		this.isRock = false;
		this.isMetal = false;
		this.isDiamond = false;
		this.isLit = false;
		this.hits = 0;

		this.isWall = false;
		this.isEaten = false;
		
		this.isRoad = false;
		this.isShrine = false;
		this.isMine = false;
		this.isBase = false;
		this.isBlackHole = false;
		this.isDestroyed = false;
		this.isMineral = false;
		this.isBroken = false;
		this.isEmpty = true;
		this.isFactory = false;
		
		this.id = id;
		this.setBackground(Color.black);
	}

	public boolean isGold() {
		return isGold;
	}

	public boolean isRock() {
		return isRock;
	}

	public boolean isMetal() {
		return isMetal;
	}

	public boolean isDiamond() {
		return isDiamond;
	}
	
	public void setGold(boolean isGold) {
		this.isGold = isGold;
	}

	public void setRock(boolean isRock) {
		hits = 2;
		this.isRock = isRock;
	}

	public void setMetal(boolean isMetal) {
		hits = 3;
		this.isMetal = isMetal;
	}
	
	public void setDiamond(boolean isDiamond) {
		hits = 4;
		this.isDiamond = isDiamond;
	}

	public int getId() {
		return id;
	}

	public void setCreated(int difficulty) {
		randomize(difficulty);
		this.setFalling();
	}

	public void setFalling() {
		this.falling = true;
		this.setEnabled(false);
	}

	public boolean isFalling() {
		return falling;
	}

	public boolean isLanded() {
		return landed;
	}

	public void setLanded() {
		this.landed = true;
		this.falling = false;
		this.setEnabled(true);
	}

	public boolean isPath() {
		return isPath;
	}

	public void setPath(boolean isPath) {
		this.isPath = isPath;
	}

	public void randomize(int difficulty) {
		int random = 0 + (int) (Math.random() * ((difficulty / 2) + 2));
		switch (random) {
		case 0:
			this.setBackground(Color.red);
			break;
		case 1:
			this.setBackground(Color.blue);
			break;
		case 2:
			this.setBackground(Color.green);
			break;
		case 3:
			this.setBackground(Color.yellow);
			break;
		case 4:
			this.setBackground(Color.magenta);
			break;
		case 5:
			this.setBackground(Color.white);
			break;
		case 6:
			this.setBackground(Color.cyan);
			break;
		case 7:
			this.setBackground(Color.pink);
			break;
		}
	}

	public void minerDisplay(int flag, int end) {
		if (flag == 0) {
		
			if (this.id==end)
				this.setIcon(new ImageIcon("res/ladder.jpg"));
			else if (isPath)
				this.setBackground(new Color(205,205,205));
			else if (this.getText() == "")
				this.setText("????");
		} else {
			if (!isLit) {
				this.setText("");
				this.setBackground(Color.black);
				this.setIcon(null);
			}
		}

	}

	public boolean isLit() {
		return isLit;
	}

	public void setLit(boolean isLit) {
		this.isLit = isLit;
		if(this.isRock){
			this.setBackground(new Color(50, 50, 50));
			this.setText("Rock +"+ this.getHits());
		}
		if(this.isMetal){
			this.setBackground(new Color(125,125,125));
			this.setText("Metal +"+ this.getHits());
		}
		if(this.isDiamond){
			this.setBackground(Color.white);
			this.setText("Diamond +"+ this.getHits());
		}
		if(this.isGold){
			this.setBackground(new Color(255,205,0));
			this.setText("Gold!!");
		}
		if(this.isPath){
			this.setBackground(new Color(200, 200, 200));
		}
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean isWall) {
		this.isWall = isWall;
		this.setBackground(Color.white);
	}

	public boolean isEaten() {
		return isEaten;
	}

	public void setEaten(boolean isEaten) {
		this.isEaten = isEaten;
		this.setBackground(Color.black);
	}

	public Seeker getS() {
		return s;
	}

	public void setS(Seeker s) {
		this.s = s;
	}
	
	public boolean isRoad() {
		return isRoad;
	}

	public void setRoad(boolean isRoad) {
		this.isRoad = isRoad;
	}

	public boolean isShrine() {
		return isShrine;
	}

	public void setShrine(boolean isShrine) {
		this.isShrine = isShrine;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public boolean isBase() {
		return isBase;
	}

	public void setBase(boolean isBase) {
		this.isBase = isBase;
	}

	public boolean isBlackHole() {
		return isBlackHole;
	}

	public void setBlackHole(boolean isBlackHole) {
		this.isBlackHole = isBlackHole;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	public boolean isMineral() {
		return isMineral;
	}

	public void setMineral(boolean isMineral) {
		this.isMineral = isMineral;
	}
	
	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public boolean isFactory() {
		return isFactory;
	}

	public void setFactory(boolean isFactory) {
		this.isFactory = isFactory;
	}

	public Base getBase() {
		return base;
	}

	public void setBase(Base base) {
		this.base = base;
	}

	public void reset() {
		this.falling = false;
		this.landed = false;
		this.created = false;
		
		this.isPath = false;
		this.isGold = false;
		this.isRock = false;
		this.isMetal = false;
		this.isDiamond = false;
		this.isLit = false;
		this.isWall = false;
		this.hits = 0;
		
		this.isEaten = false;
		this.s = null;
		
		this.isRoad = false;
		this.isShrine = false;
		this.isMine = false;
		this.isBase = false;
		this.isBlackHole = false;
		this.isDestroyed = false;
		this.isMineral = false;
		this.isBroken = false;
		this.isEmpty = true;
		this.isFactory = false;
		if(base != null)
			RNGGod.removeBase(base);
		this.base = null;
		
		this.setBackground(Color.black);
		this.setText("");
		this.setIcon(null);
	}
}
