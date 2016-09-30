package org.hippomeetsskunk.physics.particles;

import org.hippomeetsskunk.physics.PhysicsWorld;
import org.hippomeetsskunk.ui.renderer.passive.CenteredOriginWorldTransformerXY;
import org.hippomeetsskunk.ui.renderer.passive.CenteredOriginWorldTransformerZY;
import org.hippomeetsskunk.ui.renderer.passive.ParticleRenderingGameController;
import org.hippomeetsskunk.ui.renderer.passive.PassiveRenderingUiCreator;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by SRZCHX on 30.09.2016.
 */
public class GravityTest {
    public static void main(String[] args) {
        // Constructor kicks off the GUI
        GravityTest gravityTest = new GravityTest(700, 700);
        gravityTest.start();
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
    public GravityTest(final int width, final int height) {
        gameData = new ParticleRenderingGameController(width, height, new CenteredOriginWorldTransformerXY(1.5 * PhysicsWorld.dEarthSun, 1.5 * PhysicsWorld.dEarthSun));

        Particle p1 = new Particle(1.989e30);

        Particle p2 = new Particle(PhysicsWorld.mEarth);
        p2.setPosition(PhysicsWorld.dEarthSun, 0.0, 0.0);
        p2.setVelocity(0.0, 29.8e3, 0.0);

        gameData.addParticle(p1);
        gameData.addParticle(p2);

        Gravity gravity = new Gravity(p1, p2);
        gameData.addForce(gravity);

        // 1 year = 30 seconds?
        gameData.setTimeScale(PhysicsWorld.secondsPerYear / 30);

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
