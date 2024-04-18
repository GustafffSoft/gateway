package com.prueba.addressservice.feignclients;

import com.prueba.addressservice.model.WasteAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "waste-address-service")
@RequestMapping("/wasteaddress")
public interface WasteAddressFeignClient {

    @PostMapping()
    WasteAddress save(@RequestBody WasteAddress wasteAddress);

    @GetMapping("/bywastemanager/{wasteManagerId}")
    List<WasteAddress> getWasteAddress(@PathVariable("wasteManagerId") int wasteManagerId);
}
