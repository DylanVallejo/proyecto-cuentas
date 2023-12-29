package com.api.hateoas;


import com.api.hateoas.model.Cuenta;
import com.api.hateoas.repository.CuentaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Rollback(value = true)
public class CuentasRepositoryTest {

//    verificar como hacer las pruebas unitarias para la creacion de una cuenta nueva

//    @Autowired
//    private CuentaRepository cuentaRepository;
//
//    @Test
//    void testAgregarCuenta(){
//        Cuenta cuenta = new Cuenta(1223,"2333410");
//        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
//
//
//        Assertions.assertThat( cuentaGuardada).isNotNull();
//        Assertions.assertThat(cuentaGuardada.getId()).isGreaterThan(0);
//    }

}
