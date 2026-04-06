package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ElectionSystemTest {

    // Test subclass to access protected method
    private static class TestElectionSystem extends ElectionSystem {
        public TestElectionSystem(double threshold, int mandates) {
            super(threshold, mandates);
        }

        @Override
        public MandateResult countMandates(List<Constituency> electionResults) {
            return new MandateResult(new HashMap<>()); // Dummy implementation
        }

        @Override
        public String getName() {
            return "Test System";
        }

        // Expose the protected method
        public Set<Integer> testGetPartiesOverThreshold(List<Constituency> electionResult) {
            return getPartiesOverThreshold(electionResult);
        }
    }

    @Test
    public void testGetPartiesOverThreshold_AllAbove() {
        TestElectionSystem system = new TestElectionSystem(5.0, 10); // 5% threshold

        List<Constituency> constituencies = new ArrayList<>();
        HashMap<Integer, Integer> votes = new HashMap<>();
        votes.put(1, 60); // 60% of 100 = 6% > 5%
        votes.put(2, 4); // 4% < 5%
        Constituency c = new Constituency(1, "Test", 100, votes);
        constituencies.add(c);

        Set<Integer> result = system.testGetPartiesOverThreshold(constituencies);

        assertTrue(result.contains(1));
        assertFalse(result.contains(2));
    }

    @Test
    public void testGetPartiesOverThreshold_SomeAbove() {
        TestElectionSystem system = new TestElectionSystem(10.0, 10); // 10% threshold

        List<Constituency> constituencies = new ArrayList<>();
        HashMap<Integer, Integer> votes = new HashMap<>();
        votes.put(1, 15); // 15% > 10%
        votes.put(2, 11); // 11% > 10%
        votes.put(3, 5);  // 5% < 10%
        Constituency c = new Constituency(1, "Test", 100, votes);
        constituencies.add(c);

        Set<Integer> result = system.testGetPartiesOverThreshold(constituencies);

        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertFalse(result.contains(3));
    }

    @Test
    public void testGetPartiesOverThreshold_EmptyConstituencies() {
        TestElectionSystem system = new TestElectionSystem(5.0, 10);

        List<Constituency> constituencies = new ArrayList<>();

        Set<Integer> result = system.testGetPartiesOverThreshold(constituencies);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPartiesOverThreshold_ZeroThreshold() {
        TestElectionSystem system = new TestElectionSystem(0.0, 10); // 0% threshold

        List<Constituency> constituencies = new ArrayList<>();
        HashMap<Integer, Integer> votes = new HashMap<>();
        votes.put(1, 1); // Any positive votes
        votes.put(2, 0); // Zero votes
        Constituency c = new Constituency(1, "Test", 1, votes);
        constituencies.add(c);

        Set<Integer> result = system.testGetPartiesOverThreshold(constituencies);

        assertTrue(result.contains(1));
        assertFalse(result.contains(2)); // 0 not > 0
    }
}