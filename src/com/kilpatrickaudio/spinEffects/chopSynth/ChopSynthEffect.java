package com.kilpatrickaudio.spinEffects.chopSynth;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class ChopSynthEffect extends ElmProgram {
	
	public ChopSynthEffect(boolean oscStrip, boolean oscOn) {
		super("ChopSynth");

		// tremolo
		int tremControl = REG0;
		int dry = REG1;
		int duty = REG2;
				
		// oscillator
		int freq = REG10;
		int s = REG11;
		int c = REG12;
		int p0fil = REG13;  // coarse frequency filter
		int oscOut = REG16;  // override output
		
		// effect override
		int overrideOut = REG15;  // override output
		int overrideFilt = REG17;  // override filter 
		int overrideFiltIn = REG18;  // override filter input 
		int submix = REG19;  // the submix output
		int overrideOutInv = REG20;  // the inverse of the override
		
		// control inputs
		int freqPot = POT0;
		int speedPot = POT1;
		int dutyPot = POT2;
		
		// oscillator setup
		skip(SKP_RUN, 2);
		scaleOffset(0.0, 0.5);  // setup LFO with amp of 0.5
		writeRegister(s, 0.0);
		
		// trem LFO setup
		skip(SKP_RUN, 1);
		loadRampLFO(0, 10, 4096);
		readRegister(speedPot, 1.0);
		scaleOffset(1.0, 0.01);
		writeRegister(RMP0_RATE, 0.0);

		// duty cycle pot
		readRegister(dutyPot, 0.5);
		scaleOffset(0.9, -0.22);
		writeRegister(duty, 0.0);
		
		// process tremolo signal
		clear();
		chorusReadValue(CHO_LFO_RMP0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-1.0, -0.5);
		skip(SKP_GEZ, 1);  // make a triangle wave from a ramp
		scaleOffset(-1.0, 0.0);
		readRegister(duty, 1.0);  // add in the duty pot
		scaleOffset(1.0, -0.25);  // clip it
		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
//		scaleOffset(-2.0, 0.0);
		scaleOffset(-2.0, 0.0);
		scaleOffset(0.5, 0.5);
		writeRegister(tremControl, 0.0);	
		
		// process oscillator amp override
		clear();
		scaleOffset(0.0, 0.999);
		writeRegister(overrideFiltIn, 0.0);	
		
		// if the speed pot is at 0 - ramp down the override value to 0
		readRegister(speedPot, 1.0);
		scaleOffset(-1.0, 0.05);
		skip(SKP_NEG, 2);
		clear();
		writeRegister(overrideFiltIn, 0.0);
		clear();
		readRegister(overrideFiltIn, 1.0);
		readRegisterFilter(overrideFilt, 0.05);
		writeRegister(overrideFilt, 1.0);
		writeRegister(overrideOut, 1.0);
		scaleOffset(-1.0, 0.999);
		writeRegister(overrideOutInv, 0.0);
		
		// trem override if the duty cycle is >95%
		clear();
		readRegister(dutyPot, 1.0);
		scaleOffset(1.0, -0.95);
		skip(SKP_NEG, 3);
		clear();
		scaleOffset(0.0, 0.999);
		writeRegister(tremControl, 0.0);
		
		// process audio
		clear();
		readRegister(ADCL, 1.0);
		writeRegister(dry, 0.0);
		
		// oscillator
		if(oscStrip) {
			readRegister(speedPot, 1.0);  // the striptease should control the frequency
			scaleOffset(0.7, -0.2);  // scale to proper expo limit			
		}
		readRegister(freqPot, 0.5);  // main frequency control
		scaleOffset(1.0, -0.66);
		exp(1.0, 0.0);
		readRegisterFilter(p0fil, 0.01);  // smooth frequency adjustments
		writeRegister(p0fil, 1.0);
		writeRegister(freq, 0.0);
		
		// osc oversample 1
		readRegister(c, 1.0);  // oversample the oscillator
		mulx(freq);
		readRegister(s, 1.0);
		writeRegister(s, -1.0);
		mulx(freq);
		readRegister(c, 1.0);
		writeRegister(c, 0.0);
		// osc oversample 2
		readRegister(c, 1.0);  // oversample the oscillator
		mulx(freq);
		readRegister(s, 1.0);
		writeRegister(s, -1.0);
		mulx(freq);
		readRegister(c, 1.0);
		writeRegister(c, 0.0);
		// osc oversample 3
		readRegister(c, 1.0);  // oversample the oscillator
		mulx(freq);
		readRegister(s, 1.0);
		writeRegister(s, -1.0);
		mulx(freq);
		readRegister(c, 1.0);
		writeRegister(c, 1.99);
//		scaleOffset(-2.0, 0.0);  // affect the timbre of the oscillator sound
		scaleOffset(1.3, 0.2);  // do a bit of clipping
		scaleOffset(0.125, 0.0);  // turn down the result
//		mulx(overrideOut);
		writeRegister(oscOut, 0.0);
			
		// mixdown
		clear();
		readRegister(dry, 1.0);
		if(oscOn) {
			readRegister(oscOut, 0.5);  // mix in the oscillator
		}
		mulx(tremControl);  // modulate the audio
		mulx(overrideOut);  // process the override
		writeRegister(submix, 0.0);  // save the result

		// crossfade the submix and dry signals
		readRegister(dry, 1.0);
		mulx(overrideOutInv);
		readRegister(submix, 1.0);
		// amplify and output the result on the left channel only
		scaleOffset(-2.0, 0.0);
		scaleOffset(-1.0, 0.0);
		writeRegister(DACL, 0.0);  // this is the mono output
		writeRegister(DACR, 0.0);  // nothing comes out of here
	}

}
