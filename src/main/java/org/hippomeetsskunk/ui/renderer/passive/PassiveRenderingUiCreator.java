package org.hippomeetsskunk.ui.renderer.passive;

import javax.swing.*;
import java.awt.*;

/**
 * http://jamesgames.org/resources/double_buffer/double_buffering_and_passive_rendering.html
 * call from main:
 *
     this.gameData = new GameData(width, height, numberOfCircles);
     // This runnable will construct the GUI on the EDT, but also update our
     // GameData object with a reference to the component that our update
     // task will request repaints on.
     Runnable guiCreator =
     new GuiCreatorRunnable(width, height, gameData);
     try {
     SwingUtilities.invokeAndWait(guiCreator);
     } catch (InterruptedException | InvocationTargetException e) {
     e.printStackTrace();
     }
 */
public class PassiveRenderingUiCreator implements Runnable {
    private int width;
    private int height;

    private GameController gameData;

    /**
     * Constructs a GuiCreatorRunnableFuture, with the requested width
     * and height, and the GameData object to update with a reference to
     * the component that will paint the GameData object
     */
    public PassiveRenderingUiCreator(int width, int height, GameController gameData) {
        this.width = width;
        this.height = height;
        this.gameData = gameData;
    }

    @Override
    public void run() {
        JFrame frame = new JFrame();
        frame.setTitle("Passive rendering and double buffering " +
                "circles using a Timer");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Change width and height of window so that the available
        // screen space actually corresponds to what is passed, another
        // method is the Canvas object + pack()
        frame.setSize(width, height);
        Insets insets = frame.getInsets();
        int insetWide = insets.left + insets.right;
        int insetTall = insets.top + insets.bottom;
        frame.setSize(frame.getWidth() + insetWide,
                frame.getHeight() + insetTall);

        PassiveRenderingPanel gameDataPanel = new PassiveRenderingPanel(gameData,
                width, height);
        frame.add(gameDataPanel);

        // Update GameData on what component will paint it's data
        gameData.setGameDrawingComponent(gameDataPanel);
    }
}
