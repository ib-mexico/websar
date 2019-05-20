package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CotizacionEstatusEntity;

@Repository("cotizacionEstatusRepository")
public interface ICotizacionEstatusRepository extends JpaRepository<CotizacionEstatusEntity, Serializable> {
	
	public abstract CotizacionEstatusEntity findByIdCotizacionEstatus(int idCotizacionEstatus);

}
