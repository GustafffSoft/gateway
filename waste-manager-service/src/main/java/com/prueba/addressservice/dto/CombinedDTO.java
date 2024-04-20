package com.prueba.addressservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CombinedDTO {
    private WasteManagerDTO wasteManager;
    private WasteAddressDTO wasteAddress;
}
