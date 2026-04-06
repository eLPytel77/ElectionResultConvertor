package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.QuotaSystem;

/**
 * Election system using the Hare quota.
 */
public class HareQuota extends QuotaSystem {

    /**
     * Creates a Hare quota system instance.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandates number of mandates to allocate
     */
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
