package Main;

import javax.swing.*;
import AGBG.*;
import BasedRNGGod.BasedRNGGod;
import BasedRNGGod.BasedRNGGodControlPanel;
import Miner.*;
import ManPac.*;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame {
	private final int WINDOW_HEIGHT = 560;
	private GameBoard gb;
	private AGBG agbg;
	private Miner miner;
	private ManPac manpac;
	private BasedRNGGod rnggod;
	
	public Frame(int game) {
		setLayout(new BorderLayout());
		gb = new GameBoard(game, this);
		setLocation(50, 50);

		switch (game) {
		case 0:
			setTitle("ANOTHER GENERIC BLOCK GAME");
			setSize(800, WINDOW_HEIGHT);
			agbg = gb.getAgbg();
			AGBGControlPanel acp = new AGBGControlPanel(agbg);
			add(gb, BorderLayout.CENTER);
			add(acp, BorderLayout.EAST);
			break;
		case 1:
			setTitle("Miner");
			setSize(1200, WINDOW_HEIGHT);
			miner = gb.getMiner();
			MinerControlPanel micp = new MinerControlPanel(miner);
			add(gb, BorderLayout.CENTER);
			add(micp, BorderLayout.EAST);
			break;
		case 2:
			setTitle("ManPac");
			setSize(700, WINDOW_HEIGHT);
			manpac = gb.getManPac();
			ManPacControlPanel macp = new ManPacControlPanel(manpac);
			add(gb, BorderLayout.CENTER);
			add(macp, BorderLayout.EAST);
			break;
		case 3:
			setTitle("BasedRNGGod");
			setSize(964, WINDOW_HEIGHT);
			rnggod = gb.getRngGod();
			BasedRNGGodControlPanel rngcp = new BasedRNGGodControlPanel(rnggod);
			add(gb, BorderLayout.CENTER);
			add(rngcp, BorderLayout.EAST);
			break;
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
}
