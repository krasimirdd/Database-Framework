package com.kdd.cardealer.domain.dtos.imports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerImportRootDTO {

    @XmlElement(name = "customer")
    private CustomerImportDTO[] customerImportDTOS;

    public CustomerImportRootDTO() {
    }

    public CustomerImportDTO[] getCustomerImportDTOS() {
        return customerImportDTOS;
    }

    public void setCustomerImportDTOS(CustomerImportDTO[] customerImportDTOS) {
        this.customerImportDTOS = customerImportDTOS;
    }
}
