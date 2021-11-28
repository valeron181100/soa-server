package xml;

import model.Vehicle;
import model.Vehicles;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class XMLConverter {

    public static String convert(Vehicle product) {

        StringWriter writer = new StringWriter();

        if (product != null) {

            try {
                JAXBContext context = JAXBContext.newInstance(Vehicle.class);

                Marshaller marshaller = context.createMarshaller();

                marshaller.marshal(product, writer);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();
    }

    public static String convert(Vehicles vehicles) {

        StringWriter writer = new StringWriter();

        if (vehicles != null) {

            try {
                JAXBContext context = JAXBContext.newInstance(Vehicles.class);

                Marshaller marshaller = context.createMarshaller();

                marshaller.marshal(vehicles, writer);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();
    }

    public static Vehicle convertToJava(String xml) throws JAXBException {

        StringWriter writer = new StringWriter();

        Vehicle vehicle = null;
        try {

            JAXBContext context = JAXBContext.newInstance(Vehicle.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setAdapter(new NormalizedStringAdapter());
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            String xsdFileName = "Vehicle.xsd";
            URL resource = XMLConverter.class.getClassLoader().getResource(xsdFileName);
            File xsdFile = null;
            if (resource != null) {
                xsdFile = new File(resource.toURI());
            }
            Schema employeeSchema = sf.newSchema(xsdFile);
            unmarshaller.setSchema(employeeSchema);
            vehicle = (Vehicle) unmarshaller.unmarshal(new StringReader(xml));

        } catch (SAXException | URISyntaxException e) {
            e.printStackTrace();
        }

        return vehicle;
    }

    public static void listf(File directory, List<File> files) {

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(new File(file.getAbsolutePath()), files);
                }
            }
    }


}

