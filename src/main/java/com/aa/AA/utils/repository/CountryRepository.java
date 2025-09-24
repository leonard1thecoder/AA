package com.aa.AA.utils.repository;

import com.aa.AA.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,String> {
}
