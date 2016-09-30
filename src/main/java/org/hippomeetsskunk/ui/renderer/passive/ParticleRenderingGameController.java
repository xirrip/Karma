package org.hippomeetsskunk.ui.renderer.passive;

import org.hippomeetsskunk.physics.particles.Particle;
import org.hippomeetsskunk.physics.particles.ParticleForceGenerator;
import org.hippomeetsskunk.physics.particles.ParticleSimulator;

import java.awt.*;
import java.util.*;

/**
 * Created by SRZCHX on 28.09.2016.
 */
public class ParticleRenderingGameController implements GameController {

    private final double WORLD_SCALE = 0.5;

    private static final int slowUpdateSpeed = 20;
    // 1, because timer speeds have to be positive
    private static final int fastUpdateSpeed = 1;

    // Represents the known bounds of the game's area
    private Rectangle boundedGameArea;

    // Array of our sprites
    private java.util.List<Particle> particles;
    private final ParticleSimulator particleSimulator;

    private long oldTime;
    private long nanoseconds;

    private int frames;
    private int updates;

    // Holds the latest calculated value of frames per second
    private int fps;
    // Holds the latest calculated value of updates per
    private int ups;

    // Reference to the component which will draw the game data
    // This is used to call repaint by our update task, which is also not
    // available at construction time
    private Component gameDrawingComponent;

    // The timer that calls our update method
    private java.util.Timer updateTimer;

    // The current task that's updating this GameData's state
    private TimerTask updateTask;

    private final WorldToDisplayTransformer worldToDisplayTransformer;

    /**
     * Constructs a new GameData. GameData's constructor creates and
     * schedules an initial updating task
     *  @param width  The width of the bounded game area
     * @param height The height of the bounded game area
     * @param worldToDisplayTransformer
     */
    public ParticleRenderingGameController(int width, int height, WorldToDisplayTransformer worldToDisplayTransformer) {
        this.worldToDisplayTransformer = worldToDisplayTransformer;

        boundedGameArea = new Rectangle(width, height);
        particles = new ArrayList<>();
        particleSimulator = new ParticleSimulator(particles); // share the list

        // Initialize the time, fps, and other variables
        oldTime = System.nanoTime();
        nanoseconds = 0;
        frames = 0;
        updates = 0;
        fps = 0;
        ups = 0;

        // Create the timer that will handle our update task
        updateTimer = new java.util.Timer();
        // Initializing TimerTask with a default do nothing task to avoid
        // having a null reference
        updateTask = new TimerTask() {
            @Override
            public void run() {
                // do nothing default initialization
            }
        };

    }

    public synchronized void addParticle(Particle particle){
        this.particles.add(particle);
    }

    public synchronized  void addForce(ParticleForceGenerator force){
        particleSimulator.add(force);
    }

    /**
     * Starts updating the game at a slow rate.
     * This should only be called once, when we first want to start
     * updating, as it resets the internal tracking of the last time an
     * update occurred.
     * Method may be improved by throwing a unchecked exception for calling
     * it twice, as it will point out the subtle bug of having an inaccurate
     * tracking of the last time an update occurred.
     */
    public synchronized void start() {
        // For max accuracy, reset oldTime to really reflect how much
        // time will have passed since we wanted to start the updating
        oldTime = System.nanoTime();
        updateGameDataAtSlowRate();
    }

    /**
     * Updates the reference to what component to repaint when GameData's
     * update task runs
     */
    public synchronized void setGameDrawingComponent(Component comp) {
        this.gameDrawingComponent = comp;
    }

    /**
     * Returns the latest calculated updates per second
     */
    public synchronized int getUPS() {
        return ups;
    }

    /**
     * Returns the latest calculated frames per second
     */
    public synchronized int getFPS() {
        return fps;
    }

    private synchronized void scheduleGameUpdate(int updateSpeedInMilliseconds) {
        // Stop the previous update task (safe to call if not scheduled)
        updateTask.cancel();
        // Create the update task which will simply call updateData
        updateTask = new TimerTask() {
            @Override
            public void run() {
                updateData();
            }
        };
        updateTimer.scheduleAtFixedRate(updateTask, 0,
                updateSpeedInMilliseconds);
    }

    /**
     * Start updating the game data at a slow update speed
     */
    public synchronized void updateGameDataAtSlowRate() {
        scheduleGameUpdate(slowUpdateSpeed);
    }

    /**
     * Start updating the game data at a fast update speed
     */
    public synchronized void updateGameDataAtFastRate() {
        scheduleGameUpdate(fastUpdateSpeed);
    }

    /**
     * Draws the GameData as needed
     */
    public synchronized void drawGameData(Graphics2D drawingBoard,
                                          int drawAreaWidth,
                                          int drawAreaHeight) {
        // This allows our text and graphics to be nice and smooth
        drawingBoard.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawingBoard.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Always draw over the image with a blank background, so we
        // don't see the last frame's drawings! (comment this out and
        // see what happens, it's fun pressing the change color button
        // rapidly too!)
        drawingBoard.setColor(Color.LIGHT_GRAY);
        drawingBoard.fillRect(0, 0, drawAreaWidth, drawAreaHeight);


        // Creating a graphics object to not clobber parameter drawingBoard
        // where MovingCircle's drawing method may change some state of
        // the drawingBoard parameter graphics object
        Graphics particleGraphics = drawingBoard.create();
        // Now draw all the circles, location 0,0 will be top left
        // corner within the borders of the window
        for (Particle particle : particles) {
            Color color = getColor(particle.getType());
            particleGraphics.setColor(color);
            particleGraphics.fillOval(
                    worldToDisplayTransformer.getX(particle.getPosition(), drawAreaWidth),
                    worldToDisplayTransformer.getY(particle.getPosition(), drawAreaHeight),
                    10, 10);
        }
        particleGraphics.dispose();

        // We should only increment frames here, because Swing coalesces
        // repaint calls. So if a repaint is scheduled and hasn't
        // completed yet, other repaint requests will be ignored, so
        // this is the only spot in code within GameData where we can know
        // for sure that a frame has been produced.
        // (Assuming this method being called means the graphics will be
        // used for a frame update)
        frames++;
    }

    private Color getColor(int type) {
        switch(type){
            case 0: return Color.BLUE;
            case 1: return Color.BLACK;
            case 2: return Color.RED;
            case 3: return Color.YELLOW;
        }
        return Color.BLACK;
    }

    private synchronized void updateData() {
        // Calculating a new fps/ups value every second
        if (nanoseconds >= 1000000000) {
            fps = frames;
            ups = updates;
            nanoseconds = nanoseconds - 1000000000;
            frames = 0;
            updates = 0;
        }

        long elapsedTime = System.nanoTime() - oldTime;
        oldTime = oldTime + elapsedTime;
        nanoseconds = nanoseconds + elapsedTime;

        particleSimulator.update(elapsedTime * 1e-9);

        // An update occurred, increment.
        updates++;

        // Ask for a repaint if we know of a component to repaint
        if (gameDrawingComponent != null) {
            gameDrawingComponent.repaint();
        }
    }

    public synchronized void setTimeScale(double timeScale){
        particleSimulator.setTimeScale(timeScale);
    }

}

