package com.prueba.addressservice.dto;

import com.prueba.addressservice.entity.WasteCenterAuthorization;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class WasteManagerDTO {

    private String nombre;
    private String nif;
    private List<WasteCenterAuthorizationDTO> listOfWasteCenterAuthorization;
    private Boolean isEnabled;
    private Long version;
    private Date createdDate;
    private Date lastModifiedDate;

}
