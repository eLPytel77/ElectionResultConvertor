package cz.cuni.mff.steinbao.electionResultConvertor.DataTypes;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;

import java.util.List;
import java.util.Map;

/**
 * Configuration data loaded from the user input.
 *
 * @param electionSystems selected election system instances
 * @param customizedConstituencies constituencies to be used for mandate calculation
 * @param partyNames mapping of party ids to their display names
 */
public record SystemConfiguration (List<ElectionSystem> electionSystems, List<Constituency> customizedConstituencies, Map<Integer, String> partyNames) {}
