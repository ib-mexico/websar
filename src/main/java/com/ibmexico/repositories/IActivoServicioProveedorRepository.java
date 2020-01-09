package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.ActivoServicioProveedorEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("activoServicioProveedorRepository")
public interface IActivoServicioProveedorRepository extends JpaRepository<ActivoServicioProveedorEntity, Serializable>{
    
    //Busqueda por id servicio -> proveedor
    public abstract ActivoServicioProveedorEntity findByIdServicioProveedor(int idServicioProveedor);

    @Modifying
    @Query("DELETE FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.idServicioProveedor = ?1")
    public void deleteServicio(int idServicioProveedor);

    //Consulta de proveedor servicio, dependiendo del tipoactivo seleccionado
    @Query("SELECT DISTINCT objServicioProveedor  FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.activoServicio.tipoActivo.idCatalogoActivo=?1 AND objServicioProveedor.activoServicio.idServicioActivo=?2 AND objServicioProveedor.activoServicio.idServicioActivo=?2  GROUP BY objServicioProveedor.activoProveedor.proveedor")
    public abstract List<ActivoServicioProveedorEntity> findByActivoServicio(int idTipoActivo, int idServicioActivo);

    @Query("SELECT objServProvee  FROM ActivoServicioProveedorEntity objServProvee WHERE  objServProvee.activoProveedor.idProveedor = ?1")
    public abstract List<ActivoServicioProveedorEntity> lstServicioIdProveedor(int idProveedor);

    //TABLE
	@Query("SELECT COUNT(objServicioProveedor) FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.activoProveedor.idProveedor  = ?1")	
	public abstract long countForDataTable(int idProveedor);
	
	@Query("SELECT COUNT(objServicioProveedor) FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.activoProveedor.idProveedor  = ?1 AND (objServicioProveedor.activoProveedor.proveedor like %?2% OR objServicioProveedor.activoProveedor.razonSocial like %?2% OR objServicioProveedor.activoProveedor.rfc like %?2%)")	
	public abstract long countForDataTable(int idProveedor, String search);
	
	@Query("SELECT objServicioProveedor FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.activoProveedor.idProveedor = ?1 order by objServicioProveedor.idServicioProveedor ASC")
	public abstract List<ActivoServicioProveedorEntity> findForDataTable(int idProveedor, Pageable page);
				
	@Query("SELECT objServicioProveedor FROM ActivoServicioProveedorEntity objServicioProveedor WHERE objServicioProveedor.activoProveedor.idProveedor  = ?1 AND (objServicioProveedor.activoProveedor.proveedor like %?2% OR objServicioProveedor.activoProveedor.razonSocial like %?2% OR objServicioProveedor.activoProveedor.rfc like %?2%) order by objServicioProveedor.idServicioProveedor ASC")
	public abstract List<ActivoServicioProveedorEntity> findForDataTable(int idProveedor, String search, Pageable page);
	 
}