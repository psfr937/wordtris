package com.wordtris;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by raymond on 11/26/16.
 *
 * Check out the explosion.png under android/assets. It is a image with 8 by 8 small images as know as TextureRegion.
 *
 * The principle of animation is looping to draw the 8x8 array of textureRegion so fast inside rendering. Each drawing of the textureRegion is called a "frame"
 * Note that "frame per second" is different for different machine. The time difference is used to determine which frame the method should draw.
 * we can set the total duration of the animation.
 *
 * Since we are familiar with Sprie. We handle the animation better by extending Animation class with some behaviours similar to sprite, such that we can scale and translate easily.
 */

public class SpriteAnimation extends Animation {

    private float scaleX = 1;
    private float scaleY = 1;

    public SpriteAnimation(Float duration, TextureRegion[] region){
        super(duration, region);
    }



    /*...duplicate and call through to super constructors here...*/

    public void setScaling(float scale){
        scaleX = scale;
        scaleY = scale;
    }

    public void draw (float stateTime, Batch batch, float x, float y) {
        TextureRegion region = getKeyFrame(stateTime);
        batch.draw(region, x, y, region.getRegionWidth() * scaleX, region.getRegionHeight() *scaleY);
    }
}