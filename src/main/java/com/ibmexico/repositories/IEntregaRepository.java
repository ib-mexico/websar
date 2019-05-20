package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.EntregaEntity;

@Repository("entregaRepository")
public interface IEntregaRepository extends JpaRepository<EntregaEntity, Serializable> {

	public abstract EntregaEntity findByIdEntrega(int idEntrega);
	
	@Query("SELECT COUNT(objEntrega) FROM EntregaEntity objEntrega WHERE objEntrega.eliminado = false AND (objEntrega.folio like %?1% OR objEntrega.usuarioEntrega.nombreCompleto like %?1% OR objEntrega.cliente.cliente like %?1%)")
	public abstract long countForDataTable(String search);
	
	@Query("SELECT COUNT(objEntrega) FROM EntregaEntity objEntrega WHERE objEntrega.usuarioEntrega.idUsuario = ?1 AND objEntrega.eliminado = false AND (objEntrega.folio like %?2% OR objEntrega.usuarioEntrega.nombreCompleto like %?2% OR objEntrega.cliente.cliente like %?2%)")	
	public abstract long countForDataTable(int idUsuario, String search);
	
	@Query("SELECT objEntrega FROM EntregaEntity objEntrega WHERE objEntrega.eliminado = false AND (objEntrega.folio like %?1% OR objEntrega.usuarioEntrega.nombreCompleto like %?1% OR objEntrega.cliente.cliente like %?1%) order by objEntrega.creacionFecha DESC")
	public abstract List<EntregaEntity> findForDataTable(String search, Pageable page);
	
	@Query("SELECT objEntrega FROM EntregaEntity objEntrega WHERE objEntrega.usuarioEntrega.idUsuario = ?1 AND objEntrega.eliminado = false AND (objEntrega.folio like %?2% OR objEntrega.usuarioEntrega.nombreCompleto like %?2% OR objEntrega.cliente.cliente like %?2%) order by objEntrega.creacionFecha DESC")
	public abstract List<EntregaEntity> findForDataTable(int idUsuario, String search, Pageable page);
}
