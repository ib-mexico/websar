package com.ibmexico.services;

import java.util.List;

import com.ibmexico.entities.ActivoServicioProveedorEntity;
import com.ibmexico.repositories.IActivoServicioProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ActivoServicioProveedorService {

    @Autowired
    @Qualifier("activoServiciorRepository")
    private IActivoServicioProveedorRepository aSProveedorRep;

    public List<ActivoServicioProveedorEntity> lstServicioProveedor(int idTipoActivo, int IdServicio){
        return aSProveedorRep.findByIdTipo(idTipoActivo, IdServicio);
    }
}