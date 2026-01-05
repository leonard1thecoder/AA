package com.users.application.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"userFullName","userIdentityNo"})
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(nullable = false)
    private Long id;
    @Column( nullable = false)
    private Integer fk_privilege_id;

    @Column(unique = true,nullable = false)

    private String userIdentityNo, userCellphoneNo;

    @Column(unique = true,nullable = false)
    private String userEmailAddress;
     
    @Column(columnDefinition = "TEXT",nullable = false)
    private String userPassword;
    @Column(columnDefinition = "TEXT")
    private String previousPassword;
    @Column(nullable = false)
    private Short userStatus, userAge;
    @Column(nullable = false, unique = true)

    private String userFullName;
    @Column(nullable = false)

    private String userRegistrationDate, userModifiedDate;
    private Short passwordUpdateStatus;
    @Column(columnDefinition = "TEXT")
    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(fk_privilege_id == 1)
        return List.of(new SimpleGrantedAuthority("Alcohol Agent"));
        else if (fk_privilege_id == 2)
             return List.of(new SimpleGrantedAuthority("Alcohol Agent Artists"));
        else if (fk_privilege_id == 3)
            return List.of(new SimpleGrantedAuthority("Alcohol Agent Event hosts"));
        else if(fk_privilege_id == 4)
             return List.of(new SimpleGrantedAuthority("Alcohol Agent Retails"));
        else
            throw new IllegalArgumentException("Incorrect privilege");
    }

    @Override
    public String getPassword() {
        return this.getUserPassword();
    }

    @Override
    public String getUsername() {
        return this.getUserEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
