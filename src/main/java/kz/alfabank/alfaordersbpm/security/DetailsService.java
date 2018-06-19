package kz.alfabank.alfaordersbpm.security;

import kz.alfabank.alfaordersbpm.domain.models.exception.BadRequestException;
import kz.alfabank.alfaordersbpm.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class DetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(DetailsService.class);
    private static final String ROLE = "LIFERAYUSER";

    @Override
    public UserDetails loadUserByUsername(String username) {
        LOG.debug("UserDetailsService->loadUserByUsername for username={}", username);

        Optional<UserCredentials> credentialsHolder = RequestUtil.getFromBasicAuth();
        if (!credentialsHolder.isPresent()){
            LOG.error("Can't get credentials from basic auth");
            throw new BadRequestException("Can't get credentials from basic auth");
        }
        UserCredentials credentials = credentialsHolder.get();
        LOG.debug("UserCredentials {}", credentials);
        if (!credentials.hasUsername() || !credentials.hasPassword())
            throw new BadRequestException("Could not get username or pwd from basic auth");
        String apiRoles = RequestUtil.getApiRoles();
        LOG.debug("User GrantedAuthorities {}", apiRoles);
        if (apiRoles == null)
            throw new BadRequestException("no header or value for api roles");
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(apiRoles);
        if (grantedAuthorities.isEmpty())
            throw new BadRequestException("Could not get user grantedAuthorities");
        User.UserBuilder builder = User.withUsername(credentials.getUsername())
                                    .password(new BCryptPasswordEncoder().encode(credentials.getPassword()))
                                    .roles(ROLE)
                                    .authorities(grantedAuthorities)
                                    ;

        return builder.build();
    }
}