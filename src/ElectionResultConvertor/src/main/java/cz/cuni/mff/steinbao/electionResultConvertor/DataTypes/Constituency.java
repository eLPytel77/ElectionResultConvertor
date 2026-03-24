package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constituency {
    private int id;
    private String name;
    private int allCorrectCastedVotes;
    HashMap<Integer, Integer> votesPerParty;

    public Constituency(int id, String name, int correctVotes, HashMap<Integer, Integer> votesPerParty) {
        this.id = id;
        this.name = name;
        this.allCorrectCastedVotes = correctVotes;
        this.votesPerParty = votesPerParty;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAllVotes() {return this.allCorrectCastedVotes;}
    public HashMap<Integer, Integer> getVotesPerParty() {return this.votesPerParty;}
}
