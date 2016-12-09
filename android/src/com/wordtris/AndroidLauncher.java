package com.wordtris;

import android.os.Bundle;

import com.wordtris.AndroidLogger;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.wordtris.Wordtris;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//initialize(new Wordtris(), config);

		initialize(new Wordtris(new AndroidLogger()), config);
	}
}
