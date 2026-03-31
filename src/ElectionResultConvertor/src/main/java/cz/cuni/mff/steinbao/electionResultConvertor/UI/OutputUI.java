package cz.cuni.mff.steinbao.electionResultConvertor.UI;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.Statistics;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.SystemConfiguration;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

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
    public void showResults(List<MandateResult> results, SystemConfiguration config, List<Statistics> stats) {
        PrintWriter writer = this.getOutput();


    }
}
