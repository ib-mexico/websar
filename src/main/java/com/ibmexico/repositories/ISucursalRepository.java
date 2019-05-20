package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.SucursalEntity;

@Repository("sucursalRepository")
public interface ISucursalRepository extends JpaRepository<SucursalEntity, Serializable> {

	public abstract SucursalEntity findByIdSucursal(int idSucursal);
	
	//TABLE
	@Query("SELECT COUNT(objSucursal) FROM SucursalEntity objSucursal ")	
	public abstract long countForDataTable();
				
	@Query("SELECT objSucursal FROM SucursalEntity objSucursal ")
	public abstract List<SucursalEntity> findForDataTable(Pageable page);
}
