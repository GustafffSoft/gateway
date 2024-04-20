package com.prueba.addressservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WasteAddressDTO {

    private String direccion;
    private Boolean isEnabled;
    private Long version;
    private Date createdDate;
    private Date lastModifiedDate;
}
