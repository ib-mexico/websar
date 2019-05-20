package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ProveedorContactoEntity;

@Repository("proveedorContactoRepository")
public interface IProveedorContactoRepository extends JpaRepository<ProveedorContactoEntity, Serializable> {

	public abstract ProveedorContactoEntity findByIdProveedorContacto(int idProveedorContacto);
	
	public abstract List<ProveedorContactoEntity> findByProveedor_IdProveedor(int idProveedor);
	
	@Query("SELECT objProveedorContacto FROM ProveedorContactoEntity objProveedorContacto WHERE objProveedorContacto.proveedor.idProveedor = ?1 AND objProveedorContacto.eliminado = false")
	public abstract List<ProveedorContactoEntity> findContactosActivos(int idProveedor);
	
	
	//TABLE
	@Query("SELECT COUNT(objProveedorContacto) FROM ProveedorContactoEntity objProveedorContacto WHERE objProveedorContacto.proveedor.idProveedor  = ?1")	
	public abstract long countForDataTable(int idProveedor);
	
	@Query("SELECT COUNT(objProveedorContacto) FROM ProveedorContactoEntity objProveedorContacto WHERE objProveedorContacto.proveedor.idProveedor  = ?1 AND (objProveedorContacto.contacto like %?2% OR objProveedorContacto.correo like %?2%)")	
	public abstract long countForDataTable(int idProveedor, String search);
	
	@Query("SELECT objProveedorContacto FROM ProveedorContactoEntity objProveedorContacto WHERE objProveedorContacto.proveedor.idProveedor = ?1 order by objProveedorContacto.contacto ASC")
	public abstract List<ProveedorContactoEntity> findForDataTable(int idProveedor, Pageable page);
				
	@Query("SELECT objProveedorContacto FROM ProveedorContactoEntity objProveedorContacto WHERE objProveedorContacto.proveedor.idProveedor = ?1 AND (objProveedorContacto.contacto like %?2% OR objProveedorContacto.correo like %?2%) order by objProveedorContacto.contacto ASC")
	public abstract List<ProveedorContactoEntity> findForDataTable(int idProveedor, String search, Pageable page);
	 	
}
