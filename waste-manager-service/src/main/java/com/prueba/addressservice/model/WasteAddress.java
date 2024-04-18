package com.prueba.addressservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WasteAddress {
    private String brand;
    private String model;
    private int wasteManagerId;
}
