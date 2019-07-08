package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.EquipoMarcaEntity;

@Repository("equipoMarcaRepository")
public interface IEquipoMarcaRepository extends JpaRepository<EquipoMarcaEntity, Serializable> {
	
	public abstract EquipoMarcaEntity findByIdEquipoMarca(int idEquipoMarca);

	@Query("SELECT objMarca FROM EquipoMarcaEntity objMarca WHERE objMarca.eliminado = false")
	public abstract List<EquipoMarcaEntity> listMarcasActivas();
}
