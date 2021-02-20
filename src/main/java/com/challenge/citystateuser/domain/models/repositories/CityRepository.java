package com.challenge.citystateuser.domain.models.repositories;

import com.challenge.citystateuser.domain.models.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}
