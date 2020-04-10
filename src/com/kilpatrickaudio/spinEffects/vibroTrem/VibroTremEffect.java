package com.kilpatrickaudio.spinEffects.vibroTrem;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class VibroTremEffect extends ElmProgram {
	
	public VibroTremEffect(boolean touch, boolean filter) {
		super("Tremelo");

		allocDelayMem("delay", 256);

		int tremControlL = REG0;
		int tremControlR = REG1;
		int tremDepth = REG2;
//		int outGain = REG3;
		int vibratoL = REG4;
		int vibratoR = REG5;		
		int temp = REG6;
		int levelFilt = REG7;
		int averageLevel = REG8;
		
		// filter stuff
		int dry = REG16;
		int lowPassL = REG17;
		int bandPassL = REG18;
		int filterFreqVarL = REG19;
		int lowPassR = REG20;
		int bandPassR = REG21;
		int filterFreqVarR = REG22;
		
		skip(SKP_RUN, 2);
		loadSinLFO(0, 2.0, 0.999);
		loadSinLFO(1, 2.0, 0.999);
		
		// filter range
		if(filter) {
			readRegister(POT0, 1.0);
			if(touch) {
				mulx(averageLevel);
			}
			writeRegister(SIN1_RANGE, 0.0);
		}
		// vibrato range
		else {
			readRegister(POT0, 0.01);
			if(touch) {
				mulx(averageLevel);
			}
			writeRegister(SIN1_RANGE, 0.0);
		}

		// rate
		readRegister(POT1, 1.0);
		scaleOffset(0.5, 0.01);
		writeRegister(SIN0_RATE, 1.0);
		writeRegister(SIN1_RATE, 0.0);
		
//		// tremelo depth
		readRegister(POT2, 1.0);
		if(touch){
			mulx(averageLevel);
		}
		writeRegister(tremDepth, 1.0);
		
		// set sin (left) tremelo value
		chorusReadValue(CHO_LFO_SIN0);
		mulx(tremDepth);
		scaleOffset(-2.0, 0.0);
		scaleOffset(0.5, 0.5);
		writeRegister(tremControlL, 0.0);

		// set cos (right) tremelo value
		chorusReadValue(CHO_LFO_COS0);
		mulx(tremDepth);		
		scaleOffset(-2.0, 0.0);
		scaleOffset(0.5, 0.5);
		writeRegister(tremControlR, 0.0);

		// process audio
		readRegister(ADCL, 1.0);
		writeRegister(dry, 0.0);

		// find the average level
		readRegister(dry, 1.0);
		writeRegister(temp, 1.0);
		mulx(temp);
		readRegisterFilter(levelFilt, 0.0005);
		writeRegisterLowshelf(levelFilt, -1.0);
		log(0.5, 0.0);
		exp(1.0, 0.0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
		writeRegister(averageLevel, 0.0);
		
		// filter mode
		if(filter) {
			readRegister(POT0, 0.0);
			scaleOffset(-0.25, 0.0);
			writeRegister(temp, 0.0);
			
			chorusReadValue(CHO_LFO_SIN1);
			scaleOffset(0.25, -0.1);
			readRegister(temp, 1.0);
			exp(1.0, 0.0);
			writeRegister(filterFreqVarL, 0.0);

			// filter
			readRegister(bandPassL, 1.0);
			mulx(filterFreqVarL);
			readRegister(lowPassL, 1.0);		
			writeRegister(lowPassL, -1.0);
			readRegister(bandPassL, -0.3);  // set the Q - 0.1 = high, 0.5 = low
			readRegister(dry, 1.0);
			mulx(filterFreqVarL);
			readRegister(bandPassL, 1.0);
			writeRegister(bandPassL, 0.0);
			
			chorusReadValue(CHO_LFO_COS1);
			scaleOffset(0.25, -0.1);
			readRegister(temp, 1.0);
			exp(1.0, 0.0);
			writeRegister(filterFreqVarR, 0.0);

			// filter
			readRegister(bandPassR, 1.0);
			mulx(filterFreqVarR);
			readRegister(lowPassR, 1.0);		
			writeRegister(lowPassR, -1.0);
			readRegister(bandPassR, -0.3);  // set the Q - 0.1 = high, 0.5 = low
			readRegister(dry, 1.0);
			mulx(filterFreqVarR);
			readRegister(bandPassR, 1.0);
			writeRegister(bandPassR, 0.0);
			
			
			readRegister(bandPassL, 1.0);
			writeRegister(vibratoL, 0.0);
			readRegister(bandPassR, 1.0);
			writeRegister(vibratoR, 0.0);
		}
		// vibe mode
		else {
			readRegister(dry, 1.0);
			writeDelay("delay", 0, 0.0);
			
			// right vibrato
			chorusReadDelay(CHO_LFO_SIN1, CHO_REG | CHO_COMPC, 127);
			chorusReadDelay(CHO_LFO_SIN1, 0, 128);
			writeRegister(vibratoR, 0.0);
			
			// left vibrato
			chorusReadDelay(CHO_LFO_SIN1, CHO_COS | CHO_REG | CHO_COMPC, 127);
			chorusReadDelay(CHO_LFO_SIN1, CHO_COS, 128);		
			writeRegister(vibratoL, 0.0);			
		}
		
		// mixdown - left channel
		readRegister(vibratoL, 1.0);
		mulx(tremControlL);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-1.0, 0.0);
//		scaleOffset(1.0, -0.035);  // DC offset hack
		writeRegister(DACL, 0.0);

		// mixdown - right channel
		readRegister(vibratoR, 1.0);
		mulx(tremControlR);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-1.0, 0.0);
//		scaleOffset(1.0, -0.035);  // DC offset hack
		writeRegister(DACR, 0.0);
	}

}
