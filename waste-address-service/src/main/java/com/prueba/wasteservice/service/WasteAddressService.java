package com.prueba.wasteservice.service;

import com.prueba.wasteservice.entity.WasteAddress;
import com.prueba.wasteservice.repository.WasteAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasteAddressService {

    @Autowired
    WasteAddressRepository wasteAddressRepository;

    public List<WasteAddress> getAll() {
        return wasteAddressRepository.findAll();
    }

    public WasteAddress getWasteAddressById(int id) {
        return wasteAddressRepository.findById(id).orElse(null);
    }

    public WasteAddress save(WasteAddress wasteAddress) {
        WasteAddress wasteAddressNew = wasteAddressRepository.save(wasteAddress);
        return wasteAddressNew;
    }

    public List<WasteAddress> byUserId(int userId) {
        return wasteAddressRepository.findByUserId(userId);
    }
}
