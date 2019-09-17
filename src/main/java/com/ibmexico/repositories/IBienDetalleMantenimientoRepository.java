package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.BienDetalleMantenimientoEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("bienDetalleMantenimientoRepository")
public interface IBienDetalleMantenimientoRepository extends JpaRepository<BienDetalleMantenimientoEntity, Serializable>{

    public abstract BienDetalleMantenimientoEntity findByIdDetalleMantenimiento(int idDetalleMantenimiento);

    //TABLE

	@Query("SELECT COUNT(objDetalleMant) FROM BienDetalleMantenimientoEntity objDetalleMant ")	
	public abstract long countForDataTable();
				
	@Query("SELECT objDetalleMant FROM BienDetalleMantenimientoEntity objDetalleMant  WHERE objDetalleMant.finalizado=true")
    public abstract List<BienDetalleMantenimientoEntity> findForDataTable(Pageable page);


}