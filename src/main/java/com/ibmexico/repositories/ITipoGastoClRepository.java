package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.TipoGastoClEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository("principal_clasificacion_gasto_rep")
public interface ITipoGastoClRepository extends JpaRepository<TipoGastoClEntity, Serializable>{
    
    public abstract TipoGastoClEntity findByIdTipoGastoCl(int id);

    @Query("SELECT objTipogastoCl FROM TipoGastoClEntity objTipogastoCl WHERE objTipogastoCl.eliminado=false")
    public abstract List<TipoGastoClEntity> findActivos();
}