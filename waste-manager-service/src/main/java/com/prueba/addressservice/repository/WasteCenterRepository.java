package com.prueba.addressservice.repository;

import com.prueba.addressservice.entity.WasteCenterAuthorization;
import com.prueba.addressservice.entity.WasteManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteCenterRepository extends JpaRepository<WasteCenterAuthorization, Long> {
}
