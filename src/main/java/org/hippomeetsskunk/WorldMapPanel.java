package org.hippomeetsskunk;

import org.hippomeetsskunk.world.World;
import org.hippomeetsskunk.world.map.Terrain;
import org.hippomeetsskunk.world.map.WorldMap;

import javax.swing.*;
import java.awt.*;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public class WorldMapPanel extends JPanel {
    private final WorldMap map;

    public WorldMapPanel(WorldMap map) {
        this.map = map;
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public Dimension getPreferredSize() {
        return new Dimension(map.getMaxX() + 20,map.getMaxY() + 20);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int x = 0; x< map.getMaxX(); ++x){
            for(int y=0; y<map.getMaxY(); ++y){
                Terrain t = map.get(x, y);
                g.setColor(getColor(t));
                g.drawRect(x+10, y+10, 1, 1);
            }
        }
    }

    private Color getColor(Terrain t) {
        switch(t.getTerrainType()){
            case SEA: return Color.BLUE;
            case PLAIN: return Color.GREEN;
            case HILL: return Color.BLACK;
            case MOUNTAIN: return Color.WHITE;
            case RIVER_LAKE: return Color.CYAN;
        }
        throw new IllegalArgumentException("Unknown terrain type: " + t.getTerrainType());
    }



}
