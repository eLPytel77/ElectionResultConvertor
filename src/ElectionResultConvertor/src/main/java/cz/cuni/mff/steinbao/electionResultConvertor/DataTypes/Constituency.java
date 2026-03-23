package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constituency {
    private int id;
    private String name;
    private int allCorrectCastedVotes;
    HashMap<Integer, Integer> votesPerParty = new HashMap<Integer, Integer>();

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
