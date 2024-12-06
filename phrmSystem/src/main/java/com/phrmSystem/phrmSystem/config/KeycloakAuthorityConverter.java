package com.phrmSystem.phrmSystem.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {

        Map<String, Object> realm = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realm == null || realm.isEmpty()) {
            return new ArrayList();
        }

        Collection<GrantedAuthority> authorities = ((List<String>) realm.get("roles"))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }
}
