package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@ToString
@Setter
@Getter
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkUsersId;
    private Integer ftPrivilegeId;
    private Short usersStatus;
    private String usersFullName, usersEmailAddress, usersPassword, usersCountryName, usersRegistrationDate, usersLanguage, usersModifiedDate;


}
