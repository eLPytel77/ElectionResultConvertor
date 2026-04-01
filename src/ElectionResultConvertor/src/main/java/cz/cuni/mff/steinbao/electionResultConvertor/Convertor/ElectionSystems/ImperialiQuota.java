package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.QuotaSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public class ImperialiQuota extends QuotaSystem {

    public ImperialiQuota(double threshold, int mandates) {
        super(threshold, mandates);
    }

    @Override
    protected int getQuota(int allVotes, int mandates) {
        return allVotes / (mandates + 2);
    }
}
