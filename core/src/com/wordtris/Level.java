package com.wordtris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by raymond on 11/4/16.
 *
 * This would be more useful if multiple levels exist in the game
 */



public class Level{




    private int squareSize = 0;
    private int levelNumber;
    private int numberOfColumns = 10;
    private int numberOfRows = 20;
    private int groundRow;
    private float fallSpeed = 0, sinkSpeed = 150, pullSpeed = 200;


    private int top;
    private int ground;
    private int bottom;
    private int left;
    private int right;




    private Box[][] grid = new Box[numberOfColumns][numberOfRows];


    public Level(int levelNumber, int height, int width, int x, int y, Texture defaultBoxSkin) {

        this.levelNumber = levelNumber;
        this.groundRow = numberOfRows / 2;
        this.top = height - (height / 1024 * 10);
        this.bottom = y + (height / 1024 * 15);
        this.squareSize = (top - bottom) / numberOfRows;
        this.left =  x + (width / 512 * 20);
        this.right =  width - x - squareSize * numberOfColumns;
        this.ground = (top - bottom) / 2;



        initializeGrid(defaultBoxSkin);


    }

    public void initializeGrid(Texture defaultBoxSkin){
        for(int i = 0; i < numberOfColumns; i++){
            for(int j = 0; j < numberOfRows; j++){
                grid[i][j] = new Box(defaultBoxSkin,'.', this, i, j);
                grid[i][j].setPosition(left + i * getSquareSize(), bottom + j * getSquareSize());
                grid[i][j].setSize(getSquareSize(), getSquareSize());
         }
        }
    }

    public int top() {
        return this.top;
    }

    public int ground() {
        return this.ground;
    }

    public int bottom() {
        return this.bottom;
    }

    public int left() {
        return this.left;
    }

    public int right() {
        return this.right;
    }

    public int getGroundRow() {
        return this.groundRow;
    }

    public int getSquareSize() {
        return this.squareSize;
    }

    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    public float getFallSpeed() {
        return this.fallSpeed;
    }

    public float getSinkSpeed() {
        return this.sinkSpeed;
    }

    public float getPullSpeed() {
        return this.pullSpeed;
    }



    public Box[][] getGrid(){
        return this.grid;
    }

    public void setGrid(int col, int row, Box box){
        this.grid[col][row]= box;
    }

    public void setFallSpeed(float speed){
        this.fallSpeed = speed;
    }

    public void setFloatSpeed(float speed){
        this.pullSpeed = speed;
    }
}