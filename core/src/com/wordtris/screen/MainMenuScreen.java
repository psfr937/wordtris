package com.wordtris.screen;

import com.wordtris.InputController;
import com.wordtris.Wordtris;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by raymond on 11/15/16.
 */

public class MainMenuScreen implements Screen {



    Texture startGameButtonSkin, backgroundSkin;

    Sprite startGameButton, background;

    private final Wordtris game;

    public MainMenuScreen(Wordtris game) {
        this.game = game;

    }
    private int START_GAME_BUTTON_WIDTH, START_GAME_BUTTON_HEIGHT, START_GAME_BUTTON_X, START_GAME_BUTTON_Y;

    @Override
    public void show(){
        startGameButtonSkin = new Texture("images/start_game_button.png");
        backgroundSkin = new Texture("images/OMFG.png");

        START_GAME_BUTTON_WIDTH = (int)(game.width * 0.4);
        START_GAME_BUTTON_HEIGHT = (int)(START_GAME_BUTTON_WIDTH / 3);
        START_GAME_BUTTON_X = game.width / 2 - START_GAME_BUTTON_WIDTH / 2;
        START_GAME_BUTTON_Y = game.height / 6 - START_GAME_BUTTON_HEIGHT / 2;

        startGameButton = new Sprite(startGameButtonSkin);
      startGameButton.setSize(START_GAME_BUTTON_WIDTH, START_GAME_BUTTON_HEIGHT);
        startGameButton.setPosition( START_GAME_BUTTON_X, START_GAME_BUTTON_Y );

        background = new Sprite(backgroundSkin);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition( 0, 0 );

        game.soundController.menuMusic.setLooping(true);
        game.soundController.menuMusic.setVolume(0.3f);
        game.soundController.menuMusic.play();
    }

    @Override
    public void render(float delta) {

        if(Gdx.input.getX() <  START_GAME_BUTTON_X + START_GAME_BUTTON_WIDTH && Gdx.input.getX() >  START_GAME_BUTTON_X && game.height - Gdx.input.getY() < START_GAME_BUTTON_Y + START_GAME_BUTTON_HEIGHT && game.height - Gdx.input.getY() > START_GAME_BUTTON_Y) {
            if (Gdx.input.isTouched() && game.isButtonSelected() == false)
                game.setButtonSelected(true);
                game.logger.debug("Button", "go back to game screen");
                game.setScreen(new GameScreen(game));
                dispose();
        }

        if(game.inputController.getMovement() == InputController.Movement.BACK && game.isButtonSelected() == false){
            Gdx.app.exit();
        }


        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        background.draw(game.batch);
        startGameButton.draw(game.batch);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        startGameButtonSkin.dispose();
        backgroundSkin.dispose();
        game.soundController.menuMusic.dispose();
        game.logger.debug("okay", "okay_dispose");
    }
}
