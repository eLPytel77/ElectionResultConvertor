package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.DivisorSystem;

/**
 * Election system using the Imperiali divisor method.
 */
public class ImperialiDivisor extends DivisorSystem {

    /**
     * Creates an Imperiali divisor system instance.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandates number of mandates to allocate
     */
    public ImperialiDivisor(double threshold, int mandates) {
        super(threshold, mandates);
    }

    @Override
    protected int[] getDivisors(int count) {
        int[] divisorsSequence = new int[count];
        for (int i = 0; i < count; ++i) {
            divisorsSequence[i] = i+2;
        }
        return divisorsSequence;
    }

    @Override
    public String getName() {
        return "Imperialiho dělitel";
    }
}
