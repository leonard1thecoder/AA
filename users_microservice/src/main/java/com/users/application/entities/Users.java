package com.users.application.entities;

import com.privileges.application.entity.Privileges;
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

    @OneToOne
    @JoinColumn(name = "fk_privilege_id", nullable = false)
    private Privileges privileges;

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
if (privileges == null) {
        throw new IllegalStateException("User has no associated privilege");
    }

    String roleName = switch (privileges.getId()) {  
        case 1  -> "Alcohol Agent";
        case 2  -> "Alcohol Agent Artists";
        case 3  -> "Alcohol Agent Event hosts";
        case 4  -> "Alcohol Agent Retails";
        default  -> throw new IllegalArgumentException(
            "Incorrect privilege: " + privileges.getId());
    };

    return List.of(new SimpleGrantedAuthority(roleName));
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
