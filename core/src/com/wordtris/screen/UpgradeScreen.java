package com.wordtris.screen;

import com.wordtris.Config;
import com.wordtris.InputController;
import com.wordtris.Tuple;
import com.wordtris.Wordtris;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by raymond on 11/17/16.
 *
 * Not Developed due to limited project time
 */

public class UpgradeScreen implements Screen {

    private Texture texture;
    private Sprite sprite;
    private BitmapFont font;

    private static Wordtris game;
    public UpgradeScreen(Wordtris game){
        this.game = game;
    }

    private static int UPGRADE_MAP_WIDTH =  (int)(game.width * 0.3);
    private static int UPGRADE_MAP_HEIGHT = (int)(UPGRADE_MAP_WIDTH * 0.5);
    private static int UPGRADE_MAP_X = (int)(game.width * 0.35);
    private static int UPGRADE_MAP_Y = (int)(game.height * 0.85);

    private static int WILDCARD_WIDTH =  (int)(game.width * 0.3);
    private static int WILDCARD_HEIGHT = (int)(WILDCARD_WIDTH * 0.35);
    private static int WILDCARD_X = (int)(game.height * 0.85);
    private static int WILDCARD_Y = (int)(game.height * 0.85);

    private static int BOMB_WIDTH =  (int)(game.width * 0.3);
    private static int BOMB_HEIGHT = (int)(BOMB_WIDTH * 0.5);
    private static int BOMB_X = (int)(game.width * 0.35);
    private static int BOMB_Y = (int)(game.height * 0.85);

    private static int TIMEDBOMB_WIDTH =  (int)(game.width * 0.3);
    private static int TIMEDBOMB_HEIGHT = (int)(TIMEDBOMB_WIDTH * 0.5);
    private static int TIMEDBOMB_X = (int)(game.width * 0.35);
    private static int TIMEDBOMB_Y = (int)(game.height * 0.85);

    private static int DYNAMIC_BOMB_WIDTH =  (int)(game.width * 0.3);
    private static int DYNAMIC_BOMB_HEIGHT = (int)(DYNAMIC_BOMB_WIDTH * 0.5);
    private static int DYNAMIC_BOMB_X = (int)(game.width * 0.35);
    private static int DYNAMIC_BOMB_Y = (int)(game.height * 0.85);

    private static int STEAMPUNK_WIDTH =  (int)(game.width * 0.3);
    private static int STEAMPUNK_HEIGHT = (int)(STEAMPUNK_WIDTH * 0.5);
    private static int STEAMPUNK_X = (int)(game.width * 0.35);
    private static int STEAMPUNK_Y = (int)(game.height * 0.85);

    private static int OCEAN_WIDTH =  (int)(game.width * 0.3);
    private static int OCEAN_HEIGHT = (int)(OCEAN_WIDTH * 0.5);
    private static int OCEAN_X = (int)(game.width * 0.35);
    private static int OCEAN_Y = (int)(game.height * 0.85);

    private static int CYBERPUNK_WIDTH =  (int)(game.width * 0.3);
    private static int CYBERPUNK_HEIGHT = (int)(CYBERPUNK_WIDTH * 0.5);
    private static int CYBERPUNK_X = (int)(game.width * 0.35);
    private static int CYBERPUNK_Y = (int)(game.height * 0.85);

    private static int OMFG_WIDTH =  (int)(game.width * 0.3);
    private static int OMFG_HEIGHT = (int)(OMFG_WIDTH * 0.5);
    private static int OMFG_X = (int)(game.width * 0.35);
    private static int OMFG_Y = (int)(game.height * 0.85);


    @Override
    public void show() {

        texture = new Texture(Gdx.files.internal("images/upgrade_list.png"));
        sprite = new Sprite(texture);

        sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);

        Gdx.input.setInputProcessor(new GestureDetector(game.inputController));
    }

    @Override
    public void render(float delta) {

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();


        Config.save(game);
        sprite.draw(game.batch);
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
        game.batch.dispose();
        texture.dispose();
        font.dispose();
        game.soundController.disposeSound();
    }


}
