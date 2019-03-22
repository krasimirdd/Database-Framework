package com.kdd.cardealer.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class XmlParserImpl implements XmlParser {
    @Override
    public <T> T parseXml(Class<T> objectClass, String path) throws JAXBException, FileNotFoundException {

        JAXBContext jaxbContext = JAXBContext.newInstance(objectClass);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return (T) unmarshaller.unmarshal(reader);
    }

    @Override
    public <T> void exportToXml(T object, Class<T> objectClass, String path) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(objectClass);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(object, new File(path));
    }
}
