package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ActivoEstatusEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activo_estatus_repository")
public interface IActivoEstatusRepository extends JpaRepository<ActivoEstatusEntity, Serializable>{

    public abstract ActivoEstatusEntity findByIdActivoEstatus(int idActivoEstatus);
    
    @Query("SELECT objEstatus FROM ActivoEstatusEntity objEstatus WHERE objEstatus.eliminado=false AND objEstatus.opcion_Direccion!=false")
    public abstract List<ActivoEstatusEntity> lstEstatus();
}