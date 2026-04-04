package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.*;

public abstract class DivisorSystem extends ElectionSystem {
    public DivisorSystem(double threshold, int mandates) {
        super(threshold, mandates);
    }
    protected abstract int[] getDivisors(int count);

    @Override
    public MandateResult countMandates(List<Constituency> electionResults) {
        HashMap<Integer, Integer> tempMandateRes = new HashMap<>();
        HashMap<Constituency, Integer> constituencyMandates = getConstituencyMandates(electionResults);
        Set<Integer> validParties = getPartiesOverThreshold(electionResults);
        int[] divisorsSequence = getDivisors(this.mandatesCount);

        for (var constituency : electionResults) {
            //Fill the table
            PriorityQueue<partyVotes> table = new PriorityQueue<>(Comparator.comparingInt(p -> p.votes));
            for (int i = 0; i < constituencyMandates.get(constituency); ++i) {
                for (var party : constituency.getVotesPerParty().entrySet()) {
                    table.add(new partyVotes(party.getKey(), party.getValue()/divisorsSequence[i]));
                }
            }

            //Chose the parties from the table
            for (int i = 0; i < constituencyMandates.get(constituency); ++i) {
                Integer partyId = table.poll().partyId;
                tempMandateRes.put(partyId, tempMandateRes.getOrDefault(partyId, 0)+1);
            }
        }

        return new MandateResult(tempMandateRes);
    }
    private record partyVotes(Integer partyId, Integer votes) {}
}
