package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.EntregaProductoEntity;

@Repository("entregaProductoRepository")
public interface IEntregaProductoRepository extends JpaRepository<EntregaProductoEntity, Serializable> {

	public abstract EntregaProductoEntity findByIdEntregaProducto(int idEntregaProducto);
	
	@Query("SELECT objProducto FROM EntregaProductoEntity objProducto WHERE objProducto.entrega.idEntrega = ?1")
	public abstract List<EntregaProductoEntity> findEntregasProductos(int idEntrega);
	
	@Modifying
	@Query("DELETE FROM EntregaProductoEntity objProducto WHERE objProducto.entrega.idEntrega = ?1")
	public void removeEntregaProductos(int idEntrega);
}
