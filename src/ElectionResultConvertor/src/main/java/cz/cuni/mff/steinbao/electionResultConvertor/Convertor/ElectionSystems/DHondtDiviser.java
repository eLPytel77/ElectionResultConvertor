package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.PartyMandates;

import java.util.*;

public class DHondtDiviser extends ElectionSystem {
    public DHondtDiviser(double threshold, int mandatesCount) {
        super(threshold, mandatesCount);
    }

    @Override
    public MandateResult countMandates(List<Constituency> electionResults) {
        HashMap<Integer, Integer> tempMandateRes = new HashMap<>();
        HashMap<Constituency, Integer> constituencyMandates = getConstituencyMandates(electionResults);
        Set<Integer> validParties = getPartiesOverThreshold(electionResults);


        for (var constituency : electionResults) {
            //Fill the table
            PriorityQueue<partyVotes> table = new PriorityQueue<>(Comparator.comparingInt(p -> p.votes));
            for (int i = 1; i <= constituencyMandates.get(constituency); ++i) {
                for (var party : constituency.getVotesPerParty().entrySet()) {
                    table.add(new partyVotes(party.getKey(), party.getValue()/i));
                }
            }

            //Chose the parties from the table
            for (int i = 0; i < constituencyMandates.get(constituency); ++i) {
                Integer partyId = table.poll().partyId;
                tempMandateRes.put(partyId, tempMandateRes.get(partyId)+1);
            }
        }

        return new MandateResult(tempMandateRes.entrySet());
    }
    private record partyVotes(Integer partyId, Integer votes) {}
}
