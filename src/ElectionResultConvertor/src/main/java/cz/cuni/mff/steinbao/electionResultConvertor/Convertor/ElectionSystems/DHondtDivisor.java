package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.DivisorSystem;

/**
 * Election system implementing the D'Hondt divisor method.
 */
public class DHondtDivisor extends DivisorSystem {
    /**
     * Creates a D'Hondt divisor system instance.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandatesCount number of mandates to allocate
     */
    public DHondtDivisor(double threshold, int mandatesCount) {
        super(threshold, mandatesCount);
    }

    @Override
    protected int[] getDivisors(int count) {
        int[] divisorsSequence = new int[count];
        for (int i = 0; i < count; ++i) {
            divisorsSequence[i] = i+1;
        }
        return divisorsSequence;
    }

    @Override
    public String getName() {
        return "D'Hondtův dělitel";
    }
}
