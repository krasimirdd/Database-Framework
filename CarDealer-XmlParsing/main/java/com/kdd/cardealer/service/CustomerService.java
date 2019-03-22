package com.kdd.cardealer.service;

import com.kdd.cardealer.domain.dtos.exports.CustomerExportRootDTO;
import com.kdd.cardealer.domain.dtos.imports.CustomerImportRootDTO;

public interface CustomerService {
    void importCustomers(CustomerImportRootDTO customerImportRootDTO);

    CustomerExportRootDTO exportCustomers();
}
