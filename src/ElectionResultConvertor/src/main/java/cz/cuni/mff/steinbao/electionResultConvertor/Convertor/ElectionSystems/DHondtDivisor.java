package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.DivisorSystem;

public class DHondtDivisor extends DivisorSystem {
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
}
