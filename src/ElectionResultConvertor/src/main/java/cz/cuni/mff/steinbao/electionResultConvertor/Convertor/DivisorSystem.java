package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.*;

/**
 * Base class for divisor-based election systems.
 * <p>
 * Applications provide a divisor sequence used to allocate mandates.
 */
public abstract class DivisorSystem extends ElectionSystem {
    /**
     * Creates a divisor-based election system.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandates total number of mandates to allocate
     */
    public DivisorSystem(double threshold, int mandates) {
        super(threshold, mandates);
    }

    /**
     * Returns the divisor sequence used for allocation.
     *
     * @param count number of mandates in the constituency
     * @return divisor array used to compute quotients
     */
    protected abstract int[] getDivisors(int count);

    @Override
    public MandateResult countMandates(List<Constituency> electionResults) {
        HashMap<Integer, Integer> tempMandateRes = new HashMap<>();
        HashMap<Constituency, Integer> constituencyMandates = getConstituencyMandates(electionResults);
        Set<Integer> validParties = getPartiesOverThreshold(electionResults);
        int[] divisorsSequence = getDivisors(this.mandatesCount);

        for (var constituency : electionResults) {
            //Fill the table
            PriorityQueue<partyVotes> table = new PriorityQueue<>(Comparator.comparingInt(p -> -p.votes));
            for (int i = 0; i < constituencyMandates.get(constituency); ++i) {
                for (var party : constituency.getVotesPerParty().entrySet()) {
                    if (validParties.contains(party.getKey())) {
                        int currentdivisor = divisorsSequence[i];
                        table.add(new partyVotes(party.getKey(), party.getValue()/currentdivisor));
                    }
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

    /**
     * Container for party vote quotients during divisor allocation.
     */
    private record partyVotes(Integer partyId, Integer votes) {}
}
