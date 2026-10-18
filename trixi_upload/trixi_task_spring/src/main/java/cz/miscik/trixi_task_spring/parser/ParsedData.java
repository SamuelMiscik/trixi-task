package cz.miscik.trixi_task_spring.parser;
import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.entities.CastObce;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ParsedData record class holds the parsed data from the XML file.
 * It contains two lists: one for villages (obce) and another for parts of villages (castiObce).
 */
public record ParsedData(
        Map<Integer, Obec> obce,
        List<CastObce> castiObce
) {}