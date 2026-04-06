package cz.cuni.mff.steinbao.electionResultConvertor;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.Statistics;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.SystemConfiguration;
import cz.cuni.mff.steinbao.electionResultConvertor.Exceptions.ParsingException;
import cz.cuni.mff.steinbao.electionResultConvertor.UI.InputUI;
import cz.cuni.mff.steinbao.electionResultConvertor.UI.OutputUI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    /**
     * Runs the election result conversion process.
     * <p>
     * Reads voter data and system settings from the user, calculates
     * mandates for each selected election system, and displays the results.
     */
    public static void main(String[] args) {
        try {
            List<Class<? extends ElectionSystem>> availableElextionSystems = ElectionSystem.getAllSystemsClass();
            InputUI input = new InputUI(availableElextionSystems);
            SystemConfiguration config = input.GetConfigurationFromUser();
            ArrayList<MandateResult> results = new ArrayList<MandateResult>();
            ArrayList<Statistics> stats = new ArrayList<>();
            for(ElectionSystem system : config.electionSystems()) {
                var mandates = system.countMandates(config.customizedConstituencies());
                results.add(mandates);
                stats.add(Statistics.computeStats(mandates, config.customizedConstituencies()));
            }
            OutputUI output = new OutputUI();
            output.showResults(results, config, stats);
        } catch (ParsingException e) {
            System.out.println("Data file has incorrect format or it wasnt found.");
        } catch (FileNotFoundException e) {
            System.out.println("Output file name is invalid.");
        }

    }
}
