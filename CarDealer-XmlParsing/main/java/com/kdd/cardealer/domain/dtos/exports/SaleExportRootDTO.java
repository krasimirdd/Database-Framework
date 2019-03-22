package com.kdd.cardealer.domain.dtos.exports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleExportRootDTO {

    @XmlElement(name = "sale")
    private List<SaleExportDTO> saleExportDTOS;

    public SaleExportRootDTO() {
    }

    public List<SaleExportDTO> getSaleExportDTOS() {
        return saleExportDTOS;
    }

    public void setSaleExportDTOS(List<SaleExportDTO> saleExportDTOS) {
        this.saleExportDTOS = saleExportDTOS;
    }
}
