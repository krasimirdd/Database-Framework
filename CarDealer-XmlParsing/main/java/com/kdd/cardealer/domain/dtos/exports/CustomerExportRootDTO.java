package com.kdd.cardealer.domain.dtos.exports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerExportRootDTO {

    @XmlElement(name = "customer")
    private List<CustomerExportDTO> customerExportDTOS;

    public CustomerExportRootDTO() {
    }

    public List<CustomerExportDTO> getCustomerExportDTOS() {
        return customerExportDTOS;
    }

    public void setCustomerExportDTOS(List<CustomerExportDTO> customerExportDTOS) {
        this.customerExportDTOS = customerExportDTOS;
    }
}
