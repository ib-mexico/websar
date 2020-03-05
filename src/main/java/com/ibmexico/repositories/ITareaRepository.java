package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.TareaEntity;

@Repository("tareaRepository")
public interface ITareaRepository extends JpaRepository<TareaEntity, Serializable> {

	public abstract TareaEntity findByIdTarea(int idTarea);	
	
	@Query("SELECT objTarea FROM TareaEntity objTarea")
	public abstract List<TareaEntity> listTareas();
	
	@Query("SELECT objTarea FROM TareaEntity objTarea WHERE objTarea.usuario.idUsuario = ?1")
	public abstract List<TareaEntity> listTareas(int idUsuario);

	@Query("SELECT objTarea FROM TareaEntity objTarea WHERE objTarea.usuario.idUsuario = ?1 AND CONVERT(objTarea.inicioFecha, DATE) BETWEEN ?2 AND ?3")
	public abstract List<TareaEntity> listTareas(int idUsuario, LocalDate fechaInicio, LocalDate fechaFin);
}
