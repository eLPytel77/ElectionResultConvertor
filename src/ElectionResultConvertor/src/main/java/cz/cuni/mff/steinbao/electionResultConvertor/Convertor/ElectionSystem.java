package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public abstract class ElectionSystem {
    protected int threshold;
    public abstract MandateResult countMandates(List<Constituency> electionResults);

    protected ElectionSystem(int threshold) {
        this.threshold = threshold;
    }
}
