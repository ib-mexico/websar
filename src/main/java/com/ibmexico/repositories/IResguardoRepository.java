package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ResguardoEntity;

@Repository("resguardoRepository")
public interface IResguardoRepository extends JpaRepository<ResguardoEntity, Serializable> {

	public abstract ResguardoEntity findByIdResguardo(int idResguardo);
	
	@Query("SELECT COUNT(objResguardo) FROM ResguardoEntity objResguardo WHERE objResguardo.eliminado = false AND (objResguardo.folio like %?1% OR objResguardo.usuarioEntrega.nombreCompleto like %?1% OR objResguardo.usuarioRecibe.nombreCompleto like %?1%)")
	public abstract long countForDataTable(String search);
	
	@Query("SELECT COUNT(objResguardo) FROM ResguardoEntity objResguardo WHERE objResguardo.usuarioEntrega.idUsuario = ?1 AND objResguardo.eliminado = false AND (objResguardo.folio like %?2% OR objResguardo.usuarioEntrega.nombreCompleto like %?2% OR objResguardo.usuarioRecibe.nombreCompleto like %?2%)")	
	public abstract long countForDataTable(int idUsuario, String search);
	
	@Query("SELECT objResguardo FROM ResguardoEntity objResguardo WHERE objResguardo.eliminado = false AND (objResguardo.folio like %?1% OR objResguardo.usuarioEntrega.nombreCompleto like %?1% OR objResguardo.usuarioRecibe.nombreCompleto like %?1%) order by objResguardo.creacionFecha DESC")
	public abstract List<ResguardoEntity> findForDataTable(String search, Pageable page);
	
	@Query("SELECT objResguardo FROM ResguardoEntity objResguardo WHERE objResguardo.usuarioEntrega.idUsuario = ?1 AND objResguardo.eliminado = false AND (objResguardo.folio like %?2% OR objResguardo.usuarioEntrega.nombreCompleto like %?2% OR objResguardo.usuarioRecibe.nombreCompleto like %?2%) order by objResguardo.creacionFecha DESC")
	public abstract List<ResguardoEntity> findForDataTable(int idUsuario, String search, Pageable page);
}
