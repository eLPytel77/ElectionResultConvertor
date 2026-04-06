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
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Factory for loading and merging constituency data.
 */
public class ConstituencyFactory {
    /**
     * Loads constituencies from an XML file.
     *
     * @param filename XML file path containing election results
     * @return system configuration with loaded constituencies and party names
     * @throws ParsingException when the XML file cannot be parsed or loaded
     */
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

    /**
     * Merges multiple constituencies into a single combined constituency.
     *
     * @param constituencies list of constituencies to merge
     * @return merged constituency containing combined votes and name
     */
    public static Constituency mergeConstituencies(List<Constituency> constituencies) {
        StringBuilder newName = new StringBuilder();
        int allCorrectVotes = 0;
        HashMap<Integer, Integer> newPartyVotes = new HashMap<Integer, Integer>();
        for (int i = 0; i < constituencies.size(); i++) {
            var constituency = constituencies.get(i);
            newName.append(constituency.getName());
            if (i < constituencies.size() - 1) {
                newName.append(", ");
            }
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
        return new Constituency(constituencies.getFirst().getId(), newName.toString(), allCorrectVotes, newPartyVotes);
    }
}
