package cz.cuni.mff.steinbao.electionResultConvertor.Convertor;

import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.Constituency;
import cz.cuni.mff.steinbao.electionResultConvertor.DataTypes.MandateResult;

import org.reflections.Reflections;

import java.util.List;

public abstract class ElectionSystem {
    protected final double threshold;
    protected final int mandatesCount;


    public static List<Class<? extends ElectionSystem>> getAllSystemsClass() {
        Reflections reflections = new Reflections("cz.cuni.mff.steinbao.electionResultConvertor.Convertor");
        return reflections.getSubTypesOf(ElectionSystem.class).stream().toList();
    }

    public abstract MandateResult countMandates(List<Constituency> electionResults);

    public ElectionSystem(double threshold, int mandatesCount) {
        this.threshold = threshold;
        this.mandatesCount = mandatesCount;
    }

}
/*

implementovat tyto systémy:

Hareova kvóta
Hagenbach-Bischoffova kvóta
Imperiali kvóta

dHondt dělitel
Saint-Lagueh dělitel
Imperiali dělitel

u dělitelů jedno skrutinium
u kvót možné druhé (když zbydou mandáty), ve 2. skrutiniu dHondt

 */


/*

Listinné volební systémy

Hlavní proměnné:
- velikost volebních obvodů
    - pevně stanovený počet mandátů pro obvod x plovoucí
- volební formule
    - kvóty (Q - kvóta, V - hlasy, S - mandáty) - kolikrát strana dosáhne kvóty, tolik dostane mandátů
        - Hareova
            - Q = V / S
        - Hagenbach-Bischoffova
            - Q = V / (S + 1)
        - Imperiali
            - Q = V / (S + 2), v posílené verzi Q = V / (S + 3)
        - Hareova-Neimeyerova formule - (v - zisk hlasů konkrétní strany, s - počet mandátů konkrétní strany)
            - s = v * (S / V)
        - Přidělování zbylých mandátů:
            - metoda největších zbytků - zbylé mandáty přpadnou postupně subjektům které mají největší absolutní počet nevyužitých hlasů
            - metoda největších průměrů - zbylé mandáty získají postupně subjekty, které vykážou nejvěší průměrný počet hlasů na mandát o jehož přidělení se právě jedná
            - další skrutinium
    - dělitelé
        - d'Hondtův
            - zisky hlasů jednotlivých subjektů se dělí posloupností čísel 1, 2, 3, ...
            - mandát vždy získá subjekt s největším podílem (aka z tabulky podílů vytáhnu V největších čísel)
        - Sainte-Lagueho
            - posloupnost lichých čísel
        - modifikovaný sainte-Lagueho
            - posloupnost 1, 4, 3, 5, 7, 9, ...
        - Huntingtonův
            - (n^(1/2))*(n-1) - n počet mandátů
        - Imperiali
            - posloupnost 2, 3, 4, ...
        - český d'Hondt
            - posloupnost 1.42, 2, 3, 4, ...
        - dánský dělitel
            - 1, 4, 7, 10
- uzavírací klauzule
    - na úrovni celého státu x na úrovní obvodů
    - alternativou volební kvórům - jasně definovaný absolutní počet hlasů
    - aditivní klauzule pro koalice
- počet a charakter úrovní distribuce mandátů
    - 2. skrutinium
        - systém zbytkových mandátů ze zbytkových hlasů - nerozdělené mandáty a hlasy z 1. skrutinia se dají dohromady
        - systém kompenzačních mandátů
            - počet kompenzačních mandátů dán ze zákona
            - nepotřebuji aby v 1. skrutiniu zbyly mandáty
            - na základě všech hlasů je vypočítáno ideální rozdělení mandátů
            - od tohoto rozdělení se odečítají mandáty získané v základních volebních obvodech






 */