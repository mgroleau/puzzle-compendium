package Miner;

import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;

public class Shop extends JDialog {

	private final int WINDOW_WIDTH = 400;
	private final int WINDOW_HEIGHT = 200;

	private JButton upgradeO2, flashlight, dynamite, refillO2;
	private JLabel o2label, flashlabel, dynalabel, refilllabel;
	private ButtonHandler bl = new ButtonHandler();
	private ImageIcon o2image, flashimage, dynaimage, refillimage;
	private Miner miner;
	private int upgradeCost, refillCost, flashlightCost, dynamiteCost;
	private ImageIcon image;
	
	public Shop(Miner m) {
		miner = m;
		setLayout(new GridLayout(4, 2));
		setModal(true);
		calcCosts();

		o2image = new ImageIcon("res/o2image.jpg");
		flashimage = new ImageIcon("res/flashimage.jpg");
		dynaimage = new ImageIcon("res/dynaimage.jpg");
		refillimage = new ImageIcon("res/refillimage.jpg");

		upgradeO2 = new JButton("upgrade");
		upgradeO2.setIcon(o2image);
		upgradeO2.addActionListener(bl);
		o2label = new JLabel("Upgrade O2 - Cost: " + upgradeCost + " Gold");

		refillO2 = new JButton("refill");
		refillO2.setIcon(refillimage);
		refillO2.addActionListener(bl);
		refilllabel = new JLabel("Refill O2 - Cost: " + refillCost + " Gold");

		flashlight = new JButton("flash");
		flashlight.setIcon(flashimage);
		flashlight.addActionListener(bl);
		flashlabel = new JLabel("Flashlight - Cost: " + flashlightCost
				+ " Gold");

		dynamite = new JButton("dyna");
		dynamite.setIcon(dynaimage);
		dynamite.addActionListener(bl);
		dynalabel = new JLabel("Dynamite - Cost: " + dynamiteCost + " Gold");

		add(o2label);
		add(refilllabel);
		add(upgradeO2);
		add(refillO2);
		add(flashlabel);
		add(dynalabel);
		add(flashlight);
		add(dynamite);
		
		if(miner.getPowerUp() != 0){
			flashlight.setEnabled(false);
			dynamite.setEnabled(false);
		}
		
		if(miner.getO2()==miner.getMaxO2())
			refillO2.setEnabled(false);
		
		setTitle("Miners R' Us");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(150,400);
		setResizable(false);
		setVisible(true);
	}

	public void calcCosts() {
		upgradeCost = 40 * (int)Math.pow(2, Miner.upgrades);
		refillCost = 2 * (miner.getMaxO2()-miner.getO2()) * miner.getDifficulty();
		flashlightCost = 5 * (int)Math.pow(2, Miner.flashlights) * miner.getDifficulty();
		dynamiteCost = 5 * (int)Math.pow(2, Miner.dynamite) * miner.getDifficulty();
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			
			if (command == "upgrade") {
				if(miner.getGold()>=upgradeCost){
				miner.setMaxO2(miner.getMaxO2()+10);
				miner.setGold(miner.getGold()-upgradeCost);
				miner.upgrades++;
				}
				else
					JOptionPane.showMessageDialog(null, "Not enough gold.");
			}
			if (command == "refill") {
				if(miner.getGold()>=refillCost){
					miner.setO2(miner.getMaxO2());
					miner.setGold(miner.getGold()-refillCost);
				}
				else{
					int gold = miner.getGold();
					int O2 = gold/miner.getDifficulty()/2;
					int reply = JOptionPane.showConfirmDialog(null,
							"Pay all gold to refill " + O2 + " O2?", "Not enough gold.",
							JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION){
						miner.setO2(miner.getO2()+O2);
						miner.setGold(miner.getGold()-O2*miner.getDifficulty()*2);
					}
				}
			}
			if (command == "flash") {
				if(miner.getGold()>=flashlightCost){
					miner.setPowerUp(1);
					miner.setGold(miner.getGold()-flashlightCost);
					image = new ImageIcon("res/flash.jpg");
					MinerControlPanel.powerup.setIcon(image);
					miner.flashlights++;
				}
				else
					JOptionPane.showMessageDialog(null, "Not enough gold.");
			}
			if (command == "dyna") {
				if(miner.getGold()>=dynamiteCost){
					miner.setGold(miner.getGold()-dynamiteCost);
					miner.setPowerUp(2);
					image = new ImageIcon("res/dyna.jpg");
					MinerControlPanel.powerup.setIcon(image);
					miner.dynamite++;
				}
				else
					JOptionPane.showMessageDialog(null, "Not enough gold.");
			}
				
			dispose();
		}
	}
}
