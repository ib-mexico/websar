package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.GarantiaEntity;

@Repository("garantiaRepository")
public interface IGarantiaRepository extends JpaRepository<GarantiaEntity, Serializable> {

	public abstract GarantiaEntity findByIdGarantia(int idGarantia);
	
	@Query("SELECT objGarantia FROM GarantiaEntity objGarantia")
	public abstract List<GarantiaEntity> listGarantias();
	
	//TABLE
	@Query("SELECT COUNT(objGarantia) FROM GarantiaEntity objGarantia ")	
	public abstract long countForDataTable();
	
	@Query("SELECT COUNT(objGarantia) FROM GarantiaEntity objGarantia WHERE (objGarantia.cliente.cliente like %?1% OR objGarantia.cliente.razonSocial like %?1%)")	
	public abstract long countForDataTable(String search);
				
	@Query("SELECT objGarantia FROM GarantiaEntity objGarantia order by objGarantia.idGarantia DESC")
	public abstract List<GarantiaEntity> findForDataTable(Pageable page);
				
	@Query("SELECT objGarantia FROM GarantiaEntity objGarantia WHERE (objGarantia.cliente.cliente like %?1% OR objGarantia.cliente.razonSocial like %?1%) order by objGarantia.idGarantia DESC")
	public abstract List<GarantiaEntity> findForDataTable(String search,Pageable page);
}
