
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class XmlToJson extends DefaultHandler {
    private static final String CLASS_NAME = XmlToJson.class.getName();
    private final static Logger LOG = Logger.getLogger(CLASS_NAME);

    private SAXParser parser = null;
    private SAXParserFactory spf;

    private double totalSales;
    private boolean inSales;
    private boolean inId;
    private boolean inRecordSale;
    private boolean inFirstName;
    private boolean inLastName;
    private boolean inState;
    private boolean inDepartament;
    private String currElement;
    public XmlToJson() {
        super();
        spf = SAXParserFactory.newInstance();
        // verificar espacios de nombre
        spf.setNamespaceAware(true);
        // validar que el documento este bien formado (well formed)
        spf.setValidating(true);
    }

    private void process(File file) {
        try {
            // obtener un parser para verificar el documento
            parser = spf.newSAXParser();
            LOG.info("Parser object is: " + parser);
        } catch (SAXException | ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nStarting parsing of " + file + "\n");
        try {
            // iniciar analisis del documento
            parser.parse(file, this);
        } catch (IOException | SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public void startDocument() throws SAXException {
        // al inicio del documento inicializar
        // las ventas totales
        totalSales = 0.0;
        System.out.printf("[");
    }

    @Override
    public void endDocument() throws SAXException {
        // Se proceso todo el documento, imprimir resultado
        System.out.printf("]");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        switch (localName){
            case "sale_record":
          inRecordSale=true;
                System.out.println("{");
                break;
            case "id":
                inId=true;
                System.out.print("\"id\":");
                break;
            case "first_name":
                inFirstName=true;
                System.out.print("\"firstName\":");
                break;
            case "last_name":
               inLastName=true;
                System.out.print("\"lastname\":");
                break;
            case "sales":
                inSales=true;
                System.out.print("\"sales\":");
                break;
            case "state":
                inState=true;
                System.out.print("\"state\":");
                break;
            case "department":
                inDepartament=true;
                System.out.print("\"department\":");
                break;
        }
        currElement=localName;
    }

    @Override
    public void characters(char[] bytes, int start, int length) throws SAXException {
      String data=new String(bytes,start,length);
        switch (currElement){
            case "sale_record":
                //inRecordSale=false;
                //System.out.printf("{");
                break;
            case "id":
                inId=true;
                System.out.printf("\"%s\",%n",data);
                //System.out.printf("\"id\":");
                break;
            case "first_name":
                inFirstName=false;
                System.out.printf("\"%s\",%n",data);
                //System.out.printf("\"firstName\":");
                break;
            case "last_name":
                inLastName=false;
                System.out.printf("\"%s\",%n",data);
                //System.out.printf("\"lastname\":");
                break;
            case "sales":
                inSales=false;
                System.out.printf("\"%s\",%n",data);
                //System.out.printf("\"sales\":");
                break;
            case "state":
                inState=false;
                System.out.printf("\"%s\",%n",data);
                //System.out.printf("\"state\":");
                break;
            case "department":
                inDepartament=false;
                System.out.printf("\"%s\",%n",data);
                //System.out.printf("\"department\":");
                break;

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if( localName.equals("sale_record") )        {
            System.out.printf("}");
            inSales = false;
        }
    }


    public static void main(String args[]) {
        if (args.length == 0) {
            LOG.severe("No file to process. Usage is:" + "\njava DisplayXML <filename>");
            return;
        }
        File xmlFile = new File(args[0]);
        XmlToJson handler = new XmlToJson();
        handler.process(xmlFile);
    }}
