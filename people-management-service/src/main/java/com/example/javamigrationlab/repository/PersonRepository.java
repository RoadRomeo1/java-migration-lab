package com.example.javamigrationlab.repository;

import com.example.javamigrationlab.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
}
