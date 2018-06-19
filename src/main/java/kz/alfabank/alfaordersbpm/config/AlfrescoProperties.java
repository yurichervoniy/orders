package kz.alfabank.alfaordersbpm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@ConfigurationProperties(prefix = "alfresco", ignoreUnknownFields = false)
@Validated
public class AlfrescoProperties {

    @NotBlank
    private String mainSiteName;

    @NotBlank
    private String creditPilPath;

    @Valid
    private final Config config = new Config();

    public static class Config {
        @NotBlank
        private String protocol;
        @NotBlank
        private String host;
        @Positive
        @Max(value = 65535)
        private Integer port;
        @NotBlank
        private String apiUri;
        @NotBlank
        private String userName;
        @NotBlank
        private String userPassword;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getApiUri() {
            return apiUri;
        }

        public void setApiUri(String apiUri) {
            this.apiUri = apiUri;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "protocol='" + protocol + '\'' +
                    ", host='" + host + '\'' +
                    ", port=" + port +
                    ", apiUri='" + apiUri + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userPassword='" + userPassword + '\'' +
                    '}';
        }
    }

    public Config getConfig() {
        return config;
    }

    public String getMainSiteName() {
        return mainSiteName;
    }

    public void setMainSiteName(String mainSiteName) {
        this.mainSiteName = mainSiteName;
    }

    public String getCreditPilPath() {
        return creditPilPath;
    }

    public void setCreditPilPath(String creditPilPath) {
        this.creditPilPath = creditPilPath;
    }

    @Override
    public String toString() {
        return "AlfrescoProperties{" +
                "mainSiteName='" + mainSiteName + '\'' +
                ", creditPilPath='" + creditPilPath + '\'' +
                ", config=" + config +
                '}';
    }
}
