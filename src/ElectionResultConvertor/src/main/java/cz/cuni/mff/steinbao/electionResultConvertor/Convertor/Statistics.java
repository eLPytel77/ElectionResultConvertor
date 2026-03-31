package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import java.util.List;

public record Statistics (double proportionalityIndicator, double voteToSeatRatio, double thresholdImpact) {

    public static Statistics computeStats(MandateResult result, List<Constituency> constituencyList) {

    }
}

// Indikátor poměrnosti - jak moc se výsledek blíží opravdovému rozložení sil
// vote to seat ratio
// threshold impact - propadlé hlasy
// regional disproportionality