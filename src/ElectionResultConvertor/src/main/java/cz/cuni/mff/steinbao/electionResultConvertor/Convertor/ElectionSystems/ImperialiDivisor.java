package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public class ImperialiDivisor extends ElectionSystem {

    public ImperialiDivisor(double threshold, int mandates) {
        super(threshold, mandates);
    }

    @Override
    public MandateResult countMandates(List<Constituency> electionResults) {
        return null;
    }
}
