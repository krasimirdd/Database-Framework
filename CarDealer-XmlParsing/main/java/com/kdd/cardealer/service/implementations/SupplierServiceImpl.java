package com.kdd.cardealer.service.implementations;

import com.kdd.cardealer.domain.dtos.imports.SupplierImportDTO;
import com.kdd.cardealer.domain.dtos.imports.SupplierImportRootDTO;
import com.kdd.cardealer.domain.entities.Supplier;
import com.kdd.cardealer.repository.SupplierRepository;
import com.kdd.cardealer.service.SupplierService;
import com.kdd.cardealer.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMaper;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, ValidationUtil validationUtil, ModelMapper modelMaper) {
        this.supplierRepository = supplierRepository;
        this.validationUtil = validationUtil;
        this.modelMaper = modelMaper;
    }

    @Override
    public void importSuppliers(SupplierImportRootDTO supplierImportDTO) {
        for (SupplierImportDTO suppliedImportDTO : supplierImportDTO.getSupplierImportDTOS()) {
            if (!this.validationUtil.isValid(suppliedImportDTO)) {
                System.out.println("Errrr...");

                continue;
            }

            Supplier supplierEntity = this.modelMaper.map(suppliedImportDTO, Supplier.class);

            this.supplierRepository.saveAndFlush(supplierEntity);
        }
    }
}
