package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@ToString
@Setter
@Getter
@NoArgsConstructor
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pkUsersId,usersIdentityNo;
    private Integer ftPrivilegeId;
    private Short usersStatus,usersAge;
    private String usersFullName, usersEmailAddress, usersPassword, usersCountryName, usersRegistrationDate, usersLanguage, usersModifiedDate;


}
