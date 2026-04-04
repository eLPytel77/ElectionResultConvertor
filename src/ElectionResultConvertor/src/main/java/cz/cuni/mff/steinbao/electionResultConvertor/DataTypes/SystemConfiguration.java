package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;

import java.util.List;
import java.util.Map;

public record SystemConfiguration (List<ElectionSystem> electionSystems, List<Constituency> customizedConstituencies, Map<Integer, String> partyNames) {}
