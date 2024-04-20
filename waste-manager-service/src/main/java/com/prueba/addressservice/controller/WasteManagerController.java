package com.prueba.addressservice.controller;

import com.prueba.addressservice.dto.CombinedDTO;
import com.prueba.addressservice.entity.WasteManager;
import com.prueba.addressservice.service.WasteManagerService;
import com.prueba.addressservice.model.WasteAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (wasteManagers.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(wasteManagers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteManager> getById(@PathVariable("id") Long id) {
        WasteManager wasteManager = wasteManagerService.getWasteManagerById(id);
        if (wasteManager == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(wasteManager);
    }

    @PostMapping("/save")
    public ResponseEntity<WasteManager> save(@RequestBody WasteManager wasteManager) {
        WasteManager wasteManagerNew = wasteManagerService.save(wasteManager);
        return ResponseEntity.ok(wasteManagerNew);
    }

    @PostMapping("/savewasteaddress/{wasteManagerId}")
    public ResponseEntity<WasteAddress> saveWasteAddress(@PathVariable("wasteManagerId") Long wasteManagerId, @RequestBody WasteAddress wasteAddress) {
        if (wasteManagerService.getWasteManagerById(wasteManagerId) == null)
            return ResponseEntity.notFound().build();
        WasteAddress wasteAddressNew = wasteManagerService.saveWasteAddress(wasteManagerId, wasteAddress);
        return ResponseEntity.ok(wasteAddress);
    }

    @GetMapping("/getall/{wasteManagerId}")
    public ResponseEntity<CombinedDTO> getAllWasteManagerAndAddress(@PathVariable("wasteManagerId") Long wasteManagerId) {
        CombinedDTO result = wasteManagerService.getWasteManagerAndAddress(wasteManagerId);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateWasteManager(@PathVariable Long id, @RequestBody WasteManager wasteManager) {
        try {
            WasteManager updatedWasteManager = wasteManagerService.updateWasteManager(id, wasteManager);
            return ResponseEntity.ok(updatedWasteManager);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWasteManager(@PathVariable Long id) {
        try {
            wasteManagerService.deleteWasteManager(id);
            return ResponseEntity.ok().build();
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/update-address/{id}")
    public ResponseEntity<WasteAddress> updateWasteAddress(@PathVariable Long id, @RequestBody WasteAddress wasteAddress) {
        try {
            WasteAddress updatedWasteAddress = wasteManagerService.updateWasteAddress(id, wasteAddress);
            return ResponseEntity.ok(updatedWasteAddress);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
