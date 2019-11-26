package com.ibmexico.repositories;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.OrdenServicioEntity;

@Repository("ordenServicioRepository")
public interface IOrdenServicioRepository extends JpaRepository<OrdenServicioEntity, Serializable> {

	public abstract OrdenServicioEntity findByIdOrdenServicio(int idOrdenServicio);
	
	@Query("SELECT objOrdenServicio FROM OrdenServicioEntity objOrdenServicio")
	public abstract List<OrdenServicioEntity> listOrdenesServicios();
	
	@Query("SELECT objOrdenServicio FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.usuarioElabora.idUsuario = ?1")
	public abstract List<OrdenServicioEntity> listOrdenesServicios(int idUsuario);
	
	//SUMA DE SUBTOTALES
	@Query("SELECT COALESCE(SUM(objOrdenServicio.subtotal), 0) FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = ?1")
	public abstract BigDecimal sumTotalCotizacionPartidas(int idCotizacion);
	
	//TABLE
	@Query("SELECT COUNT(objOrdenServicio) FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = ?1")	
	public abstract long countForDataTable(int idCotizacion);
	
	@Query("SELECT COUNT(objOrdenServicio) FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = ?1 AND objOrdenServicio.eliminado = false AND (objOrdenServicio.folio like %?2% OR objOrdenServicio.usuarioElabora.nombreCompleto like %?2%) order by objOrdenServicio.idOrdenServicio DESC")	
	public abstract long countForDataTable(int idCotizacion, String search);
	
	@Query("SELECT objOrdenServicio FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = ?1 order by objOrdenServicio.idOrdenServicio DESC")
	public abstract List<OrdenServicioEntity> findForDataTable(int idCotizacion, Pageable page);
				
	@Query("SELECT objOrdenServicio FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = ?1 AND objOrdenServicio.eliminado = false AND (objOrdenServicio.folio like %?2% OR objOrdenServicio.usuarioElabora.nombreCompleto like %?2%) order by objOrdenServicio.idOrdenServicio DESC")
	public abstract List<OrdenServicioEntity> findForDataTable(int idCotizacion, String search, Pageable page);

	//TABLE FOR ORDENES SERVICIO SIN ASOCIACION DE UNA COTIZACION

	@Query("SELECT COUNT(objOrdenServicio) FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = null AND objOrdenServicio.usuarioElabora.idUsuario=?1")	
	public abstract long countForDataTableOrdenes(int usuarioID);
		
	@Query("SELECT COUNT(objOrdenServicio) FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = null AND objOrdenServicio.usuarioElabora.idUsuario=?2 AND objOrdenServicio.eliminado = false AND (objOrdenServicio.folio like %?1% OR objOrdenServicio.usuarioElabora.nombreCompleto like %?1%) order by objOrdenServicio.idOrdenServicio DESC")	
	public abstract long countForDataTableOrdenes(String search, int usuarioID);
		
	@Query("SELECT objOrdenServicio FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = null AND objOrdenServicio.usuarioElabora.idUsuario=?1 order by objOrdenServicio.idOrdenServicio DESC")
	public abstract List<OrdenServicioEntity> findForDataTableOrdenes(int usuarioID,Pageable page);
					
	@Query("SELECT objOrdenServicio FROM OrdenServicioEntity objOrdenServicio WHERE objOrdenServicio.cotizacion.idCotizacion = null AND objOrdenServicio.usuarioElabora.idUsuario=?2  AND objOrdenServicio.eliminado = false AND (objOrdenServicio.folio like %?1% OR objOrdenServicio.usuarioElabora.nombreCompleto like %?1%) order by objOrdenServicio.idOrdenServicio DESC")
	public abstract List<OrdenServicioEntity> findForDataTableOrdenes(String search,int usuarioID, Pageable page);



}
