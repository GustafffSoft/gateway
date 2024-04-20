package com.prueba.wasteservice.service;

import com.prueba.wasteservice.entity.WasteAddress;
import com.prueba.wasteservice.repository.WasteAddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class WasteAddressService {

    private static final Logger log = LoggerFactory.getLogger(WasteAddressService.class);

    @Autowired
    WasteAddressRepository wasteAddressRepository;

    public WasteAddress getWasteAddressById(Long id) {
        return wasteAddressRepository.findById(id).orElse(null);
    }

    public WasteAddress save(WasteAddress wasteAddress) {
        try {
            WasteAddress savedWasteAddress = wasteAddressRepository.save(wasteAddress);

            log.info("WasteManager saved successfully: {}", savedWasteAddress);
            return savedWasteAddress;

        }catch (DataAccessException e) {
            log.error("Error saving WasteManager: {}", wasteAddress, e);
            throw new RuntimeException("Failed to save WasteManager", e);
        } catch (Exception e) {
            log.error("Unexpected error when saving WasteManager: {}", wasteAddress, e);
            throw e;
        }
    }

    public WasteAddress updateWasteAddress(Long id, WasteAddress wasteAddressDetails) {
        try {
            log.info("Attempting to update wasteAddress with id: {}", id);
            WasteAddress wasteAddress = wasteAddressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("WasteAddress not found with id " + id));

            log.info("Found WasteAddress with id: {}, proceeding with update", id);

            if (wasteAddressDetails.getDireccion() != null && !wasteAddressDetails.getDireccion().isEmpty()) {
                wasteAddress.setDireccion(wasteAddressDetails.getDireccion());
            }
            if (wasteAddressDetails.getIsEnabled() != null) {
                wasteAddress.setIsEnabled(wasteAddressDetails.getIsEnabled());
            }
            if (wasteAddressDetails.getVersion() != null) {
                wasteAddress.setVersion(wasteAddressDetails.getVersion());
            }

            WasteAddress updatedWasteAddress = wasteAddressRepository.save(wasteAddress);
            log.info("wasteAddress with id: {} updated successfully", id);
            return updatedWasteAddress;
        } catch (Exception e) {
            log.error("Error updating wasteAddress with id: {}, Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error updating wasteAddress with id " + id, e);
        }
    }


    public void deleteWasteAddress(Long id) {
        try {
            WasteAddress wasteAddress = wasteAddressRepository.findById(id).orElse(null);
            if (wasteAddress == null) {
                log.error("No WasteAddress found with id: {}", id);
                throw new RuntimeException("No WasteAddress found with id " + id);
            }

            wasteAddressRepository.delete(wasteAddress);
            log.info("WasteAddress with id: {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Error deleting WasteAddress with id: {}, Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error deleting WasteAddress with id " + id, e);
        }
    }

}
