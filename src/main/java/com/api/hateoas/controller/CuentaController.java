package com.api.hateoas.controller;


import com.api.hateoas.model.Cuenta;
import com.api.hateoas.model.Monto;
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

import javax.swing.undo.CannotUndoException;
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
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("retiros"));
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

//              enlace para realizar depositos
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
//            enlace para realizar retiros
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("retiros"));
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

// le indicamos que se ha a gregado y vamos a adjuntar la cunta que se ha creado
        cuentaNueva.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaNueva.getId())).withSelfRel());
        cuentaNueva.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaNueva.getId(), null)).withRel("depositos"));
        cuentaNueva.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaNueva.getId(), null)).withRel("retiros"));
        cuentaNueva.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return  ResponseEntity.created(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaNueva.getId())).toUri()).body(cuentaNueva);
    }


//    como funciona el put wtf como sabe  a cual editar ???????????? no cacho
    @PutMapping
    public ResponseEntity<Cuenta> editarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaEditada = cuentaService.save(cuenta);

        cuentaEditada.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaEditada.getId())).withSelfRel());
        cuentaEditada.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaEditada.getId(), null)).withRel("depositos"));
        cuentaEditada.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaEditada.getId(), null)).withRel("retiros"));
        cuentaEditada.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return new ResponseEntity<>(cuentaEditada, HttpStatus.OK);
    }

// AQUI USARMAOS PatchMapping ES IGUAL QUE EL PUTMAPPING SE USA PARA ACTULIZAR *** UN SOLO CAMPO ***  EN ESTE CASO EL MONTO
//    estoy usando el id cmabiarlo por el numero de cuenta ya que para depositar uso el numero de cuenta no el id
    @PatchMapping("/{id}/deposito")
    public ResponseEntity<Cuenta> depositarDinero(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBBDD = cuentaService.depositar(monto.getMonto(), id);
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depositos"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return new ResponseEntity<>(cuentaBBDD, HttpStatus.OK);
    }

    @PatchMapping("/{id}/retiro")
    public ResponseEntity<Cuenta> retirarDinero(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBBDD = cuentaService.retirar(monto.getMonto(), id);

        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBBDD.getId())).withSelfRel());
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("depositos"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBBDD.getId(), null)).withRel("retiros"));
        cuentaBBDD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return new ResponseEntity<>(cuentaBBDD, HttpStatus.OK);
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
