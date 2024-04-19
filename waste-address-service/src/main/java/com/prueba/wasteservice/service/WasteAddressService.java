package com.prueba.wasteservice.service;

import com.prueba.wasteservice.entity.WasteAddress;
import com.prueba.wasteservice.repository.WasteAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class WasteAddressService {

    @Autowired
    WasteAddressRepository wasteAddressRepository;

    public List<WasteAddress> getAll() {
        return wasteAddressRepository.findAll();
    }

    public WasteAddress getWasteAddressById(Long id) {
        return wasteAddressRepository.findById(id).orElse(null);
    }

    public WasteAddress save(WasteAddress wasteAddress) {
        return wasteAddressRepository.save(wasteAddress);
    }

    public WasteAddress updateWasteAddress(Long id, WasteAddress wasteAddressDetails) throws Exception {
        WasteAddress existingWasteAddress = wasteAddressRepository.findById(id).orElse(null);

        boolean updateNeeded = false;

        if (!Objects.equals(existingWasteAddress.getDireccion(), wasteAddressDetails.getDireccion())) {
            existingWasteAddress.setDireccion(wasteAddressDetails.getDireccion());
            updateNeeded = true;
        }

        if (!Objects.equals(existingWasteAddress.getIsEnabled(), wasteAddressDetails.getIsEnabled())) {
            existingWasteAddress.setIsEnabled(wasteAddressDetails.getIsEnabled());
            updateNeeded = true;
        }

        if (updateNeeded) {
            existingWasteAddress.setLastModifiedDate(new Date());
            return wasteAddressRepository.save(existingWasteAddress);
        } else {
            throw new Exception("No updates were performed as no changes were detected.");
        }
    }


    public void deleteWasteAddress(Long id) {
        WasteAddress wasteAddress = wasteAddressRepository.findById(id).orElse(null);
        wasteAddressRepository.delete(wasteAddress);
    }

}
