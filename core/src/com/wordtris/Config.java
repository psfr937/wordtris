package com.wordtris;

/**
 * Created by raymond on 11/22/16.
 *
 * for loading and saving games
 *
 */


import com.wordtris.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

public class Config {

    public static final Preferences pref = Gdx.app.getPreferences("preferences");


    public static Json json = new Json();

    public static void load(Wordtris game) {


        if(pref.contains("style")) {
            game.setStyle(GameScreen.Style.valueOf(pref.getString("style").toUpperCase()));
        }
        if(pref.contains("highScore")) {
            game.setHighScore(pref.getInteger("highScore"));

        }
        if(pref.contains("netScore")) {
            game.setNetScore(pref.getInteger("netScore"));
        }
        if(pref.contains("wrodHistory")) {
            String serializedWords = pref.getString("wordHistory");
            game.setWordHistory(json.fromJson(List.class, serializedWords));
        }
    }

    public static void save(Wordtris game){
        pref.putString("style", game.getStyle().name());
        pref.putInteger("highScore", game.getHighScore());
        pref.putInteger("netScore", game.getNetScore());
        Hashtable<String, String> hashTable = new Hashtable<String, String>();
        hashTable.put("wordHistory", json.toJson(game.getWordHsitory()));
        pref.flush();

    }
}