package models;

import java.time.LocalDate;

public class Currency {
    private String name;
    private String charCode;
    private float value;
    private String valuteID;
    private LocalDate updatedAt;

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
