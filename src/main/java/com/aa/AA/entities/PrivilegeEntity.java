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

    public int privilegeId() {
        return privilegeId;
    }

    public PrivilegeEntity setPrivilegeId(int privilegeId) {
        this.privilegeId = privilegeId;
        return this;
    }

    public String privilegeName() {
        return privilegeName;
    }

    public PrivilegeEntity setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
        return this;
    }

    public byte privilegeStatus() {
        return privilegeStatus;
    }

    public PrivilegeEntity setPrivilegeStatus(byte privilegeStatus) {
        this.privilegeStatus = privilegeStatus;
        return this;
    }
}
