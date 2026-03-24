package cz.cuni.mff.steinbao.electionResultConvertor;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.SystemConfiguration;
import cz.cuni.mff.steinbao.electionResultConvertor.UI.InputUI;
import cz.cuni.mff.steinbao.electionResultConvertor.UI.OutputUI;

import java.util.ArrayList;


public class Main {
    //TODO Implement exception handling!!!
    static void main() {
        ElectionSystem[] availableElextionSystems = {};
        InputUI input = new InputUI(availableElextionSystems);
        SystemConfiguration config = input.GetConfigurationFromUser();
        ArrayList<MandateResult> results = new ArrayList<MandateResult>();
        for(ElectionSystem system : config.electionSystems()) {
            results.add(system.countMandates(config.customizedConstituencies()));
        }
        OutputUI output = new OutputUI();
        output.showResults(results, config);
    }
}
