package com.kilpatrickaudio.spinEffects.vibroTrem;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class Mute extends ElmProgram {

	public Mute() {
		super("Mute");
		clear();
		scaleOffset(1.0, -0.035);  // DC offset hack
		writeRegister(DACL, 1.0);
		writeRegister(DACR, 0.0);
	}

}
