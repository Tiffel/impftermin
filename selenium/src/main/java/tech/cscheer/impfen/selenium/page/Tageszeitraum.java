package tech.cscheer.impfen.selenium.page;

public enum Tageszeitraum {
    UNDEFINED("Undefined"),
    MORNING("Morning"),
    AFTERNOON("Afternoon");


    private String value;

    Tageszeitraum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
