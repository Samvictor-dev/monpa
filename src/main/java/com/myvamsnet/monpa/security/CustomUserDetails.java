package com.myvamsnet.monpa.security;

import com.myvamsnet.monpa.model.AccountStatus;
import com.myvamsnet.monpa.model.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

    }

    @Override
    public String getPassword() {

        return user.getPassword();

    }

    @Override
    @NullMarked
    public String getUsername() {

        return user.getEmail();

    }

    @Override
    public boolean isAccountNonExpired() {

        return true;

    }

    @Override
    public boolean isAccountNonLocked() {

        return user.getAccountStatus() == AccountStatus.ACTIVE;

    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;

    }

    @Override
    public boolean isEnabled() {

        return user.getAccountStatus() == AccountStatus.ACTIVE;

    }

}
