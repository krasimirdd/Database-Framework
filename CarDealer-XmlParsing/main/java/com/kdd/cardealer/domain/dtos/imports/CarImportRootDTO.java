package com.kdd.cardealer.domain.dtos.imports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarImportRootDTO {

    @XmlElement(name = "car")
    private CarImportDTO[] carImportDTO;

    public CarImportRootDTO() {
    }

    public CarImportDTO[] getCarImportDTO() {
        return carImportDTO;
    }

    public void setCarImportDTO(CarImportDTO[] carImportDTO) {
        this.carImportDTO = carImportDTO;
    }
}
