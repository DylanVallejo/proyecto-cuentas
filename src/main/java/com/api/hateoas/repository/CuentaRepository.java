package com.api.hateoas.repository;

import com.api.hateoas.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaRepository  extends JpaRepository<Cuenta, Integer> {


}
