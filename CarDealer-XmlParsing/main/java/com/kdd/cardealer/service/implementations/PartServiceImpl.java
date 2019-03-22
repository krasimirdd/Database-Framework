package com.kdd.cardealer.service.implementations;

import com.kdd.cardealer.domain.dtos.imports.PartImportDTO;
import com.kdd.cardealer.domain.dtos.imports.PartImportRootDTO;
import com.kdd.cardealer.domain.entities.Part;
import com.kdd.cardealer.domain.entities.Supplier;
import com.kdd.cardealer.repository.PartRepository;
import com.kdd.cardealer.repository.SupplierRepository;
import com.kdd.cardealer.service.PartService;
import com.kdd.cardealer.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PartServiceImpl implements PartService {

    private final SupplierRepository supplierRepository;
    private final PartRepository partRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public PartServiceImpl(SupplierRepository supplierRepository, PartRepository partRepository, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.partRepository = partRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void importParts(PartImportRootDTO partImportRootDTO) {

        for (PartImportDTO partImportDto : partImportRootDTO.getPartImportDTOS()) {
            if (!this.validationUtil.isValid(partImportDto)) {
                System.out.println("Errrr.....");

                continue;
            }

            Part partEntity = this.modelMapper.map(partImportDto, Part.class);
            partEntity.setSupplier(this.getRandomSupplier());

            this.partRepository.saveAndFlush(partEntity);
        }
    }

    private Supplier getRandomSupplier() {

        Random random = new Random();
        int randomIndex = random.nextInt((int) (this.supplierRepository.count() - 1)) + 1;

        return this.supplierRepository.findAll().get(randomIndex);
    }
}
