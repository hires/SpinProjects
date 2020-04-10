package com.kilpatrickaudio.spinEffects.chorus;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class ChorusEffect extends ElmProgram {

	public ChorusEffect(boolean chorusOn, boolean alternate) {
		super("Chorus");

		// chorus stuff
		int dry = REG0;
		int chorusWetL = REG1;
		int chorusWetR = REG4;
		int reverbLevel = REG2;
		int depthSupress = REG3;
		allocDelayMem("chorus", 16384);

		// reverb stuff
		double kap = 0.55;  // AP coefficient
		int lptemp1 = REG31;
		int revTemp = REG30;
		int apout = REG29;
		int revWetL = REG28;
		int revWetR = REG27;
		allocDelayMem("api3", 122);
		allocDelayMem("api4", 303);
		allocDelayMem("api1", 553);
		allocDelayMem("api2", 922);
		allocDelayMem("ap1", 2062);
		allocDelayMem("del1", 3375);  // input = left output
		allocDelayMem("ap2", 2500);
		allocDelayMem("del2", 2250);
		
		// pot 0 - depth (pot 1 causes the depth to decrease as it is increased)
		readRegister(POT1, 0.8);
		scaleOffset(-1.0, 0.999);
		writeRegister(depthSupress, 0.0);
		readRegister(POT0, 1.0);
		scaleOffset(0.015, 0.0);
		mulx(depthSupress);
		writeRegister(SIN0_RANGE, 1.0);
		writeRegister(SIN1_RANGE, 0.0);
		
		// pot 1 - rate
		readRegister(POT1, 1.0);
		mulx(POT1);
		scaleOffset(0.5, 0.0);
		writeRegister(SIN0_RATE, 1.0);
		if(alternate) scaleOffset(0.8, 0.0);
		else scaleOffset(0.95, 0.0);
		writeRegister(SIN1_RATE, 0.0);
	
		// pot 2 - reverb mix
		readRegister(POT2, 1.0);
		writeRegister(reverbLevel, 0.0);
		
		// audio processing
		clear();
		readRegister(ADCL, 1.0);
		writeRegister(dry, 1.0);
		writeDelay("chorus", 0, 0.0);
		
		// chorus left
		if(alternate) {
			int pos = 200;
			chorusReadDelay(CHO_LFO_SIN1, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN1, CHO_SIN, pos + 1);
		}
		else {
			int pos = 450;
			pos = 200;
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN, pos + 1);
			pos = 1290;
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN, pos + 1);
			pos = 1140;
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN, pos + 1);
			pos = 1220;
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN, pos + 1);
			pos = 1301;
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN, pos + 1);
			pos = 330;
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN, pos + 1);
			pos = 450;
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_SIN, pos + 1);
			scaleOffset(0.5, 0.0);
		}
		writeRegister(chorusWetL, 0.0);
	
		// chorus right
		if(alternate) {
			int pos = 200;
			chorusReadDelay(CHO_LFO_SIN1, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN1, CHO_COS, pos + 1);
		}
		else {
			int pos = 450;
			pos = 200;
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS, pos + 1);
			pos = 1290;
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS, pos + 1);
			pos = 1140;
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS, pos + 1);
			pos = 1220;
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS, pos + 1);
			pos = 1301;
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS, pos + 1);
			pos = 330;
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS, pos + 1);
			pos = 450;
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS | CHO_REG | CHO_COMPC, pos);
			chorusReadDelay(CHO_LFO_SIN0, CHO_COS, pos + 1);
			scaleOffset(0.5, 0.0);
		}
		writeRegister(chorusWetR, 0.0);		
		
		// process reverb - mix the dry and chorus signal
		clear();
		readRegister(dry, 0.75);
		if(chorusOn) readRegister(chorusWetL, 0.5);
		
		// main APs
		readRegisterFilter(lptemp1, 0.5);
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
		scaleOffset(0.55, 0.0);
		writeRegister(revTemp, 0.0);
		writeRegister(apout, 1.0);
		readRegister(revTemp, 1.0);
		readDelay("ap1", 1.0, -kap);
		writeAllpass("ap1", 0.0, kap);
		writeDelay("del1", 0.0, 1.0);
		mulx(reverbLevel);
		writeRegister(revWetL, 0.0);
		
		// second loop
		readDelay("del1", 1.0, 1.0);
		scaleOffset(0.55, 0.0);
		writeRegister(revTemp, 0.0);
		readRegister(apout, 1.0);
		readRegister(revTemp, 1.0);
		readDelay("ap2", 1.0, kap);
		writeAllpass("ap2", 0.0, -kap);
		writeDelay("del2", 0.0, 1.0);
		mulx(reverbLevel);
		writeRegister(revWetR, 0.0);
		
		/*
		// production release - all units
		// main mixdown - left channel
		if(chorusOn) {
			readRegister(revWetL, 0.75);
			readRegister(chorusWetL, 0.5);
		}
		else {
			readRegister(revWetL, 0.75);
		}
		readRegister(dry, 0.5);
		if(chorusOn) scaleOffset(0.75, 0.0);
		writeRegister(DACL, 0.0);
		
		// main mixdown - right channel
		if(chorusOn) {
			readRegister(revWetR, 0.75);
			readRegister(chorusWetR, 0.5);
		}
		else {
			readRegister(revWetR, 0.75);
		}
		readRegister(dry, 0.5);			
		if(chorusOn) scaleOffset(0.75, 0.0);
		writeRegister(DACR, 0.0);
		*/
		
		// 2014-03-30 special version for Sean
		// main mixdown - left channel
		if(chorusOn) {
			readRegister(revWetL, 0.75);
			readRegister(chorusWetL, 0.5);
		}
		else {
			readRegister(revWetL, 0.75);
		}
		readRegister(dry, 0.5);
//		if(chorusOn) scaleOffset(0.75, 0.0);
		if(chorusOn) scaleOffset(1.5, 0.0);
		writeRegister(DACL, 0.0);
		
		// main mixdown - right channel
		if(chorusOn) {
			readRegister(revWetR, 0.75);
			readRegister(chorusWetR, 0.5);
		}
		else {
			readRegister(revWetR, 0.75);
		}
		readRegister(dry, 0.5);			
//		if(chorusOn) scaleOffset(0.75, 0.0);
		if(chorusOn) scaleOffset(1.5, 0.0);
		writeRegister(DACR, 0.0);
	}
}
