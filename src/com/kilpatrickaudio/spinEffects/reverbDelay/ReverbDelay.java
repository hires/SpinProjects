package com.kilpatrickaudio.spinEffects.reverbDelay;

import java.io.IOException;

import org.andrewkilpatrick.elmGen.EEPromHandler;

public class ReverbDelay {	
	
	/**
	 * Creates code for the Lush Puppy.
	 */
	public ReverbDelay() {
		// generate the code
		BigEffect big = new BigEffect(false);
		BigEffect bigDelay = new BigEffect(true);
		SmallEffect small = new SmallEffect(false);
		SmallEffect smallDelay = new SmallEffect(true);
		Mute mute = new Mute();
		System.out.println("Code generation complete!");
		
		// build the EEPROM file
//		EEPromHandler eeprom = new EEPromHandler("COM5");
		EEPromHandler eeprom = new EEPromHandler("COM3");
		eeprom.fillBank(big, 0);
		eeprom.fillBank(small, 1);
		eeprom.fillBank(bigDelay, 2);
		eeprom.fillBank(smallDelay, 3);
		eeprom.fillBank(mute, 4);
		eeprom.fillBank(mute, 5);
		eeprom.fillBank(mute, 6);
		eeprom.fillBank(mute, 7);
		
		try {
			eeprom.writeAllBanks();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	
	public static void main(String args[]) {
		new ReverbDelay();
	}
}
