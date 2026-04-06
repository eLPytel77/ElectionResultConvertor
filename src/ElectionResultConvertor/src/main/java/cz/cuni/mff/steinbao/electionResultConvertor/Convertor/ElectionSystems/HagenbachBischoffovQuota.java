package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.QuotaSystem;

/**
 * Election system using the Hagenbach-Bischoff quota.
 */
public class HagenbachBischoffovQuota extends QuotaSystem {
    /**
     * Creates a Hagenbach-Bischoff quota system instance.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandatesCount number of mandates to allocate
     */
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
