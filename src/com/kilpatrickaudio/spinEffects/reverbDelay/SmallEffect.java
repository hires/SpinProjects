package com.kilpatrickaudio.spinEffects.reverbDelay;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class SmallEffect extends ElmProgram {

	public SmallEffect(boolean delayEnable) {
		super("Small - delay: " + delayEnable);

		double smooth = 0.01;
		
		// allocate delay memory
		allocDelayMem("echo", 16384);
		allocDelayMem("api4", 222);
		allocDelayMem("api2", 303);
		allocDelayMem("api3", 653);
		allocDelayMem("api1", 522);
		allocDelayMem("ap2", 962);
		allocDelayMem("del1", 1775);  // input = left output
		allocDelayMem("ap1", 1050);
		allocDelayMem("del2", 1550);  // input = right output
		double kap = 0.55;  // AP coefficient

		int apout = REG0; 		// holding reg input AP
		int dry = REG1;			// dry signal
		int wet = REG2;			// wet signal
		int wetLevel = REG3;		// wet level
		int revTime = REG4;		// reverb time
		int temp = REG5;		// temp var
		int lptemp1 = REG6;		// low-pass filter temp
		int lptemp2 = REG7;		// low-pass filter temp
		int lptemp3 = REG8;		// low-pass filter temp
		int delayLevel = REG9;  // delay level
		int delayTime = REG10;	// delay time
		
		// read wet level from pot 0
		readRegister(POT0, 1.0);
		writeRegister(delayLevel, 1.0);
//		mulx(POT0);
		writeRegister(wetLevel, 0.0);
		
		// read reverb time from pot 1
		readRegister(POT1, 0.25);
		mulx(POT1);
		scaleOffset(1.0, 0.6);
		writeRegister(revTime, 0.0);
		
//		// read delay time from pot 2
//		or(0x400000);
//		mulx(POT2);
//		writeRegister(ADDR_PTR, 0.0);

		// read delay time from pot 2
		or(0x400000);
		mulx(POT2);
		readRegisterFilter(delayTime, smooth);
		writeRegister(ADDR_PTR, 1.0);
		writeRegister(delayTime, 0.0);	
		
		// get inputs
		readRegister(ADCL, 1.0);
		writeRegister(dry, 1.0);
		writeDelay("echo", 0.0, 0.0);

		// reverb
		readRegister(dry, 0.75);
		readRegisterFilter(lptemp1, 0.3);
		writeRegister(lptemp1, 1.0);
		readDelay("api1", 1.0, kap);
		writeAllpass("api1", 0.0, -kap);
		readDelay("api2", 1.0, kap);
		writeAllpass("api2", 0.0, -kap);
		readDelay("api3", 1.0, kap);
		writeAllpass("api3", 0.0, -kap);
		readDelay("api4", 1.0, kap);
		writeAllpass("api4", 0.0, -kap);
		
		// first loop
		readDelay("del2", 1.0, 1.0);
		mulx(revTime);
		writeRegister(temp, 0.0);
		writeRegister(apout, 1.0);
		readRegister(temp, 1.0);
		readDelay("ap1", 1.0, -kap);
		writeAllpass("ap1", 0.0, kap);
		writeDelay("del1", 0.0, 1.0);
		mulx(wetLevel);
		writeRegister(wet, 0.0);

		// output left
		if(delayEnable) {
			readDelayPointer(0.5);
			mulx(delayLevel);
			readRegisterFilter(lptemp2, 0.3);
			writeRegister(lptemp2, 1.0);
		}
		readRegister(dry, 1.0);
		readRegister(wet, 1.0);
		writeRegister(DACL, 0.0);
		
		// second loop
		readDelay("del1", 1.0, 1.0);
		mulx(revTime);
		writeRegister(temp, 0.0);
		readRegister(apout, 1.0);
		readRegister(temp, 1.0);
		readDelay("ap2", 1.0, kap);
		writeAllpass("ap2", 0.0, -kap);
		writeDelay("del2", 0.0, 1.0);
		mulx(wetLevel);
		writeRegister(wet, 0.0);

		// output right
		if(delayEnable) {
			readDelayPointer(0.5);
			mulx(delayLevel);
			readRegisterFilter(lptemp3, 0.3);
			writeRegister(lptemp3, 1.0);
		}
		readRegister(dry, 1.0);
		readRegister(wet, 1.0);
		writeRegister(DACR, 0.0);
	}
}
