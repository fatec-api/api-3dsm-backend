package br.com.jth.gateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KeycloakJwtReactiveConverter {

    public ReactiveJwtAuthenticationConverterAdapter getConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    private static class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        @SuppressWarnings("unchecked")
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            
            if (realmAccess == null || realmAccess.isEmpty()) {
                return Collections.emptyList();
            }

            List<String> roles = (List<String>) realmAccess.get("roles");
            
            return roles.stream()
                    .map(roleName -> "ROLE_" + roleName) 
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}