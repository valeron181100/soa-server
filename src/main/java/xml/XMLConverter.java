package xml;

import model.Vehicle;
import model.Vehicles;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
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

    public static Vehicle convertToJava(String xml) {

        StringWriter writer = new StringWriter();

        Vehicle vehicle = null;
        try {

            JAXBContext context = JAXBContext.newInstance(Vehicle.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setAdapter(new NormalizedStringAdapter());
            vehicle = (Vehicle) unmarshaller.unmarshal(new StringReader(xml));

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return vehicle;
    }

}

