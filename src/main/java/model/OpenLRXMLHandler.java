package model;

import model.location.CoordinateValue;
import model.traffic.TrafficIncident;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.binary.data.RawBinaryData;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 03.05.2017.
 */
public class OpenLRXMLHandler extends OpenLRFileHandler{

    private List<TrafficIncident> incidents;
    private final OpenLRBinaryDecoder bDecoder;
    XMLInputFactory factory;
    XMLEventReader eventReader;

    private final QName ID = new QName("id");

    public OpenLRXMLHandler(File file){
        super(file);
        bDecoder = new OpenLRBinaryDecoder();
        this.incidents = new ArrayList<TrafficIncident>();
        factory = XMLInputFactory.newInstance();
    }

    public List<TrafficIncident> getIncidents() {
        return incidents;
    }

    @Override
    public void process(){
        boolean rawBinary =false;
        TrafficIncident incident = null;
        boolean creationTime = false;
        boolean trafficType = false;
        boolean averageSpeed = false;
        boolean delayTime = false;
        if(file != null){
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                eventReader = factory.createXMLEventReader(reader);
                while(eventReader.hasNext()){
                    XMLEvent event = eventReader.nextEvent();
                    switch(event.getEventType()){
                        case XMLStreamConstants.START_ELEMENT:
                            StartElement startElement = event.asStartElement();
                            String qName = startElement.getName().getLocalPart();
                            if(qName.equals("binary")){
                                rawBinary = true;
                            }else if(qName.equals("situation")){
                                Attribute id = startElement.getAttributeByName(ID);
                                if(id != null)
                                    incident = new TrafficIncident(id.getValue());
                            }else if(qName.equals("situationRecordCreationTime")){
                                creationTime = true;
                            }else if(qName.equals("abnormalTrafficType")){
                                trafficType = true;
                            }else if(qName.equals("averageSpeed")){
                                averageSpeed = true;
                            }else if(qName.equals("delayTimeValue")){
                                delayTime = true;
                            }

                            break;
                        case XMLStreamConstants.CHARACTERS:
                            Characters characters = event.asCharacters();
                            if(rawBinary){
                                ByteArray bytes = new ByteArray(characters.getData());
                                try {
                                    RawBinaryData raw = bDecoder.resolveBinaryData("", bytes);
                                    if(incident != null) {
                                        incident.setRawData(raw);
                                        incident.setRawAsString(characters.getData());
                                    }
                                }catch(PhysicalFormatException pe){
                                    System.err.println("Decoding problem occurred");
                                }
                                rawBinary = false;
                            }else if(creationTime){
                                if(incident != null) {
                                    incident.setCreationTime(characters.getData());
                                }
                                creationTime = false;
                            }else if(trafficType){
                                if(incident != null) {
                                    incident.setTrafficType(characters.getData());
                                }
                                trafficType = false;
                            }else if(averageSpeed){
                                if(incident != null) {
                                    incident.setAverageSpeed(characters.getData());
                                }
                                averageSpeed = false;
                            }else if(delayTime){
                                if(incident != null) {
                                    incident.setDelayTime(characters.getData());
                                }
                                delayTime = false;
                            }
                            break;
                        case XMLStreamConstants.END_ELEMENT:
                            EndElement endElement = event.asEndElement();
                            String end = endElement.getName().getLocalPart();
                            if(end.equals("situation")){
                                //TODO remove later
                                if(rawWithin(incident.getRawData()))
                                    incidents.add(incident);
                            }
                            break;
                    }
                }
            } catch (FileNotFoundException fe) {
                System.err.println("File not found");
            } catch (IOException ie) {
                System.err.println("IO Error");
            } catch(XMLStreamException se){
                System.err.println("Error while processing XML document");
            }finally {
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException ie) {
                }
            }
        }
    }
}
