package org.hippomeetsskunk.ui;

import dagger.Component;
import org.hippomeetsskunk.knowledge.Fact;
import org.hippomeetsskunk.knowledge.SimpleKnowledgeBaseModule;
import org.hippomeetsskunk.ui.action.UserQueryAction;
import org.hippomeetsskunk.world.World;
import org.hippomeetsskunk.world.map.generation.WorldGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Hello world!
 * Using Dagger for dependency injection
 */
public class KarmaApp extends JFrame
{
    private static final Logger logger = LoggerFactory.getLogger(KarmaApp.class);

    private final World world;

    JTextArea output;
    private Fact selectedFact;
    private WorldMapPanel realisticWorldMap;
    private WorldMapPanel continentWorldMap;
    private WorldMapPanel regionWorldMap;
    // additionally a height map

    @Singleton
    @Component(modules = {SimpleKnowledgeBaseModule.class })
    public interface WorldGateway {
        World world();
    }
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        World world = DaggerKarmaApp_WorldGateway.create().world();

        WorldGenerator worldGenerator = new WorldGenerator();
        worldGenerator.generate(world);

        // now actually draw the terrain
        KarmaApp theKarmaApp = new KarmaApp(world);
        theKarmaApp.createUi();

    }

    public KarmaApp(World world) {
        this.world = world;
    }

    protected void createUi(){
        setSize(world.getMap().getMaxX()+100,world.getMap().getMaxY()+200);
        setPreferredSize(new Dimension(world.getMap().getMaxX()+100,world.getMap().getMaxY()+200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel scrollPanel = new JPanel();

        scrollPanel.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        realisticWorldMap = new WorldMapPanel(world.getMap(), WorldMapPanel.WorldMapPanelType.Realistic);
        tabbedPane.addTab("Realistic", realisticWorldMap);
        continentWorldMap = new WorldMapPanel(world.getMap(), WorldMapPanel.WorldMapPanelType.Continent);
        tabbedPane.addTab("Continent", continentWorldMap);
        regionWorldMap = new WorldMapPanel(world.getMap(), WorldMapPanel.WorldMapPanelType.Region);
        tabbedPane.addTab("Region", regionWorldMap);

        scrollPanel.add(tabbedPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextField input = new JTextField();
        bottomPanel.add(input, BorderLayout.NORTH);
        output = new JTextArea("Hello", 10, 30);
        output.setEditable(false);
        bottomPanel.add(output, BorderLayout.CENTER);

        input.addActionListener(new UserQueryAction(world.getKnowledgeBase(), this));

        scrollPanel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        add(scrollPane);

        pack();

        setVisible(true);
    }

    public void setOutput(String text) {
        output.setText(text);
    }

    public void select(Fact fact){
        logger.debug("selecting " + fact.getFactId());
        this.selectedFact = fact;

        realisticWorldMap.setSelected(fact);
        continentWorldMap.setSelected(fact);
        regionWorldMap.setSelected(fact);

        repaint();
    }


}
