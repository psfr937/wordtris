package com.wordtris;

/**
 * Created by raymond on 11/4/16.
 *
 * Since we use x / y, column / row pairs a lot in complicated 2D logic. It is useful to make a data structure which contains a pair of numbers.
 *
 */

public class Tuple<X extends Number, Y extends Number> {
    public X x;
    public Y y;
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X x(){
        return x;
    }

    public Y y(){
        return y;
    }

}