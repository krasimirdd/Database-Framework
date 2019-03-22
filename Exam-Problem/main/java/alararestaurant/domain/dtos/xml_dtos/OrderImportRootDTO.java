package alararestaurant.domain.dtos.xml_dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orders")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderImportRootDTO {

    @XmlElement(name = "order")
    private OrderImportDTO[] orderImportDTOS;

    public OrderImportRootDTO() {
    }

    public OrderImportDTO[] getOrderImportDTOS() {
        return orderImportDTOS;
    }

    public void setOrderImportDTOS(OrderImportDTO[] orderImportDTOS) {
        this.orderImportDTOS = orderImportDTOS;
    }
}
