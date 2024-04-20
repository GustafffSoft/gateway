package com.prueba.addressservice.service;

import com.prueba.addressservice.entity.WasteManager;
import com.prueba.addressservice.feignclients.WasteAddressFeignClient;
import com.prueba.addressservice.model.WasteAddress;
import com.prueba.addressservice.repository.WasteManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WasteManagerService {
    private static final Logger log = LoggerFactory.getLogger(WasteManagerService.class);

    @Autowired
    WasteManagerRepository wasteManagerRepository;


    @Autowired
    WasteAddressFeignClient wasteAddressFeignClient;


    public List<WasteManager> getAll() {
        return wasteManagerRepository.findAll();
    }

    public WasteManager getWasteManagerById(Long id) {
        return wasteManagerRepository.findById(id).orElse(null);
    }

    public WasteManager save(WasteManager wasteManager) {
        try {
            WasteManager savedWasteManager = wasteManagerRepository.save(wasteManager);
            log.info("WasteManager saved successfully: {}", savedWasteManager);
            return savedWasteManager;
        } catch (DataAccessException e) {
            log.error("Error saving WasteManager: {}", wasteManager, e);
            throw new RuntimeException("Failed to save WasteManager", e);
        } catch (Exception e) {
            log.error("Unexpected error when saving WasteManager: {}", wasteManager, e);
            throw e;
        }
    }

//    @Transactional
//    public WasteManager save(WasteManagerAndAddressDTO dto) {
//        WasteManager wasteManager = dto.getWasteManager();
//        WasteAddress wasteAddress = dto.getWasteAddress();
//        WasteManager savedWasteManager = wasteManagerRepository.save(wasteManager);
//        wasteAddress.setWasteManagerId(savedWasteManager.getId());
//        WasteAddress savedWasteAddress = wasteAddressFeignClient.save(wasteAddress);
//        return savedWasteManager;
//    }

    public WasteAddress saveWasteAddress(Long wasteManagerId, WasteAddress wasteAddress) {
        wasteAddress.setWasteManagerId(wasteManagerId);
        WasteAddress wasteAddressNew = wasteAddressFeignClient.save(wasteAddress);
        return wasteAddressNew;
    }


    public Map<String, Object> getWasteManagerAndAddress(Long wasteManagerId) {
        Map<String, Object> result = new HashMap<>();
        WasteManager wasteManager = wasteManagerRepository.findById(wasteManagerId).orElse(null);

        result.put("Waste Manager", wasteManager);

        try {
            WasteAddress wasteAddress = wasteAddressFeignClient.getWasteAddress(wasteManagerId);
            if (wasteAddress == null) {
                result.put("Waste Address", "No hay Waste Address asociados a este Waste Manager");
            } else {
                result.put("Waste Address", wasteAddress);
            }
        } catch (Exception e) {
            result.put("Waste Address Error", "Error: " + e.getMessage());
        }

        return result;
    }
}
