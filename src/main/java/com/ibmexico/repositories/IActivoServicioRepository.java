package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ActivoServicioEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activo_servicio_repository")
public interface IActivoServicioRepository extends JpaRepository<ActivoServicioEntity, Serializable> {

    public abstract ActivoServicioEntity findByIdActivoServicio (int idActivoServicio);

    @Query("SELECT objServicio FROM ActivoServicioEntity objServicio WHERE objServicio.idTipoActivo.idCatalogoActivo = ?1")
    public abstract List<ActivoServicioEntity> findByIdTipoActivo(int idActivoServicio);


    
}