package com.kilpatrickaudio.spinEffects.chopSynth;

import java.io.IOException;

import org.andrewkilpatrick.elmGen.EEPromHandler;

public class ChopSynth {
	
	/**
	 * Creates code for the Kill Switch.
	 */
	public ChopSynth() {
		// generate the code
		ChopSynthEffect chopSynthStrip = new ChopSynthEffect(true, true);
		ChopSynthEffect chopOsc = new ChopSynthEffect(false, true);
		ChopSynthEffect chop = new ChopSynthEffect(false, false);
		
		Mute mute = new Mute();
		System.out.println("Code generation complete!");
		
		// build the EEPROM file
//		EEPromHandler eeprom = new EEPromHandler("COM5");
		EEPromHandler eeprom = new EEPromHandler("COM3");
		eeprom.fillBank(chopSynthStrip, 0);
		eeprom.fillBank(chopOsc, 1);
		eeprom.fillBank(chop, 2);
		eeprom.fillBank(chop, 3);

		eeprom.fillBank(mute, 4);
		eeprom.fillBank(mute, 5);
		eeprom.fillBank(mute, 6);
		eeprom.fillBank(mute, 7);
		
		try {
			eeprom.writeAllBanks();
//			eeprom.writeBank(0);
//			eeprom.writeBank(1);
//			eeprom.writeBank(2);
//			eeprom.writeBank(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	
	public static void main(String args[]) {
		new ChopSynth();
	}
}
