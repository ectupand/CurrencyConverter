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

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setValuteID(String valuteID) {
        this.valuteID = valuteID;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
