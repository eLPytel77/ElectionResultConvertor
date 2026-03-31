package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public class HagenbachBischoffovQuota extends ElectionSystem {
    public HagenbachBischoffovQuota(double threshold, int mandatesCount) {
        super(threshold, mandatesCount);
    }

    @Override
    public MandateResult countMandates(List<Constituency> electionResults) {
        return null;
    }
}
