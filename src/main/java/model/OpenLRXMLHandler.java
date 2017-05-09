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
public class OpenLRXMLHandler {

    private final File file;
    private List<RawBinaryData> data;
    private List<TrafficIncident> incidents;
    private final OpenLRBinaryDecoder bDecoder;
    XMLInputFactory factory;
    XMLEventReader eventReader;

    private final double VERONA_NW_LAT = 45.467219;
    private final double VERONA_NW_LON = 10.969248;
    private final double VERONA_SE_LAT = 45.446207;
    private final double VERONA_SE_LON = 10.995855;
    private final double VERONA_NE_LAT = 45.463246;
    private final double VERONA_NE_LON = 11.006842;
    private final double VERONA_SW_LAT = 45.448255;
    private final double VERONA_SW_LON = 10.968218;

    private final QName ID = new QName("id");

    public OpenLRXMLHandler(File file){

        this.file = file;
        bDecoder = new OpenLRBinaryDecoder();
        this.data = new ArrayList<RawBinaryData>();
        this.incidents = new ArrayList<TrafficIncident>();
        factory = XMLInputFactory.newInstance();
    }

    public List<RawBinaryData> getLocationData(){
        return data;
    }

    public List<TrafficIncident> getIncidents() {
        return incidents;
    }

    public void process(){
        boolean rawBinary =false;
        TrafficIncident incident = null;
        TrafficIncident holland = new TrafficIncident("Hi");
        ByteArray b = new ByteArray("Cwd26yEAdCOacgBO9vojleEDouEDI4leAtb50yMY");
        try {
            RawBinaryData test = bDecoder.resolveBinaryData("", b);
            holland.setRawData(test);
        }catch(Exception e){

        }
        incidents.add(holland);
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

    private boolean rawWithin(RawBinaryData data){
        return true;
        //CoordinateValue val = new CoordinateValue(data.getBinaryFirstLRP().getLon(), data.getBinaryFirstLRP().getLat());
        //return isWithin(val.getLatDeg(), val.getLonDeg());
    }

    private boolean isWithin(double lat, double lon){
        return lat >= VERONA_SW_LAT &&
                lat <= VERONA_NE_LAT &&
                lon >= VERONA_SW_LON &&
                lon <= VERONA_NE_LON;
    }

}
