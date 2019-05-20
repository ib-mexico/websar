package com.ibmexico.repositories;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CotizacionUsuarioQuotaEntity;

@Repository("cotizacionUsuarioQuotaRepository")
public interface ICotizacionUsuarioQuotaRepository extends JpaRepository<CotizacionUsuarioQuotaEntity, Serializable> {

	public abstract CotizacionUsuarioQuotaEntity findByIdCotizacionUsuarioQuota(int idCotizacionUsuarioQuota);
	
	public abstract CotizacionUsuarioQuotaEntity findByCotizacion_IdCotizacion(int idCotizacion);
	
	@Query("SELECT objQuota FROM CotizacionUsuarioQuotaEntity objQuota WHERE objQuota.cotizacion.idCotizacion = ?1")
	public abstract List<CotizacionUsuarioQuotaEntity> listCotizacionQuotas(int idCotizacion);
	
	@Modifying
	@Query("DELETE FROM CotizacionUsuarioQuotaEntity objQuota WHERE objQuota.cotizacion.idCotizacion = ?1")
	public void removeCotizacionCuota(int idCotizacion);
	
	@Query("SELECT objQuota FROM CotizacionUsuarioQuotaEntity objQuota WHERE objQuota.usuario.idUsuario = ?1 AND (objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4)")
	public abstract List<CotizacionUsuarioQuotaEntity> listUsuarioQuotas(int idUsuario);
	
	@Query("SELECT objQuota FROM CotizacionUsuarioQuotaEntity objQuota WHERE objQuota.usuario.idUsuario = ?1 AND (objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4) AND (CONVERT(objQuota.cotizacion.facturacionFecha, DATE) BETWEEN ?2 AND ?3)")
	public abstract List<CotizacionUsuarioQuotaEntity> listUsuarioQuotasPeriodo(int idUsuario, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COALESCE(SUM(objQuota.valorQuota), 0) FROM CotizacionUsuarioQuotaEntity objQuota WHERE objQuota.usuario.idUsuario = ?1 AND (objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4) AND (CONVERT(objQuota.cotizacion.facturacionFecha, DATE) BETWEEN ?2 AND ?3)")
	public abstract BigDecimal sumUsuarioQuotaPeriodo(int idUsuario,  LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COALESCE(SUM(objQuota.valorQuota), 0) FROM CotizacionUsuarioQuotaEntity objQuota WHERE objQuota.usuario.idUsuario = ?1 AND objQuota.cotizacion.empresa.idEmpresa = ?4 AND (objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4) AND (CONVERT(objQuota.cotizacion.facturacionFecha, DATE) BETWEEN ?2 AND ?3)")
	public abstract BigDecimal sumUsuarioQuotaPeriodo(int idUsuario,  LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEmpresa);
	
	@Query("SELECT COALESCE(SUM(objQuota.valorQuota), 0) FROM CotizacionUsuarioQuotaEntity objQuota WHERE objQuota.usuario.idUsuario = ?1 AND (objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objQuota.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4) AND objQuota.cotizacion.idCotizacion = ?2 AND (CONVERT(objQuota.cotizacion.facturacionFecha, DATE) BETWEEN ?3 AND ?4)")
	public abstract BigDecimal sumUsuarioQuotaCotizacionPeriodo(int idUsuario, int idCotizacion,  LocalDate ldFechaInicio, LocalDate ldFechaFin);
}