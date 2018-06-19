package kz.alfabank.alfaordersbpm.domain.models.dictionary;


import java.util.Objects;

public enum ServiceDecisionType {
    VERIFIER("верификатор"),
    SECURITY("служба безопасности"),
    CREDITADMIN("кредитный администратор"),
    RISK("управление рисков")
    ;

    private final String description;

    ServiceDecisionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ServiceDecisionType parseFromString(String value){
        if (value == null)
            throw new IllegalArgumentException("Value is null when trying to parse ServiceDecisionType");
        ServiceDecisionType serviceDecisionType;
        switch (value.toLowerCase()) {
            case "1" : serviceDecisionType = ServiceDecisionType.VERIFIER;
                break;
            case "2" : serviceDecisionType = ServiceDecisionType.SECURITY;
                break;
            case "3" : serviceDecisionType = ServiceDecisionType.CREDITADMIN;
                break;
            case "4" : serviceDecisionType = ServiceDecisionType.RISK;
                break;
            case "верификатор" : serviceDecisionType = ServiceDecisionType.VERIFIER;
                break;
            case "служба безопасности" : serviceDecisionType = ServiceDecisionType.SECURITY;
                break;
            case "кредитный администратор" : serviceDecisionType = ServiceDecisionType.CREDITADMIN;
                break;
            case "управление рисков" : serviceDecisionType = ServiceDecisionType.RISK;
                break;
            default : serviceDecisionType = null;
                break;
        }
        Objects.requireNonNull(serviceDecisionType, String.format("Could not parse serviceDecisionType from input value %s",value));
        return serviceDecisionType;
    }
}
