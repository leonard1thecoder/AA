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


    @JoinColumn(name= "",nullable = true)
    @OneToMany
    private List<UsersEntity> usersEntity;

    @JoinColumn(name="pkEventId",nullable = true)
    @ManyToOne
    private EventsEntity eventsEntity;

    private Byte attendanceStatus;

}
