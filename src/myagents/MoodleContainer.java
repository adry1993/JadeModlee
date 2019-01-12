package myagents;

import jade.core.ProfileImpl;
import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

/**
 * Created by hamza on 26/12/16.
 */
public class MoodleContainer {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
       	AgentController agenteController;
        Runtime runtime=Runtime.instance();
        
        Profile profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.MAIN_HOST, "169.254.135.224");
        AgentContainer mainContainer = runtime.createMainContainer(profile);
           
    
        AgentContainer agentContainer=runtime.createAgentContainer(profile);
        try {
            agenteController=mainContainer.createNewAgent
                    ("MoodleAgent",MoodleAgent.class.getName(), new Object[]{});
            agenteController.start();
        } catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
