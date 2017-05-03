package model;

import openlr.PhysicalFormatException;
import openlr.binary.ByteArray;
import openlr.binary.OpenLRBinaryDecoder;
import openlr.binary.data.RawBinaryData;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian Noack on 03.05.2017.
 */
public class OpenLRXMLHandler {

    private final File file;
    private List<RawBinaryData> data;
    private final OpenLRBinaryDecoder bDecoder;
    XMLInputFactory factory;
    XMLEventReader eventReader;

    public OpenLRXMLHandler(File file){

        this.file = file;
        bDecoder = new OpenLRBinaryDecoder();
        this.data = new ArrayList<RawBinaryData>();
        factory = XMLInputFactory.newInstance();
    }

    public List<RawBinaryData> getLocationData(){
        return data;
    }

    public void process(){
        boolean rawBinary =false;

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
                            }
                            break;
                        case XMLStreamConstants.CHARACTERS:
                            Characters characters = event.asCharacters();
                            if(rawBinary == true){
                                System.out.println(characters.getData());
                                ByteArray bytes = new ByteArray(characters.getData());
                                try {
                                    data.add(bDecoder.resolveBinaryData("", bytes));
                                }catch(PhysicalFormatException pe){
                                    System.err.println("Decoding problem occurred");
                                }
                                rawBinary = false;
                            }
                            break;
                        case XMLStreamConstants.END_ELEMENT:
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
