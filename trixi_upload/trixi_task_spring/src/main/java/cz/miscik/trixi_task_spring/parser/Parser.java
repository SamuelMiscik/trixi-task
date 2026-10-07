package cz.miscik.trixi_task_spring.parser;

import cz.miscik.trixi_task_spring.entities.Obec;
import cz.miscik.trixi_task_spring.entities.CastObce;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Parser {

    private final XMLInputFactory xmlInputFactory;

    public Parser() {
        this.xmlInputFactory = XMLInputFactory.newInstance();
    }

    public ParsedData parse(InputStream inputStream) throws XMLStreamException {

        // Initialize variables to know where in XML we are
        boolean isVillage = false;
        boolean isPartVillage = false;
        boolean firstCode = false;

        // Initialize lists to hold parsed data
        Obec village = null;
        CastObce partOfVillage = null;
        ArrayList<CastObce> partsOfVillages = new ArrayList<>();
        ArrayList<Obec> Villages = new ArrayList<>();


        XMLEventReader reader =
                xmlInputFactory.createXMLEventReader(inputStream);

        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                // Check the name of the start element and set flags accordingly
                if (startElement.getName().getLocalPart().equals("Obec") && !isPartVillage && startElement.getName().getPrefix().equals("vf")) {
                    village = new Obec();
                    isVillage = true;
                    firstCode = true;
                } else if (startElement.getName().getLocalPart().equals("CastObce") && startElement.getName().getPrefix().equals("vf")) {
                    partOfVillage = new CastObce();
                    isPartVillage = true;
                    firstCode = true;
                }
                else if (startElement.getName().getLocalPart().equals("Kod") && firstCode) {
                    if (isPartVillage) {
                        nextEvent = reader.nextEvent();
                        partOfVillage.setKod(parseInt(nextEvent.asCharacters().getData()));
                    }else if (isVillage) {
                        nextEvent = reader.nextEvent();
                        village.setKod(parseInt(nextEvent.asCharacters().getData()));
                    }
                    firstCode = false;
                } else if (startElement.getName().getLocalPart().equals("Nazev")) {
                    nextEvent = reader.nextEvent();
                    if (isVillage) {
                        village.setName(nextEvent.asCharacters().getData());
                        Villages.add(village);
                    } else if (isPartVillage) {
                        partOfVillage.setName(nextEvent.asCharacters().getData());
                    }
                }
            }

            if (nextEvent.isEndElement()) {
                if (nextEvent.asEndElement().getName().getLocalPart().equals("Obec") && isVillage && nextEvent.asEndElement().getName().getPrefix().equals("vf")) {
                    isVillage = false;
                }

                if (nextEvent.asEndElement().getName().getLocalPart().equals("CastObce") && isPartVillage && nextEvent.asEndElement().getName().getPrefix().equals("vf")) {
                    partOfVillage.setObec(village);
                    partsOfVillages.add(partOfVillage);
                    isPartVillage = false;
                }
            }


        }

        reader.close();

        return new ParsedData(Villages, partsOfVillages);
    }
}