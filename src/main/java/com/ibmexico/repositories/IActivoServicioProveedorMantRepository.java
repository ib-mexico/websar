package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ActivoServicioProveedorMantEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activo_servicio_proveedor_mant_repo")
public interface IActivoServicioProveedorMantRepository extends JpaRepository<ActivoServicioProveedorMantEntity, Serializable>{

    /* CONSULTA TODOS LOS POSIBLES PROVEEDORES MEDIANTE EL ID DETALLE MANTENIMIENTO */
    @Query("SELECT objActServProvMant FROM ActivoServicioProveedorMantEntity objActServProvMant WHERE objActServProvMant.bienDetalleMant.idDetalleMantenimiento=?1")
    public abstract List<ActivoServicioProveedorMantEntity> lstActivoServProveedorMant(int idDetalleMantenimiento);
    
}