package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.basics.Vector3;
import org.hippomeetsskunk.ui.renderer.passive.ParticleRenderingGameController;
import org.hippomeetsskunk.ui.renderer.passive.PassiveRenderingUiCreator;
import org.hippomeetsskunk.ui.renderer.passive.WorldToDisplayTransformer;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by SRZCHX on 27.09.2016.
 */
public class BurstTest {

    public static void main(String[] args) {
        // Constructor kicks off the GUI
        BurstTest ballisticsTest = new BurstTest(700, 500);
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
    public BurstTest(final int width, final int height) {
        gameData = new ParticleRenderingGameController(width, height,
                new WorldToDisplayTransformer() {
                    private final double WORLD_SCALE = 0.5;
                    @Override
                    public int getX(Vector3 position, int drawingWidth) {
                        return (int) (drawingWidth * 0.5 + WORLD_SCALE * position.getZ());
                    }

                    @Override
                    public int getY(Vector3 position, int drawingHeight) {
                        return (int) (drawingHeight - 50 + WORLD_SCALE * position.getY());
                    }
                });

        Particle fireWork = new Particle(1.0);
        fireWork.setPosition(0.0, 0.0, 0.0);
        fireWork.setVelocity(0.0, -100.0, 0.0);
        fireWork.setAcceleration(0.0, 10.0, 0.0);
        fireWork.setDamping(0.9);
        fireWork.setLifeTime(5.0);
        fireWork.setRule(new BurstRule(3, 15, 0.0, 10.0));

        gameData.addParticle(fireWork);

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
