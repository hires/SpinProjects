package com.kilpatrickaudio.spinEffects.echo;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class EchoEffect extends ElmProgram {

	public EchoEffect(boolean thirds, boolean quarters) {
		super("Echo");
//		double smooth = 0.01;
		double smooth = 0.001;
		
		allocDelayMem("delay", 32767);
		
		int wetLevel = REG0;
		int dryLevel = REG1;
		int delayTime = REG2;
		int feedbackLevel = REG3;
		int dry = REG4;
		int wetl = REG5;
		int wetr = REG6;
		int feedbackSamp = REG7;
		int lptemp = REG8;
		
		// get controls
		readRegister(POT0, 1.0);  // wet/dry mix
		scaleOffset(-1.0, 0.0);  
		writeRegister(wetLevel, 1.0);  // wet level increases clockwise
		scaleOffset(-1.0, -1.0);
		writeRegister(dryLevel, 0.0);  // dry level decreases clockwise

		// gets delay time
		clear();
		or(32767 << 8);
		mulx(POT1);
		readRegisterFilter(delayTime, smooth);
		writeRegister(delayTime, 0.0);		
		
		// gets feedback amount
		readRegister(POT2, 1.0);  // feedback
		writeRegister(feedbackLevel, 0.0);
		
		// XXX - 0x0c
		// get input
		readRegister(ADCL, 1.0);
		writeRegister(dry, 1.0);  // save the dry signal for later
		readRegister(feedbackSamp, 1.0);  // mix in the old wet level
		writeDelay("delay", 0.0, 0.0);  // put into the front of the delay
		
		// XXX - 0x10
		// get the main echo output
		readRegister(delayTime, 1.0);
		writeRegister(ADDR_PTR, 0.0);
		readDelayPointer(1.0);			
		
		writeRegister(wetl, 1.0);
		writeRegister(wetr, 1.0);
		mulx(feedbackLevel);	// scale by the feedback level
		readRegisterFilter(lptemp, 0.6);
		writeRegister(lptemp, 1.0);
		writeRegister(feedbackSamp, 0.0);  // save it for next time
		
		if(thirds) {
			// get the subdivided echo output 
			readRegister(delayTime, 1.0);
			scaleOffset(0.6667, 0.0);
			writeRegister(ADDR_PTR, 0.0);
			readRegister(wetl, 1.0);
			readDelayPointer(0.6);
			writeRegister(wetl, 0.0);
			
			readRegister(delayTime, 1.0);
			scaleOffset(0.3333, 0.0);
			writeRegister(ADDR_PTR, 0.0);
			readRegister(wetr, 1.0);
			readDelayPointer(0.6);
			writeRegister(wetr, 0.0);			
		}

		if(quarters) {
			// get the subdivided echo output
			readRegister(delayTime, 1.0);
			scaleOffset(0.75, 0.0);
			writeRegister(ADDR_PTR, 0.0);
			readRegister(wetl, 1.0);
			readDelayPointer(0.6);
			writeRegister(wetl, 0.0);
			
			readRegister(delayTime, 1.0);
			scaleOffset(0.25, 0.0);
			writeRegister(ADDR_PTR, 0.0);
			readRegister(wetr, 1.0);
			readDelayPointer(0.6);
			writeRegister(wetr, 0.0);			
		}
		
		// create outputs
		readRegister(wetl, 1.0);
		mulx(wetLevel);
		writeRegister(wetl, 0.0);		

		readRegister(wetr, 1.0);
		mulx(wetLevel);
		writeRegister(wetr, 0.0);
		
		readRegister(dry, 1.0);
		mulx(dryLevel);
		readRegister(wetl, 1.0);
		writeRegister(DACL, 0.0);

		readRegister(dry, 1.0);
		mulx(dryLevel);
		readRegister(wetr, 1.0);		
		writeRegister(DACR, 0.0);
	}
}
