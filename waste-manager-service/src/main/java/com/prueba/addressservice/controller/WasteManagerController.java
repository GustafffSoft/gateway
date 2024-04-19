package com.prueba.addressservice.controller;

import com.prueba.addressservice.entity.WasteManager;
import com.prueba.addressservice.model.WasteManagerAndAddressDTO;
import com.prueba.addressservice.service.WasteManagerService;
import com.prueba.addressservice.model.WasteAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/waste-manager")
public class WasteManagerController {

    @Autowired
    WasteManagerService wasteManagerService;

    @GetMapping
    public ResponseEntity<List<WasteManager>> getAll() {
        List<WasteManager> wasteManagers = wasteManagerService.getAll();
        if(wasteManagers.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(wasteManagers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteManager> getById(@PathVariable("id") Long id) {
        WasteManager wasteManager = wasteManagerService.getWasteManagerById(id);
        if(wasteManager == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(wasteManager);
    }

    @PostMapping("/save")
    public ResponseEntity<WasteManager> save(@RequestBody WasteManager wasteManager) {
        WasteManager wasteManagerNew = wasteManagerService.save(wasteManager);
        return ResponseEntity.ok(wasteManagerNew);
    }

//    @PostMapping("/save")
//    public ResponseEntity<WasteManager> save(@RequestBody WasteManagerAndAddressDTO dto) {
//        WasteManager savedWasteManager = wasteManagerService.save(dto);
//        return ResponseEntity.ok(savedWasteManager);
//    }

    @PostMapping("/savewasteaddress/{wasteManagerId}")
    public ResponseEntity<WasteAddress> saveWasteAddress(@PathVariable("wasteManagerId") Long wasteManagerId, @RequestBody WasteAddress wasteAddress) {
        if(wasteManagerService.getWasteManagerById(wasteManagerId) == null)
            return ResponseEntity.notFound().build();
        WasteAddress wasteAddressNew = wasteManagerService.saveWasteAddress(wasteManagerId, wasteAddress);
        return ResponseEntity.ok(wasteAddress);
    }

    @GetMapping("/getall/{wasteManagerId}")
    public ResponseEntity<Map<String, Object>> getAllWasteManagerAndAddress(@PathVariable("wasteManagerId") Long wasteManagerId) {
        Map<String, Object> result = wasteManagerService.getWasteManagerAndAddress(wasteManagerId);
        return ResponseEntity.ok(result);
    }

}
