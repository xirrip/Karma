package org.hippomeetsskunk.ui.renderer.passive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by SRZCHX on 28.09.2016.
 * http://jamesgames.org/resources/double_buffer/double_buffering_and_passive_rendering.html
 * also check part about improvement:
 * - caching images
 * - do not support stretch => draw direct to graphics object passed in -> less work & uses better graphics acceleration
 */
public class PassiveRenderingPanel extends JPanel implements ActionListener{
    // Set true to limit fps (sleep the thread), false to not
    private boolean limitingFPS;
    // Button to randomize circle colors
    private JButton changeColor;
    // Button to switch the value of limiting FPS
    private JButton limitFps;
    // We draw almost all graphics to this image, then stretch it over the
    // entire frame.
    // This allows a resize to make the game bigger, as opposed to
    // just providing a larger area for the sprites to be drawn onto.
    // We also are using this image's pixel coordinates as the coordinates
    // of our circle sprites.
    private BufferedImage drawing;
    // GameData to draw
    private GameController gameData;


    /**
     * Creates a new GameDataJPanel. When what's drawn is larger or smaller
     * than the supplied width and height, the drawn image will stretch to
     * meet the current container's size.
     *
     * @param gameData      Data of the game to draw
     * @param drawingWidth  Pixel width of what's drawn in the panel
     *                      before stretch
     * @param drawingHeight Pixel height of what's drawn in the panel
     */
    public PassiveRenderingPanel(GameController gameData, int drawingWidth,
                          int drawingHeight) {
        this.gameData = gameData;
        // Setting up the swing components;
        JPanel programTitlePanel = new JPanel(new FlowLayout());
        JLabel title = new JLabel("Passively rendering graphics!");
        programTitlePanel.add(title);
        changeColor = new JButton("Change color");
        changeColor.addActionListener(this);
        JPanel changeColorPanel = new JPanel(new FlowLayout());
        changeColorPanel.add(changeColor);
        limitFps = new JButton("Unlimit FPS");
        limitFps.addActionListener(this);
        JPanel fpsAndUpdatePanel = new JPanel(new FlowLayout());
        fpsAndUpdatePanel.add(limitFps);

        JPanel holder = new JPanel(new GridLayout(2, 1));
        holder.add(programTitlePanel);
        holder.add(changeColorPanel);

        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, holder);
        this.add(BorderLayout.SOUTH, fpsAndUpdatePanel);

        // Now set the JPanel's opaque, along with other Swing components
        // whose backgrounds we don't want shown, so we can see the
        // application's graphics underneath those components!
        // (Try commenting some out to see what would otherwise happen!)
        changeColorPanel.setOpaque(false);
        this.setOpaque(false);
        title.setOpaque(false);
        programTitlePanel.setOpaque(false);
        fpsAndUpdatePanel.setOpaque(false);
        holder.setOpaque(false);

        // Create an image to draw to, instead of the graphics
        // object found within paintComponent, because we can then stretch
        // the image over the JFrame when the user resizes. This allows the
        // application's graphics to grow or shrink with the size of the
        // containing window, versus having more available space to draw
        // more graphics to
        drawing = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration()
                .createCompatibleImage(drawingWidth, drawingHeight);

        limitingFPS = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Obtaining the graphics of our drawing image we use,
        // most of the graphics drawn are drawn to this object
        Graphics2D drawingBoard = drawing.createGraphics();
        gameData.drawGameData(drawingBoard, drawing.getWidth(), drawing.getHeight());
        drawingBoard.dispose();

        // Creating a graphics object to not clobber parameter g
        // where our state changes to g may affect other not yet rendered
        // Swing components
        Graphics tempGraphics = g.create();

        // Now draw the drawing board over the panel, and stretch the
        // image if needed.
        // NOTE: This method of stretching graphics is not optimal.
        // This causes a computation of a stretched image each time. A
        // better implementation would be to cache an image of the latest
        // representation of a drawn circle, and re-cache whenever there is
        // a visible change (like color, or it's size, which would be
        // due to a window resize), and draw that cached image at the
        // correct calculated location.
        // Additionally, it also causes the rendering to this image to
        // be done on the CPU. See the improvements section in the
        // tutorial.
        tempGraphics.drawImage(drawing, 0, 0, this.getWidth(),
                this.getHeight(), null);

        // In addition, draw the FPS/UPS post stretch, so we always can read
        // the info even if you shrink the frame really small.
        tempGraphics.setColor(Color.WHITE);
        // Grab the font height to make sure we don't draw the stats outside
        // the panel, or over each other.
        int fontHeight = g.getFontMetrics(g.getFont()).getHeight();
        tempGraphics.drawString("FPS: " + gameData.getFPS(), 0, fontHeight);
        tempGraphics.drawString("UPS: " + gameData.getUPS(), 0,
                fontHeight * 2);
        tempGraphics.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeColor) {
            // gameData.setAllCirclesToRandomColor();
        }
        if (e.getSource() == limitFps) {
            limitingFPS = !limitingFPS;
            if (limitingFPS) {
                limitFps.setText("Unlimit FPS");
                gameData.updateGameDataAtSlowRate();
            } else {
                limitFps.setText("Limit FPS");
                gameData.updateGameDataAtFastRate();
            }
        }
    }


}
