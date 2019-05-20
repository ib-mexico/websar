package com.ibmexico.repositories;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.OportunidadNegocioEntity;

@Repository("oportunidadNegocioRepository")
public interface IOportunidadNegocioRepository extends JpaRepository<OportunidadNegocioEntity, Serializable> {

	public abstract OportunidadNegocioEntity findByIdOportunidadNegocio(int idOportunidadNegocio);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimados(int idOportunidadEstatus);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.empresa.idEmpresa = ?2 AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimadosEmpresa(int idOportunidadEstatus, int idEmpresa);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimados(int idOportunidadEstatus, int idUsuario);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.empresa.idEmpresa = ?3 AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimadosEmpresa(int idOportunidadEstatus, int idUsuario, int idEmpresa);
	
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAll(int idOportunidadEstatus);
	
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.empresa.idEmpresa = ?2 AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAllEmpresa(int idOportunidadEstatus, int idEmpresa);
	
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAll(int idOportunidadEstatus, int idUsuario);
	
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.empresa.idEmpresa = ?3 AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAllEmpresa(int idOportunidadEstatus, int idUsuario, int idEmpresa);
	
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> listOportunidadesNegocios();
	
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.usuarioVendedor.idUsuario = ?1 AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> listOportunidadesNegocios(int idUsuario);
	
	
	
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE (objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 3 OR objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 4) AND CONVERT(objOportunidad.creacionFecha, DATE) BETWEEN ?1 AND ?2 " )
	public abstract BigDecimal sumTotalOportunidadPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE (objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 3 OR objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 4) AND objOportunidad.usuarioVendedor.idUsuario = ?3 AND (CONVERT(objOportunidad.creacionFecha, DATE) BETWEEN ?1 AND ?2)")
	public abstract BigDecimal sumTotalOportunidadPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);
}
