package cz.miscik.parser;
import cz.miscik.entities.CastObce;
import cz.miscik.entities.Obec;

import java.util.ArrayList;

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