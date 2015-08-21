package org.hippomeetsskunk.ui.action;

import org.hippomeetsskunk.knowledge.Fact;
import org.hippomeetsskunk.knowledge.FactType;
import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.ui.KarmaApp;
import org.hippomeetsskunk.ui.renderer.FactToTextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/19/2015.
 */
public class UserQueryAction extends AbstractAction {
    private final static Logger logger = LoggerFactory.getLogger(UserQueryAction.class);

    private final KarmaApp app;
    private final KnowledgeBase knowledgeBase;
    private final FactToTextRenderer renderer = new FactToTextRenderer();

    // TODO maybe use a KnowledgeBaseInteraction or something similar to keep context
    private Fact context = null;

    public UserQueryAction(KnowledgeBase knowledgeBase, KarmaApp app) {
        this.knowledgeBase = knowledgeBase;
        this.app = app;
    }

    public void actionPerformed(ActionEvent e) {
        JTextField source = (JTextField) e.getSource();
        String query = source.getText();

        logger.debug("User query action performed: " + query);

        // now the parser, hmmm.
        String[] split = query.split("\\s+");
        if(split.length >=1){
            if("again".equals(split[0].toLowerCase()) && context!=null) {
                String text = renderer.render(context);
                app.setOutput(text);
                source.setText("");
                return;
            }
            else if("show".equals(split[0])){
                app.select(context);
                source.setText("");
                return;
            }
        }

        if(split.length >= 2){
            if("tell".equals(split[0].toLowerCase())) {
                // object last...
                String objString = split[split.length - 1];
                // by id?
                try{
                    Fact fact = knowledgeBase.getFactCaseInsensitive(objString);
                    this.context = fact;
                    String text = renderer.render(fact);
                    app.setOutput(text);
                    source.setText("");
                    return;
                }
                catch(IllegalArgumentException exc){
                    logger.debug("Fact not found: " + objString);
                }
            }
            else if("and".equals(split[0].toLowerCase())){
                // object last...
                String objString = split[split.length - 1];
                FactType type = FactType.valueOf(objString);
                // now this could be a connection type or a fact type
                StringBuffer text = new StringBuffer();

                Collection<Fact> connectedFacts = this.context.getConnectedFactsOfType(type);
                connectedFacts.stream()
                        .forEach(f -> text.append(renderer.render(f)));

                app.setOutput(text.toString());
                source.setText("");
                return;
            }
        }
    }
}
