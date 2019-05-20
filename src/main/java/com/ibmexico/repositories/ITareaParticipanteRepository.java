package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.TareaParticipanteEntity;

@Repository("tareaParticipanteRepository")
public interface ITareaParticipanteRepository extends JpaRepository<TareaParticipanteEntity, Serializable> {

	public abstract TareaParticipanteEntity findByIdTareaParticipante(int idTareaParticipante);	
	
	public abstract List<TareaParticipanteEntity> findByTarea_IdTarea(int idTarea);
	
	@Query("SELECT objParticipante FROM TareaParticipanteEntity objParticipante")
	public abstract List<TareaParticipanteEntity> listTareaParticipantes();
	
	@Query("SELECT objParticipante FROM TareaParticipanteEntity objParticipante WHERE objParticipante.usuario.idUsuario = ?1")
	public abstract List<TareaParticipanteEntity> listTareaParticipantes(int idUsuario);
	
	@Modifying
	@Query("DELETE FROM TareaParticipanteEntity objParticipante WHERE objParticipante.tarea.idTarea = ?1")
	public void removeTareaParticipantes(int idTarea);
}
