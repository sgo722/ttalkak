package com.ttalkak.user.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


public record CustomOAuth2User(User user) implements OAuth2User, UserDetails {
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("username", user.getUsername(), "authorities", getAuthorities());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) () -> user.getUserRole().name());
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public String getName() {
        return user.getUsername();
    }

    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
//        return !user.isLocked();
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return !user.isLocked();
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return !user.isLocked();
        return false;
    }

    @Override
    public boolean isEnabled() {
//        return !user.isLocked() && user.isMattermostConfirmed();
        return true;
    }
}
