package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.EquipoProduccionEntity;

@Repository("equipoProduccionRepository")
public interface IEquipoProduccionRepository extends JpaRepository<EquipoProduccionEntity, Serializable> {

	public abstract EquipoProduccionEntity findByIdEquipoProduccion(int idEquipoProduccion);
	
	@Query("SELECT objEquipo FROM EquipoProduccionEntity objEquipo")
	public abstract List<EquipoProduccionEntity> listEquiposProduccion();
	
	@Query("SELECT objEquipo FROM EquipoProduccionEntity objEquipo WHERE objEquipo.renovacionFecha = ?1")
	public abstract List<EquipoProduccionEntity> listEquiposProduccionVencimiento(LocalDate ldFecha);
	
	//TABLE
	@Query("SELECT COUNT(objEquipo) FROM EquipoProduccionEntity objEquipo")	
	public abstract long countForDataTable();
	
	@Query("SELECT COUNT(objEquipo) FROM EquipoProduccionEntity objEquipo WHERE (objEquipo.cliente.cliente like %?1% OR objEquipo.numeroSerie like %?1% OR objEquipo.modelo like %?1%)")
	public abstract long countForDataTable(String search);
				
	@Query("SELECT objEquipo FROM EquipoProduccionEntity objEquipo order by objEquipo.idEquipoProduccion DESC")
	public abstract List<EquipoProduccionEntity> findForDataTable(Pageable page);
				
	@Query("SELECT objEquipo FROM EquipoProduccionEntity objEquipo WHERE (objEquipo.cliente.cliente like %?1% OR objEquipo.numeroSerie like %?1% OR objEquipo.modelo like %?1%) order by objEquipo.renovacionFecha ASC")
	public abstract List<EquipoProduccionEntity> findForDataTable(String search,Pageable page);
}
