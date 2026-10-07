package cz.miscik.trixi_task_spring.parser;
import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.entities.CastObce;

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