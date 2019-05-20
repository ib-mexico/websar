package com.ibmexico.repositories;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CotizacionPartidaEntity;

@Repository("cotizacionPartidaRepository")
public interface ICotizacionPartidaRepository   extends JpaRepository<CotizacionPartidaEntity, Serializable> {

	public abstract CotizacionPartidaEntity findByIdCotizacionPartida(int findByIdCotizacionPartida);
	
	@Query("SELECT COALESCE(SUM(objCotizacionPartida.total), 0) FROM CotizacionPartidaEntity objCotizacionPartida WHERE objCotizacionPartida.cotizacion.idCotizacion = ?1")
	public abstract BigDecimal sumTotalCotizacionPartidas(int idCotizacion);
	
	//TABLE
	@Query("SELECT COUNT(objCotizacionPartida) FROM CotizacionPartidaEntity objCotizacionPartida WHERE objCotizacionPartida.cotizacion.idCotizacion = ?1")	
	public abstract long countForDataTable(int idCotizacion);
	
	@Query("SELECT COUNT(objCotizacionPartida) FROM CotizacionPartidaEntity objCotizacionPartida WHERE objCotizacionPartida.cotizacion.idCotizacion = ?1 AND objCotizacionPartida.eliminado = false AND (objCotizacionPartida.descripcion like %?2% OR objCotizacionPartida.numeroParte like %?2%) order by objCotizacionPartida.ordenIndex ASC")	
	public abstract long countForDataTable(int idCotizacion, String search);
	
	@Query("SELECT objCotizacionPartida FROM CotizacionPartidaEntity objCotizacionPartida WHERE objCotizacionPartida.cotizacion.idCotizacion = ?1 order by objCotizacionPartida.ordenIndex ASC")
	public abstract List<CotizacionPartidaEntity> findForDataTable(int idCotizacion, Pageable page);
				
	@Query("SELECT objCotizacionPartida FROM CotizacionPartidaEntity objCotizacionPartida WHERE objCotizacionPartida.cotizacion.idCotizacion = ?1 AND objCotizacionPartida.eliminado = false AND (objCotizacionPartida.descripcion like %?2% OR objCotizacionPartida.numeroParte like %?2%) order by objCotizacionPartida.ordenIndex ASC")
	public abstract List<CotizacionPartidaEntity> findForDataTable(int idCotizacion, String search, Pageable page);
}
