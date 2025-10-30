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
    private Long id;
//    @JoinColumn(name = "pkPrivilegeId", nullable = true)
//    @OneToOne
//    private PrivilegeEntity fkPrivilegeId;

    @Column(unique = true)


    private String userIdentityNo;

    @Column(unique = true)

    private String userEmailAddress;

    @Column(columnDefinition = "TEXT")
    private String userPassword;

    private Short userStatus, userAge;
    private String userFullName, userRegistrationDate, userModifiedDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      //  return List.of(new SimpleGrantedAuthority(fkPrivilegeId.privilegeName()));
      return null;
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
