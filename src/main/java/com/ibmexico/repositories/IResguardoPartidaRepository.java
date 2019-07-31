package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ResguardoPartidaEntity;

@Repository("resguardoPartidaRepository")
public interface IResguardoPartidaRepository extends JpaRepository<ResguardoPartidaEntity, Serializable> {

	public abstract ResguardoPartidaEntity findByIdResguardoPartida(int idResguardoPartida);
	
	@Query("SELECT objPartida FROM ResguardoPartidaEntity objPartida WHERE objPartida.resguardo.idResguardo = ?1")
	public abstract List<ResguardoPartidaEntity> findResguardoPartidas(int idEntrega);
	
	@Modifying
	@Query("DELETE FROM ResguardoPartidaEntity objPartida WHERE objPartida.resguardo.idResguardo = ?1")
	public void removeResguardoPartidas(int idEntrega);
}
