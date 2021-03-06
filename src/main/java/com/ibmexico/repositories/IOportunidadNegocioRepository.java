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
	
	@Query("SELECT opn FROM OportunidadNegocioEntity opn WHERE  opn.idOportunidadNegocio IN (?1)")
	public abstract List<OportunidadNegocioEntity> lstOpnColaboradorEmpresa(List<Integer> opn );
	
	@Query("SELECT COALESCE(SUM(opn.ingresoEstimado),0) FROM OportunidadNegocioEntity opn WHERE  opn.idOportunidadNegocio IN (?1)")
	public abstract BigDecimal sumOpnColaboradorEmpresa(List<Integer> opn );

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
	
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.usuarioVendedor.idUsuario = ?1 AND objOportunidad.eliminado = 0 AND objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 3 AND objOportunidad.renovacionFecha = ?2")
	public abstract List<OportunidadNegocioEntity> listOportunidadesNegociosRenovaciones(int idUsuario, LocalDate ldFechaRenovacion);
	

	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE (objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 3 OR objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 4) AND CONVERT(objOportunidad.creacionFecha, DATE) BETWEEN ?1 AND ?2 " )
	public abstract BigDecimal sumTotalOportunidadPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE (objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 3 OR objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = 4) AND objOportunidad.usuarioVendedor.idUsuario = ?3 AND (CONVERT(objOportunidad.creacionFecha, DATE) BETWEEN ?1 AND ?2)")
	public abstract BigDecimal sumTotalOportunidadPorMes(LocalDate ldFechaInicio, LocalDate ldFechaFin, int idUsuario);

	/**Consultas para el segundo tablero de indicadores */
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.usuarioVendedor.usuarioGrupo.idUsuarioGrupo=?1 AND objOportunidad.usuarioVendedor.eliminado!=1 AND objOportunidad.creacionFecha!=NULL AND objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus=1 AND	(CONVERT(objOportunidad.creacionFecha, DATE) BETWEEN ?2 AND ?3)")
	public abstract List<OportunidadNegocioEntity> opnNuevasArea(int idArea,LocalDate ldFechaInicio, LocalDate ldFechaFin);

	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.usuarioVendedor.usuarioGrupo.idUsuarioGrupo=?1 AND objOportunidad.usuarioVendedor.eliminado!=1 AND objOportunidad.creacionFecha!=NULL AND objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus=4 AND	(CONVERT(objOportunidad.creacionFecha, DATE) BETWEEN ?2 AND ?3)")
	public abstract List<OportunidadNegocioEntity> opnNuevasGanadas(int idArea,LocalDate ldFechaInicio, LocalDate ldFechaFin);


	/**Consultar las oportunidades de Negocio,cerradas y perdidas de este año */
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.empresa.idEmpresa = ?2 AND CONVERT(objOportunidad.creacionFecha, DATE) like %?3%  AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAllEmpresaAnio(int idOportunidadEstatus, int idEmpresa, int anio);

	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.empresa.idEmpresa = ?3 AND CONVERT(objOportunidad.creacionFecha, DATE) like %?4% AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAllEmpresaAnio(int idOportunidadEstatus, int idUsuario, int idEmpresa, int anio);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.empresa.idEmpresa = ?2 AND CONVERT(objOportunidad.creacionFecha,DATE) LIKE %?3% AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimadosEmpresaAnio(int idOportunidadEstatus, int idEmpresa, int anio);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.empresa.idEmpresa = ?3  AND CONVERT(objOportunidad.creacionFecha,DATE) LIKE %?4% AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimadosEmpresaAnio(int idOportunidadEstatus, int idUsuario, int idEmpresa, int anio);
	

	/**Consultar las oportunidades de Negocio,cerradas y perdidas Historicos */
	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.empresa.idEmpresa = ?2 AND CONVERT(objOportunidad.creacionFecha, DATE) not like %?3%  AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAllEmpresaHistorico(int idOportunidadEstatus, int idEmpresa, int anio);

	@Query("SELECT objOportunidad FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.empresa.idEmpresa = ?3 AND CONVERT(objOportunidad.creacionFecha, DATE) not like %?4% AND objOportunidad.eliminado = 0 order by objOportunidad.creacionFecha DESC")
	public abstract List<OportunidadNegocioEntity> findAllEmpresaHistorico(int idOportunidadEstatus, int idUsuario, int idEmpresa, int anio);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.empresa.idEmpresa = ?2 AND CONVERT(objOportunidad.creacionFecha,DATE) NOT LIKE %?3% AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimadosEmpresaHistorico(int idOportunidadEstatus, int idEmpresa, int anio);
	
	@Query("SELECT COALESCE(SUM(objOportunidad.ingresoEstimado), 0) FROM OportunidadNegocioEntity objOportunidad WHERE objOportunidad.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidad.usuarioVendedor.idUsuario = ?2 AND objOportunidad.empresa.idEmpresa = ?3  AND CONVERT(objOportunidad.creacionFecha,DATE) NOT LIKE %?4% AND objOportunidad.eliminado = 0")
	public abstract BigDecimal sumIngresosEstimadosEmpresaHistorico(int idOportunidadEstatus, int idUsuario, int idEmpresa, int anio);
	
}
