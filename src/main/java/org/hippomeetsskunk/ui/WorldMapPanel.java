package org.hippomeetsskunk.ui;

import org.hippomeetsskunk.world.map.Terrain;
import org.hippomeetsskunk.world.map.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/15/2015.
 */
public class WorldMapPanel extends JPanel {

    public static enum WorldMapPanelType{
        Realistic, Continent, Region
    };

    private final java.util.List<Color> colors =  new ArrayList<>();

    private final WorldMap map;
    Map<String, Color> selectedColors = new HashMap<>();
    private final WorldMapPanelType panelType;

    public WorldMapPanel(WorldMap map, WorldMapPanelType panelType) {
        this.map = map;
        this.panelType = panelType;
        setBorder(BorderFactory.createLineBorder(Color.black));

        for(float h=0; h<1.0; h+=0.05){
            Color c1 = Color.getHSBColor(h, 0.8f, 0.7f);
            colors.add(c1);

            Color c2 = Color.getHSBColor((h + 0.4f) % 1.0f, 0.4f, 0.95f);
            colors.add(c2);

            Color c3 = Color.getHSBColor((h - 0.4f) % 1.0f, 1.0f, 0.4f);
            colors.add(c3);
        }

        selectedColors.put("SEA", Color.BLUE);
    }

    public Dimension getPreferredSize() {
        return new Dimension(map.getMaxX() + 20,map.getMaxY() + 20);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int x = 0; x< map.getMaxX(); ++x){
            for(int y=0; y<map.getMaxY(); ++y){
                Terrain t = map.get(x, y);
                switch(panelType){
                    case Realistic:
                        g.setColor(getColorByTerrainType(t));
                        break;
                    case Continent:
                        g.setColor(getColorByContinent(t, selectedColors));
                        break;
                    case Region:
                        g.setColor(getColorByRegion(t, selectedColors));
                        break;
                }
                g.drawRect(x+10, y+10, 1, 1);
            }
        }
    }

    private Color getColorByTerrainType(Terrain t) {
        switch(t.getTerrainType()){
            case SEA: return Color.BLUE;
            case PLAIN: return Color.GREEN;
            case HILL: return Color.BLACK;
            case MOUNTAIN: return Color.WHITE;
            case RIVER_LAKE: return Color.CYAN;
        }
        throw new IllegalArgumentException("Unknown terrain type: " + t.getTerrainType());
    }

    private Color getColorByContinent(Terrain t, Map<String, Color> selectedColors) {
        if(t.getContinent() == null) return Color.BLUE;

        String key = t.getContinent().getFactId();
        if(selectedColors.containsKey(key)) return selectedColors.get(key);

        Color color = colors.get(selectedColors.size() % colors.size());
        selectedColors.put(key, color);
        return color;
    }

    private Color getColorByRegion(Terrain t, Map<String, Color> selectedColors) {
        if(t.getRegion() == null) return Color.BLUE;

        String key = t.getRegion().getFactId();
        if(selectedColors.containsKey(key)) return selectedColors.get(key);

        Color color = colors.get(selectedColors.size() % colors.size());
        selectedColors.put(key, color);
        return color;
    }

}
