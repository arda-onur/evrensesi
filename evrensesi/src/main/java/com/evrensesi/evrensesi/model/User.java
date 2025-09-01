package com.evrensesi.evrensesi.model;

import com.evrensesi.evrensesi.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Entity
@Table(name = "users")
@Setter
public class User implements UserDetails {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;
    @Getter
    @Column(nullable=false, unique=true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role = Role.USER;

    @Column(nullable=false)
    private boolean enabled = true;

    @Column(nullable=false)
    private boolean accountNonLocked = true;

    @Column(nullable=false)
    private boolean accountNonExpired = true;

    @Column(nullable=false)
    private boolean credentialsNonExpired = true;

    @OneToOne
    @JoinColumn(name = "comment_id", unique = true)
    private Comment comment;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override public boolean isEnabled() { return enabled; }
    @Override public boolean isAccountNonLocked() { return accountNonLocked; }
    @Override public boolean isAccountNonExpired() { return accountNonExpired; }
    @Override public boolean isCredentialsNonExpired() { return credentialsNonExpired; }
}


