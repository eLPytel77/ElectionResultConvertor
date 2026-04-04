package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.QuotaSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public class HagenbachBischoffovQuota extends QuotaSystem {
    public HagenbachBischoffovQuota(double threshold, int mandatesCount) {
        super(threshold, mandatesCount);
    }

    @Override
    protected int getQuota(int allVotes, int mandates) {
        return allVotes / (mandates + 1);
    }
    @Override
    public String getName() {
        return "Hagenbach-Bischoffova kvóta";
    }
}
