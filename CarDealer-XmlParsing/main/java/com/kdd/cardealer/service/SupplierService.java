package com.kdd.cardealer.service;

import com.kdd.cardealer.domain.dtos.imports.SupplierImportRootDTO;

public interface SupplierService {

    void importSuppliers(SupplierImportRootDTO supplierImportDTO);
}
