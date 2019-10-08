package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ActivoServicioProveedorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activoServicioProveedorRepository")
public interface IActivoServicioProveedorRepository extends JpaRepository<ActivoServicioProveedorEntity, Serializable>{
    
    //@Query("SELECT DISTINCT objServicio.idServicioActivo, objProveedor.proveedor, objServicioProveedor.idServicioProveedor, objProveedor.idProveedorServicio, objServicio.idServicioActivo  FROM ActivoServicioProveedorEntity objServicioProveedor, ActivoProveedorEntity objProveedor, ActivoServicioEntity objServicio WHERE objServicio.tipoActivo.idCatalogoActivo=?1 AND objServicioProveedor.activoServicio.idServicioActivo=?2 AND objServicio.idServicioActivo=?2  AND objServicioProveedor.activoProveedor.idProveedorServicio=objProveedor.idProveedorServicio GROUP BY objProveedor.proveedor")
    //public abstract List<ActivoServicioProveedorEntity> findByActivoServicio(int idTipoActivo, int idServicioActivo);

    @Query("SELECT DISTINCT objServicioProveedor  FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.activoServicio.tipoActivo.idCatalogoActivo=?1 AND objServicioProveedor.activoServicio.idServicioActivo=?2 AND objServicioProveedor.activoServicio.idServicioActivo=?2  GROUP BY objServicioProveedor.activoProveedor.proveedor")
    public abstract List<ActivoServicioProveedorEntity> findByActivoServicio(int idTipoActivo, int idServicioActivo);
}