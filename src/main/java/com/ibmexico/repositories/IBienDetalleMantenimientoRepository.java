package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.ibmexico.entities.BienDetalleMantenimientoEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("bienDetalleMantenimientoRepository")
public interface IBienDetalleMantenimientoRepository extends JpaRepository<BienDetalleMantenimientoEntity, Serializable>{

    public abstract BienDetalleMantenimientoEntity findByIdDetalleMantenimiento(int idDetalleMantenimiento);

    //Lista para el historico de mantenimiento dada a un activo.
    @Query("SELECT objActivo FROM BienDetalleMantenimientoEntity objDetalleMant, BienActivoEntity objActivo  WHERE objDetalleMant.bienActivo=?1  AND objActivo.idRecursoActivo=?1")
    public abstract List<BienDetalleMantenimientoEntity> lstMantHistorico(int idActivo);

    /**Consultar mantenimientos vencidos pero no finalizadas */
    @Query("SELECT detalleManto FROM BienDetalleMantenimientoEntity detalleManto WHERE detalleManto.finalizado!=1")
    public abstract List<BienDetalleMantenimientoEntity> lstMantoVencidoNoFinalizado();

    //TABLE
	@Query("SELECT COUNT(objDetalleMant) FROM BienDetalleMantenimientoEntity objDetalleMant")	
	public abstract long countForDataTable();
				
    @Query("SELECT objDetalleMant FROM BienDetalleMantenimientoEntity objDetalleMant  WHERE objDetalleMant.finalizado=true")
    public abstract List<BienDetalleMantenimientoEntity> findForDataTable(Pageable page);

 // Conteo de la busqueda
    @Query("SELECT COUNT(objDetalleMant) FROM BienDetalleMantenimientoEntity  objDetalleMant WHERE (objDetalleMant.observaciones like %?1% OR objDetalleMant.activoEstatus.nombreEstatus like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%)")
    public abstract long countForDataTable(String search);

    // Conteo de la busqueda
    @Query("SELECT COUNT(objDetalleMant) FROM BienDetalleMantenimientoEntity objDetalleMant WHERE ( objDetalleMant.observaciones like %?1% OR objDetalleMant.activoEstatus.nombreEstatus like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%) AND CONVERT(objDetalleMant.creacionFecha, DATE) BETWEEN ?2 AND ?3")
    public abstract long countForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);

    // Lista de busqueda con la funcion LIKE y fechas
    @Query("SELECT objDetalleMant FROM BienDetalleMantenimientoEntity objDetalleMant WHERE ( objDetalleMant.observaciones like %?1% OR objDetalleMant.activoEstatus.nombreEstatus like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%) AND CONVERT(objDetalleMant.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objDetalleMant.estatus_recordatorio ASC")
    public abstract List<BienDetalleMantenimientoEntity> findForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);

    // Listado de busqueda mediante la accion search
    @Query("SELECT objDetalleMant FROM BienDetalleMantenimientoEntity objDetalleMant WHERE ( objDetalleMant.observaciones like %?1% OR objDetalleMant.activoEstatus.nombreEstatus like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%) order by objDetalleMant.estatus_recordatorio ASC")
    public abstract List<BienDetalleMantenimientoEntity> findForDataTable(String search, Pageable page);




    /**Querys para consultar los mantenimientos en validacion */
        //TABLE
	@Query("SELECT COUNT(objDetalleMant) FROM BienDetalleMantenimientoEntity objDetalleMant WHERE objDetalleMant.activoEstatus.idActivoEstatus=7")	
	public abstract long countForDataTableValidation();
				
    @Query("SELECT objDetalleMant FROM BienDetalleMantenimientoEntity objDetalleMant  WHERE objDetalleMant.finalizado=true AND objDetalleMant.activoEstatus.idActivoEstatus=7")
    public abstract List<BienDetalleMantenimientoEntity> findForDataTableValidation(Pageable page);

 // Conteo de la busqueda
    @Query("SELECT COUNT(objDetalleMant) FROM BienDetalleMantenimientoEntity  objDetalleMant WHERE ( objDetalleMant.observaciones like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%) AND objDetalleMant.activoEstatus.idActivoEstatus=7")
    public abstract long countForDataTableValidation(String search);

    // Conteo de la busqueda
    @Query("SELECT COUNT(objDetalleMant) FROM BienDetalleMantenimientoEntity objDetalleMant WHERE objDetalleMant.activoEstatus.idActivoEstatus=7 AND ( objDetalleMant.observaciones like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%) AND CONVERT(objDetalleMant.creacionFecha, DATE) BETWEEN ?2 AND ?3")
    public abstract long countForDataTableValidation(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);

    // Lista de busqueda con la funcion LIKE y fechas
    @Query("SELECT objDetalleMant FROM BienDetalleMantenimientoEntity objDetalleMant WHERE objDetalleMant.activoEstatus.idActivoEstatus=7 AND ( objDetalleMant.observaciones like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%) AND CONVERT(objDetalleMant.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objDetalleMant.estatus_recordatorio ASC")
    public abstract List<BienDetalleMantenimientoEntity> findForDataTableValidation(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);

    // Listado de busqueda mediante la accion search
    @Query("SELECT objDetalleMant FROM BienDetalleMantenimientoEntity objDetalleMant WHERE objDetalleMant.activoEstatus.idActivoEstatus=7 AND ( objDetalleMant.observaciones like %?1% OR objDetalleMant.bienActivo.numeroEconomico LIKE %?1% OR objDetalleMant.bienActivo.descripcion LIKE %?1% OR objDetalleMant.bienActivo.marca LIKE %?1%  OR objDetalleMant.bienActivo.idActivo.nombre LIKE %?1%) order by objDetalleMant.estatus_recordatorio ASC")
    public abstract List<BienDetalleMantenimientoEntity> findForDataTableValidation(String search, Pageable page);
}