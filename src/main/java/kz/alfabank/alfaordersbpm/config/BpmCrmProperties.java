package kz.alfabank.alfaordersbpm.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URL;

@ConfigurationProperties(prefix = "bpmocrm", ignoreUnknownFields = false)
@Validated
public class BpmCrmProperties {

    @NotNull
    private URL bpmAdapterUrl;

    @NotNull
    private URL bpmCreateRequestUrl;

    @Valid
    private final Dictionaries dictionaries = new Dictionaries();

    @Valid
    private Bpm bpm;

    public static class Dictionaries{

        @NotNull
        private URL commonServiceUrl;

        @Valid
        private final DefaultValues defaultValues = new DefaultValues();

        public static class DefaultValues {
            @NotBlank
            private String registrationAddressCode;
            @NotBlank
            private String liveAddressCode;
            @NotBlank
            private String mobilePhoneCode;
            @NotBlank
            private String addressTypeDictName;
            @NotBlank
            private String phoneTypeDictName;
            @NotBlank
            private String dstrKatoDictName;
            @NotBlank
            private String rgnKatoDictName;
            @NotBlank
            private String twnKatoDictName;
            @NotBlank
            private String documentTypeDictName;
            @NotBlank
            private String productDictName;
            @NotBlank
            private String industryTypeDictName;
            @NotBlank
            private String employeePosition;
            @NotBlank
            private String workAddressCode;
            @NotBlank
            private String workPhoneCode;
            @NotBlank
            private String homePhoneCode;
            @NotBlank
            private String creditPurposeDictName;
            @NotBlank
            private String creditPurposeCode;
            @NotBlank
            private String insuranceCreditPurposeCode;
            @NotBlank
            private String withoutInsuranceCode;

            public String getRegistrationAddressCode() {
                return registrationAddressCode;
            }

            public void setRegistrationAddressCode(String registrationAddressCode) {
                this.registrationAddressCode = registrationAddressCode;
            }

            public String getLiveAddressCode() {
                return liveAddressCode;
            }

            public void setLiveAddressCode(String liveAddressCode) {
                this.liveAddressCode = liveAddressCode;
            }

            public String getMobilePhoneCode() {
                return mobilePhoneCode;
            }

            public void setMobilePhoneCode(String mobilePhoneCode) {
                this.mobilePhoneCode = mobilePhoneCode;
            }

            public String getAddressTypeDictName() {
                return addressTypeDictName;
            }

            public void setAddressTypeDictName(String addressTypeDictName) {
                this.addressTypeDictName = addressTypeDictName;
            }

            public String getPhoneTypeDictName() {
                return phoneTypeDictName;
            }

            public void setPhoneTypeDictName(String phoneTypeDictName) {
                this.phoneTypeDictName = phoneTypeDictName;
            }

            public String getDstrKatoDictName() {
                return dstrKatoDictName;
            }

            public void setDstrKatoDictName(String dstrKatoDictName) {
                this.dstrKatoDictName = dstrKatoDictName;
            }

            public String getRgnKatoDictName() {
                return rgnKatoDictName;
            }

            public void setRgnKatoDictName(String rgnKatoDictName) {
                this.rgnKatoDictName = rgnKatoDictName;
            }

            public String getTwnKatoDictName() {
                return twnKatoDictName;
            }

            public void setTwnKatoDictName(String twnKatoDictName) {
                this.twnKatoDictName = twnKatoDictName;
            }

            public String getDocumentTypeDictName() {
                return documentTypeDictName;
            }

            public void setDocumentTypeDictName(String documentTypeDictName) {
                this.documentTypeDictName = documentTypeDictName;
            }

            public String getProductDictName() {
                return productDictName;
            }

            public void setProductDictName(String productDictName) {
                this.productDictName = productDictName;
            }

            public String getIndustryTypeDictName() {
                return industryTypeDictName;
            }

            public void setIndustryTypeDictName(String industryTypeDictName) {
                this.industryTypeDictName = industryTypeDictName;
            }

            public String getEmployeePosition() {
                return employeePosition;
            }

            public void setEmployeePosition(String employeePosition) {
                this.employeePosition = employeePosition;
            }

            public String getWorkAddressCode() {
                return workAddressCode;
            }

            public void setWorkAddressCode(String workAddressCode) {
                this.workAddressCode = workAddressCode;
            }

            public String getWorkPhoneCode() {
                return workPhoneCode;
            }

            public void setWorkPhoneCode(String workPhoneCode) {
                this.workPhoneCode = workPhoneCode;
            }

            public String getHomePhoneCode() {
                return homePhoneCode;
            }

            public void setHomePhoneCode(String homePhoneCode) {
                this.homePhoneCode = homePhoneCode;
            }

            public String getCreditPurposeDictName() {
                return creditPurposeDictName;
            }

            public void setCreditPurposeDictName(String creditPurposeDictName) {
                this.creditPurposeDictName = creditPurposeDictName;
            }

            public String getCreditPurposeCode() {
                return creditPurposeCode;
            }

            public void setCreditPurposeCode(String creditPurposeCode) {
                this.creditPurposeCode = creditPurposeCode;
            }

            public String getInsuranceCreditPurposeCode() {
                return insuranceCreditPurposeCode;
            }

            public void setInsuranceCreditPurposeCode(String insuranceCreditPurposeCode) {
                this.insuranceCreditPurposeCode = insuranceCreditPurposeCode;
            }

            public String getWithoutInsuranceCode() {
                return withoutInsuranceCode;
            }

            public void setWithoutInsuranceCode(String withoutInsuranceCode) {
                this.withoutInsuranceCode = withoutInsuranceCode;
            }
        }

        public DefaultValues getDefaultValues() {
            return defaultValues;
        }

        public URL getCommonServiceUrl() {
            return commonServiceUrl;
        }

        public void setCommonServiceUrl(URL commonServiceUrl) {
            this.commonServiceUrl = commonServiceUrl;
        }
    }

    @Validated
    public static class Bpm {
        @NotBlank
        private String user;
        @NotBlank
        private String pwd;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }

    public Dictionaries getDictionaries() {
        return dictionaries;
    }

    public URL getBpmAdapterUrl() {
        return bpmAdapterUrl;
    }

    public void setBpmAdapterUrl(URL bpmAdapterUrl) {
        this.bpmAdapterUrl = bpmAdapterUrl;
    }

    public URL getBpmCreateRequestUrl() {
        return bpmCreateRequestUrl;
    }

    public void setBpmCreateRequestUrl(URL bpmCreateRequestUrl) {
        this.bpmCreateRequestUrl = bpmCreateRequestUrl;
    }

    public Bpm getBpm() {
        return bpm;
    }

    public void setBpm(Bpm bpm) {
        this.bpm = bpm;
    }
}
