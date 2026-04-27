package br.com.jth.servico_gestao.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt src) {
        Map<String, Object> realmAccess = (Map<String, Object>) src.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }
        return ((List<String>) realmAccess.get("roles")).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}