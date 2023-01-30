package com.abnamro.emrahboz.assignment.retail.security;

import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import com.abnamro.emrahboz.assignment.retail.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService service;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            Optional<UserEntity> userEntity = service.getUser(username, password);


            if (userEntity.isPresent()) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(userEntity.get().getRole()));

                return new UsernamePasswordAuthenticationToken(
                        username, password, authorities);
            } else {
                return null;
            }

        } catch (Exception ex) {
            log.error(String.format("Exception occurred while authenticate. " +
                    "Exception Message : {} Exception : {}", ex.getMessage(), ex));
        }
        return null;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
