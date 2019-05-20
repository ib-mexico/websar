package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ProveedorEntity;

@Repository("proveedorRepository")
public interface IProveedorRepository extends JpaRepository<ProveedorEntity, Serializable> {

	public abstract ProveedorEntity findByIdProveedor(int idProveedor);
	
	@Query("SELECT objProveedor FROM ProveedorEntity objProveedor WHERE objProveedor.eliminado = false")
	public abstract List<ProveedorEntity> findActivos();
	
	//TABLE
	@Query("SELECT COUNT(objProveedor) FROM ProveedorEntity objProveedor ")	
	public abstract long countForDataTable();
	
	@Query("SELECT COUNT(objProveedor) FROM ProveedorEntity objProveedor WHERE (objProveedor.proveedor like %?1%)")	
	public abstract long countForDataTable(String search);
				
	@Query("SELECT objProveedor FROM ProveedorEntity objProveedor order by objProveedor.proveedor ASC")
	public abstract List<ProveedorEntity> findForDataTable(Pageable page);
				
	@Query("SELECT objProveedor FROM ProveedorEntity objProveedor WHERE (objProveedor.proveedor like %?1%) order by objProveedor.proveedor ASC")
	public abstract List<ProveedorEntity> findForDataTable(String search,Pageable page);
}
