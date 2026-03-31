package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public class ImperialiQuota extends ElectionSystem {

    public ImperialiQuota(double threshold, int mandates) {
        super(threshold, mandates);
    }

    @Override
    public MandateResult countMandates(List<Constituency> electionResults) {
        return null;
    }
}
