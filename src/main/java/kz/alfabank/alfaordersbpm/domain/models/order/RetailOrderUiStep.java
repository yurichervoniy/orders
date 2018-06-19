package kz.alfabank.alfaordersbpm.domain.models.order;

import java.util.Objects;

public enum RetailOrderUiStep {

    CUSTOMER("Анкета клиента"),
    PASSPORT("Данные документа, удостоверяющего личность"),
    SIGN("Согласие клиента"),
    CLIENT_PHOTO("Фото клиента"),
    PASSPORT_PHOTO("Фото документа, удостоверяющего личность"),
    ALTERNATIVE("Решение банка"),
    ADDRESS("Адрес фактического проживания"),
    JOB("Место работы"),
    PERSONAL_INFO("Дополнительные сведения"),
    UPDATE_INFO("Изменение анкетных данных"),
    SIGN_FINAL_PRINT("Создание договора"),
    SIGN_FINAL_DOCS("Подписание документов"),
    VERIFICATOR("Верификатор"),
    SECURITY("Служба безопасности"),
    RISKS("Проверка рисков"),
    CREDITADMIN("Кредитный администратор")
    ;

    private final String stepName;

    RetailOrderUiStep(String stepName) {
        this.stepName = stepName;
    }

    public String getStepName() {
        return stepName;
    }


    public static RetailOrderUiStep parseStepFromString(String value) {
        if (value == null)
            throw new IllegalArgumentException("Value is null when trying to parse RetailOrderUiStep");

        RetailOrderUiStep retailOrderUiStep;

        switch (value.toLowerCase()) {
            case "верификатор":
                retailOrderUiStep = RetailOrderUiStep.VERIFICATOR;
                break;
            case "служба безопасности":
                retailOrderUiStep = RetailOrderUiStep.SECURITY;
                break;
            case "проверка рисков":
                retailOrderUiStep = RetailOrderUiStep.RISKS;
                break;
            case "кредитный администратор":
                retailOrderUiStep = RetailOrderUiStep.CREDITADMIN;
                break;
            default:
                retailOrderUiStep = null;
                break;
        }

        Objects.requireNonNull(retailOrderUiStep, String.format("Could not parse RetailOrderUiStep from input value %s",value));
        return retailOrderUiStep;
    }
}
