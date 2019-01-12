package myagents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;


public class MoodleContainer {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            Runtime runtime=Runtime.instance();
            ProfileImpl profileImpl=new ProfileImpl(true);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST, "10.30.4.172");
            AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
            AgentController agentController=agentContainer.createNewAgent
                    ("MoodleAgent", MoodleAgent.class.getName(), new Object[]{});
            agentController.start();
        } catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
