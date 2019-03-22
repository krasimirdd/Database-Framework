package com.kdd.cardealer.domain.dtos.exports;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cars")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarToyotaExportRootDTO {

    @XmlElement(name = "car")
    private List<CarToyotaExportDTO> carToyotaExportDTOS;

    public CarToyotaExportRootDTO() {
    }

    public List<CarToyotaExportDTO> getCarToyotaExportDTOS() {
        return carToyotaExportDTOS;
    }

    public void setCarToyotaExportDTOS(List<CarToyotaExportDTO> carToyotaExportDTOS) {
        this.carToyotaExportDTOS = carToyotaExportDTOS;
    }
}
