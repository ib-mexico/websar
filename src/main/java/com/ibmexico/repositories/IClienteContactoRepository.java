package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ClienteContactoEntity;

@Repository("clienteContactoRepository")
public interface IClienteContactoRepository extends JpaRepository<ClienteContactoEntity, Serializable> {

	public abstract ClienteContactoEntity findByIdClienteContacto(int idClienteContacto);
	
	public abstract List<ClienteContactoEntity> findByCliente_IdCliente(int idCliente);
	
	@Query("SELECT objClienteContacto FROM ClienteContactoEntity objClienteContacto WHERE objClienteContacto.cliente.idCliente = ?1 AND objClienteContacto.eliminado = false")
	public abstract List<ClienteContactoEntity> findContactosActivos(int idCliente);
	
	
	//TABLE
	@Query("SELECT COUNT(objClienteContacto) FROM ClienteContactoEntity objClienteContacto WHERE objClienteContacto.cliente.idCliente  = ?1")	
	public abstract long countForDataTable(int idCliente);
	
	@Query("SELECT COUNT(objClienteContacto) FROM ClienteContactoEntity objClienteContacto WHERE objClienteContacto.cliente.idCliente  = ?1 AND (objClienteContacto.contacto like %?2% OR objClienteContacto.correo like %?2%)")	
	public abstract long countForDataTable(int idCliente, String search);
	
	@Query("SELECT objClienteContacto FROM ClienteContactoEntity objClienteContacto WHERE objClienteContacto.cliente.idCliente = ?1 order by objClienteContacto.contacto ASC")
	public abstract List<ClienteContactoEntity> findForDataTable(int idCliente, Pageable page);
				
	@Query("SELECT objClienteContacto FROM ClienteContactoEntity objClienteContacto WHERE objClienteContacto.cliente.idCliente = ?1 AND (objClienteContacto.contacto like %?2% OR objClienteContacto.correo like %?2%) order by objClienteContacto.contacto ASC")
	public abstract List<ClienteContactoEntity> findForDataTable(int idCliente, String search, Pageable page);
	 	
}
