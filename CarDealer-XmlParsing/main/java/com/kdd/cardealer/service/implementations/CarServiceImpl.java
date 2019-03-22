package com.kdd.cardealer.service.implementations;

import com.kdd.cardealer.domain.dtos.exports.*;
import com.kdd.cardealer.domain.dtos.imports.CarImportDTO;
import com.kdd.cardealer.domain.dtos.imports.CarImportRootDTO;
import com.kdd.cardealer.domain.entities.Car;
import com.kdd.cardealer.domain.entities.Part;
import com.kdd.cardealer.repository.CarRepository;
import com.kdd.cardealer.repository.PartRepository;
import com.kdd.cardealer.service.CarService;
import com.kdd.cardealer.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final PartRepository partRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, PartRepository partRepository, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.partRepository = partRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void importCars(CarImportRootDTO carImportRootDTO) {

        for (CarImportDTO carImportDTO : carImportRootDTO.getCarImportDTO()) {

            if (!this.validationUtil.isValid(carImportDTO)) {
                System.out.println("Errr...");

                continue;
            }

            Car carEntity = this.modelMapper.map(carImportDTO, Car.class);
            carEntity.setParts(this.getRandomParts());

            this.carRepository.saveAndFlush(carEntity);
        }
    }

    @Override
    public CarExportRootDTO exportCars() {

        List<Car> carEntities = this.carRepository.findAll();
        List<CarExportDTO> carExportDTOS = new ArrayList<>();

        for (Car carEntity : carEntities) {

            CarExportDTO carExportDTO = modelMapper.map(carEntity, CarExportDTO.class);

            List<PartExportDTO> partExportDTOS = new ArrayList<>();
            for (Part part : carEntity.getParts()) {

                PartExportDTO partExportDTO = modelMapper.map(part, PartExportDTO.class);
                partExportDTOS.add(partExportDTO);
            }

            PartExportRootDTO partExportRootDTO = new PartExportRootDTO();
            partExportRootDTO.setPartExportDTOS(partExportDTOS);

            carExportDTO.setPartExportRootDTO(partExportRootDTO);

            carExportDTOS.add(carExportDTO);
        }

        CarExportRootDTO carExportRootDTO = new CarExportRootDTO();
        carExportRootDTO.setCarExportDTOS(carExportDTOS);

        return carExportRootDTO;
    }

    private List<Part> getRandomParts() {

        List<Part> parts = new ArrayList<>();
        Random random = new Random();

        List<Part> partEntities = this.partRepository.findAll();

        int length = random.nextInt(10) + 10;
        for (int i = 0; i < length; i++) {

            int partIndex = random.nextInt((int) (partRepository.count() - 1)) + 1;

            parts.add(partEntities.get(partIndex));
        }

        return parts;
    }

    public CarToyotaExportRootDTO exportToyotas() {
        List<Car> carEntities =
                this.carRepository.findAllByMakeOrderByModelAscTravelledDistanceDesc("Toyota");

        List<CarToyotaExportDTO> toyotaExportDTOS = new ArrayList<>();

        for (Car carEntity : carEntities) {

            CarToyotaExportDTO carToyotaExportDTO =
                    modelMapper.map(carEntity, CarToyotaExportDTO.class);

            toyotaExportDTOS.add(carToyotaExportDTO);
        }

        CarToyotaExportRootDTO carToyotaExportRootDTOS = new CarToyotaExportRootDTO();
        carToyotaExportRootDTOS.setCarToyotaExportDTOS(toyotaExportDTOS);

        return carToyotaExportRootDTOS;
    }
}
