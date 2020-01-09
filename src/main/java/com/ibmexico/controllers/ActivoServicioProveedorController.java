package com.ibmexico.controllers;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.ActivoServicioProveedorEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.services.ActivoServicioProveedorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("controlPanel/servicioProveedor")
public class ActivoServicioProveedorController{

    @Autowired
    @Qualifier("activo_servicio_proveedor")
    private ActivoServicioProveedorService actServProvService;

    //Eliminar servicio - proveedor, el registro de la BD
    @RequestMapping(value = {"{idServicioProveedor}/delete","{idServicioProveedor}/delete/"} ,method = RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable(name = "idServicioProveedor")int idServicioProveedor){
        Boolean respuesta = false;
        String titulo = "Oops!";
        String mensaje = "Ocurrió un error al intentar desvincular este Servicio.";
        ActivoServicioProveedorEntity objServicioProveedor =  actServProvService.findByIdServicioProveedor(idServicioProveedor);
        System.err.println(objServicioProveedor +"valor seleccionado");
        try {
            if(objServicioProveedor != null){
                actServProvService.delete(objServicioProveedor);
                respuesta = true;
                titulo = "Eliminado!";
                mensaje = "La asociación del servicio con su proveedor ha sido eliminado exitosamente.";
            }else{
                respuesta = false;
                throw new ApplicationException(EnumException.ACTIVO_SERVICIO_DELETE_001);
            }
        } catch (ApplicationException e) {
            respuesta = false;
            throw new ApplicationException(EnumException.ACTIVO_SERVICIO_DELETE_001);
        }
        JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
        jsonReturn	.add("respuesta", respuesta)
                    .add("titulo", titulo)
                    .add("mensaje", mensaje);							
        return jsonReturn.build().toString();
       }

}