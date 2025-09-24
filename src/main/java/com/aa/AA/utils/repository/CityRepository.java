package com.aa.AA.utils.repository;

import com.aa.AA.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CityRepository extends JpaRepository<City,Integer> {
}
