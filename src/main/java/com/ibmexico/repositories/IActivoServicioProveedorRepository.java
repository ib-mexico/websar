package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ActivoServicioProveedorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activoServicioProveedorRepository")
public interface IActivoServicioProveedorRepository extends JpaRepository<ActivoServicioProveedorEntity, Serializable>{
    

    //Consulta de proveedor servicio, dependiendo del tipoactivo seleccionado
    @Query("SELECT DISTINCT objServicioProveedor  FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.activoServicio.tipoActivo.idCatalogoActivo=?1 AND objServicioProveedor.activoServicio.idServicioActivo=?2 AND objServicioProveedor.activoServicio.idServicioActivo=?2  GROUP BY objServicioProveedor.activoProveedor.proveedor")
    public abstract List<ActivoServicioProveedorEntity> findByActivoServicio(int idTipoActivo, int idServicioActivo);

    //Busqueda por id servicio->proveedor
    public abstract ActivoServicioProveedorEntity findByIdServicioProveedor(int idServicioProveedor);
}