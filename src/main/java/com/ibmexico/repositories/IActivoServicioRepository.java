package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.ibmexico.entities.ActivoServicioEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activo_servicio_repository")
public interface IActivoServicioRepository extends JpaRepository<ActivoServicioEntity, Serializable> {

    public abstract ActivoServicioEntity findByIdServicioActivo (int idServicioActivo);

    //Listar Servicio por Tipo de Activo.
    @Query("SELECT objServicio FROM ActivoServicioEntity objServicio WHERE objServicio.tipoActivo.idCatalogoActivo = ?1 AND objServicio.eliminado !=1" )
    public abstract List<ActivoServicioEntity> findByTipoActivo(int TipoActivo);

    //Listar todos los servicios
    @Query("SELECT objServicio FROM ActivoServicioEntity objServicio WHERE objServicio.eliminado != 1")
    public abstract List<ActivoServicioEntity> lstServicios();

    //TABLE
	@Query("SELECT COUNT(objServicio) FROM ActivoServicioEntity objServicio WHERE objServicio.eliminado != 1")	
	public abstract long countForDataTable();
				
	@Query("SELECT objServicio FROM ActivoServicioEntity objServicio  WHERE objServicio.eliminado != 1")
    public abstract List<ActivoServicioEntity> findForDataTable(Pageable page);

    @Query("SELECT COUNT(objServicio) FROM ActivoServicioEntity objServicio WHERE objServicio.eliminado != 1 AND (objServicio.tipoActivo.nombre LIKE %?1%  OR objServicio.tipoActivo.descripcion LIKE %?1% OR objServicio.descripcion LIKE %?1%) order by objServicio.creacionFecha DESC")
    public abstract long countForDataTable(String search);

    @Query("SELECT objServicio FROM ActivoServicioEntity objServicio WHERE objServicio.eliminado != 1 AND (objServicio.tipoActivo.nombre LIKE %?1%  OR objServicio.tipoActivo.descripcion LIKE %?1% OR objServicio.descripcion LIKE %?1%) order by objServicio.creacionFecha DESC")
    public abstract List<ActivoServicioEntity> findForDataTable(String search, Pageable page);

    @Query("SELECT COUNT(objServicio) FROM ActivoServicioEntity objServicio WHERE objServicio.eliminado != 1 AND (objServicio.tipoActivo.nombre LIKE %?1%  OR objServicio.tipoActivo.descripcion LIKE %?1% OR objServicio.descripcion LIKE %?1%) AND CONVERT(objServicio.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objServicio.creacionFecha DESC")
    public abstract long countForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);

    @Query("SELECT objServicio FROM ActivoServicioEntity objServicio WHERE objServicio.eliminado != 1 AND (objServicio.tipoActivo.nombre LIKE %?1%  OR objServicio.tipoActivo.descripcion LIKE %?1% OR objServicio.descripcion LIKE %?1%) AND CONVERT(objServicio.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objServicio.creacionFecha DESC")
    public abstract List<ActivoServicioEntity> findForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);
    
}