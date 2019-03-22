package com.kdd.cardealer.service;

import com.kdd.cardealer.domain.dtos.exports.CarExportRootDTO;
import com.kdd.cardealer.domain.dtos.exports.CarToyotaExportRootDTO;
import com.kdd.cardealer.domain.dtos.imports.CarImportRootDTO;

public interface CarService {

    void importCars(CarImportRootDTO carImportRootDTO);

    CarExportRootDTO exportCars();

    CarToyotaExportRootDTO exportToyotas();
}
