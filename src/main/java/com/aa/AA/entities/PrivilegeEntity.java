package com.aa.AA.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@ToString

@NoArgsConstructor
public class PrivilegeEntity {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int privilegeId;
    @Setter
    @Getter
    private String privilegeName;
    /*
        This byte will be used to determine whether privilege is ready to used
     */
    @Setter
    @Getter
    private byte privilegeStatus;

}
