package com.wordtris;

import com.wordtris.screen.GameScreen;
import com.wordtris.screen.MainMenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import  com.wordtris.screen.GameScreen.Style;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raymond on 11/4/16.
 *
 * This is project is prohibited from commercial use. It should also not released public before the demo day.
 *
 * This is only tested on Redmi 3 for API level 24. Screen dimension and API level may affect the experience in other devices
 */

public class Wordtris extends Game {

	public SpriteBatch batch;
	public InputController inputController;
	public SoundController soundController;
	public OrthographicCamera camera;

	public int height;
	public int width;

	private boolean buttonSelected = false;

	private GameScreen.Style selectedStyle = Style.OCEAN;
	private int highScore;
	private int netScore;
	private List<String> wordHistory = new ArrayList<String>();

	public Logger logger;

	public boolean isButtonSelected(){ return buttonSelected; }
	public void setButtonSelected(boolean bool){ this.buttonSelected = bool;}

	public Style getStyle(){
		return this.selectedStyle;
	}

	public void setStyle(Style style){
		selectedStyle = style;
	}

	public int getHighScore(){
		return highScore;
	}

	public void setHighScore(int highScore){
		this.highScore = highScore;
	}

	public int getNetScore(){
		return netScore;
	}

	public void setNetScore(int netScore){
		this.netScore = netScore;
	}

	public List<String> getWordHsitory(){
		return wordHistory;
	}

	public void insertWordRecord(String word){
		this.wordHistory.add(word);
	}

	public void setWordHistory(List<String> wordHistory){
		this.wordHistory = wordHistory;
	}


	public Wordtris(Logger logger) {
		this.logger = logger;
		logger.debug("test", "`Drop` game initialized.");

	}

	@Override
	public void create() {

		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();

		Gdx.input.setCatchBackKey(true);

		Config.load(this);

		inputController = new InputController(this);

		Gdx.input.setInputProcessor(inputController);
		soundController = new SoundController();
		soundController.initializeSound();


		batch = new SpriteBatch();

		camera = new OrthographicCamera(width, height);

		camera.position.set(width / 2, height / 2, 0);

		this.setScreen(new MainMenuScreen(this));

	}

	@Override
	public void render() {
		camera.update();
		super.render();

	}

	public void dispose() {

		soundController.disposeSound();
		batch.dispose();
	}


}
