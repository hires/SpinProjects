package com.kilpatrickaudio.spinEffects.echo;

import org.andrewkilpatrick.elmGen.ElmProgram;

public class Mute extends ElmProgram {

	public Mute() {
		super("Mute");
		clear();
		writeRegister(DACL, 1.0);
		writeRegister(DACR, 0.0);
	}

}
