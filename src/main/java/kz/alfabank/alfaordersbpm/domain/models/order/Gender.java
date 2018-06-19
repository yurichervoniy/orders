package kz.alfabank.alfaordersbpm.domain.models.order;

import java.util.Objects;

public enum Gender {
    MALE("мужской"),
    FEMALE("женский");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public static Gender parseFromString(String value){
        if (value == null)
            throw new IllegalArgumentException("Value is null when trying to parse Gender");
        Gender gender;
        switch (value.toLowerCase()) {
            case "male" : gender = Gender.MALE;
                break;
            case "female" : gender = Gender.FEMALE;
                break;
            case "мужской" : gender = Gender.MALE;
                break;
            case "женский" : gender = Gender.FEMALE;
                break;
            default : gender = null;
                break;
        }
        Objects.requireNonNull(gender, String.format("Could not parse Gender from input value %s",value));
        return gender;
    }

    public String getDescription() {
        return description;
    }
}
