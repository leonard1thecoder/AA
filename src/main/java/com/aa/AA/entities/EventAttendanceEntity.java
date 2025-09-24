package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class EventAttendanceEntity {

    @Id
    @GeneratedValue
    private Long eventAttendanceId;


    @JoinColumn(name= "",nullable = false)
    @ManyToMany
    private UsersEntity usersEntity;

    @JoinColumn(name="pkEventId",nullable = false)
    @ManyToOne
    private EventsEntity eventsEntity;

    private Byte attendanceStatus;

}
