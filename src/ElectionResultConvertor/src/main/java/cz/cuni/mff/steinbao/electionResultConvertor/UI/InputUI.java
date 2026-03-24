package cz.cuni.mff.steinbao.electionResultConvertor.UI;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.ConstituencyFactory;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.SystemConfiguration;
import cz.cuni.mff.steinbao.electionResultConvertor.Exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputUI {
    private final ElectionSystem[] availableSystems;
    public InputUI(ElectionSystem[] electionSystems) {
        this.availableSystems = electionSystems;
    }

    private void printSystems() {
        System.out.println("Volební systémy, které je možno vybrat jsou:");
        for(int i = 0; i < availableSystems.length; ++i) {
            System.out.println(i+") " + availableSystems[i].getName());
        }
        System.out.println();
    }
    private double loadThreshold() {
        System.out.println("Zadejte aditivní klauzuli jako nezáporné číslo: ");
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            return 0;
        }


    }
    private List<ElectionSystem> loadSystems() {
        boolean chooseAnother = true;
        printSystems();
        List<ElectionSystem> choosenSystems = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte pořadní číslo některého z vypsaných systémů: ");
        int systemIndex = scanner.nextInt();
        availableSystems[systemIndex].setThreshold(loadThreshold());
        choosenSystems.add(availableSystems[systemIndex]);
        while (chooseAnother) {
            System.out.println("Pokud chcete vybrat další, zadejte znovu jeho pořadní číslo, jinak zadejte nečíselný znak: ");
            try {
                systemIndex = scanner.nextInt();
                availableSystems[systemIndex].setThreshold(loadThreshold());
                choosenSystems.add(availableSystems[systemIndex]);
            } catch (Exception e) {
                chooseAnother = false;
            }
        }
        return  choosenSystems;
    }
    private void printConstituencies(List<Constituency> constituencies) {
        System.out.println("Aktuální vaše volební obvody jsou:");
        for (var constituency : constituencies) {
            System.out.println("   " + constituency.getId()+") " + constituency.getName());
        }
    }
    private List<Constituency> processConstituenciesMerge(List<Constituency> constituencies) {
        List<Constituency> mergedConstituencies = constituencies;
        boolean mergeAnother = true;
        do {
            printConstituencies(mergedConstituencies);
            System.out.println("Pokud chcete změnit uvažované volební obvody zadejte čísla, oddělená mezerou, označující obvody, které chcete sloučit:");
            Scanner scanner = new Scanner(System.in);
            List<Integer> idsToMerge = new ArrayList<>();
            try {
                while (scanner.hasNextInt()) {
                    idsToMerge.add(scanner.nextInt());
                }
            } catch (Exception e) {
                mergeAnother = false;
                continue;
            }

            List<Constituency> toMerge = new ArrayList<>();
            for (int i = 0; i < mergedConstituencies.size(); ) {
                if (idsToMerge.contains(mergedConstituencies.get(i).getId())) {
                    toMerge.add(mergedConstituencies.get(i));
                    mergedConstituencies.remove(i);
                } else {
                    ++i;
                }
            }
            mergedConstituencies.add(ConstituencyFactory.mergeConstituencies(toMerge));

        } while(mergeAnother);
        return mergedConstituencies;
    }

    public SystemConfiguration GetConfigurationFromUser() throws ParsingException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte název vstupního souboru (obsahujícího výsledky voleb):");
        String filename = scanner.nextLine();
        List<Constituency> constituencies = ConstituencyFactory.loadConsituencies(filename);
        constituencies = processConstituenciesMerge(constituencies);
        List<ElectionSystem> choosenSystems = loadSystems();

        return new SystemConfiguration(choosenSystems, constituencies);
    }
}
