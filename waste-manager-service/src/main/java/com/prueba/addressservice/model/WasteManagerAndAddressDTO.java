package com.prueba.addressservice.model;

import com.prueba.addressservice.entity.WasteManager;
import lombok.Data;

@Data
public class WasteManagerAndAddressDTO {

    private WasteManager wasteManager;
    private WasteAddress wasteAddress;
}
