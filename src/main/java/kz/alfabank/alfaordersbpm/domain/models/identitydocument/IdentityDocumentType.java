package kz.alfabank.alfaordersbpm.domain.models.identitydocument;

import java.util.Objects;

public enum IdentityDocumentType {
    PASSPORT("Паспорт РК"),
    IDCARD("Удостоверение личности"),
    RESIDENCE("Вид на жительство"),
    DIPLOMATICPASSPORT("Дипломатический паспорт РК"),
    FOREIGNPASSPORT("Паспорт иностранного гражданина"),
    BIRTHCERTIFICATE("Свидетельство о рождении"),
    REFUGEECARD("Удостоверение беженца"),
    STATELESSPERSON("Удостоверение лица без гражданства")
    ;

    private final String description;

    IdentityDocumentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static IdentityDocumentType parseFromString(String value){
        if (value == null)
            throw new IllegalArgumentException("Value is null when trying to parse IdentityDocumentType");
        IdentityDocumentType identityDocumentType;
        switch (value.toLowerCase()) {
            case "8" : identityDocumentType = IdentityDocumentType.IDCARD;
                break;
            case "3" : identityDocumentType = IdentityDocumentType.PASSPORT;
                break;
            case "1" : identityDocumentType = IdentityDocumentType.RESIDENCE;
                break;
            case "2" : identityDocumentType = IdentityDocumentType.DIPLOMATICPASSPORT;
                break;
            case "4" : identityDocumentType = IdentityDocumentType.FOREIGNPASSPORT;
                break;
            case "5" : identityDocumentType = IdentityDocumentType.BIRTHCERTIFICATE;
                break;
            case "6" : identityDocumentType = IdentityDocumentType.REFUGEECARD;
                break;
            case "7" : identityDocumentType = IdentityDocumentType.STATELESSPERSON;
                break;
            case "вид на жительство" : identityDocumentType = IdentityDocumentType.RESIDENCE;
                break;
            case "дипломатический паспорт рк" : identityDocumentType = IdentityDocumentType.DIPLOMATICPASSPORT;
                break;
            case "паспорт рк" : identityDocumentType = IdentityDocumentType.PASSPORT;
                break;
            case "паспорт иностранного гражданина" : identityDocumentType = IdentityDocumentType.FOREIGNPASSPORT;
                break;
            case "свидетельство о рождении" : identityDocumentType = IdentityDocumentType.BIRTHCERTIFICATE;
                break;
            case "удостоверение беженца" : identityDocumentType = IdentityDocumentType.REFUGEECARD;
                break;
            case "удостоверение лица без гражданства" : identityDocumentType = IdentityDocumentType.STATELESSPERSON;
                break;
            case "удостоверение личности" : identityDocumentType = IdentityDocumentType.IDCARD;
                break;
            default : identityDocumentType = null;
                break;
        }
        Objects.requireNonNull(identityDocumentType, String.format("Could not parse IdentityDocumentType from input value %s",value));
        return identityDocumentType;
    }

}
