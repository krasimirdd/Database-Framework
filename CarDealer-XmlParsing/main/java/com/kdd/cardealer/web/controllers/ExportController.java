package com.kdd.cardealer.web.controllers;

import com.kdd.cardealer.domain.dtos.exports.CarExportRootDTO;
import com.kdd.cardealer.domain.dtos.exports.CarToyotaExportRootDTO;
import com.kdd.cardealer.domain.dtos.exports.CustomerExportRootDTO;
import com.kdd.cardealer.domain.dtos.exports.SaleExportRootDTO;
import com.kdd.cardealer.service.CarService;
import com.kdd.cardealer.service.CustomerService;
import com.kdd.cardealer.service.SaleService;
import com.kdd.cardealer.util.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

@Controller
public class ExportController {

    private final CarService carService;
    private final SaleService saleService;
    private final XmlParser xmlParser;
    private final CustomerService customerService;

    @Autowired
    public ExportController(CarService carService, SaleService saleService, XmlParser xmlParser, CustomerService customerService) {
        this.carService = carService;
        this.saleService = saleService;
        this.xmlParser = xmlParser;
        this.customerService = customerService;
    }

    public String exportCars() throws JAXBException, FileNotFoundException {

        CarExportRootDTO carExportRootDTO = this.carService.exportCars();

        this.xmlParser.exportToXml(
                carExportRootDTO,
                CarExportRootDTO.class,
                "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\output\\car-and-parts.xml");

        return null;
    }

    public String exportOrderedCustomers() throws JAXBException, FileNotFoundException {
        CustomerExportRootDTO customerExportRootDTO = this.customerService.exportCustomers();

        this.xmlParser.exportToXml(
                customerExportRootDTO,
                CustomerExportRootDTO.class,
                "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\output\\ordered-customer.xml");

        return "Exported customers";
    }

    public String exportToyotas() throws JAXBException, FileNotFoundException {
        CarToyotaExportRootDTO carToyotaExportRootDTO = this.carService.exportToyotas();

        this.xmlParser.exportToXml(
                carToyotaExportRootDTO,
                CarToyotaExportRootDTO.class,
                "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\output\\toyota-cars.xml");

        return "Exported toyotas";
    }

    public String exportSalesDiscounts() throws JAXBException, FileNotFoundException {

        SaleExportRootDTO saleExportRootDTO = this.saleService.exportSalesDiscounts();

        this.xmlParser.exportToXml(
                saleExportRootDTO,
                SaleExportRootDTO.class,
                "D:\\IntelliJ_Projects\\DatabasesFrameworks\\CarDealerXML\\src\\main\\resources\\files\\output\\sales-discounts.xml"
        );

        return "Exported sales";
    }
}
