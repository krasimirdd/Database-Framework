package com.kdd.cardealer.domain.dtos.exports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "carss")
@XmlAccessorType(XmlAccessType.FIELD)
public class CarExportRootDTO {

    @XmlElement(name = "car")
    private List<CarExportDTO> carExportDTOS;

    public CarExportRootDTO() {
    }

    public List<CarExportDTO> getCarExportDTOS() {
        return carExportDTOS;
    }

    public void setCarExportDTOS(List<CarExportDTO> carExportDTOS) {
        this.carExportDTOS = carExportDTOS;
    }
}
