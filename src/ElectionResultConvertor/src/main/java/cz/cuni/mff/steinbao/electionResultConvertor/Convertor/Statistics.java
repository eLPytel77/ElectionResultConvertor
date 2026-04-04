package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.HashMap;
import java.util.List;

public record Statistics (double proportionalityIndicator, double voteToSeatRatio, double thresholdImpact) {


    public static Statistics computeStats(MandateResult result, List<Constituency> constituencyList) {
        var votesPerParty = ElectionSystem.getVotesPerParty(constituencyList);
        double tempSum = 0;
        int allVotes = ElectionSystem.getAllVotes(constituencyList);

        //Count the mandates
        int mandatesCount = 0;
        for (var party : result. mandatesList().entrySet()) {
            mandatesCount += party.getValue();
        }

        //Compute the proportionality indicator
        for (var party : result.mandatesList().entrySet()) {
            double tmp = ((double) party.getValue() /mandatesCount) - ((double) votesPerParty.get(party.getKey()) /allVotes);
            tempSum += tmp*tmp;
            votesPerParty.remove(party.getKey());
        }
        double proportionalityIndicator = Math.sqrt((1/2)*tempSum);

        //Compute votes to seat ration
        double voteToSeatRatio = (double) mandatesCount /allVotes;

        //Compute threshold impact as a ratio of failed votes to all votes
        int allFailedVotes = 0;
        for (var partyUnderThreshold : votesPerParty.entrySet()) {
            allFailedVotes += partyUnderThreshold.getValue();
        }
        double failedToUsedVotesRatio = (double) allFailedVotes / allVotes;

        return new Statistics(proportionalityIndicator, voteToSeatRatio, failedToUsedVotesRatio);
    }
}

// Indikátor poměrnosti - jak moc se výsledek blíží opravdovému rozložení sil - Gallagher index: ((1/2)*squredSumof(Si-Vi))^(1/2) where Si = seat share, Vi = vote share for party i
// average vote to seat ratio
// threshold impact - propadlé hlasy