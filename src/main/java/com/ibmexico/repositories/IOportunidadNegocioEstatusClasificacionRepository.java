package com.ibmexico.repositories;
import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.OportunidadNegocioEstatusClasificacionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("oportunidadNegocioEstatusClasificacionRepository")
public interface IOportunidadNegocioEstatusClasificacionRepository extends JpaRepository<OportunidadNegocioEstatusClasificacionEntity, Serializable> {

    public abstract OportunidadNegocioEstatusClasificacionEntity findByIdOpnNegocioEstatusClasificacion(int idOpNegocioEstatusClasificacion);
        
    @Query("SELECT objOportunidadEstatusClasificacion FROM OportunidadNegocioEstatusClasificacionEntity objOportunidadEstatusClasificacion WHERE objOportunidadEstatusClasificacion.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?1 AND objOportunidadEstatusClasificacion.eliminado = false")
    public abstract List<OportunidadNegocioEstatusClasificacionEntity> findByIdOpnEstatus(int idOpnEstatus);


	@Query("SELECT objOportunidadEstatusClasificacion FROM OportunidadNegocioEstatusClasificacionEntity objOportunidadEstatusClasificacion")
	public abstract List<OportunidadNegocioEstatusClasificacionEntity> findAll();

}