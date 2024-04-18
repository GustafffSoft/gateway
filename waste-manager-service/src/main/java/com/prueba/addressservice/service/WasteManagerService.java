package com.prueba.addressservice.service;

import com.prueba.addressservice.entity.WasteManager;
import com.prueba.addressservice.feignclients.WasteAddressFeignClient;
import com.prueba.addressservice.model.Car;
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

    public List<Car> getCars(int wasteManagerId) {
        List<Car> cars = restTemplate.getForObject("http://car-service/car/byuser/" + wasteManagerId, List.class);
        return cars;
    }



    public Car saveCar(int wasteManagerId, Car car) {
        car.setWasteManagerId(wasteManagerId);
        Car carNew = wasteAddressFeignClient.save(car);
        return carNew;
    }



    public Map<String, Object> getWasteManagerAndAddress(int wasteManagerId) {
        Map<String, Object> result = new HashMap<>();
        WasteManager wasteManager = wasteManagerRepository.findById(wasteManagerId).orElse(null);
        if(wasteManager == null) {
            result.put("Mensaje", "no existe el usuario");
            return result;
        }
        result.put("Waste Manager", wasteManager);
        List<Car> cars = wasteAddressFeignClient.getCars(wasteManagerId);
        if(cars.isEmpty())
            result.put("Waste Address", "ese waste manager no tiene coches");
        else
            result.put("WasteAddress", cars);
        return result;
    }
}
