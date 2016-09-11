package com.example.pazu.indignation;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {


    /** Width of player box in pixels **/
    private final int PLAYER_WIDTH = 100;
    /** Height of player box in pixels **/
    private final int PLAYER_HEIGHT = 70;



    /** Offset from left side of the screen of the player box **/
    private final int PLAYER_OFFSET = 200;
    private int score = 0;
    private int highscore = 0;

    /** This is the start x location, pipes start spawning at location 0 **/
    private final int PLAYER_START_POSITION = -1000;

    /** Player's y position, pixels from the top of the screen **/
    private float playerY = 0;

    private boolean canJump = true;

    /**
     * How far the player has traveled. Starts negative so that we don't have a
     * pipe right away
     **/
    private int distanceTraveled = PLAYER_START_POSITION;

    /** the change, in pixels, of the player Y position per second **/
    private float playerYVel = 0;

    /** the paintbrush we use **/
    private Paint paint;


    /**
     * Last time the frame was drawn, in milliseconds since Thursday, 1 January
     * 1970 (UTC)
     **/
    private long lastFrame = -1;

    /** Width of the screen in pixels **/
    private int screenWidth;
    /** Height of the screen in pixels **/
    private int screenHeight;

    private int building1X;
    private int building1Y;
    private final int Building1_HEIGHT = 250;
    private final int Building1_WIDTH = 800;

    private float building1right;
    private float building1bottom;
    private float building1left;
    private float building1top;

    /** This is the bounding box of the player **/
    private RectF playerRect;

    /** This is the bounding box of the background **/
    private RectF building1;

    /** List of all pipe heights, generated as we go **/
    private ArrayList<Integer> pipeHeights = new ArrayList<Integer>();

    /** Random number generator **/
    private Random random;

    /** If true, don't move the player **/
    private boolean paused = false;

    public GameView(Context context) {
        super(context);
        paint = new Paint();

        // We seed the random number generator with current time so that the
        // pipes always start at different positions.
        random = new Random();
        canJump = true;
    }

    /**
     * This code is called once when the screen is first shown, and whenever the
     * screen is resized (change orientation)
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenHeight = h;
        screenWidth = w;
        building1X = screenWidth;
        building1Y = screenHeight;
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
         * This is our main game loop, it performs both drawing and updating.
         *
         * Usually, it's advised that you multi-thread this code, but it's far
         * simpler this way.
         */

        // Draw everything on the screen
        drawBackground(canvas);
        drawPlayer(canvas);
        drawScore(canvas);
        drawBuilding1(canvas);

        // If the game is not paused, update the game state

        // This call triggers the screen to redraw (basically call onDraw
        // again).
        invalidate();
    }



    /**
     * Draw the player
     */
    private void drawBuilding1(Canvas canvas) {
         building1right = building1X;
         building1bottom = building1Y;
         building1left = building1X - Building1_WIDTH;
         building1top = building1Y - Building1_HEIGHT;

        building1 = new RectF(building1left, building1top, building1right, building1bottom);
        canvas.drawRect(building1, paint);
        building1X -= 20;

        if (building1right < 0) {
            //if (random.nextFloat() > System.currentTimeMillis()) {
                building1X = screenWidth + Building1_WIDTH + random.nextInt((750 - 0) + 1) + 0;
            //}
        }
    }
    private void drawPlayer(Canvas canvas) {
        float top = playerY;
        float bottom = top + PLAYER_HEIGHT;
        float left = PLAYER_OFFSET;
        float right = left + PLAYER_WIDTH;
        building1top = building1Y - Building1_HEIGHT;


        playerRect = new RectF(left, top, right, bottom);
        canvas.drawRect(playerRect, paint);
        /*if (building1left <= left && building1right >= right && building1top >= bottom && building1top < top) {
            playerYVel -= 0.2;
        } else {
            playerY = building1Y - 5 - PLAYER_HEIGHT;
            playerYVel *= -.7;
            canJump = true;

        }*/
        if (bottom <= building1top + 10 && bottom >= building1top - 10 && playerYVel < 0 && left >= building1left && right <= building1right) {
            playerY = building1top - PLAYER_HEIGHT;
            playerYVel *= -.7;
            canJump = true;
            score += 2;
        } else if (bottom < screenHeight) {
            playerYVel -= 0.2;
            paint.setTextSize(100f);
        } else if (top >= building1top && (right >= building1left || left <= building1right)) {
                playerY = 0;
                if (highscore < score) {
                    highscore = score;
                }
                score = 0;


        } else {
            playerY = screenHeight - PLAYER_HEIGHT;
            playerYVel *= -.7;
            canJump = true;
            score--;
        }


        playerY -= playerYVel;
    }


    private void drawScore(Canvas canvas) {
        paint.setTextSize(70f);
        canvas.drawText("Score: " + String.valueOf(score), 100, 100, paint);
        canvas.drawText("High score: " + String.valueOf(highscore), 500, 100, paint);

    }

    private void drawBackground(Canvas canvas) {
        //backgroundRect = new RectF(0, 0, screenWidth, screenHeight);
        //canvas.drawRect(backgroundRect, paint);
    }


    /**
     * Collision detection, collide with all possible pipes and the top/bottom
     * of the screen.
     **/

    private void gameOver() {
        // TODO: reset game, show score screen
    }

    /*
     * Reset to starting location
     */
    private void reset() {
        lastFrame = -1;
        // TODO: set variables to initial values
    }

    /**
     * This function is called whenever the player touches the screen.
     * ACTION_DOWN means the player just touched the screen.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canJump) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    playerYVel = 15;
                    canJump = false;
                    break;
            }
        }
        return true;
    }

    /**
     * This is the main update code, it updates the player position based on his
     * velocity, and adds gravity as well.
     */
    private void update() {

        /*
         * We keep track of the current and previous frame times to ensure that
         * speed is kept constant regardless of the FPS.
         */
        long currentTime = System.currentTimeMillis();
        if (lastFrame != -1) {
            long diff = currentTime - lastFrame;
            float mult = ((float) diff / 1000f);

            // TODO: move player, add gravity, check to see if we've passed any
            // pipes and update score if we have
        }
        lastFrame = currentTime;
    }
}
