package com.kilpatrickaudio.spinEffects.vibroTrem;

import java.io.IOException;

import org.andrewkilpatrick.elmGen.EEPromHandler;

public class VibroTrem {
	
	public VibroTrem() {
		// generate the code
		VibroTremEffect tremVibe = new VibroTremEffect(false, false);
		VibroTremEffect tremVibeTouch = new VibroTremEffect(true, false);
		VibroTremEffect tremFilt = new VibroTremEffect(false, true);
		VibroTremEffect tremFiltTouch = new VibroTremEffect(true, true);
		
		Mute mute = new Mute();
		System.out.println("Code generation complete!");
		
		// build the EEPROM file
//		EEPromHandler eeprom = new EEPromHandler("COM5");
		EEPromHandler eeprom = new EEPromHandler("COM3");
		eeprom.fillBank(tremVibe, 0);
		eeprom.fillBank(tremFilt, 1);
		eeprom.fillBank(tremVibeTouch, 2);
		eeprom.fillBank(tremFiltTouch, 3);
		eeprom.fillBank(mute, 4);
		eeprom.fillBank(mute, 5);
		eeprom.fillBank(mute, 6);
		eeprom.fillBank(mute, 7);
		
		try {
			eeprom.writeAllBanks();
//			eeprom.writeBank(0);
//			eeprom.writeBank(1);
//			eeprom.writeBank(2);
//			eeprom.writeBank(7);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	
	public static void main(String args[]) {
		new VibroTrem();
	}
}
