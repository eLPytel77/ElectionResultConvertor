package cz.cuni.mff.steinbao.electionResultConvertor.UI;

import cz.cuni.mff.steinbao.electionResultConvertor.Convertor.ElectionSystem;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.ConstituencyFactory;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.SystemConfiguration;
import cz.cuni.mff.steinbao.electionResultConvertor.Exceptions.ParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class InputUI {
    private final List<Class<? extends ElectionSystem>> availableSystems;
    public InputUI(List<Class<? extends ElectionSystem>> electionSystems) {
        this.availableSystems = electionSystems;
    }

    private void printSystems() {
        System.out.println("Volební systémy, které je možno vybrat jsou:");
        int index = 0;
        for(var systemClass : availableSystems) {
            System.out.println(index+") " + systemClass.getName());
            ++index;
        }
        System.out.println();
    }
    private double loadThreshold() {
        System.out.println("Zadejte uzavírací klauzuli jako nezáporné číslo: ");
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            return 0;
        }
    }
    private int loadMandatesCount() {
        System.out.println("Zadejte počet mandátů jako kladné celé číslo: ");
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextInt();
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
        try {
            ElectionSystem system = (ElectionSystem) availableSystems.get(systemIndex).getConstructors()[0].newInstance(loadThreshold(), loadMandatesCount());
            choosenSystems.add(system);
        } catch (Exception e) {}

        while (chooseAnother) {
            System.out.println("Pokud chcete vybrat další, zadejte znovu jeho pořadní číslo, jinak zadejte nečíselný znak: ");
            try {
                systemIndex = scanner.nextInt();
                ElectionSystem system = (ElectionSystem) availableSystems.get(systemIndex).getConstructors()[0].newInstance(loadThreshold(), loadMandatesCount());
                choosenSystems.add(system);
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
            System.out.println("Pokud chcete změnit uvažované volební obvody zadejte čísla, oddělená mezerou, označující obvody, které chcete sloučit (nebo stiskněte Enter pro dokončení):");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                mergeAnother = false;
                continue;
            }
            String[] parts = line.split("\\s+");
            List<Integer> idsToMerge = new ArrayList<>();
            boolean validInput = true;
            for (String part : parts) {
                try {
                    idsToMerge.add(Integer.parseInt(part));
                } catch (NumberFormatException e) {
                    validInput = false;
                    break;
                }
            }
            if (!validInput) {
                System.out.println("Neplatný vstup. Zadejte pouze čísla oddělená mezerami nebo stiskněte Enter pro dokončení.");
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
            if (toMerge.size() > 1) {
                mergedConstituencies.add(ConstituencyFactory.mergeConstituencies(toMerge));
            } else if (toMerge.size() == 1) {
                mergedConstituencies.addAll(toMerge);
            }

        } while(mergeAnother);
        return mergedConstituencies;
    }

    public SystemConfiguration GetConfigurationFromUser() throws ParsingException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte název vstupního souboru (obsahujícího výsledky voleb):");
        String filename = scanner.nextLine();
        var loadedData = ConstituencyFactory.loadConsituencies(filename);
        List<Constituency> constituencies = loadedData.customizedConstituencies();
        constituencies = processConstituenciesMerge(constituencies);
        List<ElectionSystem> choosenSystems = loadSystems();

        return new SystemConfiguration(choosenSystems, constituencies, loadedData.partyNames());
    }
}
