package com.kilpatrickaudio.spinEffects.chorus;

import java.io.IOException;

import org.andrewkilpatrick.elmGen.EEPromHandler;

public class Chorus {
	
	public Chorus() {
		boolean writeEeprom = true;
		// generate the code
		ChorusEffect chorusMultivoice = new ChorusEffect(true, false);
		ChorusEffect chorusSinglevoice = new ChorusEffect(true, true);
		ChorusEffect revOnly = new ChorusEffect(false, false);
		Mute mute = new Mute();
		System.out.println("Code generation complete!");
		
		if(writeEeprom) {
			// build the EEPROM file
			EEPromHandler eeprom = new EEPromHandler("/dev/ttyUSB0");
			eeprom.fillBank(chorusMultivoice, 0);
			eeprom.fillBank(chorusSinglevoice, 1);
			eeprom.fillBank(revOnly, 2);
			eeprom.fillBank(revOnly, 3);
			eeprom.fillBank(mute, 4);
			eeprom.fillBank(mute, 5);
			eeprom.fillBank(mute, 6);
			eeprom.fillBank(mute, 7);
			try {
				eeprom.writeAllBanks();
//				eeprom.writeBank(0);
//				eeprom.writeBank(1);
//				eeprom.writeBank(2);
//				eeprom.writeBank(3);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		System.exit(0);
	}		
	
	public static void main(String args[]) {
		new Chorus();
	}
}
