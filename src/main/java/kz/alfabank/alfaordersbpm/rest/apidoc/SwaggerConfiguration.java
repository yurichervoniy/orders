package kz.alfabank.alfaordersbpm.rest.apidoc;

import kz.alfabank.alfaordersbpm.rest.controllers.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final Contact DEFAULT_CONTACT = new Contact("Prime Source", "http://p-s.kz", "info@p-s.kz");

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Alfa-Orders-BPM", "API для работы с заявками кредитования", "1.0",
            "urn:tos", DEFAULT_CONTACT,
            "GNU AGPLv3", "https://www.gnu.org/licenses/agpl-3.0.ru.html", Collections.emptyList());

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(Arrays.asList("application/json"));


    @Bean
    public Docket api() {
        LOG.debug("Starting Swagger Api Doc");
        StopWatch watch = new StopWatch();
        watch.start();
        Docket docket= new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .forCodeGeneration(true)
                .securitySchemes(Collections.singletonList(new BasicAuth("Basic Authorization")))
                .securityContexts(Collections.singletonList(SecurityContext.builder()
                        .securityReferences(
                                Collections.singletonList(SecurityReference.builder()
                                        .reference("Basic Authorization")
                                        .scopes(new AuthorizationScope[]{new AuthorizationScope("", "Every call to the API is secured with a basic authorization token.")})
                                        .build())).build()))
                .genericModelSubstitutes(ResponseEntity.class, Optional.class, CompletableFuture.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .select()
                //.apis(RequestHandlerSelectors.basePackage(AppConstants.REST_BASE_PACKAGE))
                //.paths(PathSelectors.ant(DEFAULT_INCLUDE_PATTERN))
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                ;
        watch.stop();
        LOG.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
