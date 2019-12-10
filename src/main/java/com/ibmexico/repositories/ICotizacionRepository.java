package com.ibmexico.repositories;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CotizacionEntity;

@Repository("cotizacionRepository")
public interface ICotizacionRepository  extends JpaRepository<CotizacionEntity, Serializable> {
	
	public abstract CotizacionEntity findByIdCotizacion(int idCotizacion);
	
	@Query("SELECT COALESCE(SUM(objCotizacion.subtotal), 0) FROM CotizacionEntity objCotizacion WHERE (objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 4 OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 6) AND CONVERT(objCotizacion.facturacionFecha, DATE) BETWEEN ?1 AND ?2 " )
	public abstract BigDecimal sumTotalCotizacionPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COALESCE(SUM(objCotizacion.subtotal), 0) FROM CotizacionEntity objCotizacion WHERE (objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3  OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 4 OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 6) AND (objCotizacion.usuario.idUsuario = ?3 OR objCotizacion.usuarioVendedor.idUsuario = ?3) AND (CONVERT(objCotizacion.facturacionFecha, DATE) BETWEEN ?1 AND ?2)" )
	public abstract BigDecimal sumTotalCotizacionPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus != 5")
	public abstract List<CotizacionEntity> findCotizacionesActivas();
	/**Cotizaciones para el mod de gastos */
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus != 5 AND objCotizacion.cotizacionEstatus.idCotizacionEstatus !=1")
	public abstract List<CotizacionEntity> findCotizacionesActivos();
	/** */
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE (objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3) AND objCotizacion.boolNormal = true AND objCotizacion.formaPago.idFormaPago = 1")
	public abstract List<CotizacionEntity> lstCotizacionesNoCobradas();
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE (objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3) AND objCotizacion.boolNormal = true AND objCotizacion.formaPago.idFormaPago = 1 AND objCotizacion.empresa.idEmpresa = ?1")
	public abstract List<CotizacionEntity> lstCotizacionesNoCobradas(int idEmpresa);
	
	
	//GRAFICAS
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE (CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?1 AND ?2)")	
	public abstract long countCotizacionesPeriodo(LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE (CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?2 AND ?3) AND (objCotizacion.usuario.idUsuario = ?1 OR objCotizacion.usuarioVendedor.idUsuario = ?1)")	
	public abstract long countCotizacionesPeriodo(int idUsuario, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = ?1 AND (CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?2 AND ?3)")	
	public abstract long countCotizacionesEstatusPeriodo(int idCotizacionEstatus, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = ?1 AND (CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?2 AND ?3) AND (objCotizacion.usuario.idUsuario = ?4 OR objCotizacion.usuarioVendedor.idUsuario = ?4)")	
	public abstract long countCotizacionesEstatusPeriodo(int idCotizacionEstatus, LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);
	
	
	//REPORTE DE VENTAS
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE (objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 4 OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 6) AND (objCotizacion.usuario.idUsuario = ?3 OR objCotizacion.usuarioVendedor.idUsuario = ?3 OR objCotizacion.cliente.usuarioEjecutivo.idUsuario = ?3 OR objCotizacion.usuarioImplementador.idUsuario = ?3) AND (CONVERT(objCotizacion.facturacionFecha, DATE) BETWEEN ?1 AND ?2)" )
	public abstract List<CotizacionEntity> findCotizacionesPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE (objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 4 OR objCotizacion.cotizacionEstatus.idCotizacionEstatus = 6) AND objCotizacion.empresa.idEmpresa = ?4 AND (objCotizacion.usuario.idUsuario = ?3 OR objCotizacion.usuarioVendedor.idUsuario = ?3 OR objCotizacion.cliente.usuarioEjecutivo.idUsuario = ?3 OR objCotizacion.usuarioImplementador.idUsuario = ?3) AND (CONVERT(objCotizacion.facturacionFecha, DATE) BETWEEN ?1 AND ?2)" )
	public abstract List<CotizacionEntity> findCotizacionesPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario, int idEmpresa);
	
	//REPORTE DE COMISIONES
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 4 AND objCotizacion.empresa.idEmpresa = ?4 AND (objCotizacion.usuario.idUsuario = ?3 OR objCotizacion.usuarioVendedor.idUsuario = ?3 OR objCotizacion.cliente.usuarioEjecutivo.idUsuario = ?3 OR objCotizacion.usuarioImplementador.idUsuario = ?3) AND (CONVERT(objCotizacion.pagoFecha, DATE) BETWEEN ?1 AND ?2)" )
	public abstract List<CotizacionEntity> findCotizacionesPagadasPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario, int idEmpresa);
	
	//REPORTE DE COMISIONES DE COBRANZA
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 4 AND objCotizacion.empresa.idEmpresa = ?4 AND objCotizacion.usuarioCobranza.idUsuario = ?3 AND (CONVERT(objCotizacion.pagoFecha, DATE) BETWEEN ?1 AND ?2)" )
	public abstract List<CotizacionEntity> findCotizacionesCobradasPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario, int idEmpresa);
	
	//REPORTE DE COTIZACIONES APROBADAS
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 2 AND (CONVERT(objCotizacion.aprobacionFecha, DATE) = ?1) ORDER BY objCotizacion.aprobacionFecha ASC")
	public abstract List<CotizacionEntity> lstCotizacionesAprobadasIgualA(LocalDate ldFechaActual);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 2 AND (CONVERT(objCotizacion.aprobacionFecha, DATE) <= ?1) ORDER BY objCotizacion.aprobacionFecha ASC")
	public abstract List<CotizacionEntity> lstCotizacionesAprobadasMayorIgualA(LocalDate ldFechaActual);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 2 AND (CONVERT(objCotizacion.aprobacionFecha, DATE) >= ?1) ORDER BY objCotizacion.aprobacionFecha ASC")
	public abstract List<CotizacionEntity> lstCotizacionesAprobadasMenorIgualA(LocalDate ldFechaActual);
	
	//REPORTE DE COTIZACIONES FACTURADAS
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3 AND (CONVERT(objCotizacion.facturacionFecha, DATE) = ?1) ORDER BY objCotizacion.facturacionFecha ASC")
	public abstract List<CotizacionEntity> lstCotizacionesFacturadasIgualA(LocalDate ldFechaActual);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3 AND (CONVERT(objCotizacion.facturacionFecha, DATE) <= ?1) ORDER BY objCotizacion.facturacionFecha ASC")
	public abstract List<CotizacionEntity> lstCotizacionesFacturadasMayorIgualA(LocalDate ldFechaActual);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.cotizacionEstatus.idCotizacionEstatus = 3 AND (CONVERT(objCotizacion.facturacionFecha, DATE) >= ?1) ORDER BY objCotizacion.facturacionFecha ASC")
	public abstract List<CotizacionEntity> lstCotizacionesFacturadasMenorIgualA(LocalDate ldFechaActual);
	
	
	//TABLE
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion ")	
	public abstract long countForDataTable();
	
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE objCotizacion.eliminado = false AND (objCotizacion.concepto like %?1% OR objCotizacion.folio like %?1% OR objCotizacion.usuario.nombreCompleto like %?1%  OR objCotizacion.cliente.cliente like %?1%)")	
	public abstract long countForDataTable(String search);
	
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE objCotizacion.eliminado = false AND (objCotizacion.concepto like %?1% OR objCotizacion.folio like %?1% OR objCotizacion.usuario.nombreCompleto like %?1%  OR objCotizacion.cliente.cliente like %?1%) AND CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?2 AND ?3")	
	public abstract long countForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE (objCotizacion.usuario.idUsuario = ?1 OR objCotizacion.usuarioVendedor.idUsuario = ?1) AND objCotizacion.eliminado = false AND (objCotizacion.concepto like %?2% OR objCotizacion.folio like %?2% OR objCotizacion.usuario.nombreCompleto like %?2%  OR objCotizacion.cliente.cliente like %?2%)")	
	public abstract long countForDataTable(int idUsuario, String search);
	
	@Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE (objCotizacion.usuario.idUsuario = ?1 OR objCotizacion.usuarioVendedor.idUsuario = ?1) AND objCotizacion.eliminado = false AND (objCotizacion.concepto like %?2% OR objCotizacion.folio like %?2% OR objCotizacion.usuario.nombreCompleto like %?2%  OR objCotizacion.cliente.cliente like %?2%) AND CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?3 AND ?4")	
	public abstract long countForDataTable(int idUsuario, String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);
		
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion ")
	public abstract List<CotizacionEntity> findForDataTable(Pageable page);
				
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.eliminado = false AND (objCotizacion.concepto like %?1% OR objCotizacion.folio like %?1% OR objCotizacion.usuario.nombreCompleto like %?1% OR objCotizacion.cliente.cliente like %?1%) order by objCotizacion.creacionFecha DESC")
	public abstract List<CotizacionEntity> findForDataTable(String search, Pageable page);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.eliminado = false AND (objCotizacion.concepto like %?1% OR objCotizacion.folio like %?1% OR objCotizacion.usuario.nombreCompleto like %?1% OR objCotizacion.cliente.cliente like %?1%) AND CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objCotizacion.creacionFecha DESC")
	public abstract List<CotizacionEntity> findForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE (objCotizacion.usuario.idUsuario = ?1 OR objCotizacion.usuarioVendedor.idUsuario = ?1) AND objCotizacion.eliminado = false AND (objCotizacion.concepto like %?2% OR objCotizacion.folio like %?2% OR objCotizacion.usuario.nombreCompleto like %?2%  OR objCotizacion.cliente.cliente like %?2%) order by objCotizacion.creacionFecha DESC")
	public abstract List<CotizacionEntity> findForDataTable(int idUsuario, String search, Pageable page);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE (objCotizacion.usuario.idUsuario = ?1 OR objCotizacion.usuarioVendedor.idUsuario = ?1) AND objCotizacion.eliminado = false AND (objCotizacion.concepto like %?2% OR objCotizacion.folio like %?2% OR objCotizacion.usuario.nombreCompleto like %?2%  OR objCotizacion.cliente.cliente like %?2%) AND CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?3 AND ?4 order by objCotizacion.creacionFecha DESC")
	public abstract List<CotizacionEntity> findForDataTable(int idUsuario, String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);

	// //GRAFICAS INDICADORES DE PRODUCCION
	// @Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE objCotizacion.usuario.idUsuario=?3 AND (CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?1 AND ?2)")	
	// public abstract long countCotizacionesPeriodoEjecutivo(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo);

	// @Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE objCotizacion.usuario.idUsuario=?3 AND (CONVERT(objCotizacion.aprobacionFecha, DATE) BETWEEN ?1 AND ?2)")	
	// public abstract long countCotizacionesAprobada(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo);
	
	// @Query("SELECT COUNT(objCotizacion) FROM CotizacionEntity objCotizacion WHERE objCotizacion.usuario.idUsuario=?1 AND (CONVERT(objCotizacion.facturacionFecha, DATE) BETWEEN ?2 AND ?3)")	
	// public abstract long countCotizacionesFacturada(int idEjecutivo, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	//GRAFICAS INDICADORES DE PRODUCCION
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.usuario.idUsuario=?3 AND (CONVERT(objCotizacion.creacionFecha, DATE) BETWEEN ?1 AND ?2)")	
	public abstract List<CotizacionEntity> cotizacionesPeriodoEjecutivo(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo);

	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.usuario.idUsuario=?3 AND (CONVERT(objCotizacion.aprobacionFecha, DATE) BETWEEN ?1 AND ?2)")	
	public abstract List<CotizacionEntity> cotizacionesAprobada(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idEjecutivo);
	
	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.usuario.idUsuario=?1 AND (CONVERT(objCotizacion.facturacionFecha, DATE) BETWEEN ?2 AND ?3)")	
	public abstract List<CotizacionEntity> cotizacionesFacturada(int idEjecutivo, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	

	@Query("SELECT objCotizacion FROM CotizacionEntity objCotizacion WHERE objCotizacion.usuario.idUsuario=?1 AND objCotizacion.pagoFecha!=NULL AND objCotizacion.aprobacionFecha!=NULL AND (CONVERT(objCotizacion.pagoFecha, DATE) BETWEEN ?2 AND ?3)")	
	public abstract List<CotizacionEntity> totalCotizacionesPagadas(int idEjecutivo, LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	
}
