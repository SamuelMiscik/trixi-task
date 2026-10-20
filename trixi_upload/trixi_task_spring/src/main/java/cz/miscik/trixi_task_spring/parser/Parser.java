package cz.miscik.trixi_task_spring.parser;

import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.entities.CastObce;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Constants for XML stream events
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;

import static java.lang.Integer.parseInt;

/**
 * Parses XML data into Obec and CastObce entities.
 *
 * <p>The parser uses StAX and {@link XMLStreamReader}, so the XML is processed
 * sequentially without loading the whole document into memory.</p>
 *
 */
public class Parser {

    private final XMLInputFactory xmlInputFactory;

    // Controversial, but this "namespace" should not be changed as frequently as "vf" in the XML file, so it is better to use this constant instead of hardcoding it in the code
    private static final String VF_NAMESPACE = "urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1";
    private static final String OBEC_NAMESPACE = "urn:cz:isvs:ruian:schemas:ObecIntTypy:v1";
    private static final String CAST_OBCE_NAMESPACE = "urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1";

    /**
     * Constructor: Creates a new parser with a default {@link XMLInputFactory}.
     */
    public Parser() {
        this.xmlInputFactory = XMLInputFactory.newInstance();
    }

    /**
     * Parses the supplied XML stream and returns extracted villages
     * and their parts.
     *
     * @param inputStream XML input stream to parse
     * @return parsed Obec and CastObce data
     * @throws XMLStreamException if the XML cannot be parsed or required data is missing
     */
    public ParsedData parse(InputStream inputStream) throws XMLStreamException {

        // Initialize lists to hold parsed data
        Obec obec = null;
        CastObce castObce = null;
        Map<Integer, Obec> obce = new HashMap<>();
        List<CastObce> castiObce = new ArrayList<>();

        XMLStreamReader reader = null;

        try {
            reader = xmlInputFactory.createXMLStreamReader(inputStream);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == START_ELEMENT && VF_NAMESPACE.equals(reader.getNamespaceURI())) { // if its starting element + starts with vf namespace -> important because without it we will get wrong data

                    switch (reader.getLocalName()) {
                        case "Obec":
                            obec = partParse(reader); // This object will last for the duration of the parts of village being parsed so they can use it
                            obce.put(obec.getKod(), obec);
                            break;

                        case "CastObce":
                            castObce = partParse(reader, obce);
                            castiObce.add(castObce);
                            break;
                    }
                }
            }
        } finally { // try + finally here so the reader is closed even if an exception occurs during parsing
            if (reader != null) {
                reader.close();
            }
        }

        return new ParsedData(obce, castiObce);
    }

    /**
     * Parses a single Obec element.
     *
     * <p>The method expects the reader to be positioned on the opening
     * {@code vf:Obec} element and consumes events until its closing element.</p>
     *
     * @param reader XML stream reader positioned at an Obec element
     * @return parsed Obec entity
     * @throws XMLStreamException if required values such as Kod or Nazev are missing
     */
    private Obec partParse (XMLStreamReader reader) throws XMLStreamException {
        Integer kod = null;
        String name = null;

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == START_ELEMENT && OBEC_NAMESPACE.equals(reader.getNamespaceURI())) {
                if ("Kod".equals(reader.getLocalName())) {
                    kod = parseInt(reader.getElementText().trim());
                } else if ("Nazev".equals(reader.getLocalName())) {
                    name = reader.getElementText().trim();
                }
            } else if (event == END_ELEMENT && "Obec".equals(reader.getLocalName()) && VF_NAMESPACE.equals(reader.getNamespaceURI())) {
                break; // Exit the loop when the end of the Obec element is reached
            }
        }

        // Validate that both kod and name are not null before creating the Obec object
        if (kod == null || name == null) {
            throw new XMLStreamException("Obec is missing required Kod or Nazev");
        }

        Obec obec = new Obec();
        obec.setKod(kod);
        obec.setName(name);

        return obec;
    }

    /**
     * Parses a single CastObce element and links it to its parent Obec.
     *
     * <p>The parent village is identified by its code from the XML
     * and looked up in the supplied map.</p>
     *
     * @param reader XML stream reader positioned at a CastObce element
     * @param obce parsed Obec entities indexed by their code
     * @return parsed CastObce entity linked to its parent Obec
     * @throws XMLStreamException if required values are missing or the parent Obec cannot be found
     */
    private CastObce partParse (XMLStreamReader reader, Map<Integer, Obec> obce) throws XMLStreamException {
        Integer kod = null;
        String name = null;
        Integer obecKod = null;

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == START_ELEMENT) {

                if (CAST_OBCE_NAMESPACE.equals(reader.getNamespaceURI())) {

                    if ("Kod".equals(reader.getLocalName())) {
                        kod = parseInt(reader.getElementText().trim());

                    } else if ("Nazev".equals(reader.getLocalName())) {
                        name = reader.getElementText().trim();
                    }

                } else if (OBEC_NAMESPACE.equals(reader.getNamespaceURI()) && "Kod".equals(reader.getLocalName())) {
                    obecKod = parseInt(reader.getElementText().trim());
                }

            }  else if (event == END_ELEMENT && VF_NAMESPACE.equals(reader.getNamespaceURI()) && "CastObce".equals(reader.getLocalName())) {
                break; // Exit the loop when the end of the Obec element is reached
            }
        }

        // Validate that both kod and name are not null before creating the Obec object
        if (kod == null || name == null || obecKod == null) {
            throw new XMLStreamException("CastObce is missing required Kod or Nazev or ObecKod");
        }

        CastObce castObce = new CastObce();
        castObce.setKod(kod);
        castObce.setName(name);
        Obec obec = obce.get(obecKod);

        // Validate that the parent Obec exists for the given obecKod
        if (obec == null) {
            throw new XMLStreamException(
                    "Parent Obec with kod " + obecKod + " was not found"
            );
        }

        castObce.setObec(obec); // Set the parent Obec for this CastObce

        return castObce;
    }
}