package alararestaurant.domain.dtos.xml_dtos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemImportRootDTO {

    @XmlElement(name = "item")
    private ItemImportDTO[] itemImportDTOS;

    public ItemImportRootDTO() {
    }

    public ItemImportDTO[] getItemImportDTOS() {
        return itemImportDTOS;
    }

    public void setItemImportDTOS(ItemImportDTO[] itemImportDTOS) {
        this.itemImportDTOS = itemImportDTOS;
    }
}
