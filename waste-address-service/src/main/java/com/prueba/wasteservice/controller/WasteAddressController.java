package com.prueba.wasteservice.controller;

import com.prueba.wasteservice.entity.WasteAddress;
import com.prueba.wasteservice.service.WasteAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/waste-address")
public class WasteAddressController {

    @Autowired
    WasteAddressService wasteAddressService;

    @GetMapping
    public ResponseEntity<List<WasteAddress>> getAll() {
        List<WasteAddress> wasteAddresses = wasteAddressService.getAll();
        if(wasteAddresses.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(wasteAddresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteAddress> getById(@PathVariable("id") int id) {
        WasteAddress wasteAddress = wasteAddressService.getWasteAddressById(id);
        if(wasteAddress == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(wasteAddress);
    }

    @PostMapping()
    public ResponseEntity<WasteAddress> save(@RequestBody WasteAddress wasteAddress) {
        WasteAddress wasteAddressNew = wasteAddressService.save(wasteAddress);
        return ResponseEntity.ok(wasteAddressNew);
    }

    @GetMapping("/byuser/{userId}")
    public ResponseEntity<List<WasteAddress>> getByUserId(@PathVariable("userId") int userId) {
        List<WasteAddress> wasteAddresses = wasteAddressService.byUserId(userId);
        return ResponseEntity.ok(wasteAddresses);
    }

}
