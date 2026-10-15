package cz.miscik.trixi_task_spring.parser;
import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.entities.CastObce;

import java.util.ArrayList;

/**
 * ParsedData class holds the parsed data from the CSV files.
 * It contains two lists: one for villages (Obec) and another for parts of villages (CastObce).
 */
public class ParsedData {
    private final ArrayList<Obec> villages;
    private final ArrayList<CastObce> partsOfVillages;

    public ParsedData(ArrayList<Obec> villages,
                      ArrayList<CastObce> partsOfVillages) {
        this.villages = villages;
        this.partsOfVillages = partsOfVillages;
    }

    public ArrayList<Obec> getVillages() {
        return villages;
    }

    public ArrayList<CastObce> getPartsOfVillages() {
        return partsOfVillages;
    }
}