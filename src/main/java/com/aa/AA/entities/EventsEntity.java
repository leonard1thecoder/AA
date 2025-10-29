package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class EventsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;

    @JoinColumn(name = "pkLiquorStoreId",nullable = true)
    @ManyToOne
    private RetailCompanyEntity retailCompanyEntity;

    @JoinColumn(name = "pkPrivilegeId",nullable = true)
    @OneToOne
    private PrivilegeEntity privilegeEntity;

    @OneToMany(mappedBy="eventsEntity", cascade = CascadeType.ALL)
    private List<EventAttendanceEntity> eventAttendanceEntites;

    private String eventName,eventTheme;

    private LocalDateTime eventCreatedDate, eventDate;

    private Byte eventStatus;


}
