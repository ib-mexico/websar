package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ClienteEntity;

@Repository("clienteRepository")
public interface IClienteRepository extends JpaRepository<ClienteEntity, Serializable> {

	public abstract ClienteEntity findByIdCliente(int idCliente);
	
	@Query("SELECT objCliente FROM ClienteEntity objCliente WHERE objCliente.eliminado = false")
	public abstract List<ClienteEntity> findActivos();
	
	//TABLE
	@Query("SELECT COUNT(objCliente) FROM ClienteEntity objCliente ")	
	public abstract long countForDataTable();
	
	@Query("SELECT COUNT(objCliente) FROM ClienteEntity objCliente WHERE ( objCliente.cliente like %?1% OR objCliente.razonSocial like %?1% OR objCliente.usuarioEjecutivo.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoS3s.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoR2a.nombreCompleto like %?1% OR objCliente.creacionUsuario.nombreCompleto like %?1% OR objCliente.idCliente like %?1%)")	
	public abstract long countForDataTable(String search);
	
	@Query("SELECT COUNT(objCliente) FROM ClienteEntity objCliente WHERE ( objCliente.cliente like %?1% OR objCliente.razonSocial like %?1% OR objCliente.usuarioEjecutivo.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoS3s.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoR2a.nombreCompleto like %?1% OR objCliente.creacionUsuario.nombreCompleto like %?1% OR objCliente.idCliente like %?1%) AND CONVERT(objCliente.creacionFecha, DATE) BETWEEN ?2 AND ?3")	
	public abstract long countForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);
				
	@Query("SELECT objCliente FROM ClienteEntity objCliente order by objCliente.idCliente ASC")
	public abstract List<ClienteEntity> findForDataTable(Pageable page);
				
	@Query("SELECT objCliente FROM ClienteEntity objCliente WHERE (objCliente.cliente like %?1% OR objCliente.razonSocial like %?1% OR objCliente.usuarioEjecutivo.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoS3s.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoR2a.nombreCompleto like %?1% OR objCliente.creacionUsuario.nombreCompleto like %?1% OR objCliente.idCliente like %?1%) order by objCliente.idCliente ASC")
	public abstract List<ClienteEntity> findForDataTable(String search,Pageable page);
	
	@Query("SELECT objCliente FROM ClienteEntity objCliente WHERE (objCliente.cliente like %?1% OR objCliente.razonSocial like %?1% OR objCliente.usuarioEjecutivo.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoS3s.nombreCompleto like %?1% OR objCliente.usuarioEjecutivoR2a.nombreCompleto like %?1% OR objCliente.creacionUsuario.nombreCompleto like %?1% OR objCliente.idCliente like %?1%) AND CONVERT(objCliente.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objCliente.idCliente ASC")
	public abstract List<ClienteEntity> findForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);
}
