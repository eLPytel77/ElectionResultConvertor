package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.DivisorSystem;

/**
 * Election system implementing the Saint-Laguë divisor method.
 */
public class SaintLaguehDivisor extends DivisorSystem {

    /**
     * Creates a Saint-Laguë divisor system instance.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandatesCount number of mandates to allocate
     */
    public SaintLaguehDivisor(double threshold, int mandatesCount) {
        super(threshold, mandatesCount);
    }

    @Override
    protected int[] getDivisors(int count) {
        int[] divisorsSequence = new int[count];
        for (int i = 0; i < count; ++i) {
            divisorsSequence[i] = 2*i+1;
        }
        return divisorsSequence;
    }

    @Override
    public String getName() {
        return "SaintLagueho dělitel";
    }
}
