package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;
import kz.alfabank.alfaordersbpm.domain.models.Constants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

public class PhoneConfirmation {

    @ApiModelProperty(notes="Мобильный телефон", required = true)
    @NotBlank(message = "Мобильный телефон не может быть пустым")
    @Size(min = Constants.PHONE_LENGTH, max = Constants.PHONE_LENGTH)
    @Pattern(regexp = Constants.PHONE_REGEX, message = "Номер телефона не соответсвует regexp-у")
    private String mobilePhone;

    @ApiModelProperty(notes="Вид заявки", required = true)
    @NotBlank(message = "Вид заявки не может быть пустым")
    private String orderType;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneConfirmation that = (PhoneConfirmation) o;
        return Objects.equals(mobilePhone, that.mobilePhone) &&
                Objects.equals(orderType, that.orderType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mobilePhone, orderType);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PhoneConfirmation{");
        sb.append("mobilePhone='").append(mobilePhone).append('\'');
        sb.append(", orderType='").append(orderType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
