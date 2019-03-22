package bookshopsystemapp.domain.entities;

import java.math.BigDecimal;

public interface ReducedBook {
    void setTitle();

    String getTitle();

    void setEditionType();

    String getEditionType();

    void setAgeRestriction();

    String getAgeRestriction();

    void setPrice();

    BigDecimal getPrice();
}
