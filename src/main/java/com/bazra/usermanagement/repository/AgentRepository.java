package com.bazra.usermanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazra.usermanagement.model.AgentInfo;


@Repository
public interface AgentRepository extends JpaRepository<AgentInfo, Long> {
	Optional<AgentInfo> findByUsername(String username);

}
