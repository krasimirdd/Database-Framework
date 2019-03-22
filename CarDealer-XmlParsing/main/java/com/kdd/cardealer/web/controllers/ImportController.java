package com.kdd.cardealer.web.controllers;

import com.kdd.cardealer.domain.dtos.imports.CarImportRootDTO;
import com.kdd.cardealer.domain.dtos.imports.CustomerImportRootDTO;
import com.kdd.cardealer.domain.dtos.imports.PartImportRootDTO;
import com.kdd.cardealer.domain.dtos.imports.SupplierImportRootDTO;
import com.kdd.cardealer.service.*;
import com.kdd.cardealer.util.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

@Controller
public class ImportController {


    private static final String SUPPLIERS_XML_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\suppliers.xml";
    private static final String PARTS_XML_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\parts.xml";
    private static final String CARS_XML_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\cars.xml";
    private static final String CUSTOMERS_XML_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\customers.xml";

    private final SupplierService supplierService;
    private final SaleService saleService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;
    private final XmlParser xmlParser;

    @Autowired
    public ImportController(SupplierService supplierService, SaleService saleService, PartService partService, CarService carService, XmlParser xmlParser, CustomerService customerService) {
        this.supplierService = supplierService;
        this.saleService = saleService;
        this.partService = partService;
        this.carService = carService;
        this.xmlParser = xmlParser;
        this.customerService = customerService;
    }

    public String importSuppliers() throws JAXBException, FileNotFoundException {

        SupplierImportRootDTO supplierImportRootDTO =
                this.xmlParser.parseXml(SupplierImportRootDTO.class, SUPPLIERS_XML_FILE_PATH);

        this.supplierService.importSuppliers(supplierImportRootDTO);
        return "Imported suppliers";
    }

    public String importParts() throws JAXBException, FileNotFoundException {

        PartImportRootDTO partImportRootDTO =
                this.xmlParser.parseXml(PartImportRootDTO.class, PARTS_XML_FILE_PATH);

        this.partService.importParts(partImportRootDTO);
        return "Imported parts";
    }

    public String importCars() throws JAXBException, FileNotFoundException {

        CarImportRootDTO carImportRootDTO =
                this.xmlParser.parseXml(CarImportRootDTO.class, CARS_XML_FILE_PATH);

        this.carService.importCars(carImportRootDTO);
        return "Imported cars";
    }

    public String importCustomers() throws JAXBException, FileNotFoundException {

        CustomerImportRootDTO customerImportRootDTO =
                this.xmlParser.parseXml(CustomerImportRootDTO.class, CUSTOMERS_XML_FILE_PATH);

        this.customerService.importCustomers(customerImportRootDTO);
        return "Imported customers";
    }

    public String importSales() {

        for (int i = 0; i < 30; i++) {

            this.saleService.importSales();

        }

        return "Imported sales";
    }
}
