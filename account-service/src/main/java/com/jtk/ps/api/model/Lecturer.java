package com.jtk.ps.api.model;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "lecturer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lecturer {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "prodi_id")
    private EProdi prodi;

    @Column(name = "email")
    private String email;
}
