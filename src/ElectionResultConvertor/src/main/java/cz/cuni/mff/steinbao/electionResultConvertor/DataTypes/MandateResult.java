package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import java.util.Map;

/**
 * Represents mandate allocation results for parties.
 *
 * @param mandatesList map of party id to allocated mandates
 */
public record MandateResult(Map<Integer, Integer> mandatesList) {

    /**
     * Merges two mandate results by summing mandates for the same parties.
     *
     * @param first first mandate result
     * @param second second mandate result
     * @return merged mandate result containing combined mandate totals
     */
    public static MandateResult mergeMandateResults(MandateResult first, MandateResult second) {
        for (var item : second.mandatesList().entrySet()) {
            first.mandatesList().put(item.getKey(), item.getValue()+first.mandatesList().getOrDefault(item.getKey(), 0));
        }
        return first;
    }
}

