package com.wordtris;

import com.wordtris.screen.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by raymond on 11/4/16.
 */

public class InputController implements InputProcessor, GestureDetector.GestureListener {

    Movement movement;


    Wordtris game;

    GameScreen screen;

    public enum Movement {
        TOUCH_RIGHT, TOUCH_LEFT, SWIPE_UP, SWIPE_DOWN, SWIMPE_LEFT, SWIPE_RIGHT, DRAGDOWN, BACK, NONE,
    }

   public InputController(Wordtris game){
       this.game = game;
       movement = Movement.NONE;
   }

    public void setInputControllerScreen(GameScreen screen){ this.screen = screen; }

    public Movement getMovement(){ return this.movement; }

    public void clearMovement(){
        this.movement = Movement.NONE;
    }


//********Only these are implemented***************************************************
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {


        if(screenY > game.height * 0.1) {
            if (screenX < Gdx.graphics.getWidth() * 0.45) {
                this.movement = Movement.TOUCH_LEFT;
            } else if (screenX > Gdx.graphics.getWidth() * 0.55) {
                this.movement = Movement.TOUCH_RIGHT;
            }
        }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.BACK){
            this.movement = Movement.BACK;
        }
        return false;

    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.BACK) {
            this.movement = Movement.BACK;
        }

        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {    // Fail to work

        this.movement = Movement.DRAGDOWN;
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {    // Fail to work

        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (velocityX > 0) {
                movement = Movement.SWIPE_RIGHT;
            } else {
                movement = Movement.SWIPE_RIGHT;
            }
        } else {
            if (velocityY > 0) {
                movement = Movement.SWIPE_DOWN;
            } else {
                movement = Movement.SWIPE_UP;
            }
        }

        return true;
    }
    //***********************************************************************************

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }




    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {

        return false;
    }



    @Override
    public boolean tap(float x, float y, int count, int button) {

       return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
    /*This method is not used, but only in case a scrolling screen is needed

    public Tuple<Float, Float> inputConvertor(Input input) {
        Vector3 touchV = new Vector3(input.getX(), input.getY(), 0);
        game.camera.unproject(touchV);
        float x = game.camera.position.x - game.camera.viewportWidth + touchV.x;
        float y = game.camera.position.y - game.camera.viewportHeight + touchV.y;
        return new Tuple<Float, Float>(x ,y);
    }
    */

}
