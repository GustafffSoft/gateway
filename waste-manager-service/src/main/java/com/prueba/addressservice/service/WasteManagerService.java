package com.prueba.addressservice.service;

import com.prueba.addressservice.dto.CombinedDTO;
import com.prueba.addressservice.dto.WasteAddressDTO;
import com.prueba.addressservice.dto.WasteCenterAuthorizationDTO;
import com.prueba.addressservice.dto.WasteManagerDTO;
import com.prueba.addressservice.entity.WasteCenterAuthorization;
import com.prueba.addressservice.entity.WasteManager;
import com.prueba.addressservice.feignclients.WasteAddressFeignClient;
import com.prueba.addressservice.model.WasteAddress;
import com.prueba.addressservice.repository.WasteManagerRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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
            List<WasteCenterAuthorization> authorizations = wasteManager.getListOfWasteCenterAuthorization();
            for (WasteCenterAuthorization authorization : authorizations) {
                authorization.setWasteManager(wasteManager);
            }

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

    public WasteAddress saveWasteAddress(Long wasteManagerId, WasteAddress wasteAddress) {
        wasteAddress.setWasteManagerId(wasteManagerId);
        return wasteAddressFeignClient.save(wasteAddress);
    }


    public CombinedDTO getWasteManagerAndAddress(Long wasteManagerId) {
        WasteManager wasteManager = wasteManagerRepository.findById(wasteManagerId).orElse(null);
        WasteAddress wasteAddress = null;

        try {
            wasteAddress = wasteAddressFeignClient.getWasteAddress(wasteManagerId);
        } catch (FeignException.NotFound e) {
            log.info("No WasteAddress found for WasteManagerId: {}", wasteManagerId);
        } catch (Exception e) {
            log.error("Error searching for WasteAddress for WasteManagerId: {}", wasteManagerId, e);
        }

        CombinedDTO combinedDTO = new CombinedDTO();
        if (wasteManager != null) {
            combinedDTO.setWasteManager(convertToWasteManagerDTO(wasteManager));
        }
        if (wasteAddress != null) {
            combinedDTO.setWasteAddress(convertToWasteAddressDTO(wasteAddress));
        } else {
            // Opcionalmente establecer un objeto predeterminado o nulo en el DTO para indicar que no se encontraron datos
            combinedDTO.setWasteAddress(new WasteAddressDTO());
        }

        return combinedDTO;
    }

    private WasteManagerDTO convertToWasteManagerDTO(WasteManager wasteManager) {
        WasteManagerDTO dto = new WasteManagerDTO();
        dto.setNombre(wasteManager.getNombre());
        dto.setNif(wasteManager.getNif());
        dto.setIsEnabled(wasteManager.getIsEnabled());
        dto.setVersion(wasteManager.getVersion());
        dto.setCreatedDate(wasteManager.getCreatedDate());
        dto.setLastModifiedDate(wasteManager.getLastModifiedDate());
        dto.setListOfWasteCenterAuthorization(wasteManager.getListOfWasteCenterAuthorization().stream()
                .map(this::convertToAuthorizationDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private WasteCenterAuthorizationDTO convertToAuthorizationDTO(WasteCenterAuthorization authorization) {
        WasteCenterAuthorizationDTO dto = new WasteCenterAuthorizationDTO();
        dto.setAuthorizationNumber(authorization.getAuthorizationNumber());
        return dto;
    }

    private WasteAddressDTO convertToWasteAddressDTO(WasteAddress wasteAddress) {
        WasteAddressDTO dto = new WasteAddressDTO();
        dto.setDireccion(wasteAddress.getDireccion());
        dto.setIsEnabled(wasteAddress.getIsEnabled());
        dto.setVersion(wasteAddress.getVersion());
        dto.setCreatedDate(wasteAddress.getCreatedDate());
        dto.setLastModifiedDate(wasteAddress.getLastModifiedDate());
        return dto;
    }


    @Transactional
    public WasteManager updateWasteManager(Long id, WasteManager wasteManagerDetails) {
        try {
            log.info("Attempting to update WasteManager with id: {}", id);
            WasteManager wasteManager = wasteManagerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("WasteManager not found with id " + id));

            log.info("Found WasteManager with id: {}, proceeding with update", id);

            if (wasteManagerDetails.getNombre() != null && !wasteManagerDetails.getNombre().isEmpty()) {
                wasteManager.setNombre(wasteManagerDetails.getNombre());
            }
            if (wasteManagerDetails.getNif() != null && !wasteManagerDetails.getNif().isEmpty()) {
                wasteManager.setNif(wasteManagerDetails.getNif());
            }
            if (wasteManagerDetails.getIsEnabled() != null) {
                wasteManager.setIsEnabled(wasteManagerDetails.getIsEnabled());
            }

            if (wasteManagerDetails.getVersion() != null) {
                wasteManager.setVersion(wasteManagerDetails.getVersion());
            }

            if (wasteManagerDetails.getListOfWasteCenterAuthorization() != null) {
                updateAuthorizations(wasteManager, wasteManagerDetails.getListOfWasteCenterAuthorization());
            }

            WasteManager updatedWasteManager = wasteManagerRepository.save(wasteManager);
            log.info("WasteManager with id: {} updated successfully", id);
            return updatedWasteManager;
        } catch (Exception e) {
            log.error("Error updating WasteManager with id: {}, Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error updating WasteManager with id " + id, e);
        }
    }

    private void updateAuthorizations(WasteManager wasteManager, List<WasteCenterAuthorization> newAuthorizations) {
        try {
            List<WasteCenterAuthorization> currentAuthorizations = wasteManager.getListOfWasteCenterAuthorization();

            currentAuthorizations.removeIf(auth -> !newAuthorizations.contains(auth));

            for (WasteCenterAuthorization newAuth : newAuthorizations) {
                int index = currentAuthorizations.indexOf(newAuth);
                if (index >= 0) {
                    WasteCenterAuthorization currentAuth = currentAuthorizations.get(index);
                    currentAuth.setAuthorizationNumber(newAuth.getAuthorizationNumber());
                } else {
                    currentAuthorizations.add(newAuth);
                    newAuth.setWasteManager(wasteManager);
                }
            }

            log.info("Authorization update successful for WasteManager with id: {}", wasteManager.getId());
        } catch (Exception e) {
            log.error("Error updating authorizations for WasteManager with id: {}, Error: {}", wasteManager.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update authorizations for WasteManager with id " + wasteManager.getId(), e);
        }
    }

    public void deleteWasteManager(Long id) {
        try {
            WasteManager wasteManager = wasteManagerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("No WasteManager found with id: " + id));
            wasteManagerRepository.delete(wasteManager);
            log.info("WasteManager with id: {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Error deleting WasteManager with id: {}, Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error deleting WasteManager with id: " + id, e);
        }
    }

    public WasteAddress updateWasteAddress(Long id, WasteAddress wasteAddress) {
        return wasteAddressFeignClient.updateWasteAddress(id, wasteAddress);
    }
}
