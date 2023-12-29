package com.api.hateoas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false, unique = true)
    private String numeroDeCuenta;

    private float balance;

    public Cuenta(Integer id, String numeroDeCuenta) {
        this.id = id;
        this.numeroDeCuenta = numeroDeCuenta;
    }
}
