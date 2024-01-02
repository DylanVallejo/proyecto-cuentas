package com.api.hateoas.service;


import com.api.hateoas.Exception.CuentasNotFoundException;
import com.api.hateoas.model.Cuenta;
import com.api.hateoas.repository.CuentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cuenta> listAll(){
        return cuentaRepository.findAll();
    }


    public  Cuenta getCuenta(Integer id){
        return cuentaRepository.findById(id).get();
    }


    public Cuenta save(Cuenta cuenta){
        return cuentaRepository.save(cuenta);
    }
// el metodo eliminar retorna un not found averiguar porque no lanza la exepcion
    public void delete(Integer id ) throws CuentasNotFoundException {
        if (!cuentaRepository.existsById(id)){
            throw new CuentasNotFoundException("Cuenta no encontrada con el ID: " + id);
        }
        cuentaRepository.deleteById(id);
    }


    public Cuenta depositar(float monto, Integer id){
        cuentaRepository.actualizarMonto(monto,id);
        return cuentaRepository.findById(id).get();
    }

    public Cuenta retirar(float monto, Integer id){
        cuentaRepository.actualizarMonto(-monto,id);
        return cuentaRepository.findById(id).get();
    }

}
