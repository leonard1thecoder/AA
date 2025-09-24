package com.aa.AA.utils.repository;

import com.aa.AA.entities.EventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface EventRepository extends JpaRepository<EventsEntity,Long> {
}
