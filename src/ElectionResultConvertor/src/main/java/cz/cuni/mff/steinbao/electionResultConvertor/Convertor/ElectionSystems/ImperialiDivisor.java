package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.DivisorSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public class ImperialiDivisor extends DivisorSystem {

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
