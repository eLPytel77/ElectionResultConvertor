package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems.DHondtDivisor;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import org.reflections.Reflections;

import java.util.*;

public abstract class ElectionSystem {
    protected final double thresholdPercentage;
    protected final int mandatesCount;


    public static List<Class<? extends ElectionSystem>> getAllSystemsClass() {
        Reflections reflections = new Reflections("cz.cuni.mff.steinbao.electionResultConvertor.Convertor");
        return reflections.getSubTypesOf(ElectionSystem.class).stream().toList();
    }
    public ElectionSystem(double thresholdPercentage, int mandatesCount) {
        this.thresholdPercentage = thresholdPercentage;
        this.mandatesCount = mandatesCount;
    }

    public abstract MandateResult countMandates(List<Constituency> electionResults);

    public abstract String getName();

    public int getMandatesCount() {
        return mandatesCount;
    }
    public double getThresholdPercentage() {
        return this.thresholdPercentage;
    }

    protected static MandateResult countSecondScrutunium(int remainingMandates, int unUsedVotes, HashMap<Integer, Integer> partyRemainingVotes) {
        // Create a fake all republic constituency, then use DHondtDiviser class
        List<Constituency> republic = new ArrayList<>();
        republic.add(new Constituency(-1, "Czech republic", unUsedVotes, partyRemainingVotes));
        ElectionSystem divisor = new DHondtDivisor(0, remainingMandates);
        return divisor.countMandates(republic);
    }

    public static int getAllVotes(List<Constituency> electionResults) {
        int allVotes = 0;
        for (var constituency : electionResults) {
            allVotes += constituency.getAllVotes();
        }
        return allVotes;
    }

    public static HashMap<Integer, Integer> getVotesPerParty(List<Constituency> constituencyList) {
        HashMap<Integer, Integer> tempVotesPerParty = new HashMap<>();
        for (var constituency : constituencyList) {
            for (var party : constituency.getVotesPerParty().entrySet()) {
                tempVotesPerParty.put(party.getKey(), party.getValue()+tempVotesPerParty.getOrDefault(party.getKey(), 0));
            }
        }
        return  tempVotesPerParty;
    }

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

    protected HashMap<Constituency, Integer> getConstituencyMandates(List<Constituency> electionResults) {
        HashMap<Constituency, Integer> constituencySize = new HashMap<>();
        //Compute the republic mandate number
        int allVotes = getAllVotes(electionResults);
        int republicMandateNumber = allVotes / mandatesCount;
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


    private record ConstituencyVotesRemainder(Constituency constituency, int votesRemainder){}
    private static class RemainderComparator implements Comparator<ConstituencyVotesRemainder> {


        @Override
        public int compare(ConstituencyVotesRemainder o1, ConstituencyVotesRemainder o2) {
            return o1.votesRemainder() - o2.votesRemainder();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

}
/*

u dělitelů jedno skrutinium
u kvót možné druhé (když zbydou mandáty), ve 2. skrutiniu dHondt

 */

