package cz.cuni.mff.steinbao.electionResultConvertor.UI;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.Statistics;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.SystemConfiguration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * User interface for printing election results.
 * <p>
 * This class formats election results and statistics and writes them
 * to the console or an output file.
 */
public class OutputUI {
    // Constants for column headers
    private static final String PARTY_HEADER = "Strana";
    private static final String VOTES_HEADER = "Hlasy";
    private static final String VOTES_PERCENT_HEADER = "% hlasů";
    private static final String SYSTEM_TYPE_HEADER = "Typ volebního systému";
    private static final String MANDATE_COUNT_HEADER = "Počet mandátů";
    private static final String THRESHOLD_HEADER = "Uzavírací klauzule";
    private static final String PROPORTIONALITY_HEADER = "Indikátor proporc.";
    private static final String MANDATE_VOTE_RATIO_HEADER = "Počet hlasů na mandát";
    private static final String THRESHOLD_IMPACT_HEADER = "Procento propadlých";
    
    /**
     * Prompts the user for an optional output file name and returns a writer.
     *
     * @return PrintWriter writing to the chosen destination
     * @throws FileNotFoundException if the provided filename cannot be opened
     */
    private PrintWriter getOutput() throws FileNotFoundException {
        System.out.println("Pro uložení výstupu do souboru napište jeho název, jinak nechte prázdné: ");
        Scanner scanner = new Scanner(System.in);
        String output = scanner.nextLine();
        if (output.isEmpty()) {
            return new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));
        }
        return new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8));
    }

    /**
     * Writes the election results and statistics to the configured output.
     *
     * @param results election mandate results for each selected system
     * @param config the user-defined election configuration
     * @param stats computed statistics for each system
     * @throws FileNotFoundException if the output file cannot be opened
     */
    public void showResults(List<MandateResult> results, SystemConfiguration config, List<Statistics> stats) throws FileNotFoundException {
        PrintWriter writer = this.getOutput();
        List<Integer> partyIDs = new ArrayList<>(config.partyNames().keySet());
        
        // Get total votes per party and calculate percentages
        var votesPerParty = ElectionSystem.getVotesPerParty(config.customizedConstituencies());
        int totalVotes = votesPerParty.values().stream().mapToInt(Integer::intValue).sum();
        
        // Print header with election systems as columns
        int longestPartyName = Math.max(PARTY_HEADER.length(), 
            config.partyNames().values().stream().mapToInt(String::length).max().orElse(0));
        int votesColumnWidth = Math.max(VOTES_HEADER.length(), 
            String.valueOf(totalVotes).length());
        int percentColumnWidth = VOTES_PERCENT_HEADER.length();

        int systemTypeColumnWidth = Math.max(SYSTEM_TYPE_HEADER.length(), config.electionSystems().stream().mapToInt(system -> system.getName().length()).max().orElse(0));
        
        writer.printf("%-" + longestPartyName + "s | %" + votesColumnWidth + "s | %" + percentColumnWidth + "s |", 
            PARTY_HEADER, VOTES_HEADER, VOTES_PERCENT_HEADER);
        for (int i = 0; i < config.electionSystems().size(); ++i) {
            ElectionSystem system = config.electionSystems().get(i);
            writer.printf(" %8s |", system.getName().substring(0, Math.min(8, system.getName().length())));
        }
        writer.print("\n");
        
        // Print separator line
        int totalWidth = longestPartyName + votesColumnWidth + percentColumnWidth + 6 + // 6 for " | " separators
            (10 * config.electionSystems().size()) + (config.electionSystems().size() * 2) + 1;
        for (int j = 0; j < totalWidth; j++) {
            writer.print("-");
        }
        writer.print("\n");
        
        // Print data rows - one row per party
        for (var partyID : partyIDs) {
            String partyName = config.partyNames().get(partyID);
            int partyVotes = votesPerParty.getOrDefault(partyID, 0);
            double partyPercentage = totalVotes > 0 ? (partyVotes * 100.0) / totalVotes : 0.0;
            
            writer.printf("%-" + longestPartyName + "s | %" + votesColumnWidth + "d | %" + percentColumnWidth + ".2f%% |", 
                partyName, partyVotes, partyPercentage);
            
            for (int i = 0; i < config.electionSystems().size(); ++i) {
                Map<Integer, Integer> systemsResult = results.get(i).mandatesList();
                Integer res = systemsResult.get(partyID);
                if (res == null) {
                    res = 0;
                }
                writer.printf(" %8d |", res);
            }
            writer.print("\n");
        }
        
        // Print statistics section
        writer.print("\nStatistiky volebních systémů:\n");
        writer.printf("%-" + systemTypeColumnWidth + "s | %-" + MANDATE_COUNT_HEADER.length() + "s | %-" + THRESHOLD_HEADER.length() + "s | %-" + PROPORTIONALITY_HEADER.length() + "s | %-" + MANDATE_VOTE_RATIO_HEADER.length() + "s | %-" + THRESHOLD_IMPACT_HEADER.length() + "s\n",
            SYSTEM_TYPE_HEADER, MANDATE_COUNT_HEADER, THRESHOLD_HEADER, 
            PROPORTIONALITY_HEADER, MANDATE_VOTE_RATIO_HEADER, THRESHOLD_IMPACT_HEADER);
        
        // Calculate width for statistics separator
        int statsWidth = systemTypeColumnWidth + MANDATE_COUNT_HEADER.length() + THRESHOLD_HEADER.length() +
            PROPORTIONALITY_HEADER.length() + MANDATE_VOTE_RATIO_HEADER.length() + THRESHOLD_IMPACT_HEADER.length() + 11; // 11 for " | " separators
        for (int j = 0; j < statsWidth; j++) {
            writer.print("-");
        }
        writer.print("\n");
        
        for (int i = 0; i < config.electionSystems().size(); ++i) {
            ElectionSystem system = config.electionSystems().get(i);
            Statistics systemsStats = stats.get(i);
            
            writer.printf("%-" + systemTypeColumnWidth + "s | %" + MANDATE_COUNT_HEADER.length() + "d | %" + (THRESHOLD_HEADER.length() - 1)+ ".1f%% | %" + PROPORTIONALITY_HEADER.length() + ".4f | %" + MANDATE_VOTE_RATIO_HEADER.length() + ".4f | %" + (THRESHOLD_IMPACT_HEADER.length()-1) + ".4f%%\n",
                system.getName(),
                system.getMandatesCount(),
                system.getThresholdPercentage(),
                systemsStats.proportionalityIndicator(),
                systemsStats.voteToSeatRatio(),
                systemsStats.thresholdImpact());
        }
        
        writer.flush();
    }
}
