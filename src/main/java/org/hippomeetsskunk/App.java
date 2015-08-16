package org.hippomeetsskunk;

import dagger.Component;
import org.hippomeetsskunk.knowledge.SimpleKnowledgeBaseModule;
import org.hippomeetsskunk.world.World;
import org.hippomeetsskunk.world.map.Terrain;
import org.hippomeetsskunk.world.map.WorldMap;

import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;

/**
 * Hello world!
 * Using Dagger for dependency injection
 */
public class App extends JFrame
{
    private final World world;

    @Singleton
    @Component(modules = {SimpleKnowledgeBaseModule.class })
    public interface WorldGateway {
        World world();
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        World world = DaggerApp_WorldGateway.create().world();
        world.run();

        // now actually draw the terrain
        App theApp = new App(world);

    }

    public App(World world){
        this.world = world;
        setSize(world.getMap().getMaxX()+10,world.getMap().getMaxY()+10);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new WorldMapPanel(world.getMap()));
        pack();

        setVisible(true);
    }

}
