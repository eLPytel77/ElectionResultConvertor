package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public abstract class ElectionSystem {
    protected String name;
    protected double threshold = 0;
    public abstract MandateResult countMandates(List<Constituency> electionResults);

    /*protected ElectionSystem(int threshold) {
        this.threshold = threshold;
    }*/
    public String getName() {
        return name;
    }
    public void setThreshold (double threshold) {
        this.threshold = threshold;
    }
}
