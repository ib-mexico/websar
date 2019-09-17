package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.BienActivoEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("bienActivoRepository")
public interface IBienActivoRepository extends JpaRepository<BienActivoEntity, Serializable>{


    public abstract BienActivoEntity findByIdActivoMobiliario(int idActivoMobiliario);

    //TABLE

	@Query("SELECT COUNT(objActivo) FROM BienActivoEntity objActivo ")	
	public abstract long countForDataTable();
				
	@Query("SELECT objActivo FROM BienActivoEntity objActivo  WHERE objActivo.estatus=true")
    public abstract List<BienActivoEntity> findForDataTable(Pageable page);
    
}