package com.ibmexico.services;

import java.util.List;

import com.ibmexico.entities.ActivoEstatusEntity;
import com.ibmexico.repositories.IActivoEstatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("activo_estatus_service")
public class ActivoEstatusService{

    @Autowired
    @Qualifier("activo_estatus_repository")
    private IActivoEstatusRepository activoEstatus;

    public ActivoEstatusEntity findByIdActivoEstatus(int idActivoEstatus){
        return activoEstatus.findByIdActivoEstatus(idActivoEstatus);
        
    }
    
}