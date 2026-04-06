package cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystems;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.QuotaSystem;

/**
 * Election system using the Imperiali quota.
 */
public class ImperialiQuota extends QuotaSystem {

    /**
     * Creates an Imperiali quota system instance.
     *
     * @param threshold minimum voting threshold percentage
     * @param mandates number of mandates to allocate
     */
    public ImperialiQuota(double threshold, int mandates) {
        super(threshold, mandates);
    }

    @Override
    protected int getQuota(int allVotes, int mandates) {
        return allVotes / (mandates + 2);
    }

    @Override
    public String getName() {
        return "Imperialiho kvóta";
    }
}
