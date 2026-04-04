package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.QuotaSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HareQuota extends QuotaSystem {

    public HareQuota(double threshold, int mandates)  {
        super(threshold, mandates);
    }

    @Override
    protected int getQuota(int allVotes, int mandates) {
        return allVotes / mandates;
    }
    @Override
    public String getName() {
        return "Harehova kvóta";
    }
}
