package com.ibmexico.services;

import java.time.LocalDateTime;

import com.ibmexico.entities.BienDetalleMantenimientoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.IBienDetalleMantenimientoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("bienDetalleMantService")
public class BienDetalleMantenimientoService{

    @Autowired
    @Qualifier("bienDetalleMantenimientoRepository")
    private IBienDetalleMantenimientoRepository bienMantRep;

    @Autowired
    @Qualifier("sessionService")
    private SessionService sesionService;

    public void create(BienDetalleMantenimientoEntity objDetalle){
        if(objDetalle!=null){
            UsuarioEntity objUser=sesionService.getCurrentUser();
            LocalDateTime ldtNow = LocalDateTime.now();
            objDetalle.setCreacionUsuario(objUser);
            objDetalle.setModificacionUsuario(objUser);
            objDetalle.setCreacionFecha(ldtNow);
            objDetalle.setModificacionFecha(ldtNow);
            bienMantRep.save(objDetalle);     
        }else{
            throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
        }
    }
    
}