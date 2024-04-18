package com.prueba.addressservice.service;

import com.prueba.addressservice.entity.WasteManager;
import com.prueba.addressservice.feignclients.WasteAddressFeignClient;
import com.prueba.addressservice.model.WasteAddress;
import com.prueba.addressservice.repository.WasteManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WasteManagerService {

    @Autowired
    WasteManagerRepository wasteManagerRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WasteAddressFeignClient wasteAddressFeignClient;


    public List<WasteManager> getAll() {
        return wasteManagerRepository.findAll();
    }

    public WasteManager getWasteManagerById(int id) {
        return wasteManagerRepository.findById(id).orElse(null);
    }

    public WasteManager save(WasteManager wasteManager) {
        WasteManager wasteManagerNew = wasteManagerRepository.save(wasteManager);
        return wasteManagerNew;
    }

    public List<WasteAddress> getWasteAddress(int wasteManagerId) {
        List<WasteAddress> wasteAddresses = restTemplate.getForObject("http://waste-address-service/wasteaddress/bywastemanager/" + wasteManagerId, List.class);
        return wasteAddresses;
    }



    public WasteAddress saveWasteAddress(int wasteManagerId, WasteAddress wasteAddress) {
        wasteAddress.setWasteManagerId(wasteManagerId);
        WasteAddress wasteAddressNew = wasteAddressFeignClient.save(wasteAddress);
        return wasteAddressNew;
    }



    public Map<String, Object> getWasteManagerAndAddress(int wasteManagerId) {
        Map<String, Object> result = new HashMap<>();
        WasteManager wasteManager = wasteManagerRepository.findById(wasteManagerId).orElse(null);
        if(wasteManager == null) {
            result.put("Mensaje", "no existe el usuario");
            return result;
        }
        result.put("Waste Manager", wasteManager);
        List<WasteAddress> wasteAddresses = wasteAddressFeignClient.getWasteAddress(wasteManagerId);
        if(wasteAddresses.isEmpty())
            result.put("Waste Address", "ese waste manager no tiene coches");
        else
            result.put("WasteAddress", wasteAddresses);
        return result;
    }
}
