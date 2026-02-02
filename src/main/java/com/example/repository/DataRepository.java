package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.DataEntity;

public interface DataRepository extends JpaRepository<DataEntity, Long> {
    // Additional query methods can be defined here if needed
}