package com.ibmexico.repositories;

import java.io.Serializable;

import com.ibmexico.entities.ActivoEstatusEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("activo_estatus_repository")
public interface IActivoEstatusRepository extends JpaRepository<ActivoEstatusEntity, Serializable>{

    public abstract ActivoEstatusEntity findByIdActivoEstatus(int idActivoEstatus);
    
}