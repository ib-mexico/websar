package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.ibmexico.entities.ActivoServicioProveedorMant2Entity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activo_servicio_proveedor_mant2_repo")
public interface IActivoServicioProveedorMant2Repository extends JpaRepository<ActivoServicioProveedorMant2Entity, Serializable>{

    /* CONSULTA TODOS LOS POSIBLES PROVEEDORES MEDIANTE EL ID DETALLE MANTENIMIENTO */
    @Query("SELECT objActServProvMant FROM ActivoServicioProveedorMant2Entity objActServProvMant WHERE objActServProvMant.bienDetalleMant.idDetalleMantenimiento=?1")
    public abstract List<ActivoServicioProveedorMant2Entity> lstActivoServProveedorMant(int idDetalleMantenimiento);

    /*Encontrar un servicio-proveedor registrado en una solicitud de mant con su ID*/
    public abstract ActivoServicioProveedorMant2Entity findByIdServicioProveedorMant(int IdServicioProvManto);

    /**Encontrar gastos por ID json */
    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE  objGasto.idServicioProveedorMant=?1")
    public abstract List<ActivoServicioProveedorMant2Entity> findByGasto(int IdServicioProvManto);

    /**Consultar los proveedores-servicios que fueron aceptados */
    @Query("SELECT objActServProvMant FROM ActivoServicioProveedorMant2Entity objActServProvMant WHERE objActServProvMant.bienDetalleMant.idDetalleMantenimiento=?1 and objActServProvMant.aceptado=1")
    public abstract List<ActivoServicioProveedorMant2Entity> lstServProvAceptado(int idDetalleMantenimiento);

    //TABLE
	@Query("SELECT COUNT(objGasto) FROM ActivoServicioProveedorMant2Entity objGasto WHERE objGasto.aceptado=true")	
	public abstract long countForDataTable();
				
    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto  WHERE objGasto.aceptado=true")
    public abstract List<ActivoServicioProveedorMant2Entity> findForDataTable(Pageable page);


    /**CONSULTAS PARA EL MODULO DE GASTOS */
    // Conteo de la busqueda 
    @Query("SELECT COUNT(objGasto) FROM ActivoServicioProveedorMant2Entity  objGasto WHERE objGasto.isGasto=true AND objGasto.isEliminado=false AND (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%)")
    public abstract long countForDataTableGasto(String search);

    @Query("SELECT COUNT(objGasto) FROM ActivoServicioProveedorMant2Entity objGasto WHERE objGasto.isGasto=true AND objGasto.isEliminado=false AND (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%) AND CONVERT(objGasto.creacionFecha, DATE) BETWEEN ?2 AND ?3")
    public abstract long countForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);
    //Gastos 
    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE objGasto.isGasto=true AND objGasto.isEliminado=false AND (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%) order by objGasto.creacionFecha DESC")
    public abstract List<ActivoServicioProveedorMant2Entity> findForDataTableGasto(String search, Pageable page);

    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE objGasto.isGasto=true AND objGasto.isEliminado=false AND  (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%) AND CONVERT(objGasto.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objGasto.creacionFecha DESC")
    public abstract List<ActivoServicioProveedorMant2Entity> findForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);

 
    /**CONSULTAS PARA EL MODULO DE MANTENIMEINTO */
    @Query("SELECT COUNT(objGasto) FROM ActivoServicioProveedorMant2Entity  objGasto WHERE objGasto.isGasto=false  AND objGasto.isEliminado=false AND (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%)")
    public abstract long countForDataTableManto(String search);

    @Query("SELECT COUNT(objGasto) FROM ActivoServicioProveedorMant2Entity objGasto WHERE objGasto.isGasto=false AND objGasto.isEliminado=false AND (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%) AND CONVERT(objGasto.creacionFecha, DATE) BETWEEN ?2 AND ?3")
    public abstract long countForDataTableManto(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);
    //Mantos
    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE objGasto.isGasto=false AND objGasto.isEliminado=false AND (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%) order by objGasto.folioCotizacion ASC")
    public abstract List<ActivoServicioProveedorMant2Entity> findForDataTableManto(String search, Pageable page);

    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE objGasto.isGasto=false AND  objGasto.isEliminado=false AND (objGasto.folioCotizacion like %?1% OR objGasto.facturaGasto.numeroFactura like %?1% OR objGasto.tipoGasto.nombre LIKE %?1% OR objGasto.observaciones LIKE %?1%  OR objGasto.usuario.nombreCompleto LIKE %?1%) AND CONVERT(objGasto.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objGasto.folioCotizacion ASC")
    public abstract List<ActivoServicioProveedorMant2Entity> findForDataTableManto(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);


    /*REPORTE DE GASTOS */
    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE (objGasto.isEliminado=false) AND (objGasto.usuario.idUsuario=?3) AND (CONVERT(objGasto.fechaPago, DATE) BETWEEN ?1 AND ?2)")
    public abstract List<ActivoServicioProveedorMant2Entity> findByGastoUsarioFecha(LocalDate ldFechaInicio, LocalDate ldFechaFin,int idUsuario);

    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE (objGasto.isEliminado=false) AND (objGasto.usuario.idUsuario=?3) AND (objGasto.tipoGasto.idTipoGasto=?4) AND (CONVERT(objGasto.fechaPago, DATE) BETWEEN ?1 AND ?2)")
    public abstract List<ActivoServicioProveedorMant2Entity> findByGastoUsuarioFechaTipo(LocalDate ldFechaInicio, LocalDate ldFechaFin,int idUsuario, int idTipoGasto);

    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE (objGasto.isEliminado=false) AND (objGasto.tipoGasto.idTipoGasto=?3) AND (CONVERT(objGasto.fechaPago, DATE) BETWEEN ?1 AND ?2)")
    public abstract List<ActivoServicioProveedorMant2Entity> findByGastoTipoFecha(LocalDate ldFechaInicio, LocalDate ldFechaFin,int idTipoGasto);

    @Query("SELECT objGasto FROM ActivoServicioProveedorMant2Entity objGasto WHERE (objGasto.isEliminado=false) AND (CONVERT(objGasto.fechaPago, DATE) BETWEEN ?1 AND ?2)")
    public abstract List<ActivoServicioProveedorMant2Entity> findByGastoFecha(LocalDate ldFechaInicio, LocalDate ldFechaFin);
}