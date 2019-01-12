package myagents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import moodle.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class MoodleAgent extends Agent {
    private int cmp;

    protected void setup() {
        System.out.println("MoodleAgant: My name is " + getLocalName());


        addBehaviour(new CyclicBehaviour() {
            private AID requester;

            @Override
            public void action() {
                try {
                    MessageTemplate messageTemplate = MessageTemplate.or(
                            MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                            MessageTemplate.or(
                                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM),
                                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST)));

                    ACLMessage aclMessage = receive(messageTemplate);
                    if (aclMessage != null) {
                        switch (aclMessage.getPerformative()) {
                            case ACLMessage.REQUEST:
                                System.out.println("------------------------");
                                System.out.println("Request");
                                ACLMessage reply = aclMessage.createReply();
                                reply.setPerformative(ACLMessage.INFORM);
                                // Reply With Courses ArrayList
                                reply.setContent(parseXml(getCourses()).toString());
                                System.out.println("...... En cours");
                                send(reply);
                                break;

                            case ACLMessage.INFORM:
                                String informations = aclMessage.getContent();
                                System.out.println("INFORM : " + informations);
                                break;

                            case ACLMessage.CONFIRM:

                                break;
                        }
                    } else
                        block();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    protected String getCourses() throws ProtocolException, IOException {

        /// NEED TO BE CHANGED
        String token = "52a731e5add6f03878a2dfe84512c072";
        String domainName = "http://localhost";

        /// REST RETURNED VALUES FORMAT
        String restformat = "json"; //Also possible in Moodle 2.2 and later: 'json'
        //Setting it to 'json' will fail all calls on earlier Moodle version
        if (restformat.equals("json")) {
            restformat = "&moodlewsrestformat=" + restformat;
        } else {
            restformat = "";
        }

        /// PARAMETERS - NEED TO BE CHANGED IF YOU CALL A DIFFERENT FUNCTION
        String functionName = "core_course_get_courses";
        String urlParameters = "users[0][username]=" + URLEncoder.encode("testusername1", "UTF-8");
        urlParameters = "";
        /// REST CALL

        // Send request
        String serverurl = domainName + "/webservice/rest/server.php" + "?wstoken=" + token + "&wsfunction=" + functionName + restformat;
        HttpURLConnection con = (HttpURLConnection) new URL(serverurl).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Language", "en-US");
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(
                con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        //Get Response
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }
        rd.close();
        return response.toString();
    }


    protected ArrayList<Cours> parseXml(String response) {

        ArrayList<Cours> cours = new ArrayList<>();
        Document document;
        Element racine;

        //On crée une instance de SAXBuilder
        SAXBuilder sxb = new SAXBuilder();
        try {
            //On crée un nouveau document JDOM avec en argument le fichier XML
            //Le parsing est terminé ;)
            document = sxb.build(new StringReader(response));
            //On initialise un nouvel élément racine avec l'élément racine du document.
            racine = document.getRootElement();
            System.out.println(document);
            List listCoures = racine.getChild("MULTIPLE").getChildren("SINGLE");
            System.out.println(listCoures);
            Iterator i = listCoures.iterator();
            while (i.hasNext()) {
                Element element = (Element) i.next();
                Cours cr = new Cours();
                cr.setName(getKeyValue(element, "fullname"));
                cr.setId(Integer.valueOf(getKeyValue(element, "id")));
                cours.add(cr);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return cours;
    }
    protected String getKeyValue(Element element, String attr){
        try {
            XPathFactory xFactory = XPathFactory.instance();
            XPathExpression<Element> expr = xFactory.compile(String.format("KEY[@name='%s']", attr), Filters.element());
            Element key = expr.evaluateFirst(element);
            return key.getChildText("VALUE");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
