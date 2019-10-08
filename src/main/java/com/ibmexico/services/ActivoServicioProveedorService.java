package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import com.ibmexico.entities.ActivoServicioProveedorEntity;
import com.ibmexico.repositories.IActivoServicioProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service("activo_servicio_proveedor")
public class ActivoServicioProveedorService {

    @Autowired
    @Qualifier("activoServicioProveedorRepository")
    private IActivoServicioProveedorRepository aSProveedorRep;


    public JsonObject jsonlstServicioProveedor(int idTipoActivo, int idServicioActivo){
        JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
        JsonArrayBuilder jsonRows = Json.createArrayBuilder();
        List<ActivoServicioProveedorEntity> lstServicioProv=aSProveedorRep.findByActivoServicio(idTipoActivo, idServicioActivo);

        lstServicioProv.forEach((item)->{ 

            jsonRows.add(Json.createObjectBuilder()
            .add("id_servicio_proveedor", item.getIdServicioProveedor())
            .add("id_servicio", item.getActivoServicio()!=null ? item.getActivoServicio().getIdServicioActivo() : 0)
            .add("id_proveedor", item.getActivoProveedor()!=null ? item.getActivoProveedor().getIdProveedorServicio() : 0)
            .add("nombre_proveedor",item.getActivoProveedor().getProveedor()!=null ? item.getActivoProveedor().getProveedor() :"")
            .add("nombre_servicio", item.getActivoServicio()!=null ? item.getActivoServicio().getDescripcion() : ""));

         });
        jsonReturn.add("rows", jsonRows);
        return  jsonReturn.build();
    }

    

}