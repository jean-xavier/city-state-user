package com.challenge.citystateuser.domain.models.repositories;

import com.challenge.citystateuser.domain.models.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRespository extends JpaRepository<State, Long> {
}
