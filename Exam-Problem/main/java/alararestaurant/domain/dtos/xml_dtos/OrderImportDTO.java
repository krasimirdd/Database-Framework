package alararestaurant.domain.dtos.xml_dtos;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderImportDTO {

    @NotNull
    @XmlElement(name = "customer")
    private String customer;

    @NotNull
    @XmlElement(name = "employee")
    private String employee;

    @NotNull
    @XmlElement(name = "date-time")
    private String dateTime;

    @NotNull
    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "items")
    private ItemImportRootDTO itemImportRootDTO;

    public OrderImportDTO() {
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ItemImportRootDTO getItemImportRootDTO() {
        return itemImportRootDTO;
    }

    public void setItemImportRootDTO(ItemImportRootDTO itemImportRootDTO) {
        this.itemImportRootDTO = itemImportRootDTO;
    }
}
