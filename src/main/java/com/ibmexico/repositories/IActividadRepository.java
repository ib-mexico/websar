package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ActividadEntity;

@Repository("actividadRepository")
public interface IActividadRepository extends JpaRepository<ActividadEntity, Serializable> {

	public abstract ActividadEntity findByIdActividad(int idActividad);
	
	public abstract List<ActividadEntity> findByUsuario_IdUsuario(int idUsuario);
	
	@Query("SELECT objActividad FROM ActividadEntity objActividad WHERE objActividad.usuario.idUsuario = ?1 AND objActividad.finalizado = false")
	public abstract List<ActividadEntity> listActividadesNoFinalizadas(int idUsuario);
	
	@Query("SELECT objActividad FROM ActividadEntity objActividad")
	public abstract List<ActividadEntity> listActividades();
	
	@Query("SELECT objActividad FROM ActividadEntity objActividad WHERE objActividad.usuario.idUsuario = ?1")
	public abstract List<ActividadEntity> listActividades(int idUsuario);
}
