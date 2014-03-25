package Main;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import AGBG.AGBG;
import BasedRNGGod.BasedRNGGod;
import ManPac.ManPac;
import Miner.Miner;

public class GameBoard extends JPanel {

	private Block[] blocks = new Block[210];
	private ButtonHandler bl = new ButtonHandler();
	private int game;
	private AGBG agbg;
	private Miner miner;
	private ManPac manpac;
	private BasedRNGGod rnggod;
	private JFrame window;
	
	public GameBoard(int game, JFrame w) {
		this.game = game;
		window = w;
		
		setLayout(new GridLayout(20, 10));
		for (int i = 0; i < 200; i++) {
			blocks[i] = new Block(i);
			blocks[i].setEnabled(false);
			blocks[i].addActionListener(bl);
			add(blocks[i]);
		}

		switch (game) {
		case 0:
			agbg = new AGBG(this);
			break;
		case 1:
			miner = new Miner(this);
			break;
		case 2:
			manpac = new ManPac(this);
			break;
		case 3:
			rnggod = new BasedRNGGod(this);
			break;
		}
	}

	public AGBG getAgbg() {
		return agbg;
	}

	public Miner getMiner() {
		return miner;
	}

	public ManPac getManPac() {
		return manpac;
	}
	
	public BasedRNGGod getRngGod() {
		return rnggod;
	}
	
	public Block[] getBlocks() {
		return blocks;
	}
	
	public void enableInner(){
		for (int i=10;i<190;i++)
			if(i % 10 != 0 && i % 10 != 9)
			blocks[i].setEnabled(true);
	}
	
	public void enable(){
		for (int i=0;i<200;i++)
			blocks[i].setEnabled(true);
	}
	
	public void disable(){
		for (int i=0;i<199;i++)
			blocks[i].setEnabled(false);
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Block b = (Block) e.getSource();
			switch (game) {
			case 0:
				agbg.handleButton(b);
				break;
			case 1:
				miner.handleButton(b);
				break;
			case 2:
				manpac.handleButton(b);
				break;
			case 3:
				rnggod.handleButton(b);
				break;
			}
		}
	}
	
	public void end(){
		window.dispose();
	}
}
