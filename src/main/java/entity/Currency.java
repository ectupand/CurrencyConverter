package entity;

import java.time.LocalDate;

public class Currency {
    private String name;
    private String charCode;
    private Float value;
    private String valuteID;
    private LocalDate updatedAt;

    public Currency(String name, String charCode, Float value, String valuteID, LocalDate updatedAt){
        this.name = name;
        this.charCode = charCode;
        this.value = value;
        this.valuteID = valuteID;
        this.updatedAt = updatedAt;
    }
    public Currency(){}

    public String getName() {
        return name;
    }

    public String getCharCode() {
        return charCode;
    }

    public Float getValue() {
        return value;
    }

    public String getValuteID() {
        return valuteID;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

}
