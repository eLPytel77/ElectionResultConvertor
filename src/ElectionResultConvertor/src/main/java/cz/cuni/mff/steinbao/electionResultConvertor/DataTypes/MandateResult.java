package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import java.util.List;
import java.util.Map;
import java.util.Set;


public record MandateResult(Set<Map.Entry<Integer, Integer>> mandatesList) {}

