package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.OportunidadNegocioFicheroEntity;


@Repository("oportunidadNegocioFicheroRepository")
public interface IOportunidadNegocioFicheroRepository extends JpaRepository<OportunidadNegocioFicheroEntity, Serializable> {

	public abstract OportunidadNegocioFicheroEntity findByIdOportunidadNegocioFichero(int idOportunidadNegocioFichero);
	
	public abstract List<OportunidadNegocioFicheroEntity> findByOportunidadNegocio_IdOportunidadNegocio(int idOportunidad);
	
}
