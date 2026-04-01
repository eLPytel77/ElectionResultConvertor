package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public record MandateResult(Map<Integer, Integer> mandatesList) {

    public static MandateResult mergeMandateResults(MandateResult first, MandateResult second) {
        for (var item : second.mandatesList().entrySet()) {
            first.mandatesList().put(item.getKey(), item.getValue()+first.mandatesList().getOrDefault(item.getKey(), 0));
        }
        return first;
    }
}

