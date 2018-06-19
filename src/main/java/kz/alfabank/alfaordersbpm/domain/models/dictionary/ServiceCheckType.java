package kz.alfabank.alfaordersbpm.domain.models.dictionary;

import java.util.Objects;

public enum ServiceCheckType {
    ADDINFO("дополнительная информация"),
    NEGATIVEINFO("негативная информация"),
    IDNDOC("документы"),
    JUDGE("судебные исполнители"),
    ADDRESS("адреса")
    ;

    private final String description;

    ServiceCheckType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ServiceCheckType parseFromString(String value){
        if (value == null)
            throw new IllegalArgumentException("Value is null when trying to parse ServiceCheckType");
        ServiceCheckType serviceCheckType;
        switch (value.toLowerCase()) {
            case "1" : serviceCheckType = ServiceCheckType.ADDINFO;
                break;
            case "2" : serviceCheckType = ServiceCheckType.NEGATIVEINFO;
                break;
            case "3" : serviceCheckType = ServiceCheckType.IDNDOC;
                break;
            case "4" : serviceCheckType = ServiceCheckType.JUDGE;
                break;
            case "5" : serviceCheckType = ServiceCheckType.ADDRESS;
                break;
            default : serviceCheckType = null;
                break;
        }
        Objects.requireNonNull(serviceCheckType, String.format("Could not parse serviceCheckType from input value %s",value));
        return serviceCheckType;
    }
}
