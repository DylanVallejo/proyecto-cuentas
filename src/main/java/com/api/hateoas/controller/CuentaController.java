package com.api.hateoas.controller;


import com.api.hateoas.model.Cuenta;
import com.api.hateoas.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
//importando de esta forma podemos reducir la cantidad de codigo
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService;



    @GetMapping
    public ResponseEntity<List<Cuenta>> listarCuentas(){
        List<Cuenta> cuentas = cuentaService.listAll();

        if(cuentas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        for (Cuenta cuenta:cuentas){
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        }
        CollectionModel<Cuenta> modelo = CollectionModel.of(cuentas);
        modelo.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withSelfRel());
        return new ResponseEntity<>(cuentas, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> listarCuenta(@PathVariable Integer id ){
        try {
            Cuenta cuenta = cuentaService.getCuenta(id);
//            indicamos con add que agregaremos enlaces en la respuesta json y llamamaos al metodo linkTo  withSelfRel
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());

//            indicamos una relacion no propia que retornara una coleccion de enlaces
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
//            cuenta.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }catch (Exception exception){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Cuenta> guardarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaNueva = cuentaService.save(cuenta);
        return new ResponseEntity<>(cuenta, HttpStatus.CREATED);
    }


//    como funciona el put wtf como sabe  a cual editar ???????????? no cacho
    @PutMapping
    public ResponseEntity<Cuenta> editarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaEditada = cuentaService.save(cuenta);
        return new ResponseEntity<>(cuentaEditada, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminaCuenta(@PathVariable Integer id){

        try{
            cuentaService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception exception){
            return ResponseEntity.notFound().build();
        }
    }



}
