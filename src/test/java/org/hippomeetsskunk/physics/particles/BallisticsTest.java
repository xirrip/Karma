package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.basics.Vector3;
import org.hippomeetsskunk.ui.renderer.passive.*;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class BallisticsTest {

    public static void main(String[] args) {
        // Constructor kicks off the GUI
        BallisticsTest ballisticsTest = new BallisticsTest(50, 700, 500);
        ballisticsTest.start();
    }

    private ParticleRenderingGameController gameData;

    /**
     * Constructor for PassiveCircles
     *
     * @param width           The width of the program's inside portion of
     *                        the frame
     * @param height          The height of the program's inside portion of
     *                        the frame
     */
    public BallisticsTest(int numberOfCircles, final int width,
                             final int height) {
        this.gameData = new ParticleRenderingGameController(width, height,
                new WorldToDisplayTransformer() {
                    private final double WORLD_SCALE = 0.5;
                    @Override
                    public int getX(Vector3 position, int drawingWidth) {
                        return (int) (20 + WORLD_SCALE * position.getZ());
                    }

                    @Override
                    public int getY(Vector3 position, int drawingHeight) {
                        return (int) (drawingHeight * 0.5 + WORLD_SCALE * position.getY());
                    }
                });

        for(BallisticParticle.ShotType type : BallisticParticle.ShotType.values()){
            Particle particle = BallisticParticle.createParticle(type);
            gameData.addParticle(particle);
        }

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
