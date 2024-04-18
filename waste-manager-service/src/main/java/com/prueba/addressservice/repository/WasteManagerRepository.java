package com.prueba.addressservice.repository;

import com.prueba.addressservice.entity.WasteManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteManagerRepository extends JpaRepository<WasteManager, Integer> {
}
