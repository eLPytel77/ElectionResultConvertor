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
    private static HashMap<Integer, Integer> loadPartyVotes(Node constituencyNode) {
        HashMap<Integer, Integer> partyVotes = new HashMap<>();
        NodeList partiesVotesNodes = constituencyNode.getChildNodes();
        for (int i = 1; i < partiesVotesNodes.getLength(); ++i) {
            Node node = partiesVotesNodes.item(i);
            partyVotes.put(Integer.parseInt(((Element)node).getAttribute("KSTRANA")), Integer.parseInt(((Element)node).getAttribute("HLASY")));
        }
        return partyVotes;
    }
    public static List<Constituency> loadConsituencies(String filename) throws ParsingException {
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
        NodeList resultNodeList = document.getElementsByTagName("CELKEM");
        List<Constituency> returnList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);
            Element elem = (Element) node;
            int id = Integer.parseInt(elem.getAttribute("CIS_KRAJ"));
            String name = elem.getAttribute("NAZ_KRAJ");
            Node resultNode = resultNodeList.item(i);

            Node infoNode = (resultNode.getChildNodes()).item(0);
            int correctVotes = Integer.parseInt(((Element)infoNode).getAttribute("PLATNE_HLASY"));
            var partyVotes = ConstituencyFactory.loadPartyVotes(resultNode);
            returnList.add(new Constituency(id, name, correctVotes, partyVotes));
        }
        return returnList;
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
