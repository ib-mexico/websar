package com.ibmexico.repositories;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CotizacionFicheroEntity;


@Repository("cotizacionFicheroRepository")
public interface ICotizacionFicheroRepository extends JpaRepository<CotizacionFicheroEntity, Serializable> {

	public abstract CotizacionFicheroEntity findByIdCotizacionFichero(int idCotizacionFichero);
	
	public abstract CotizacionFicheroEntity findByGastoIdServicioProveedorMant(int idGasto);

	@Query("SELECT objFichero FROM CotizacionFicheroEntity objFichero WHERE objFichero.gasto.idServicioProveedorMant=?1")
	public abstract List<CotizacionFicheroEntity> findCotizacionFicheroByGastoID(int idGasto);

	@Query("SELECT objFichero FROM CotizacionFicheroEntity  objFichero WHERE objFichero.gasto.idServicioProveedorMant=?1")
	public abstract List<CotizacionFicheroEntity> findByIdGasto(int idGasto);
	
	public abstract List<CotizacionFicheroEntity> findByCotizacion_IdCotizacion(int IdCotizacion);
	
	@Query("SELECT objCotizacionFichero FROM CotizacionFicheroEntity objCotizacionFichero WHERE objCotizacionFichero.cotizacion.idCotizacion = ?1 AND objCotizacionFichero.cotizacionTipoFichero.idCotizacionTipoFichero = ?2")
	public abstract List<CotizacionFicheroEntity> listTipoDocumento(int idCotizacion, int idFicheroTipo);
	
	@Query("SELECT objCotizacionFichero FROM CotizacionFicheroEntity objCotizacionFichero WHERE objCotizacionFichero.cotizacionTipoFichero.idCotizacionTipoFichero = ?1 AND (objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4 OR objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 6) AND (CONVERT(objCotizacionFichero.cotizacion.facturacionFecha, DATE) BETWEEN ?2 AND ?3) AND (objCotizacionFichero.cotizacion.usuario.idUsuario = ?4 OR objCotizacionFichero.cotizacion.usuarioVendedor.idUsuario = ?4)")
	public abstract List<CotizacionFicheroEntity> listTipoDocumentoPeriodo(int idFicheroTipo, LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);
	
	@Query("SELECT COALESCE(SUM(objCotizacionFichero.importe), 0) FROM CotizacionFicheroEntity objCotizacionFichero WHERE objCotizacionFichero.cotizacion.idCotizacion = ?1 AND objCotizacionFichero.cotizacionTipoFichero.idCotizacionTipoFichero = ?2")
	public abstract BigDecimal sumTipoDocumento(int idCotizacion, int idFicheroTipo);
	
	@Query("SELECT COALESCE(SUM(objCotizacionFichero.importe), 0) FROM CotizacionFicheroEntity objCotizacionFichero WHERE objCotizacionFichero.cotizacionTipoFichero.idCotizacionTipoFichero = ?1 AND (objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4 OR objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 6) AND (CONVERT(objCotizacionFichero.cotizacion.facturacionFecha, DATE) BETWEEN ?2 AND ?3) AND (objCotizacionFichero.cotizacion.usuario.idUsuario = ?4 OR objCotizacionFichero.cotizacion.usuarioVendedor.idUsuario = ?4)")
	public abstract BigDecimal sumTipoDocumentoPeriodo(int idFicheroTipo,  LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);
	
	@Query("SELECT COALESCE(SUM(objCotizacionFichero.importe), 0) FROM CotizacionFicheroEntity objCotizacionFichero WHERE objCotizacionFichero.cotizacionTipoFichero.idCotizacionTipoFichero = ?1 AND (objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 3 OR objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 4 OR objCotizacionFichero.cotizacion.cotizacionEstatus.idCotizacionEstatus = 6) AND (CONVERT(objCotizacionFichero.cotizacion.facturacionFecha, DATE) BETWEEN ?3 AND ?4) AND (objCotizacionFichero.cotizacion.usuario.idUsuario = ?5 OR objCotizacionFichero.cotizacion.usuarioVendedor.idUsuario = ?5 OR objCotizacionFichero.cotizacion.usuarioImplementador.idUsuario = ?5 OR objCotizacionFichero.cotizacion.cliente.usuarioEjecutivo.idUsuario = ?5) AND objCotizacionFichero.cotizacion.idCotizacion = ?2")
	public abstract BigDecimal sumTipoDocumentoPeriodo(int idFicheroTipo, int idCotizacion,  LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);
}
