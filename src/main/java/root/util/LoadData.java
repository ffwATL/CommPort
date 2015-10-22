package root.util;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import root.model.Person;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class LoadData {

    private static Logger logger = LogManager.getLogger();


    public static ObservableList<Person> getList(){
        ObservableList<Person> list =  FXCollections.observableArrayList();
        try {
            File xmlFile = new File("src/main/resources/listUsers.xml");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            NodeList nodeList = doc.getElementsByTagName("user");
            for(int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    list.add(new Person(element.getElementsByTagName("firstName").item(0).getTextContent(),
                            element.getElementsByTagName("lastName").item(0).getTextContent()));
                }
            }
        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException is occurred: " + e.getMessage());
        } catch (SAXException e) {
            logger.error("SAXException is occurred: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException is occurred: " + e.getMessage());
        }
        return list;
    }
}
