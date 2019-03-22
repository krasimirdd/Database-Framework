package com.kdd.cardealer.service.implementations;

import com.kdd.cardealer.domain.dtos.exports.CustomerExportDTO;
import com.kdd.cardealer.domain.dtos.exports.CustomerExportRootDTO;
import com.kdd.cardealer.domain.dtos.imports.CustomerImportDTO;
import com.kdd.cardealer.domain.dtos.imports.CustomerImportRootDTO;
import com.kdd.cardealer.domain.entities.Customer;
import com.kdd.cardealer.repository.CarRepository;
import com.kdd.cardealer.repository.CustomerRepository;
import com.kdd.cardealer.service.CustomerService;
import com.kdd.cardealer.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void importCustomers(CustomerImportRootDTO customerImportRootDTO) {

        for (CustomerImportDTO customerImportDTO : customerImportRootDTO.getCustomerImportDTOS()) {

            if (!this.validationUtil.isValid(customerImportDTO)) {
                System.out.println("Errr...");

                continue;
            }

            Customer customerEntity = this.modelMapper.map(customerImportDTO, Customer.class);
            customerEntity.setBirthDate(LocalDate.parse(customerImportDTO.getBirthDate()));

            this.customerRepository.saveAndFlush(customerEntity);
        }
    }

    @Override
    public CustomerExportRootDTO exportCustomers() {
        List<Customer> customerEntities = this.customerRepository.getAllCustomersOrderedByBirthDate();
        List<CustomerExportDTO> customerExportDTOS = new ArrayList<>();

        for (Customer customerEntity : customerEntities) {

            CustomerExportDTO customerExportDTO =
                    modelMapper.map(customerEntity, CustomerExportDTO.class);

            customerExportDTOS.add(customerExportDTO);
        }

        CustomerExportRootDTO customerExportRootDTO = new CustomerExportRootDTO();
        customerExportRootDTO.setCustomerExportDTOS(customerExportDTOS);

        return customerExportRootDTO;
    }

}
