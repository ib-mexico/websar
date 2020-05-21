package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.CotizacionClasificacionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("cotizacionClasificacionRepository")
public interface ICotizacionClasificacionRepository extends JpaRepository<CotizacionClasificacionEntity, Serializable> {
	
	public abstract CotizacionClasificacionEntity findByIdCotizacionClasificacion(int idCotizacionEstatus);

	public abstract List<CotizacionClasificacionEntity> findAllByOrderByOrdenAsc();

}
