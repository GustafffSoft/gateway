package com.prueba.wasteservice.repository;

import com.prueba.wasteservice.entity.WasteAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteAddressRepository extends JpaRepository<WasteAddress, Integer> {

    List<WasteAddress> findByUserId(int userId);
}
