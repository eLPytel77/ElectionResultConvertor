package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;

import java.util.List;

public record SystemConfiguration (List<ElectionSystem> electionSystems, List<Constituency> customizedConstituencies) {}
