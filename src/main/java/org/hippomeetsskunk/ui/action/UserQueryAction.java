package org.hippomeetsskunk.ui.action;

import org.hippomeetsskunk.knowledge.Fact;
import org.hippomeetsskunk.knowledge.KnowledgeBase;
import org.hippomeetsskunk.ui.renderer.FactToTextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by skunk@hippomeetsskunk.ch on 8/19/2015.
 */
public class UserQueryAction extends AbstractAction {
    private final static Logger logger = LoggerFactory.getLogger(UserQueryAction.class);

    private final KnowledgeBase knowledgeBase;
    private final JTextArea reply;
    private final FactToTextRenderer renderer = new FactToTextRenderer();

    public UserQueryAction(KnowledgeBase knowledgeBase, JTextArea reply) {
        this.knowledgeBase = knowledgeBase;
        this.reply = reply;
    }

    public void actionPerformed(ActionEvent e) {
        JTextField source = (JTextField) e.getSource();
        String query = source.getText();

        logger.debug("User query action performed: " + query);

        // now the parser, hmmm.
        String[] split = query.split("\\s+");
        if(split.length >= 2){
            if("tell".equals(split[0].toLowerCase())) {
                // object last...
                String objString = split[split.length - 1];
                // by id?
                try{
                    Fact fact = knowledgeBase.getFactCaseInsensitive(objString);
                    String text = renderer.render(fact);
                    reply.setText(text);
                    source.setText("");
                }
                catch(IllegalArgumentException exc){
                    logger.debug("Fact not found: " + objString);
                }
            }
        }
    }
}
