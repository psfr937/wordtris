package com.wordtris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 * Created by raymond on 11/5/16.
 */

public class Matching{

    private static List<String> s_rows = new ArrayList<String>();
    private static List<String> s_cols = new ArrayList<String>();

    private static List list = new ArrayList();
    private static List<String> matchedWords = new ArrayList<String>();
    private static List<ArrayList<Tuple<Integer, Integer>>> voidRecord = new ArrayList<ArrayList<Tuple<Integer, Integer>>>();

    private static List<Sprite> matchedBoxes = new ArrayList<Sprite>();

    private static BufferedReader br = null;

    private static ArrayList<String> words= new ArrayList<String>();

    public static BufferedReader readFile() {
        br = new BufferedReader(new InputStreamReader(Gdx.files.internal("words.txt").read()));
        return br;
    }

    public static List<Sprite> getMatchedBoxes(){
        return matchedBoxes;
    }

    public static List<String> getMatchedWords(){
        return matchedWords;
    }

    public static void findMatch(Level level, Box currentBox, Wordtris game, Texture highlightBoxSkin) throws IOException{

        voidRecord.clear();

        s_rows.clear();
        s_cols.clear();

        if(br == null) {

            br = new BufferedReader(new InputStreamReader(Gdx.files.internal("words.txt").read()));     //save file words to a list of strings
            String line = "";

            while ((line = br.readLine()) != null) {
                words.add(line.toUpperCase());
            }
            br.close();
        }

        for (int i = 0; i < level.getNumberOfRows(); i++) { //extract all strings row by row
            String sr = "";
            for (int j = 0; j < level.getNumberOfColumns(); j++){
                sr += level.getGrid()[j][i].getChar();
            }
            s_rows.add(sr);
        }

        for (int i = 0; i < level.getNumberOfColumns(); i++) { //extract all strings column by column
            String sc = "";
            for (int j = level.getNumberOfRows() - 1; j >= 0; j--){
                sc += level.getGrid()[i][j].getChar();
            }
            System.out.println(sc);
            s_cols.add(sc);
        }

        for (String word : words) {

            if (word.length() >= 3) {

                for (int i = 0; i < s_rows.size(); i++) {

                    String sr = s_rows.get(i);
                    int position = -1;

                    for (int j = -1; (j = sr.indexOf(word, j + 1)) != -1; ) { //find substring "line" inside string "sr", return position of first letter

                        if (j != position) {
                            position = j;
                            matchedWords.add(word);
                            game.logger.debug("MATCH WORD", MessageFormat.format("Row, {0}", word));
                            game.insertWordRecord(word);
                            Config.save(game);

                            for (int k = 0; k < word.length(); k++) {       //for every matched string, find the position of the letters from the first letter

                                list.add(new Tuple<Integer, Integer>(j + k, i));

                                Sprite highlightBox = new Sprite(highlightBoxSkin);
                                highlightBox.setSize(level.getSquareSize(), level.getSquareSize());
                                highlightBox.setPosition(level.getGrid()[j+k][i].getX(), level.getGrid()[j+k][i].getY());
                                matchedBoxes.add(highlightBox);

                            }
                            voidRecord.add((ArrayList<Tuple<Integer, Integer>>)( (ArrayList) list).clone());
                        }
                        list.clear();
                    }
                }

                for (int i = 0; i < s_cols.size(); i++) {

                    String sc = s_cols.get(i);
                    int position = -1;

                    for (int j = -1; (j = sc.indexOf(word, j + 1)) != -1; ) { //find substring "line" inside string "sc", return position of first letter

                        if (j != position) {
                            position = j;
                            matchedWords.add(word);
                            game.logger.debug("MATCH WORD", MessageFormat.format("Column, {0}", word));

                            for (int k = 0; k < word.length(); k++) {        //for every matched string, find the position of the letters from the first letter

                                list.add(new Tuple<Integer, Integer>(i, j + k));

                                Sprite highlightBox = new Sprite(highlightBoxSkin);
                                highlightBox.setSize(level.getSquareSize(), level.getSquareSize());
                                highlightBox.setPosition(level.getGrid()[i][j+k].getX(), level.getGrid()[i][level.getNumberOfRows()-1-(j+k)].getY());
                                matchedBoxes.add(highlightBox);


                            }
                            voidRecord.add((ArrayList<Tuple<Integer, Integer>>)( (ArrayList) list).clone());
                        }
                        list.clear();
                    }
                }
            }
        }
    }

    public static void boom(Level level, Box currentBox) {

        voidRecord.clear();

        for(int i = currentBox.getColumn() - 1; i <= currentBox.getColumn() + 1; i ++) {
            for (int j = currentBox.getRow() - 1; j <= currentBox.getRow() + 1; j++) {
                if (i  >= 0 && i < level.getNumberOfColumns() && j >= 0 && j < level.getNumberOfRows()) {
                    if (level.getGrid()[i][j].getChar() != '.') {

                        list.add(new Tuple<Integer, Integer>(i, j));
                    }
                }
            }
        }
        voidRecord.add((ArrayList<Tuple<Integer, Integer>>)( (ArrayList) list).clone());
        list.clear();
    }

    private static boolean matched, blocked;
    private static String s_dynamic = "";
    private static String s_reverse = "";
    private static List<Tuple<Integer, Integer>> seenBoxes = new ArrayList<Tuple<Integer, Integer>>();


    public static boolean superMatch(Level level, int i, int j) throws IOException {    //Not developed due to project time limit

        voidRecord.clear();

        blocked = false;
        matched = false;
        if (br == null) {
            readFile();
        }


        s_dynamic += level.getGrid()[i][j].getChar();
        s_reverse = new StringBuilder(s_dynamic).reverse().toString();
        int n = 0;


        seenBoxes.add(new Tuple<Integer, Integer>(i, j));

        if (s_dynamic.length() == 1)
            matched = true;
        else {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (s_dynamic == line) {
                    matchedWords.add(line);

                    voidRecord.add((ArrayList)seenBoxes);

                    for (Tuple tuple : seenBoxes) {

                        level.getGrid()[tuple.x.intValue()][tuple.y.intValue()].setChar('.');
                    }
                    return true;
                }
                if (s_reverse == line) {
                    matchedWords.add(line);
                    return true;
                }

                for (int k = -1; (k = s_dynamic.indexOf(line, k + 1)) != -1; ) {
                    matched = true;
                }

                for (int k = -1; (k = new StringBuilder(s_dynamic).reverse().toString().indexOf(line, k + 1)) != -1; ) {
                    matched = true;
                }
            }
        }
            n = i + 1;
            if (seenBoxes.contains(new Tuple(n, j))) {
                superMatch(level, n, j);
            }
            n = i - 1;
            if (seenBoxes.contains(new Tuple(n, j))) {
                superMatch(level, n, j);
            }
            n = j + 1;
            if (seenBoxes.contains(new Tuple(i, n))) {
                superMatch(level, n, j);
            }
            n = j - 1;
            if (seenBoxes.contains(new Tuple(i, n))) {
                superMatch(level, n, j);
            } else {
                blocked = true;
            }

            voidRecord.remove(voidRecord.size() - 1);
            return false;
    }

    public static List<ArrayList<Tuple<Integer, Integer>>> getVoidRecord(){
        return voidRecord;
    }
}