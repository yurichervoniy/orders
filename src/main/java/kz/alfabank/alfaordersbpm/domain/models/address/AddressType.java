package kz.alfabank.alfaordersbpm.domain.models.address;

import java.util.Objects;

public enum AddressType {
    REGISTRATION("регистрации"),
    WORK("места работы"),
    RESIDENCE("проживания")
    ;

    private final String description;

    AddressType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AddressType parseFromString(String value){
        if (value == null)
            throw new IllegalArgumentException("Value is null when trying to parse AddressType");
        AddressType addressType;
        switch (value.toLowerCase()) {
            case "2" : addressType = AddressType.REGISTRATION;
                break;
            case "3" : addressType = AddressType.RESIDENCE;
                break;
            case "4" : addressType = AddressType.WORK;
                break;
            case "адрес регистрации" : addressType = AddressType.REGISTRATION;
                break;
            case "место жительства" : addressType = AddressType.RESIDENCE;
                break;
            case "место работы" : addressType = AddressType.WORK;
                break;
            case "регистрации" : addressType = AddressType.REGISTRATION;
                break;
            case "места работы" : addressType = AddressType.WORK;
                break;
            case "проживания" : addressType = AddressType.RESIDENCE;
                break;
            case "registration" : addressType = AddressType.REGISTRATION;
                break;
            case "work" : addressType = AddressType.WORK;
                break;
            case "residence" : addressType = AddressType.RESIDENCE;
                break;
            default : addressType = null;
                break;
        }
        Objects.requireNonNull(addressType, String.format("Could not parse AddressType from input value %s",value));
        return addressType;
    }
}
