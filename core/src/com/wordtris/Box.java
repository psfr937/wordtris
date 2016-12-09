package com.wordtris;

import com.wordtris.screen.GameScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.text.MessageFormat;

/**
 * Created by raymond on 11/4/16.
 */

public class Box extends Sprite {

    private char alphabet = 'x';
    private int column, row;

    private boolean falling = true, sinking = true, pulling = true;
    private int stableDestination;

    private float dy = 0;



    public Box(Texture texture, char alphabet, Level level, int column, int row) {

        super(texture);
        setSize(level.getSquareSize(), level.getSquareSize());
        this.alphabet = alphabet;
        this.column = column;
        this.row = row;
        setPosition(level.left() + column * level.getSquareSize(), level.bottom() + row * level.getSquareSize());


    }

    public void setChar(char c) {
        this.alphabet = c;
    }

    public int getColumn() { return this.column; }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row, Level level) {
        this.row = row;
    }

    public char getChar() {
        return this.alphabet;
    }

    public boolean isFalling() {
        return falling;
    }

    public boolean isSinking() {
        return sinking;
    }

    public boolean isPulling() {
        return pulling;
    }

    public void moveLeft(Level level) {

        column -= 1;

        this.setX(level.left() + column * level.getSquareSize());


    }

    public void moveRight(Level level) {

        column += 1;

        this.setX(level.left() + column * level.getSquareSize());

    }

    public void fall(Level level, float fallSpeed, float dt, int boxCount) {
        dy += fallSpeed * dt;
        while(dy >= 1) {
            dy -= 1;
            setY(getY() - 1);


            setRow(Math.round((getY() - level.bottom()) / level.getSquareSize()), level);

            if (getY() <= level.bottom() + getRow() * level.getSquareSize()) {
                if (getRow() == level.getGroundRow() || getRow() == 0) {
                    falling = false;
                } else {
                    if (level.getGrid()[getColumn()][getRow() - 1].getChar() != '.') {
                        falling = false;
                    }
                }
            }
        }
        if (falling == false && alphabet != '!' && boxCount != 0){
            setY(level.bottom() + getRow() * level.getSquareSize());
            level.setGrid(column, row, this);
        }
    }

    public void sink(Level level, float sinkSpeed, float dt) {

        dy += sinkSpeed * dt;
        while(dy >= 1) {
            dy -= 1;
            setY(getY() - 1);

            if(getY() <= level.bottom() + (getRow() - 1) * level.getSquareSize() ){
                setRow(getRow() - 1, level);
                setY(level.bottom() + getRow() * level.getSquareSize());
                level.setGrid(column, row, this);
                sinking = false;
            }
        }
    }

    public void pull(Level level, float pullSpeed, float dt, int numberOfHoles) {

        stableDestination = (getRow() + numberOfHoles);
        dy += pullSpeed * dt;
        while(dy >= 1) {
            dy -= 1;
            setY(getY() + Math.signum(numberOfHoles));

            if(numberOfHoles > 0){
                if (getY() >= level.bottom() + level.getSquareSize() * stableDestination){
                    setRow(stableDestination, level);
                    setY(level.bottom() + getRow() * level.getSquareSize());
                    level.setGrid(column, row, this);
                    pulling = false;
                }
            }
            else {
                if(getY() <= level.bottom() + level.getSquareSize() * stableDestination) {
                    setRow(stableDestination, level);
                    setY(level.bottom() + getRow() * level.getSquareSize());
                    level.setGrid(column, row, this);
                    pulling = false;
                }
            }
        }
    }
}

