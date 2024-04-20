package com.prueba.addressservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WasteManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String nif;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "wasteManager")
    private List<WasteCenterAuthorization> listOfWasteCenterAuthorization = new ArrayList<>();
    private Boolean isEnabled = Boolean.TRUE;
    private long version = 0L;
    private Date createdDate;
    private Date lastModifiedDate;
}
