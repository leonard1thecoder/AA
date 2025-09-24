package com.aa.AA.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private String eventName,eventTheme;

    private LocalDateTime eventCreatedDate, eventDate;

    private Byte eventStatus;


}
