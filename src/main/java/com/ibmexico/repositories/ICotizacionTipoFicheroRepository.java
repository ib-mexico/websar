package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CotizacionTipoFicheroEntity;

@Repository("cotizacionTipoFicheroRepository")
public interface ICotizacionTipoFicheroRepository extends JpaRepository<CotizacionTipoFicheroEntity, Serializable> {

	public abstract CotizacionTipoFicheroEntity findByIdCotizacionTipoFichero(int idCotizacionTipoFichero);
}
