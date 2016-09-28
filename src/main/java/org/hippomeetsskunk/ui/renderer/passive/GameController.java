package org.hippomeetsskunk.ui.renderer.passive;

import java.awt.*;

/**
 * Created by SRZCHX on 28.09.2016.
 */
public interface GameController {
    void drawGameData(Graphics2D drawingBoard, int width, int height);

    int getFPS();
    int getUPS();

    void updateGameDataAtSlowRate();
    void updateGameDataAtFastRate();

    void setGameDrawingComponent(Component gameDataPanel);

    void start();
}
