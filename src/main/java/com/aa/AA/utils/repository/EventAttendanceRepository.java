package com.aa.AA.utils.repository;

import com.aa.AA.entities.EventAttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface EventAttendanceRepository extends JpaRepository<EventAttendanceEntity,Long> {
}
