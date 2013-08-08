package com.compomics.software.autoupdater;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * MetaDataXMLParser.
 *
 * @author Davy Maddelein
 */
public class MetaDataXMLParser {

    /**
     * The highest version number.
     */
    private String highestVersionNumber;
    /**
     * The XML event.
     */
    private XMLEvent XMLEvent;

    /**
     * Create a new MetaDataXMLParser.
     * 
     * @param xmlReader the XML reader
     * @throws XMLStreamException 
     */
    public MetaDataXMLParser(XMLEventReader xmlReader) throws XMLStreamException {
        while (xmlReader.hasNext()) {
            XMLEvent = xmlReader.nextEvent();
            if (XMLEvent.isStartElement()) {
                if (XMLEvent.asStartElement().getName().getLocalPart().equalsIgnoreCase("versions")) {
                    parseVersionNumbers(xmlReader);
                    break;
                }
            }
        }
    }

    /**
     * Returns the highest version number.
     * 
     * @return the highest version number
     */
    public String getHighestVersionNumber() {
        return highestVersionNumber;
    }

    /**
     * Parses the version numbers of a maven repository website (or just about
     * any proper xml containing the tag version).
     *
     * @param xmlReader
     * @throws XMLStreamException
     */
    private void parseVersionNumbers(XMLEventReader xmlReader) throws XMLStreamException {
        CompareVersionNumbers versionNumberComparator = new CompareVersionNumbers();
        while (xmlReader.hasNext()) {
            XMLEvent = xmlReader.nextEvent();
            if (XMLEvent.isStartElement()) {
                if (XMLEvent.asStartElement().getName().getLocalPart().equalsIgnoreCase("version")) {
                    if (highestVersionNumber == null) {
                        highestVersionNumber = xmlReader.nextEvent().asCharacters().getData();
                    } else {
                        String versionNumberToCompareWith = xmlReader.nextEvent().asCharacters().getData();
                        if (versionNumberComparator.compare(highestVersionNumber, versionNumberToCompareWith) == 1) {
                            highestVersionNumber = versionNumberToCompareWith;
                        }
                    }
                }
            } else if (XMLEvent.isEndElement()) {
                if (XMLEvent.asEndElement().getName().getLocalPart().equalsIgnoreCase("versions")) {
                    break;
                }
            }
        }
    }
}