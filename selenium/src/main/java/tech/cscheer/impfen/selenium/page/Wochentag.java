package tech.cscheer.impfen.selenium.page;

public enum Wochentag {
    UNDEFINED("Undefined"),
    WEEKDAY("Weekday"),
    WEEKEND_DAY("WeekendDay");


    private String value;

    Wochentag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
