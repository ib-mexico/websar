package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.OportunidadNegocioEstatusEntity;

@Repository("oportunidadNegocioEstatusRepository")
public interface IOportunidadNegocioEstatusRepository extends JpaRepository<OportunidadNegocioEstatusEntity, Serializable> {

	public abstract OportunidadNegocioEstatusEntity findByIdOportunidadNegocioEstatus(int idOportunidadNegocioEstatus);
	
	@Query("SELECT objOportunidadEstatus FROM OportunidadNegocioEstatusEntity objOportunidadEstatus")
	public abstract List<OportunidadNegocioEstatusEntity> findAll();
}
