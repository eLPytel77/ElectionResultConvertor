package cz.cuni.mff.steinbao.electionResultConvertor.UI;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.Statistics;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.SystemConfiguration;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class OutputUI {
    private PrintWriter getOutput() throws FileNotFoundException {
        System.out.println("Pro uložení výstupu do souboru napište jeho název, jinak nechte prázdné: ");
        Scanner scanner = new Scanner(System.in);
        String output = scanner.nextLine();
        if (output.isEmpty()) {
            return new PrintWriter(System.out);
        }
        return new PrintWriter(output);
    }
    private String[] getPartyNames(Set<Integer> partyIDs, HashMap<Integer, String> partiesNames ) {
        //TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String[] partyNames = new String[partyIDs.size()];
        for (var id : partyIDs) {

        }


        return  new String[0];
    }
    public void showResults(List<MandateResult> results, SystemConfiguration config, List<Statistics> stats) throws FileNotFoundException {
        PrintWriter writer = this.getOutput();
        //Print header
        writer.print("Typ volebního systému   Počet mandátů   Uzavírací klauzule   Indikátor proporcionality   Poměr mandátů k hlasům   Poměr propadlých hlasů   ");
        Integer[] partyIds = (Integer[]) config.partyNames().keySet().toArray();
        for (var partyName : getPartyNames(results.get(0).mandatesList().keySet()) ) {
            writer.print(partyName + "   ");
        }
        writer.print("\n");
        for (int i = 0; i < config.electionSystems().size(); ++i) {
            ElectionSystem system = config.electionSystems().get(i);
            writer.print(system.getName() + "   ");
            writer.print(system.getMandatesCount() + "   ");
            writer.print(system.getThresholdPercentage()+"%   ");
            Statistics systemsStats = stats.get(i);
            writer.print(systemsStats.proportionalityIndicator() + "   ");
            writer.print(systemsStats.voteToSeatRatio() + "   ");
            writer.print(systemsStats.thresholdImpact() + "   ");
            Map<Integer, Integer> systemsResult = results.get(i).mandatesList();
            for (var partyRes : systemsResult.values()) {
                writer.print(partyRes + "   ");
            }
        }
        writer.flush();
    }
}
