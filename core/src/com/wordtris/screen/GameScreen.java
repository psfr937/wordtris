package com.wordtris.screen;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.wordtris.Box;
import com.wordtris.InputController;
import com.wordtris.Level;
import com.wordtris.Matching;
import com.wordtris.SpriteAnimation;
import com.wordtris.Wordtris;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;



import static com.wordtris.Config.save;
import static com.badlogic.gdx.Gdx.input;

/**
 * Created by raymond on 11/15/16.
 */


public class GameScreen implements Screen {

    private BitmapFont scoreFont, timeFont, gameOverScoreFont, highestScoreFont, eliminatedBoxCountFont, boxCountFont, speedFont, matchedWordsFont;


    private Texture defaultBoxSkin, highlightBoxSkin, bombSkin, pauseMenuSkin, gameOverMenuSkin, speedDisplaySkin, explosionImage;
    private Texture backgroundSkin, boardSkin, resumeButtonSkin, exitButtonSkin, restartButtonSkin;
    private Sprite background, board, pauseMenu, gameOverMenu, speedDisplay, speedUpButton, speedDownButton, resumeButton, restartButton, exitButton, restartButton2, exitButton2;

    public Sound dropSound, bombSound;

    private static Level level;

    private final Wordtris game;

    public GameScreen(Wordtris game) {
        this.game = game;
    }

    private static int BOARD_WIDTH;
    private static int BOARD_HEIGHT;
    private static int BOARD_X;
    private static int BOARD_Y;

    private static int SPEED_DISPLAY_WIDTH;
    private static int SPEED_DISPLAY_HEIGHT;
    private static int SPEED_DISPLAY_X;
    private static int SPEED_DISPLAY_Y;

    private static int PAUSE_MENU_WIDTH;
    private static int PAUSE_MENU_HEIGHT;
    private static int PAUSE_MENU_X;
    private static int PAUSE_MENU_Y;
    private static int RESUME_BUTTON_WIDTH;
    private static int RESUME_BUTTON_HEIGHT;
    private static int RESUME_BUTTON_X;
    private static int RESUME_BUTTON_Y;
    private static int RESTART_BUTTON_WIDTH;
    private static int RESTART_BUTTON_HEIGHT;
    private static int RESTART_BUTTON_X;
    private static int RESTART_BUTTON_Y;
    private static int EXIT_BUTTON_WIDTH;
    private static int EXIT_BUTTON_HEIGHT;
    private static int EXIT_BUTTON_X;
    private static int EXIT_BUTTON_Y;

    private static int GAME_OVER_MENU_WIDTH;
    private static int GAME_OVER_MENU_HEIGHT;
    private static int GAME_OVER_MENU_X;
    private static int GAME_OVER_MENU_Y;
    private static int RESTART_BUTTON2_WIDTH;
    private static int RESTART_BUTTON2_HEIGHT;
    private static int RESTART_BUTTON2_X;
    private static int RESTART_BUTTON2_Y;
    private static int EXIT_BUTTON2_WIDTH;
    private static int EXIT_BUTTON2_HEIGHT;
    private static int EXIT_BUTTON2_X;
    private static int EXIT_BUTTON2_Y;


    private float dt = 0;


    public enum State {
        FALL,
        SINK,
        PULL,
        MATCH,
        DISAPPEAR,
        PAUSE,
        GAME_OVER
    }

    /*
    *
    *
    **/
    public enum Style {
        MATCHED,
        VINTAGE,

        STEAMPUNK,
        OCEAN,
        MODERN,
        DOODLE,
        CYBERPUNK,
        FUTURISTIC,
        OMFG
    }

    private State state = State.FALL, recentState = State.FALL;

    private List<Box> sinkingBoxes = new ArrayList<Box>();
    private Map<Integer, Integer> unstableBoxesSlots = new HashMap<Integer, Integer>();
    private Map<Box, Integer> unstableBoxes = new HashMap<Box, Integer>();


    private SortedMap<Character, Texture> boxSkin = new TreeMap<Character, Texture>();


    int score = 0;
    private int levelNumber;

    Random r = new Random();
    int nextItem;
    char alphabet;

    float recentFade = 1;


    long startTime, startPauseTime, startExplodingTime, timeElapsed;
    int secondsElapsed;

    public State getState() {
        return state;
    }

    public Level getLevel() {
        return level;
    }

    TextureRegion[] explosionFrames;

    private SpriteAnimation explosion;

    boolean isExploding, pulled = false;

    float explosion_X = 0, explosion_Y = 0;

    String speedFontData = "x1";

    public Texture getHighlightBoxSkin() {
        return this.highlightBoxSkin;
    }

    FreeTypeFontGenerator generator;
    FreeTypeFontParameter parameter;

    GlyphLayout glyphLayout = new GlyphLayout();

    public void initAnimation() {
        explosionImage = new Texture("images/explosion.png");


        explosionFrames = new TextureRegion[64];


        TextureRegion[][] tmpFrames = TextureRegion.split(explosionImage, 128, 128);

        int index = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                explosionFrames[index++] = tmpFrames[j][i];
            }
        }
        explosion = new SpriteAnimation(5f, explosionFrames);
        explosion.setScaling(3);
    }

    @Override
    public void show() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/SEASRN__.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 96;


        matchedWordsFont = generator.generateFont(parameter);


        game.inputController.setInputControllerScreen(this);

        dropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"));
        bombSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));

        game.logger.debug("okay", "okay");

        speedDisplaySkin = new Texture("images/speed_display.png");
        scoreFont = new BitmapFont();
        timeFont = new BitmapFont();
        highestScoreFont = new BitmapFont();
        gameOverScoreFont = new BitmapFont();
        boxCountFont = new BitmapFont();
        eliminatedBoxCountFont = new BitmapFont();


        speedFont = new BitmapFont();

        initAnimation();

        defaultBoxSkin = new Texture("images/default_box.png");

        highlightBoxSkin = new Texture("images/highlight_box.png");


        backgroundSkin = new Texture(MessageFormat.format("images/{0}/background.png", game.getStyle().name().toLowerCase()));
        boardSkin = new Texture(MessageFormat.format("images/{0}/board.png", game.getStyle().name().toLowerCase()));

        pauseMenuSkin = new Texture("images/pause_menu.png");
        gameOverMenuSkin = new Texture("images/game_over_menu.png");


        resumeButtonSkin = new Texture("images/resume_button.png");
        restartButtonSkin = new Texture("images/restart_button.png");
        exitButtonSkin = new Texture("images/exit_button.png");

        for (char c = 'A'; c <= 'Z'; c++) {
            boxSkin.put(c, new Texture(MessageFormat.format("images/{0}/{1}.png", game.getStyle().name().toLowerCase(), c)));
        }
        bombSkin = new Texture("images/bomb.png");

        float ratio = 0.95f;

        BOARD_HEIGHT = (int) (game.height * ratio);
        BOARD_WIDTH = (BOARD_HEIGHT - BOARD_HEIGHT / 1024 * 20) / 2 + BOARD_HEIGHT / 1024 * 26;
        BOARD_X = game.width / 2 - BOARD_WIDTH / 2;
        BOARD_Y = 0;

        level = new Level(levelNumber, BOARD_HEIGHT, BOARD_WIDTH, BOARD_X, BOARD_Y, defaultBoxSkin);


        SPEED_DISPLAY_WIDTH = (int) (game.width * 0.25);
        SPEED_DISPLAY_HEIGHT = (int) (level.getSquareSize() * 0.8);
        SPEED_DISPLAY_X = (int) (game.width * 0.50);
        SPEED_DISPLAY_Y = (int) (game.height * 0.955);

        PAUSE_MENU_WIDTH = (int) (game.width * 0.6);
        PAUSE_MENU_HEIGHT = (int) (PAUSE_MENU_WIDTH * 1.7f);
        PAUSE_MENU_X = (int) (game.width * 0.5 - PAUSE_MENU_WIDTH / 2);
        PAUSE_MENU_Y = (int) (game.height * 0.5 - PAUSE_MENU_HEIGHT / 2);
        RESUME_BUTTON_WIDTH = (int) (game.width * 0.45);
        RESUME_BUTTON_HEIGHT = (int) (RESUME_BUTTON_WIDTH / 5);
        RESUME_BUTTON_X = (int) (game.width * 0.5 - RESUME_BUTTON_WIDTH / 2);
        RESUME_BUTTON_Y = (int) (PAUSE_MENU_Y + PAUSE_MENU_HEIGHT * 0.55);
        RESTART_BUTTON_WIDTH = (int) (game.width * 0.45);
        RESTART_BUTTON_HEIGHT = (int) (RESTART_BUTTON_WIDTH / 5);
        RESTART_BUTTON_X = (int) (game.width * 0.5 - RESTART_BUTTON_WIDTH / 2);
        RESTART_BUTTON_Y = (int) (PAUSE_MENU_Y + PAUSE_MENU_HEIGHT * 0.35);
        EXIT_BUTTON_WIDTH = (int) (game.width * 0.45);
        EXIT_BUTTON_HEIGHT = (int) (EXIT_BUTTON_WIDTH / 5);
        EXIT_BUTTON_X = (int) (game.width * 0.5 - EXIT_BUTTON_WIDTH / 2);
        EXIT_BUTTON_Y = (int) (PAUSE_MENU_Y + PAUSE_MENU_HEIGHT * 0.15);

        GAME_OVER_MENU_WIDTH = (int) (game.width * 0.6);
        GAME_OVER_MENU_HEIGHT = (int) (GAME_OVER_MENU_WIDTH * 1.5);
        GAME_OVER_MENU_X = (int) (game.width * 0.5 - GAME_OVER_MENU_WIDTH / 2);
        GAME_OVER_MENU_Y = (int) (game.height * 0.5 - GAME_OVER_MENU_HEIGHT / 2);
        RESTART_BUTTON2_WIDTH = (int) (game.width * 0.45);
        RESTART_BUTTON2_HEIGHT = (int) (RESTART_BUTTON2_WIDTH / 5);
        RESTART_BUTTON2_X = (int) (game.width * 0.5 - RESTART_BUTTON_WIDTH / 2);
        RESTART_BUTTON2_Y = (int) (PAUSE_MENU_Y + GAME_OVER_MENU_HEIGHT * 0.25);
        EXIT_BUTTON2_WIDTH = (int) (game.width * 0.45);
        EXIT_BUTTON2_HEIGHT = (int) (EXIT_BUTTON2_WIDTH / 5);
        EXIT_BUTTON2_X = (int) (game.width * 0.5 - EXIT_BUTTON_WIDTH / 2);
        EXIT_BUTTON2_Y = (int) (PAUSE_MENU_Y + GAME_OVER_MENU_HEIGHT * 0.1);

        game.logger.debug("okay", "okay2");

        background = new Sprite(backgroundSkin);
        background.setPosition(0, 0);
        background.setSize(game.width, game.height);
        board = new Sprite(boardSkin);
        board.setPosition(BOARD_X, BOARD_Y);
        board.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        pauseMenu = new Sprite(pauseMenuSkin);
        pauseMenu.setPosition(PAUSE_MENU_X, PAUSE_MENU_Y);
        pauseMenu.setSize(PAUSE_MENU_WIDTH, PAUSE_MENU_HEIGHT);

        gameOverMenu = new Sprite(gameOverMenuSkin);
        gameOverMenu.setPosition(GAME_OVER_MENU_X, GAME_OVER_MENU_Y);
        gameOverMenu.setSize(GAME_OVER_MENU_WIDTH, GAME_OVER_MENU_HEIGHT);
        game.logger.debug("okay", "okay2.3");


        speedDisplay = new Sprite(speedDisplaySkin);
        speedDisplay.setPosition(SPEED_DISPLAY_X, SPEED_DISPLAY_Y);
        speedDisplay.setSize(SPEED_DISPLAY_WIDTH, SPEED_DISPLAY_HEIGHT);


        resumeButton = new Sprite(resumeButtonSkin);
        resumeButton.setPosition(RESUME_BUTTON_X, RESUME_BUTTON_Y);
        resumeButton.setSize(RESUME_BUTTON_WIDTH, RESUME_BUTTON_HEIGHT);
        game.logger.debug("okay", "okay2.6");
        restartButton = new Sprite(restartButtonSkin);
        restartButton.setPosition(RESTART_BUTTON_X, RESTART_BUTTON_Y);
        restartButton.setSize(RESTART_BUTTON_WIDTH, RESTART_BUTTON_HEIGHT);
        restartButton2 = new Sprite(restartButtonSkin);
        restartButton2.setPosition(RESTART_BUTTON2_X, RESTART_BUTTON2_Y);
        restartButton2.setSize(RESTART_BUTTON2_WIDTH, RESTART_BUTTON2_HEIGHT);
        exitButton = new Sprite(exitButtonSkin);
        exitButton.setPosition(EXIT_BUTTON_X, EXIT_BUTTON_Y);
        exitButton.setSize(EXIT_BUTTON2_WIDTH, EXIT_BUTTON2_HEIGHT);
        game.logger.debug("okay", "okay2.9");
        exitButton2 = new Sprite(exitButtonSkin);
        exitButton2.setPosition(EXIT_BUTTON2_X, EXIT_BUTTON2_Y);
        exitButton2.setSize(EXIT_BUTTON2_WIDTH, EXIT_BUTTON2_HEIGHT);
        game.logger.debug("okay", "okay2.95");


        game.inputController.setInputControllerScreen(new GameScreen(game));

        startPauseTime = 0;
        startExplodingTime = 0;
        timeElapsed = 0;
        secondsElapsed = 0;
        startTime = System.currentTimeMillis();

        game.soundController.gameMusic.setLooping(true);
        game.soundController.gameMusic.setVolume(0.3f);
        game.soundController.gameMusic.play();
    }


    private Box currentBox;
    private boolean dropDirectly = false;

    private int eliminatedBoxCount = 0;
    private int boxCount = 0;

    private boolean firstRenderLoop = true;
    private float inputMoment = 0;
    private long buttonSelectedTime = 0;

    private LinkedList<Box> boxQueue = new LinkedList<Box>();
    private int queueSize = 2;
    private Map<Integer, Set<Integer>> uniqueHoles = new TreeMap<Integer, Set<Integer>>();

    private long matchedMoment = 0;

    public void withdrawBox() {
        if (!boxQueue.isEmpty()) {
            currentBox = boxQueue.removeFirst();
            currentBox.setPosition(level.left() + currentBox.getColumn() * level.getSquareSize(), level.bottom() + currentBox.getRow() * level.getSquareSize());
            game.setNetScore(game.getNetScore() + 10);
            score += 10;
            save(game);
            boxCount++;
        }
    }

    @Override
    public void render(float delta) {

        game.setButtonSelected(false);

        if (state != State.PAUSE && state != State.GAME_OVER) {
            timeElapsed = System.currentTimeMillis() - startTime + startPauseTime;
            secondsElapsed = (int) (timeElapsed / 1000);
        }

        game.camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (state != State.PAUSE && state != State.GAME_OVER) {

            if (input.getX() < SPEED_DISPLAY_X + SPEED_DISPLAY_WIDTH * 1.2 && input.getX() > SPEED_DISPLAY_X + (SPEED_DISPLAY_WIDTH / 2) && game.height - input.getY() < SPEED_DISPLAY_Y + SPEED_DISPLAY_HEIGHT && game.height - input.getY() > SPEED_DISPLAY_Y) {
                if (input.isTouched() && (System.currentTimeMillis() - buttonSelectedTime) > 500) {
                    level.setFallSpeed(400);
                    speedFontData = "x2";
                    buttonSelectedTime = System.currentTimeMillis();

                }
            } else if (input.getX() < SPEED_DISPLAY_X + (SPEED_DISPLAY_WIDTH / 2) && input.getX() > SPEED_DISPLAY_X - SPEED_DISPLAY_WIDTH * 0.2 && game.height - input.getY() < SPEED_DISPLAY_Y + SPEED_DISPLAY_HEIGHT && game.height - input.getY() > SPEED_DISPLAY_Y) {
                if (input.isTouched() && (System.currentTimeMillis() - buttonSelectedTime) > 500) {
                    level.setFallSpeed(150);
                    speedFontData = "x1";
                    buttonSelectedTime = System.currentTimeMillis();

                }
            }

            if (game.inputController.getMovement() == InputController.Movement.BACK && (System.currentTimeMillis() - buttonSelectedTime) > 500) {

                if (state != State.PAUSE && state != State.GAME_OVER) {
                    recentState = state;
                    startPauseTime = timeElapsed;
                    state = State.PAUSE;
                } else {
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                }
                buttonSelectedTime = System.currentTimeMillis();
            }

            if (boxCount == 0) {
                nextItem = r.nextInt(27);
                if (nextItem != 26) {
                    alphabet = (char) (nextItem + 'A');
                    currentBox = new Box(boxSkin.get(alphabet), alphabet, level, level.getNumberOfColumns() / 2, level.getNumberOfRows() - 1);
                } else {
                    alphabet = '!';
                    currentBox = new Box(bombSkin, alphabet, level, level.getNumberOfColumns() / 2, level.getNumberOfRows() - 1);
                }
                boxCount++;
            }

            while (boxQueue.size() < queueSize) {

                nextItem = r.nextInt(27);
                if (nextItem < 26) {
                    alphabet = (char) (nextItem + 'A');
                    boxQueue.add(new Box(boxSkin.get(alphabet), alphabet, level, level.getNumberOfColumns() / 2, level.getNumberOfRows() - 1));
                } else {
                    alphabet = '!';
                    boxQueue.add(new Box(bombSkin, alphabet, level, level.getNumberOfColumns() / 2, level.getNumberOfRows() - 1));
                }

            }
        }


        switch (state) {

            case FALL:

                dt = Gdx.graphics.getDeltaTime();


                if (firstRenderLoop) {
                    firstRenderLoop = false;
                    level.setFallSpeed(150);
                    break;
                }

                if (game.inputController.getMovement() == InputController.Movement.NONE || Math.abs(currentBox.getY() - inputMoment) < 5) {
                    currentBox.fall(level, level.getFallSpeed(), dt, boxCount);        //no command, just fall
                } else {

                    if (game.inputController.getMovement() == InputController.Movement.TOUCH_LEFT && !dropDirectly) {            //move box left

                        inputMoment = currentBox.getY();

                        if (currentBox.getColumn() > 0) {
                            if (level.getGrid()[currentBox.getColumn() - 1][currentBox.getRow()].getChar() == '.') {
                                currentBox.moveLeft(level);
                            }
                        }

                    } else if (game.inputController.getMovement() == InputController.Movement.TOUCH_RIGHT && !dropDirectly) {        //move box right

                        inputMoment = currentBox.getY();
                        if (currentBox.getColumn() < level.getNumberOfColumns() - 1) {

                            if (level.getGrid()[currentBox.getColumn() + 1][currentBox.getRow()].getChar() == '.') {
                                currentBox.moveRight(level);
                            }
                        }

                    }
                    if (Math.abs(Gdx.input.getAccelerometerZ()) > 20) {    //swipe down to drop quickier

                        game.logger.debug("okay", "QUICK");
                        inputMoment = currentBox.getY();
                        level.setFallSpeed(level.getFallSpeed() * 6);
                        dropDirectly = true;

                    }
                    game.inputController.clearMovement();
                }

                if (!currentBox.isFalling()) {

                    dropSound.play();

                    if (dropDirectly) {
                        level.setFallSpeed(20);
                        dropDirectly = false;
                    }
                    if (currentBox.getRow() != level.getGroundRow() && level.getGrid()[currentBox.getColumn()][0].getChar() == '.' && currentBox.getChar() != '!') {
                        for (int j = 0; j <= currentBox.getRow(); j++) {
                            Box box = level.getGrid()[currentBox.getColumn()][j];
                            if (box.getChar() != '.') {
                                sinkingBoxes.add(new Box(boxSkin.get(box.getChar()), box.getChar(), level, currentBox.getColumn(), j));
                                box.setChar('.');   //disappear for boxes which are going to sink
                            }
                        }

                        state = State.SINK;
                    } else {
                        recentState = state;
                        state = State.MATCH;        //box dropped, now for matching
                    }
                }
                break;

            case SINK:

                int unfinishedSinkingBoxes = 0;
                for (Box box : sinkingBoxes) {
                    if (box.isSinking()) {
                        unfinishedSinkingBoxes++;
                        box.sink(level, level.getSinkSpeed(), dt);
                    }
                }
                if (unfinishedSinkingBoxes == 0) {
                    currentBox = level.getGrid()[currentBox.getColumn()][currentBox.getRow() - 1];
                    sinkingBoxes.clear();
                    state = State.MATCH;
                }

                break;

            case MATCH:

                Matching.getVoidRecord().clear();

                if (recentState == State.FALL && currentBox.getRow() >= level.getNumberOfRows() - 1) {
                    state = State.GAME_OVER;
                    break;
                }
                if (currentBox.getChar() != '!') {  //if it is not a bomb
                    try {
                        Matching.findMatch(level, currentBox, game, highlightBoxSkin);

                        String text = "", lastText = "";
                        for (int i = 0; i < Matching.getMatchedWords().size(); i++) {
                            if (Matching.getMatchedWords().get(i) != lastText) {
                                lastText = Matching.getMatchedWords().get(i);
                                text += MessageFormat.format("{0}\n", lastText);
                                game.logger.debug("words", MessageFormat.format("{0}", lastText));
                            }
                        }

                        glyphLayout.setText(matchedWordsFont, text);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (pulled == false) {
                    isExploding = true;
                    bombSound.play();
                    startExplodingTime = System.currentTimeMillis();
                    explosion_X = currentBox.getX() - explosionImage.getWidth() / 16 * 3;
                    explosion_Y = currentBox.getY();
                    Matching.boom(level, currentBox);
                    game.logger.debug("boom", "boom");
                }

                if (Matching.getVoidRecord().isEmpty()) {

                    withdrawBox();

                    pulled = false;

                    state = State.FALL;   //no reaction happened, next round of falling box

                } else {

                    if (Matching.getMatchedWords().isEmpty() == false) {
                        matchedMoment = timeElapsed;
                    }

                    for (int i = 0; i < Matching.getVoidRecord().size(); i++) {

                        int scoreAdded = Matching.getVoidRecord().get(i).size() * (50 * (int) Math.pow(2, 3 - Matching.getVoidRecord().get(i).size()));
                        game.setNetScore(game.getNetScore() + scoreAdded);
                        score += scoreAdded;
                        eliminatedBoxCount += Matching.getVoidRecord().get(i).size();

                    }
                    save(game);


                    state = State.DISAPPEAR;
                }


                break;

            case DISAPPEAR:

                if (timeElapsed - matchedMoment > 600) {

                    int x, y;

                    Matching.getMatchedWords().clear();
                    Matching.getMatchedBoxes().clear();

                    for (int i = 0; i < Matching.getVoidRecord().size(); i++) {

                        for (int j = 0; j < Matching.getVoidRecord().get(i).size(); j++) {

                            x = Matching.getVoidRecord().get(i).get(j).x();
                            y = Matching.getVoidRecord().get(i).get(j).y();
                            level.getGrid()[x][y].setChar('.'); //disppear for boxes which are being destroyed
                            if (uniqueHoles.containsKey(x)) {
                                uniqueHoles.get(x).add(y);

                            } else {
                                uniqueHoles.put(x, new HashSet());     //extract unique holes
                                uniqueHoles.get(x).add(y);

                            }
                        }
                    }


                    for (int i = 0; i < level.getNumberOfColumns(); i++) {

                        if (uniqueHoles.containsKey(i)) {        //for all column with holes



                            for (int j = 0; j < level.getNumberOfRows() / 2 + 1; j++) {
                                if (level.getGrid()[i][j].getChar() != '.') {
                                    for (int k : uniqueHoles.get(i)) {
                                        if ((k > j && k < level.getNumberOfRows() / 2) || (k == level.getNumberOfRows() / 2 && level.getGrid()[i][k + 1].getChar() == '.')) {

                                            int slot = level.getNumberOfRows() * i + j;
                                            if (unstableBoxesSlots.containsKey(slot)) {
                                                unstableBoxesSlots.put(slot, unstableBoxesSlots.get(slot) + 1);
                                            } else {
                                                unstableBoxesSlots.put(slot, 1);
                                            }
                                        }
                                    }
                                }
                            }
                            for (int j = level.getNumberOfRows() - 1; j > level.getNumberOfRows() / 2; j--) {
                                if (level.getGrid()[i][j].getChar() != '.') {
                                    for (int k : uniqueHoles.get(i)) {
                                        if (k < j && (k > level.getNumberOfRows() / 2 || (k == level.getNumberOfRows() / 2 && level.getGrid()[i][k + 1].getChar() != '.'))) {
                                            int slot = level.getNumberOfRows() * i + j;
                                            if (unstableBoxesSlots.containsKey(slot)) {
                                                unstableBoxesSlots.put(slot, unstableBoxesSlots.get(slot) - 1);
                                            } else {
                                                unstableBoxesSlots.put(slot, -1);
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }

                    uniqueHoles.clear(); //Very important to clear after each disappearing event, we missed this line in our submission :(

                    if (unstableBoxesSlots.isEmpty()) {

                        withdrawBox();

                        state = state.FALL;
                    } else {
                        for (Map.Entry<Integer, Integer> cursor : unstableBoxesSlots.entrySet()) {
                            int i = cursor.getKey() / level.getNumberOfRows();
                            int j = cursor.getKey() % level.getNumberOfRows();

                            unstableBoxes.put(new Box(boxSkin.get(level.getGrid()[i][j].getChar()), level.getGrid()[i][j].getChar(), level, i, j), cursor.getValue());

                            level.getGrid()[i][j].setChar('.');     //disappear for boxes that are going to be pulled
                        }
                        state = State.PULL;
                    }
                }

                break;

            case PULL:

                dt = Gdx.graphics.getDeltaTime();

                int unfinishedPullingBoxes = 0;

                for (Map.Entry<Box, Integer> cursor : unstableBoxes.entrySet()) {
                    if (cursor.getKey().isPulling()) {
                        unfinishedPullingBoxes++;
                        cursor.getKey().pull(level, level.getPullSpeed(), dt, cursor.getValue());
                    }
                }
                if (unfinishedPullingBoxes == 0) {

                    unstableBoxesSlots.clear();
                    unstableBoxes.clear();
                    recentState = state;

                    pulled = true;

                    state = State.MATCH;
                }


                break;

            case PAUSE:

                if (game.isButtonSelected() == false) {
                    if (Gdx.input.getX() < RESUME_BUTTON_X + RESUME_BUTTON_WIDTH && Gdx.input.getX() > RESUME_BUTTON_X && game.height - Gdx.input.getY() < RESUME_BUTTON_Y + RESUME_BUTTON_HEIGHT && game.height - Gdx.input.getY() > RESUME_BUTTON_Y) {
                        if (input.isTouched()) {
                            game.setButtonSelected(true);
                            startTime = System.currentTimeMillis();
                            state = recentState;
                        }
                    } else if (Gdx.input.getX() < RESTART_BUTTON_X + RESTART_BUTTON_WIDTH && Gdx.input.getX() > RESTART_BUTTON_X && game.height - Gdx.input.getY() < RESTART_BUTTON_Y + RESTART_BUTTON_HEIGHT && game.height - Gdx.input.getY() > RESTART_BUTTON_Y) {
                        if (input.isTouched()) {
                            game.setButtonSelected(true);
                            game.setScreen(new GameScreen(game));
                            dispose();
                        }
                    } else if (Gdx.input.getX() < EXIT_BUTTON_X + EXIT_BUTTON_WIDTH && Gdx.input.getX() > EXIT_BUTTON_X && game.height - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && game.height - Gdx.input.getY() > EXIT_BUTTON_Y) {
                        if (input.isTouched()) {
                            game.setButtonSelected(true);
                            state = State.GAME_OVER;
                        }
                    }

                }

                break;


            case GAME_OVER:

                if (score > game.getHighScore()) {
                    game.setHighScore(score);
                }

                if (game.isButtonSelected() == false) {

                    if (Gdx.input.getX() < RESTART_BUTTON2_X + RESTART_BUTTON2_WIDTH && Gdx.input.getX() > RESTART_BUTTON2_X && game.height - Gdx.input.getY() < RESTART_BUTTON2_Y + RESTART_BUTTON2_HEIGHT && game.height - Gdx.input.getY() > RESTART_BUTTON2_Y) {
                        if (input.isTouched()) {
                            game.setButtonSelected(true);
                            game.logger.debug("Button", "restart game screen");
                            game.setScreen(new GameScreen(game));
                            dispose();
                        }
                    } else if (Gdx.input.getX() < EXIT_BUTTON2_X + EXIT_BUTTON2_WIDTH && Gdx.input.getX() > EXIT_BUTTON2_X && game.height - Gdx.input.getY() < EXIT_BUTTON2_Y + EXIT_BUTTON2_HEIGHT && game.height - Gdx.input.getY() > EXIT_BUTTON2_Y) {
                        if (input.isTouched()) {
                            game.setButtonSelected(true);
                            game.logger.debug("Button", "go to main menu");
                            save(game);
                            game.setScreen(new MainMenuScreen(game));
                            dispose();
                        }
                    }
                }

                break;
        }


        game.batch.begin();

        background.draw(game.batch);

        board.draw(game.batch);

        speedDisplay.draw(game.batch);

        if (boxQueue.size() >= 1) {
            boxQueue.get(0).setPosition(game.width * 0.8f, game.height * 0.95f);
            boxQueue.get(0).setSize(level.getSquareSize(), level.getSquareSize());
            boxQueue.get(0).draw(game.batch);
        }

        if (boxQueue.size() >= 2) {
            boxQueue.get(1).setPosition(game.width * 0.9f, game.height * 0.95f);
            boxQueue.get(1).setSize(level.getSquareSize(), level.getSquareSize());
            boxQueue.get(1).draw(game.batch);
        }


        if (timeElapsed - matchedMoment < 5900) {

            matchedWordsFont.setColor(1,1,1, 0.7f);

            matchedWordsFont.draw(game.batch, glyphLayout, (int) (game.width / 2) - glyphLayout.width / 2, (int) ((level.ground() + level.top()) * 0.6 - glyphLayout.height / 2));
        }



        speedFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        speedFont.getData().setScale(3);
        speedFont.draw(game.batch, speedFontData, (int) (SPEED_DISPLAY_X + SPEED_DISPLAY_WIDTH * 0.4), (int) (SPEED_DISPLAY_Y + SPEED_DISPLAY_HEIGHT * 0.7));

        scoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        scoreFont.getData().setScale(3);
        scoreFont.draw(game.batch, MessageFormat.format("Score: {0}", score), (int) (game.width * 0.05), (int) (game.height * 0.98));

        timeFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        timeFont.getData().setScale(3);
        if (state == State.PAUSE || state == State.GAME_OVER) {
            timeFont.draw(game.batch, String.format("%02d:%02d", (int) (startPauseTime / 60000), (int) (startPauseTime / 1000) % 60), (int) (game.width * 0.35), (int) (game.height * 0.98));
        } else {
            timeFont.draw(game.batch, String.format("%02d:%02d", secondsElapsed / 60, secondsElapsed % 60), (int) (game.width * 0.35), (int) (game.height * 0.98));
        }


        for (int i = 0; i < level.getNumberOfColumns(); i++) {
            for (int j = 0; j < level.getNumberOfRows(); j++) {
                if (level.getGrid()[i][j].getChar() != '.')
                    level.getGrid()[i][j].draw(game.batch);
            }
        }
        if (state == State.DISAPPEAR && Matching.getMatchedBoxes().isEmpty() == false) {
            for (Sprite highlightBox : Matching.getMatchedBoxes()) {
                highlightBox.draw(game.batch);
            }
        }

        if (state != State.SINK && state != State.DISAPPEAR && state != State.PULL && boxCount != 0 && pulled == false) {
            currentBox.draw(game.batch);
        }
        if (state == State.SINK) {
            for (Box box : sinkingBoxes) {
                box.draw(game.batch);
            }
        }

        if (state == State.PULL) {
            for (Box box : unstableBoxes.keySet()) {
                box.draw(game.batch);
            }
        }

        if (isExploding) {
            explosion.draw(System.currentTimeMillis() - startExplodingTime, game.batch, explosion_X, explosion_Y);
            if (System.currentTimeMillis() - startExplodingTime > explosion.getAnimationDuration()) {
                isExploding = false;
            }
        }


        if (state == State.PAUSE) {
            pauseMenu.draw(game.batch);
            resumeButton.draw(game.batch);
            restartButton.draw(game.batch);
            exitButton.draw(game.batch);
        }


        if (state == State.GAME_OVER) {

            gameOverMenu.draw(game.batch);
            restartButton2.draw(game.batch);
            exitButton2.draw(game.batch);


            gameOverScoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            gameOverScoreFont.getData().setScale(4);
            gameOverScoreFont.draw(game.batch, MessageFormat.format("Total Score: {0}", score), (int) (game.width * 0.3), (int) (game.height * 0.60));

            eliminatedBoxCountFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            eliminatedBoxCountFont.getData().setScale(3);
            eliminatedBoxCountFont.draw(game.batch, MessageFormat.format("Box destroyed: {0}", eliminatedBoxCount), (int) (game.width * 0.3), (int) (game.height * 0.55));


            boxCountFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            boxCountFont.getData().setScale(3);
            boxCountFont.draw(game.batch, MessageFormat.format("Box created: {0}", boxCount), (int) (game.width * 0.3), (int) (game.height * 0.5));

            highestScoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            highestScoreFont.getData().setScale(3);
            highestScoreFont.draw(game.batch, MessageFormat.format("Highest Score: {0}", game.getHighScore()), (int) (game.width * 0.3), (int) (game.height * 0.45));
        }

        game.batch.end();


    }


    @Override
    public void resize ( int width, int height){

    }

    @Override
    public void pause () {
        recentState = state;
        state = State.PAUSE;
    }

    @Override
    public void resume () {

    }

    @Override
    public void hide () {

    }

    @Override
    public void dispose () {

        scoreFont.dispose();
        timeFont.dispose();
        boxCountFont.dispose();
        eliminatedBoxCountFont.dispose();
        highestScoreFont.dispose();
        gameOverScoreFont.dispose();
        matchedWordsFont.dispose();


        for (char c = 'A'; c < 'Z'; c++) {
            boxSkin.get(c).dispose();
        }
        defaultBoxSkin.dispose();
        highlightBoxSkin.dispose();
        bombSkin.dispose();
        backgroundSkin.dispose();
        boardSkin.dispose();
        pauseMenuSkin.dispose();
        timeFont.dispose();
        scoreFont.dispose();
        pauseMenuSkin.dispose();
        gameOverMenuSkin.dispose();
        resumeButtonSkin.dispose();
        exitButtonSkin.dispose();
        restartButtonSkin.dispose();

        explosionImage.dispose();
        dropSound.dispose();
        bombSound.dispose();

        if (generator != null) {
            generator.dispose();
        }

    }
}
