package com.prueba.addressservice.controller;

import com.prueba.addressservice.entity.WasteManager;
import com.prueba.addressservice.service.WasteManagerService;
import com.prueba.addressservice.model.Car;
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
    public ResponseEntity<WasteManager> getById(@PathVariable("id") int id) {
        WasteManager wasteManager = wasteManagerService.getWasteManagerById(id);
        if(wasteManager == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(wasteManager);
    }

    @PostMapping()
    public ResponseEntity<WasteManager> save(@RequestBody WasteManager wasteManager) {
        WasteManager wasteManagerNew = wasteManagerService.save(wasteManager);
        return ResponseEntity.ok(wasteManagerNew);
    }

    @GetMapping("/cars/{wasteManagerId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable("wasteManagerId") int wasteManagerId) {
        WasteManager wasteManager = wasteManagerService.getWasteManagerById(wasteManagerId);
        if(wasteManager == null)
            return ResponseEntity.notFound().build();
        List<Car> cars = wasteManagerService.getCars(wasteManagerId);
        return ResponseEntity.ok(cars);
    }

    @PostMapping("/savecar/{wasteManagerId}")
    public ResponseEntity<Car> saveCar(@PathVariable("wasteManagerId") int wasteManagerId, @RequestBody Car car) {
        if(wasteManagerService.getWasteManagerById(wasteManagerId) == null)
            return ResponseEntity.notFound().build();
        Car carNew = wasteManagerService.saveCar(wasteManagerId, car);
        return ResponseEntity.ok(car);
    }

    @GetMapping("/getAll/{wasteManagerId}")
    public ResponseEntity<Map<String, Object>> getAllVehicles(@PathVariable("wasteManagerId") int wasteManagerId) {
        Map<String, Object> result = wasteManagerService.getWasteManagerAndAddress(wasteManagerId);
        return ResponseEntity.ok(result);
    }

}
