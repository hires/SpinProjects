package com.kilpatrickaudio.spinEffects.reverbDelay;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class Mute extends ElmProgram {

	public Mute() {
		super("Mute");
		clear();
		writeRegister(DACL, 0.0);
		writeRegister(DACR, 0.0);
	}

}
