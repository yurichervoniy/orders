package kz.alfabank.alfaordersbpm.domain.models;

public final class Constants {
    private Constants(){}
    public static final String PHONE_REGEX = "[0-9]{11}";
    public static final int PHONE_LENGTH = 11;
    public static final int PAYDAY_MAX = 30;
    public static final int PAYDAY_MIN = 1;
    public static final String ORDER_PERMISSION = " and exists (select 1 from OrderPermission p where p.targetType = upper('RETAILORDER') and p.targetId = t.id and p.permission = upper('READ') and p.grantTo in (?1) and (p.grantOrgcode is null or p.grantOrgcode = ?2))";

}