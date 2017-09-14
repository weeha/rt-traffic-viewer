package model;

import model.traffic.TrafficFlow;
import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.data.RawBinaryData;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 29.05.2017.
 */
public class OpenLRXMLFlowHandler extends FlowHandler{

    XMLInputFactory factory;
    XMLEventReader eventReader;
    private final QName ID = new QName("id");

    public OpenLRXMLFlowHandler(){
        super();
        factory = XMLInputFactory.newInstance();
    }

    public List<TrafficFlow> getFlows(){
        return flows;
    }

    @Override
    public void process(){
        if(reader != null){
            TrafficFlow flow = null;
            boolean rawBinary =false;
            boolean averageSpeed = false;
            boolean relativeSpeed = false;
            boolean trafficCondition = false;
            boolean travelTime = false;
            boolean aStops = false;
            try{
                eventReader = factory.createXMLEventReader(reader);
                while(eventReader.hasNext()){
                    XMLEvent event = eventReader.nextEvent();
                    switch(event.getEventType()){
                        case XMLStreamConstants.START_ELEMENT:
                            StartElement startElement = event.asStartElement();
                            String qName = startElement.getName().getLocalPart();
                            if(qName.equals("binary")){
                                rawBinary = true;
                            }else if(qName.equals("elaboratedData")){
                                flow = new TrafficFlow();
                            }else if(qName.equals("relativeSpeed")){
                                relativeSpeed = true;
                            }else if(qName.equals("trafficCondition")){
                                trafficCondition = true;
                            }else if(qName.equals("averageSpeed")){
                                averageSpeed = true;
                            }else if(qName.equals("travelTime")){
                                travelTime = true;
                            }else if(qName.equals("averageNumberOfStops")){
                                aStops = true;
                            }
                            break;
                        case XMLStreamConstants.CHARACTERS:
                            Characters characters = event.asCharacters();
                            if(rawBinary){
                                ByteArray bytes = new ByteArray(characters.getData());
                                try {
                                    RawBinaryData raw = bDecoder.resolveBinaryData("", bytes);
                                    if(flow != null) {
                                        flow.setRawData(raw);
                                        flow.setRawAsString(characters.getData());
                                    }
                                }catch(PhysicalFormatException pe){
                                    System.err.println("Decoding problem occurred");
                                }
                                rawBinary = false;
                            }else if(travelTime){
                                if(flow != null) {
                                    flow.setTravelTime(characters.getData());
                                }
                                travelTime = false;
                            }else if(averageSpeed){
                                if(flow != null) {
                                    flow.setAverageSpeed(characters.getData());
                                }
                                averageSpeed = false;
                            }else if(relativeSpeed){
                                if(flow != null) {
                                    flow.setRelativeSpeed(characters.getData());
                                }
                                relativeSpeed = false;
                            }else if(trafficCondition){
                                if(flow != null) {
                                    flow.setTrafficCondition(characters.getData());
                                }
                                trafficCondition = false;
                            }else if(aStops){
                                if(flow != null) {
                                    flow.setAverageStops(characters.getData());
                                }
                                aStops = false;
                            }
                            break;
                        case XMLStreamConstants.END_ELEMENT:
                            EndElement endElement = event.asEndElement();
                            String end = endElement.getName().getLocalPart();
                            if(end.equals("elaboratedData")){
                                //TODO remove later
                                if(rawWithin(flow.getRawData()))
                                    flows.add(flow);
                            }
                            break;
                    }
                }
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
