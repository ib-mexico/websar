package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.TipoGastoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository("tipo_gasto_rep")
public interface ITipoGastoRepository extends JpaRepository<TipoGastoEntity, Serializable>{
    
    public abstract TipoGastoEntity findByIdTipoGasto(int idTipoGasto);

    @Query("SELECT objTipogasto FROM TipoGastoEntity objTipogasto WHERE objTipogasto.eliminado=false")
    public abstract List<TipoGastoEntity> findActivos();
}