package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.OportunidadNegocioFicheroEntity;


@Repository("oportunidadNegocioFicheroRepository")
public interface IOportunidadNegocioFicheroRepository extends JpaRepository<OportunidadNegocioFicheroEntity, Serializable> {

	public abstract OportunidadNegocioFicheroEntity findByIdOportunidadNegocioFichero(int idOportunidadNegocioFichero);
	
	public abstract List<OportunidadNegocioFicheroEntity> findByOportunidadNegocio_IdOportunidadNegocio(int idOportunidad);
	
	/**Buscar si existe una llamda de calidad para esta oportunidad de Negocio */
	@Query("SELECT COUNT(objOpnFichero) FROM OportunidadNegocioFicheroEntity objOpnFichero WHERE objOpnFichero.cotizacionTipoFichero.idCotizacionTipoFichero =6 AND objOpnFichero.oportunidadNegocio.idOportunidadNegocio = ?1 AND objOpnFichero.inicioLlamada IS NOT NULL")
	public abstract long countOpnFicheroCalidad(int idOportunidadNegocio);
}
