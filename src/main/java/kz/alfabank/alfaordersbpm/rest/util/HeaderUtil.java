package kz.alfabank.alfaordersbpm.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;


public class HeaderUtil {

    private static final String PREFIX = "sakd.";

    private HeaderUtil() {
        throw new AssertionError("No HeaderUtil instances for you!");
    }

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-SAKD-ALERT", message);
        headers.add("X-SAKD-PARAMS", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert(String.format("%s%s.created", PREFIX, entityName), param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert(String.format("%s%s.updated", PREFIX, entityName), param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert(String.format("%s%s.deleted", PREFIX, entityName), param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity creation failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-SAKD-ERROR", "error." + errorKey);
        headers.add("X-SAKD-PARAMS", entityName);
        return headers;
    }
}
