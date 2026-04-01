package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class QuotaSystem extends ElectionSystem {
    public QuotaSystem(double threshold, int mandates) {
        super(threshold, mandates);
    }
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
                    tempMandateRes.put(party.getKey(), tempMandateRes.get(party.getKey())+party.getValue()/quote);
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
