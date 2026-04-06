package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems.DHondtDivisor;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import org.reflections.Reflections;

import java.util.*;

/**
 * Base class for all election systems supported by the converter.
 * <p>
 * Implementations provide mandate calculation logic and naming information.
 */
public abstract class ElectionSystem {
    protected final double thresholdPercentage;
    protected final int mandatesCount;


    /**
     * Returns the list of all concrete election system classes in the converter.
     *
     * @return available non-abstract election system implementations
     */
    public static List<Class<? extends ElectionSystem>> getAllSystemsClass() {
        Reflections reflections = new Reflections("cz.cuni.mff.steinbao.electionResultConvertor.Convertor");
        return reflections.getSubTypesOf(ElectionSystem.class).stream()
                .filter(classElem -> !java.lang.reflect.Modifier.isAbstract(classElem.getModifiers()))
                .toList();
    }

    /**
     * Creates a new election system with the specified threshold and mandate count.
     *
     * @param thresholdPercentage minimum voting percentage for party participation
     * @param mandatesCount total number of mandates to distribute
     */
    public ElectionSystem(double thresholdPercentage, int mandatesCount) {
        this.thresholdPercentage = thresholdPercentage;
        this.mandatesCount = mandatesCount;
    }

    /**
     * Computes the mandate distribution for the provided election results.
     *
     * @param electionResults list of constituencies containing vote counts
     * @return mandate result for participating parties
     */
    public abstract MandateResult countMandates(List<Constituency> electionResults);

    /**
     * Returns the display name of the election system.
     *
     * @return election system name
     */
    public abstract String getName();

    /**
     * Returns the configured number of mandates for this system.
     *
     * @return total mandates count
     */
    public int getMandatesCount() {
        return mandatesCount;
    }

    /**
     * Returns the configured threshold percentage for this system.
     *
     * @return threshold percentage
     */
    public double getThresholdPercentage() {
        return this.thresholdPercentage;
    }

    /**
     * Performs a second-scrutiny mandate computation with remaining votes.
     *
     * @param remainingMandates remaining mandates to allocate
     * @param unUsedVotes total votes that were not used in the first scrutiny
     * @param partyRemainingVotes remaining votes per party
     * @return mandate result from second scrutiny distribution
     */
    protected static MandateResult countSecondScrutunium(int remainingMandates, int unUsedVotes, HashMap<Integer, Integer> partyRemainingVotes) {
        // Create a fake all republic constituency, then use DHondtDiviser class
        List<Constituency> republic = new ArrayList<>();
        republic.add(new Constituency(-1, "Czech republic", unUsedVotes, partyRemainingVotes));
        ElectionSystem divisor = new DHondtDivisor(0, remainingMandates);
        return divisor.countMandates(republic);
    }

    /**
     * Computes the total number of votes across all constituencies.
     *
     * @param electionResults list of constituencies
     * @return total valid votes across the republic
     */
    public static int getAllVotes(List<Constituency> electionResults) {
        int allVotes = 0;
        for (var constituency : electionResults) {
            allVotes += constituency.getAllVotes();
        }
        return allVotes;
    }

    /**
     * Aggregates votes per party from all constituencies.
     *
     * @param constituencyList list of constituencies to aggregate
     * @return map of party id to total votes
     */
    public static HashMap<Integer, Integer> getVotesPerParty(List<Constituency> constituencyList) {
        HashMap<Integer, Integer> tempVotesPerParty = new HashMap<>();
        for (var constituency : constituencyList) {
            for (var party : constituency.getVotesPerParty().entrySet()) {
                tempVotesPerParty.put(party.getKey(), party.getValue()+tempVotesPerParty.getOrDefault(party.getKey(), 0));
            }
        }
        return  tempVotesPerParty;
    }

    /**
     * Determines which parties pass the configured threshold across all results.
     *
     * @param electionResult list of constituencies with vote counts
     * @return set of party IDs that exceed the threshold
     */
    protected Set<Integer> getPartiesOverThreshold(List<Constituency> electionResult) {
        int absoluteThresholdNumber = (int) (getAllVotes(electionResult) * thresholdPercentage / 100);
        //Count parties votes in the whole republic
        HashMap<Integer, Integer> allVotesPerParty = getVotesPerParty(electionResult);
        //Check if the parties have enough votes to bypass threshold
        Set<Integer> overThresholdParties = new HashSet<>();
        for (var party : allVotesPerParty.entrySet()) {
            if (party.getValue() > absoluteThresholdNumber) {
                overThresholdParties.add(party.getKey());
            }
        }
        return overThresholdParties;
    }

    /**
     * Allocates constituencies mandates proportionally across the republic.
     *
     * @param electionResults list of constituencies with vote totals
     * @return map containing the mandate count per constituency
     */
    protected HashMap<Constituency, Integer> getConstituencyMandates(List<Constituency> electionResults) {
        HashMap<Constituency, Integer> constituencySize = new HashMap<>();
        //Compute the republic mandate number
        int allVotes = getAllVotes(electionResults);
        int republicMandateNumber = (int)Math.round((double) allVotes / mandatesCount);
        int dedicatedMandates = 0;
        //Dedicate the first batch of mandates
        List<ConstituencyVotesRemainder> remainders = new ArrayList<>();
        for (var constituency : electionResults) {
            int constiuencyMandateNum =  constituency.getAllVotes()/republicMandateNumber;
            dedicatedMandates += constiuencyMandateNum;
            constituencySize.put(constituency, constiuencyMandateNum);
            remainders.add(new ConstituencyVotesRemainder(constituency, constituency.getAllVotes()%republicMandateNumber));
        }
        //Dedicate the rest of the mandates
        remainders.sort(new RemainderComparator());
        if (dedicatedMandates < mandatesCount) {
            for (int i = 0; i < mandatesCount - dedicatedMandates; ++i) {
                var constituency = remainders.get(i);
                constituencySize.put(constituency.constituency(), constituencySize.get(constituency.constituency())+1);
            }
        }
        return constituencySize;
    }


    /**
     * Internal record holding remainder vote details for a constituency.
     */
    private record ConstituencyVotesRemainder(Constituency constituency, int votesRemainder){}

    /**
     * Comparator for sorting constituencies by remainder vote count.
     */
    private static class RemainderComparator implements Comparator<ConstituencyVotesRemainder> {

        /**
         * Compares two remainder vote records by their remainder value.
         *
         * @param o1 first remainder record
         * @param o2 second remainder record
         * @return negative if first is smaller, positive if larger, zero if equal
         */
        @Override
        public int compare(ConstituencyVotesRemainder o1, ConstituencyVotesRemainder o2) {
            return o1.votesRemainder() - o2.votesRemainder();
        }

        /**
         * Compares this comparator with another object for equality.
         *
         * @param obj other object
         * @return true if the other object is also a RemainderComparator
         */
        @Override
        public boolean equals(Object obj) {
            return obj instanceof RemainderComparator;
        }
    }

}


