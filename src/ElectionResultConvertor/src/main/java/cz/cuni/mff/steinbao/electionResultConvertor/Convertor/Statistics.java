package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.HashMap;
import java.util.List;

/**
 * Statistics computed from election mandate results.
 *
 * @param proportionalityIndicator measure of proportionality between votes and mandates
 * @param voteToSeatRatio average number of votes needed per mandate
 * @param thresholdImpact percentage of votes lost due to threshold exclusion
 */
public record Statistics (double proportionalityIndicator, double voteToSeatRatio, double thresholdImpact) {

    /**
     * Computes election statistics for the given result and constituency data.
     *
     * @param result mandate result produced by an election system
     * @param constituencyList list of constituencies used for vote aggregation
     * @return computed statistics record
     */
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
        for (var party : votesPerParty.entrySet()) {
            double tmp = ((double) result.mandatesList().getOrDefault(party.getKey(), 0) /mandatesCount) - ((double) party.getValue() /allVotes);
            tempSum += tmp*tmp;
        }
        double proportionalityIndicator = Math.sqrt(((double) 1 /2)*tempSum);

        //Compute seats needed for a seat
        double voteToSeatRatio = (double) allVotes / mandatesCount;

        //Compute threshold impact as a ratio of failed votes to all votes
        int allFailedVotes = 0;
        for (var partyUnderThreshold : votesPerParty.entrySet()) {
            if (!result.mandatesList().containsKey(partyUnderThreshold.getKey())) {
                allFailedVotes += partyUnderThreshold.getValue();
            }
        }
        double failedToUsedVotesRatio = (double) (allFailedVotes*100) / allVotes;

        return new Statistics(proportionalityIndicator, voteToSeatRatio, failedToUsedVotesRatio);
    }
}
