package org.hippomeetsskunk.ui.renderer.passive;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by SRZCHX on 28.09.2016.
 */
public class PassiveCircleTest {
    public static void main(String[] args) {
        // Constructor kicks off the GUI
        PassiveCircleTest passiveCirclesExample = new PassiveCircleTest(50, 700, 500);
        passiveCirclesExample.start();
    }

    private GameController gameData;

    /**
     * Constructor for PassiveCircles
     *
     * @param numberOfCircles The number of circles you want the program to
     *                        display
     * @param width           The width of the program's inside portion of
     *                        the frame
     * @param height          The height of the program's inside portion of
     *                        the frame
     */
    public PassiveCircleTest(int numberOfCircles, final int width,
                          final int height) {
        this.gameData = new PassiveCircleGameController(width, height, numberOfCircles);
        // This runnable will construct the GUI on the EDT, but also update our
        // GameData object with a reference to the component that our update
        // task will request repaints on.
        Runnable guiCreator =
                new PassiveRenderingUiCreator(width, height, gameData);
        try {
            SwingUtilities.invokeAndWait(guiCreator);
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the updating of the game's state such as animation and movement.
     */
    public void start() {
        gameData.start();
    }

}
