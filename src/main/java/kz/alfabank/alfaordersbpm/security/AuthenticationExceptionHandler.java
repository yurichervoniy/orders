package kz.alfabank.alfaordersbpm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.alfabank.alfaordersbpm.domain.models.exception.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationExceptionHandler extends BasicAuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        LOG.error("AuthenticationExceptionHandler->commence (exception in auth)", e);
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ExceptionResponse exceptionResponse = ExceptionResponse.of(status, e, httpServletRequest);
        String responseMsg = mapper.writeValueAsString(exceptionResponse);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        httpServletResponse.getWriter().write(responseMsg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("ordersbpm");
        super.afterPropertiesSet();
    }
}
