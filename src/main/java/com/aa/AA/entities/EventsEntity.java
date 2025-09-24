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
    private Long pkEventId;

    @JoinColumn(name = "pkLiquorStoreId",nullable = false)
    @OneToMany
    private LiquorStoreEntity liquorStoreEntity;

    @JoinColumn(name = "pkPrivilegeId",nullable = false)
    @OneToOne
    private PrivilegeEntity privilegeEntity;

    @OneToMany(mappedBy="eventsEntity", cascade = CascadeType.ALL)
    private List<EventAttendanceEntity> eventAttendanceEntites;

    private String eventName,eventTheme;

    private LocalDateTime eventCreatedDate, eventDate;

    private Byte eventStatus;


}
