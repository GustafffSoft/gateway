package com.prueba.wasteservice.controller;

import com.prueba.wasteservice.entity.WasteAddress;
import com.prueba.wasteservice.service.WasteAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waste-address")
public class WasteAddressController {

    @Autowired
    WasteAddressService wasteAddressService;

    @GetMapping("/{id}")
    public ResponseEntity<WasteAddress> getById(@PathVariable("id") Long id) {
        WasteAddress wasteAddress = wasteAddressService.getWasteAddressById(id);
        if(wasteAddress == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(wasteAddress);
    }

    @PostMapping("/save")
    public ResponseEntity<WasteAddress> save(@RequestBody WasteAddress wasteAddress) {
        WasteAddress wasteAddressNew = wasteAddressService.save(wasteAddress);
        return ResponseEntity.ok(wasteAddressNew);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateWasteAddress(@PathVariable Long id, @RequestBody WasteAddress wasteAddress) {
        try {
            WasteAddress updatedWasteAddress = wasteAddressService.updateWasteAddress(id, wasteAddress);
            return ResponseEntity.ok(updatedWasteAddress);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWasteAddress(@PathVariable Long id) {
        try {
            wasteAddressService.deleteWasteAddress(id);
            return ResponseEntity.ok().build();
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
