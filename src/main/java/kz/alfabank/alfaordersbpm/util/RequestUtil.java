package kz.alfabank.alfaordersbpm.util;

import org.springframework.data.authentication.UserCredentials;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.StringTokenizer;

public class RequestUtil {

    private static final String FORWARDED_HEADER = "X-Forwarded-For";
    public static final String BASIC_AUTH_HEADER = "Authorization";
    private static final String ROLES_HEADER = "X-Api-Roles";
    private static final String ORG_CODE_HEADER = "X-Org-Code";
    private static final String ORG_NAME_HEADER = "X-Org-Name";


    private RequestUtil() {
        throw new AssertionError("No RequestUtil instances for you!");
    }

    public static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
        return servletRequest;
    }

    public static HttpServletRequest getCurrentRequestSpringWay() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
        return ((ServletRequestAttributes)attrs).getRequest();
    }

    public static String getCurrentRequestUri(){
        return ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
    }

    public static String getBasicAuthHeader(){
        HttpServletRequest httpRequest = getCurrentRequest();
        return httpRequest.getHeader(BASIC_AUTH_HEADER);
    }

    public static String getApiRoles(){
        return getHeaderValue(ROLES_HEADER);
    }

    public static String getOrgCode(){
        return getHeaderValue(ORG_CODE_HEADER);
    }

    public static String getOrgName(){
        return getHeaderValue(ORG_NAME_HEADER);
    }

    private static String getHeaderValue(String orgCodeHeader) {
        String result = null;
        HttpServletRequest httpRequest = getCurrentRequest();
        String headerValue = httpRequest.getHeader(orgCodeHeader);
        if (headerValue != null)
            result = new String(Base64Utils.decodeFromString(headerValue), StandardCharsets.UTF_8);
        return result;
    }

    public static Optional<UserCredentials> getFromBasicAuth(){
        final String authorization = getBasicAuthHeader();
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            return Optional.ofNullable(new UserCredentials(values[0], values[1]));
        }
        return Optional.ofNullable(null);
    }

    public static String getClientIpAddress() {
        HttpServletRequest request = getCurrentRequest();
        String xForwardedForHeader = request.getHeader(FORWARDED_HEADER);
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ... we only want the client
            return Optional
                    .ofNullable(new StringTokenizer(xForwardedForHeader, ",").nextToken())
                    .orElse(request.getRemoteAddr())
                    .trim();
        }
    }

    public static String getClientHostName() {
        HttpServletRequest request = getCurrentRequest();
        return request.getRemoteHost();
    }

}
