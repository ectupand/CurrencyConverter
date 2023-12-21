package models;


public class CurrencyModel {
    private String name;
    private String charCode;
    private Float value;

    public CurrencyModel(String name, String charCode, Float value){
        this.name = name;
        this.charCode = charCode;
        this.value = value;
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


}
