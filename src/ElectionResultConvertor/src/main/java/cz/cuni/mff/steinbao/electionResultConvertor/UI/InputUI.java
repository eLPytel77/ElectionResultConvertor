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

/**
 * User interface for reading election configuration from the console.
 * <p>
 * This class prompts the user for input file name, constituency selection,
 * and election system choices.
 */
public class InputUI {
    private final List<Class<? extends ElectionSystem>> availableSystems;

    /**
     * Creates a new input UI with available election system types.
     *
     * @param electionSystems list of election system classes that may be selected by the user
     */
    public InputUI(List<Class<? extends ElectionSystem>> electionSystems) {
        this.availableSystems = electionSystems;
    }

    /**
     * Prints the list of election systems available for selection.
     */
    private void printSystems() {
        System.out.println("Volební systémy, které je možno vybrat jsou:");
        int index = 0;
        for(var systemClass : availableSystems) {
            System.out.println(index+") " + systemClass.getSimpleName());
            ++index;
        }
        System.out.println();
    }

    /**
     * Reads the threshold percentage from the user input.
     *
     * @return threshold percentage as a double, or 0 on invalid input
     */
    private double loadThreshold() {
        System.out.println("Zadejte uzavírací klauzuli jako nezáporné číslo: ");
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextDouble();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Reads the total number of mandates from the user input.
     *
     * @return number of mandates as an integer, or 0 on invalid input
     */
    private int loadMandatesCount() {
        System.out.println("Zadejte počet mandátů jako kladné celé číslo: ");
        Scanner scanner = new Scanner(System.in);
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Prompts the user to select one or more election systems.
     *
     * @return list of chosen election system instances
     */
    private List<ElectionSystem> loadSystems() {
        boolean chooseAnother = true;
        printSystems();
        List<ElectionSystem> choosenSystems = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Zadejte pořadní číslo některého z vypsaných systémů: ");
        String input = scanner.nextLine().trim();
        try {
            int systemIndex = Integer.parseInt(input);
            ElectionSystem system = (ElectionSystem) availableSystems.get(systemIndex).getConstructors()[0].newInstance(loadThreshold(), loadMandatesCount());
            choosenSystems.add(system);
        } catch (Exception e) {
            chooseAnother = false;
        }

        while (chooseAnother) {
            System.out.println("Pokud chcete vybrat další, zadejte znovu jeho pořadní číslo, jinak stiskněte Enter: ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                chooseAnother = false;
                continue;
            }
            try {
                int systemIndex = Integer.parseInt(input);
                ElectionSystem system = (ElectionSystem) availableSystems.get(systemIndex).getConstructors()[0].newInstance(loadThreshold(), loadMandatesCount());
                choosenSystems.add(system);
            } catch (Exception e) {
                System.out.println("Neplatný vstup. Zadejte pořadní číslo systému nebo stiskněte Enter pro dokončení.");
            }
        }
        return  choosenSystems;
    }

    /**
     * Prints a list of available constituencies.
     *
     * @param constituencies constituencies to display
     */
    private void printConstituencies(List<Constituency> constituencies) {
        System.out.println("Aktuální vaše volební obvody jsou:");
        for (var constituency : constituencies) {
            System.out.println("   " + constituency.getId()+") " + constituency.getName());
        }
    }

    /**
     * Lets the user merge selected constituencies into a combined constituency.
     *
     * @param constituencies the current list of constituencies
     * @return merged constituency list after user selection
     */
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

    /**
     * Prompts the user for the input file name and election settings.
     *
     * @return configuration containing selected systems, constituencies, and party names
     * @throws ParsingException when the input file format is invalid or cannot be parsed
     */
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
