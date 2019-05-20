package com.ibmexico.repositories;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.OrdenServicioPartidaEntity;

@Repository("ordenServicioPartidaRepository")
public interface IOrdenServicioPartidaRepository extends JpaRepository<OrdenServicioPartidaEntity, Serializable> {

	public abstract OrdenServicioPartidaEntity findByIdOrdenServicioPartida(int idOrdenServicioPartida);
	
	@Query("SELECT COALESCE(SUM(objPartida.importe), 0) FROM OrdenServicioPartidaEntity objPartida WHERE objPartida.ordenServicio.idOrdenServicio = ?1")
	public abstract BigDecimal sumOrdenServicioPartidas(int idOrdenServicio);
	
	@Query("SELECT objPartida FROM OrdenServicioPartidaEntity objPartida WHERE objPartida.ordenServicio.idOrdenServicio = ?1")
	public abstract List<OrdenServicioPartidaEntity> findOrdenServicioPartidas(int idOrdenServicio);
	
	@Modifying
	@Query("DELETE FROM OrdenServicioPartidaEntity objPartida WHERE objPartida.ordenServicio.idOrdenServicio = ?1")
	public void removeOrdenServicioPartidas(int idOrdenServicio);
}
