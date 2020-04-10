package com.kilpatrickaudio.spinEffects.echo;

import java.io.IOException;

import org.andrewkilpatrick.elmGen.EEPromHandler;
import org.andrewkilpatrick.elmGen.HexDumper;

public class Echo {

	public Echo() {
		boolean writeEeprom = false;
		boolean dumpHexFile = true;
		boolean dumpVerilogFormat = true;
		// generate the code
		EchoEffect echo = new EchoEffect(false, false);
		EchoEffect echoQuarters = new EchoEffect(false, true);
		EchoEffect echoThirds = new EchoEffect(true, false);
		
		Mute mute = new Mute();
		System.out.println("Code generation complete!");
		
		if(writeEeprom) {
			// build the EEPROM file
//			EEPromHandler eeprom = new EEPromHandler("COM5");
			EEPromHandler eeprom = new EEPromHandler("/dev/ttyUSB0");
			eeprom.fillBank(echo, 0);
			eeprom.fillBank(echo, 1);
			eeprom.fillBank(echoQuarters, 2);
			eeprom.fillBank(echoThirds, 3);
			eeprom.fillBank(mute, 4);
			eeprom.fillBank(mute, 5);
			eeprom.fillBank(mute, 6);
			eeprom.fillBank(mute, 7);
			try {
				eeprom.writeAllBanks();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}

      if(dumpHexFile) {
            // build the EEPROM file
          HexDumper dumper = new HexDumper("/home/andrew/temp/dump.hex", dumpVerilogFormat);
          dumper.fillBank(echo, 0);
          dumper.fillBank(echo, 1);
          dumper.fillBank(echoQuarters, 2);
          dumper.fillBank(echoThirds, 3);
          dumper.fillBank(mute, 4);
          dumper.fillBank(mute, 5);
          dumper.fillBank(mute, 6);
          dumper.fillBank(mute, 7);
          try {
              dumper.writeHexFile();
          } catch (IOException e) {
              e.printStackTrace();
          }           
        }

		System.exit(0);
	}		
	
	public static void main(String args[]) {
		new Echo();
	}
}
