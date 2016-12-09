package com.wordtris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

/**
 * Created by raymond on 11/4/16.
 */

public class SoundController {

    public Music menuMusic, gameMusic;

    public void initializeSound() {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/I_love_you.mp3"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Sunset_lover.mp3"));

    }

    public void disposeSound() {
        menuMusic.dispose();
        gameMusic.dispose();


    }
}
