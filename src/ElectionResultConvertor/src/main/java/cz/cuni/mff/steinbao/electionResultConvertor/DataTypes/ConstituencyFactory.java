package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import cz.cuni.mff.steinbao.electionResultConvertor.Exceptions.ParsingException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ConstituencyFactory {
    /*private static HashMap<Integer, Integer> loadPartyVotes(Node constituencyNode) {
        HashMap<Integer, Integer> partyVotes = new HashMap<>();
        NodeList partiesVotesNodes = constituencyNode.getChildNodes();
        for (int i = 0; i < partiesVotesNodes.getLength(); ++i) {
            Node node = partiesVotesNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && !"UCAST".equals(node.getNodeName())) {
                partyVotes.put(Integer.parseInt(((Element)node).getAttribute("KSTRANA")), Integer.parseInt(((Element)node).getAttribute("HLASY")));
            }
        }
        return partyVotes;
    }*/
    public static SystemConfiguration loadConsituencies(String filename) throws ParsingException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(filename);
        } catch (ParserConfigurationException | IOException | SAXException | IllegalArgumentException e) {
            throw new ParsingException();
        }

        NodeList nodeList = document.getElementsByTagName("KRAJ");
        List<Constituency> returnList = new ArrayList<>();
        HashMap<Integer, String> partyNames = new HashMap<>();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Element krajElem = (Element) nodeList.item(i);
            int id = Integer.parseInt(krajElem.getAttribute("CIS_KRAJ"));
            String name = krajElem.getAttribute("NAZ_KRAJ");

            // Find UCAST
            NodeList ucastList = krajElem.getElementsByTagName("UCAST");
            Element ucastElem = (Element) ucastList.item(0);
            int correctVotes = Integer.parseInt(ucastElem.getAttribute("PLATNE_HLASY"));

            // Find STRANA
            NodeList stranaList = krajElem.getElementsByTagName("STRANA");
            HashMap<Integer, Integer> partyVotes = new HashMap<>();
            for (int j = 0; j < stranaList.getLength(); j++) {
                Element stranaElem = (Element) stranaList.item(j);
                int kstrana = Integer.parseInt(stranaElem.getAttribute("KSTRANA"));
                String nazStr = stranaElem.getAttribute("NAZ_STR");
                // Find HODNOTY_STRANA
                NodeList hodnotyList = stranaElem.getElementsByTagName("HODNOTY_STRANA");
                Element hodnotyElem = (Element) hodnotyList.item(0);
                int hlasy = Integer.parseInt(hodnotyElem.getAttribute("HLASY"));
                partyVotes.put(kstrana, hlasy);
                partyNames.put(kstrana, nazStr);
            }

            returnList.add(new Constituency(id, name, correctVotes, partyVotes));
        }
        return new SystemConfiguration(new ArrayList<>(), returnList, partyNames);
    }
    public static Constituency mergeConstituencies(List<Constituency> constituencies) {
        String newName = "";
        int allCorrectVotes = 0;
        HashMap<Integer, Integer> newPartyVotes = new HashMap<Integer, Integer>();
        for (var constituency : constituencies) {
            newName += constituency.getName() + ", ";
            allCorrectVotes += constituency.getAllVotes();
            var currentVotesPerParty = constituency.getVotesPerParty();
            for (Integer key : currentVotesPerParty.keySet()) {
                if (newPartyVotes.containsKey(key)) {
                    newPartyVotes.put(key, newPartyVotes.get(key)+currentVotesPerParty.get(key));
                } else {
                    newPartyVotes.put(key, currentVotesPerParty.get(key));
                }
            }
        }
        return new Constituency(constituencies.getFirst().getId(), newName, allCorrectVotes, newPartyVotes);
    }
}
