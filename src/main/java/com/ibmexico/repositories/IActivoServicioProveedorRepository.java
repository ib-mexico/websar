package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ActivoServicioProveedorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activoServiciorRepository")
public interface IActivoServicioProveedorRepository extends JpaRepository<ActivoServicioProveedorEntity, Serializable>{


    @Query("SELECT DISTINCT objServicio.idActivoServicio, objProveedor.proveedor FROM ActivoServicioProveedorEntity objActivoServ, ActivoProveedorEntity objProveedor, ActivoServicioEntity objServicio WHERE objServicio.idTipoActivo =?1 AND objActivoServ.idServicio=?2 and objServicio.idActivoServicio=?2 AND objActivoServ.idProveedor=objProveedor.idActivoProveedor GROUP BY objProveedor.proveedor")
    public List<ActivoServicioProveedorEntity> findByIdTipo(int idTipoActivo, int IdServicio);
}