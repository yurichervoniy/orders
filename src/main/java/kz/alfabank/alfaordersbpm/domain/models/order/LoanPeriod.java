package kz.alfabank.alfaordersbpm.domain.models.order;

public enum LoanPeriod {
    MONTH("месяц");

    private final String description;

    LoanPeriod(String description) {
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public static LoanPeriod getDefaultPeriod(){
        return LoanPeriod.MONTH;
    }
}
