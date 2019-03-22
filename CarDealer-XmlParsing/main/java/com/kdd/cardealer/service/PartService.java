package com.kdd.cardealer.service;

import com.kdd.cardealer.domain.dtos.imports.PartImportRootDTO;

public interface PartService {

    void importParts(PartImportRootDTO partImportRootDTO);
}