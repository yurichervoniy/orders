package kz.alfabank.alfaordersbpm.domain.models.phone;

import java.util.Objects;

public enum PhoneType {
    MOBILE("мобильный"),
    HOME("домашний"),
    WORK("рабочий");

    private final String description;

    PhoneType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PhoneType parseFromString(String value){
        if (value == null)
            throw new IllegalArgumentException("Value is null when trying to parse PhoneType");
        PhoneType phoneType;
        switch (value.toLowerCase()) {
            case "1" : phoneType = PhoneType.WORK;
                break;
            case "2" : phoneType = PhoneType.HOME;
                break;
            case "3" : phoneType = PhoneType.MOBILE;
                break;
            case "рабочий" : phoneType = PhoneType.WORK;
                break;
            case "домашний" : phoneType = PhoneType.HOME;
                break;
            case "мобильный" : phoneType = PhoneType.MOBILE;
                break;
            default : phoneType = null;
                break;
        }
        Objects.requireNonNull(phoneType, String.format("Could not parse PhoneType from input value %s",value));
        return phoneType;
    }
}
