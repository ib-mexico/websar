package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ClasificacionTipoGastoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("clasificacion_tipogasto_repository")
public interface IClasificacionTipoGastoRepository extends JpaRepository<ClasificacionTipoGastoEntity, Serializable>{

    public abstract ClasificacionTipoGastoEntity findByIdClasificacion(int IdClasificacion);
    
    @Query("SELECT objClasfTipoGasto FROM ClasificacionTipoGastoEntity objClasfTipoGasto WHERE objClasfTipoGasto.eliminado=false AND objClasfTipoGasto.tipoGasto.idTipoGasto=?1")
    public abstract List<ClasificacionTipoGastoEntity> findClasificacionTipoGastoActivo(int idTipoGasto);

}