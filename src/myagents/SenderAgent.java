package myagents;
import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import moodle.Cours;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by hamza on 26/12/16.
 */
public class SenderAgent extends Agent {
    protected void setup() {

        System.out.println("SenderAgent: My name is " + getLocalName());
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(this.getAID("MoodleAgent"));
        System.out.println("...... sending request for courses list");
        send(msg);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                try {
                    MessageTemplate messageTemplate = MessageTemplate.or(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.or(
                                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST)));
                    System.out.println("aquisi");
                    ACLMessage aclMessage = receive(messageTemplate);
                    if (aclMessage != null) {
                        switch (aclMessage.getPerformative()) {
                            case ACLMessage.INFORM:
                                String content = aclMessage.getContent();
                                System.out.println("Received Courses : " + content);

                                break;

                            case ACLMessage.CONFIRM:

                                break;
                        }
                    } else{
                       System.out.println("holo");
                        block(); 
                    }
                        
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

}
