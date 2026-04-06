package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Base class for quota-based election systems.
 * <p>
 * Implementations provide a quota formula and use a two-stage allocation process.
 */
public abstract class QuotaSystem extends ElectionSystem {
    /**
     * Creates a quota-based election system.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandates total number of mandates to allocate
     */
    public QuotaSystem(double threshold, int mandates) {
        super(threshold, mandates);
    }

    /**
     * Computes the quota used to allocate mandates in a constituency.
     *
     * @param allVotes total votes cast in the constituency
     * @param mandates number of mandates available in the constituency
     * @return quota value used for the first scrutiny
     */
    protected abstract int getQuota(int allVotes, int mandates);

    @Override
    public MandateResult countMandates(List<Constituency> electionResults) {
        HashMap<Integer, Integer> tempMandateRes = new HashMap<>();
        HashMap<Integer, Integer> partyRemainderVotes = new HashMap<>();
        HashMap<Constituency, Integer> constituencyMandates = getConstituencyMandates(electionResults);
        Set<Integer> validParties = getPartiesOverThreshold(electionResults);
        int remainingMandates = 0;
        int unUsedVotes = 0;

        //Compute mandates in the first scrutiny
        for (var constituency : electionResults) {
            int quote = getQuota(constituency.getAllVotes(), constituencyMandates.get(constituency));
            int constituencyDedicatedMandates = 0;
            for (var party : constituency.getVotesPerParty().entrySet()) {
                if (validParties.contains(party.getKey())) {
                    constituencyDedicatedMandates += party.getValue()/quote;
                    tempMandateRes.put(party.getKey(), tempMandateRes.getOrDefault(party.getKey(), 0)+party.getValue()/quote);
                    int remainderVotes = tempMandateRes.get(party.getKey())+party.getValue()%quote;
                    partyRemainderVotes.put(party.getKey(), remainderVotes);
                    unUsedVotes += remainderVotes;
                }
            }
            remainingMandates += constituencyMandates.get(constituency) - constituencyDedicatedMandates;
        }

        //Use DHondt divisor for the second one
        var secondScrututiniumResult = ElectionSystem.countSecondScrutunium(remainingMandates, unUsedVotes, partyRemainderVotes);
        return MandateResult.mergeMandateResults(new MandateResult(tempMandateRes), secondScrututiniumResult);
    }
}
