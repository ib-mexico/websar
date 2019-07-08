package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.EquipoProduccionEstatusEntity;

@Repository("equipoProduccionEstatusRepository")
public interface IEquipoProduccionEstatusRepository extends JpaRepository<EquipoProduccionEstatusEntity, Serializable> {
	
	public abstract EquipoProduccionEstatusEntity findByIdEquipoProduccionEstatus(int idEquipoProduccionEstatus);

	@Query("SELECT objEstatus FROM EquipoProduccionEstatusEntity objEstatus WHERE objEstatus.eliminado = false")
	public abstract List<EquipoProduccionEstatusEntity> listEstatusActivos();
}
