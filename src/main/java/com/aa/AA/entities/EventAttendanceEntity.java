package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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


    @JoinColumn(name= "pkUsersId",nullable = true)
    @ManyToOne
    private UsersEntity usersEntity;

    @JoinColumn(name="pkEventId",nullable = true)
    @ManyToOne
    private EventsEntity eventsEntity;

    private Byte attendanceStatus;

}
