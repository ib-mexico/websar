package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ActividadTipoEntity;

@Repository("actividadTipoRepository")
public interface IActividadTipoRepository extends JpaRepository<ActividadTipoEntity, Serializable> {
	
	public abstract ActividadTipoEntity findByIdActividadTipo(int idActividadTipo);
}
