package model;

import openlr.map.InvalidMapDataException;
import org.jxmapviewer.viewer.GeoPosition;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 05.06.2017.
 */
public class RouteXMLHandler{

    XMLInputFactory factory;
    XMLEventReader eventReader;
    private List<GeoPosition> coordinates;
    protected Reader reader = null;
    private final String data;

    private final QName LONGITUDE = new QName("longitude");
    private final QName LATITUDE = new QName("latitude");

    public RouteXMLHandler(String data){
        this.data = data;
        factory = XMLInputFactory.newInstance();
        coordinates = new ArrayList<GeoPosition>();
    }

    public List<GeoPosition> getCoordinates(){
        return coordinates;
    }

    public void process(){
        reader = new StringReader(data);
        try {
            eventReader = factory.createXMLEventReader(reader);
            while(eventReader.hasNext()){
                XMLEvent event = eventReader.nextEvent();
                switch(event.getEventType()){
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if(qName.equals("point")){
                            Attribute lon = startElement.getAttributeByName(LONGITUDE);
                            Attribute lat = startElement.getAttributeByName(LATITUDE);
                            if(lon != null && lat != null){
                                coordinates.add(new GeoPosition(Double.parseDouble(lat.getValue()),
                                        Double.parseDouble(lon.getValue())));
                            }
                        }
                        break;
                }
            }
        }catch(XMLStreamException xe){
            System.err.println("Eror while loading routing data...");
        }
    }
}
