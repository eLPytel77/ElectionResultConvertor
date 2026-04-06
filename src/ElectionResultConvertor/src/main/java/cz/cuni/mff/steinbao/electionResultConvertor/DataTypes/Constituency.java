package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import java.util.HashMap;

/**
 * Represents a single constituency and its vote counts.
 */
public class Constituency {
    private int id;
    private String name;
    private int allCorrectCastedVotes;
    HashMap<Integer, Integer> votesPerParty;

    /**
     * Creates a constituency record.
     *
     * @param id constituency identifier
     * @param name constituency name
     * @param correctVotes number of valid ballots cast
     * @param votesPerParty map of party ids to votes in this constituency
     */
    public Constituency(int id, String name, int correctVotes, HashMap<Integer, Integer> votesPerParty) {
        this.id = id;
        this.name = name;
        this.allCorrectCastedVotes = correctVotes;
        this.votesPerParty = votesPerParty;
    }

    /**
     * Returns the constituency identifier.
     *
     * @return constituency id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the constituency name.
     *
     * @return constituency name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the total valid votes in the constituency.
     *
     * @return total valid votes
     */
    public int getAllVotes() {return this.allCorrectCastedVotes;}

    /**
     * Returns the vote counts per party.
     *
     * @return map of party ids to vote totals
     */
    public HashMap<Integer, Integer> getVotesPerParty() {return this.votesPerParty;}

}
