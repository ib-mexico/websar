package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.CatalogoActivoEntity;

@Repository("catalogoActivoRepository")
public interface ICatalogoActivoRepository extends JpaRepository<CatalogoActivoEntity, Serializable> {
	
	public abstract CatalogoActivoEntity findByIdCatalogoActivo(int idCatalogoActivo);
	
	@Query("SELECT objCatalogo FROM CatalogoActivoEntity objCatalogo WHERE objCatalogo.eliminado=false")
	public abstract List<CatalogoActivoEntity> listCatalogoActivo();
	
	//TABLE
	@Query("SELECT COUNT(objCatalogo) FROM CatalogoActivoEntity objCatalogo ")	
	public abstract long countForDataTable();
				
	@Query("SELECT objCatalogo FROM CatalogoActivoEntity objCatalogo  WHERE objCatalogo.eliminado=false")
	public abstract List<CatalogoActivoEntity> findForDataTable(Pageable page);
}
