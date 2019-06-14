package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CotizacionComisionEntity;

@Repository("cotizacionComisionRepository")
public interface ICotizacionComisionRepository extends JpaRepository<CotizacionComisionEntity, Serializable> {

	public abstract CotizacionComisionEntity findByIdCotizacionComision(int idCotizacionComision);
	
	public abstract CotizacionComisionEntity findByCotizacion_IdCotizacion(int idCotizacion);
	
	@Modifying
	@Query("DELETE FROM CotizacionComisionEntity objComision WHERE objComision.cotizacion.idCotizacion = ?1")
	public void removeCotizacionComision(int idCotizacion);
	
	@Query("SELECT objComision FROM CotizacionComisionEntity objComision WHERE (objComision.usuarioEjecutivo.idUsuario = ?1 OR objComision.usuarioCotizante.idUsuario = ?1 OR objComision.usuarioVendedor.idUsuario = ?1 OR objComision.usuarioImplementador.idUsuario = ?1) AND (objComision.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objComision.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4) AND (CONVERT(objComision.cotizacion.pagoFecha, DATE) BETWEEN ?2 AND ?3)")
	public abstract List<CotizacionComisionEntity> listComisionesUsuarioPeriodo(int idUsuario, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT objComision FROM CotizacionComisionEntity objComision WHERE (objComision.usuarioEjecutivo.idUsuario = ?1 OR objComision.usuarioCotizante.idUsuario = ?1 OR objComision.usuarioVendedor.idUsuario = ?1 OR objComision.usuarioImplementador.idUsuario = ?1) AND (objComision.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objComision.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4) AND objComision.cotizacion.idCotizacion = ?2 AND (CONVERT(objComision.cotizacion.facturacionFecha, DATE) BETWEEN ?3 AND ?4)")
	public abstract CotizacionComisionEntity comisionUsuarioCotizacionPeriodo(int idUsuario, int idCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT objComision FROM CotizacionComisionEntity objComision WHERE (objComision.usuarioEjecutivo.idUsuario = ?1 OR objComision.usuarioCotizante.idUsuario = ?1 OR objComision.usuarioVendedor.idUsuario = ?1 OR objComision.usuarioImplementador.idUsuario = ?1) AND objComision.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4 AND objComision.cotizacion.idCotizacion = ?2 AND (CONVERT(objComision.cotizacion.pagoFecha, DATE) BETWEEN ?3 AND ?4)")
	public abstract CotizacionComisionEntity comisionUsuarioCotizacionPagadaPeriodo(int idUsuario, int idCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT objComision FROM CotizacionComisionEntity objComision WHERE objComision.usuarioCobranza.idUsuario = ?1 AND objComision.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4 AND objComision.cotizacion.idCotizacion = ?2 AND (CONVERT(objComision.cotizacion.pagoFecha, DATE) BETWEEN ?3 AND ?4)")
	public abstract CotizacionComisionEntity comisionUsuarioCotizacionCobradaPeriodo(int idUsuario, int idCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin);
}
