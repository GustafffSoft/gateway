package com.prueba.addressservice.feignclients;

import com.prueba.addressservice.model.WasteAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "waste-address-service")
@RequestMapping("/waste-address")
public interface WasteAddressFeignClient {

    @PostMapping("/save")
    WasteAddress save(@RequestBody WasteAddress wasteAddress);

    @GetMapping("/bywastemanager/{wasteManagerId}")
    WasteAddress getWasteAddress(@PathVariable("wasteManagerId") Long wasteManagerId);
}
