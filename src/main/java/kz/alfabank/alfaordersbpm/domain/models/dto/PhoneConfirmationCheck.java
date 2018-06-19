package kz.alfabank.alfaordersbpm.domain.models.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class PhoneConfirmationCheck {

    @ApiModelProperty(notes="Признак валидности кода", required = true)
    private final boolean valid;

    @ApiModelProperty(notes="Сопроводительное сообщение", required = true)
    private final String message;

    public PhoneConfirmationCheck(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneConfirmationCheck that = (PhoneConfirmationCheck) o;
        return valid == that.valid &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valid, message);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PhoneConfirmationCheck{");
        sb.append("valid=").append(valid);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
