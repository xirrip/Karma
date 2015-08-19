package org.hippomeetsskunk.ui;

import dagger.Component;
import org.hippomeetsskunk.knowledge.SimpleKnowledgeBaseModule;
import org.hippomeetsskunk.ui.action.UserQueryAction;
import org.hippomeetsskunk.world.World;

import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        World world = DaggerApp_WorldGateway.create().world();
        world.run();

        // now actually draw the terrain
        App theApp = new App(world);

    }

    public App(World world){
        this.world = world;
        setSize(world.getMap().getMaxX()+100,world.getMap().getMaxY()+200);
        setPreferredSize(new Dimension(world.getMap().getMaxX()+100,world.getMap().getMaxY()+200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel scrollPanel = new JPanel();

        scrollPanel.setLayout(new BorderLayout());
        scrollPanel.add(new WorldMapPanel(world.getMap()), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextField input = new JTextField();
        bottomPanel.add(input, BorderLayout.NORTH);
        JTextArea output = new JTextArea("Hello", 10, 30);
        output.setEditable(false);
        bottomPanel.add(output, BorderLayout.CENTER);

        input.addActionListener(new UserQueryAction(world.getKnowledgeBase(), output));

        scrollPanel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        add(scrollPane);

        pack();

        setVisible(true);
    }

}
