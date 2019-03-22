package com.kdd.cardealer.service.implementations;

import com.kdd.cardealer.domain.dtos.exports.CarAttributeExportDTO;
import com.kdd.cardealer.domain.dtos.exports.SaleExportDTO;
import com.kdd.cardealer.domain.dtos.exports.SaleExportRootDTO;
import com.kdd.cardealer.domain.entities.Car;
import com.kdd.cardealer.domain.entities.Customer;
import com.kdd.cardealer.domain.entities.Part;
import com.kdd.cardealer.domain.entities.Sale;
import com.kdd.cardealer.repository.CarRepository;
import com.kdd.cardealer.repository.CustomerRepository;
import com.kdd.cardealer.repository.SaleRepository;
import com.kdd.cardealer.service.CarService;
import com.kdd.cardealer.service.SaleService;
import com.kdd.cardealer.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, CarService carService, CarRepository carRepository, CustomerRepository customerRepository, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.saleRepository = saleRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public void importSales() {

        List<Integer> discounts = Arrays.asList(0, 5, 10, 15, 20, 30, 40, 50);
        Random randomDiscount = new Random();
        Car car = getRandomCar().orElse(null);
        Customer customer = getRandomCustomer().orElse(null);
        Sale sale = new Sale();

        sale.setCar(car);
        sale.setCustomer(customer);
        int nextRandom = randomDiscount.nextInt(discounts.size() - 1) + 1;
        int randomToSet = discounts.get(nextRandom);

        if (customer.getYoungDriver()) {
            sale.setDiscount((randomToSet + 5) % 100);
        } else {
            sale.setDiscount(randomToSet % 100);
        }

        this.saleRepository.saveAndFlush(sale);
    }

    @Override
    public SaleExportRootDTO exportSalesDiscounts() {

        List<Sale> saleEntities = this.saleRepository.findAll();
        List<SaleExportDTO> saleExportDTOS = new ArrayList<>();
        List<Part> parts = new ArrayList<>();

        for (Sale saleEntity : saleEntities) {
            SaleExportDTO saleToExport =
                    modelMapper.map(saleEntity, SaleExportDTO.class);

            BigDecimal discount = BigDecimal.valueOf(saleEntity.getDiscount());
            BigDecimal price = BigDecimal.valueOf(0.0);

            for (Part part : saleEntity.getCar().getParts()) {
                price = price.add(part.getPrice());
            }

            saleToExport.setCustomerName(saleEntity.getCustomer().getName());
            saleToExport.setPrice(price);

            BigDecimal discountResult = price
                    .multiply(discount.divide(BigDecimal.valueOf(100)));

            saleToExport.setPriceWithDiscount(price.subtract(discountResult));

            CarAttributeExportDTO carAttributeExportDTO =
                    modelMapper.map(saleEntity.getCar(), CarAttributeExportDTO.class);

            saleToExport.setCarAttributeExportDTO(carAttributeExportDTO);

            saleExportDTOS.add(saleToExport);
        }


        SaleExportRootDTO saleExportRootDTO = new SaleExportRootDTO();
        saleExportRootDTO.setSaleExportDTOS(saleExportDTOS);

        return saleExportRootDTO;
    }

    private Optional<Customer> getRandomCustomer() {
        Random rnd = new Random();
        Optional<Customer> customer = this.customerRepository.findById(rnd.nextInt((int) (customerRepository.count() - 1)) + 1);

        return customer;
    }

    private Optional<Car> getRandomCar() {
        Random rnd = new Random();
        Optional<Car> car = this.carRepository.findById(rnd.nextInt((int) (carRepository.count() - 1)) + 1);

        return car;
    }
}
