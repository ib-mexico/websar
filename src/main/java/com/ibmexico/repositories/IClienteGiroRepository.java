package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ClienteGiroEntity;

@Repository("clienteGiroRepository")
public interface IClienteGiroRepository extends JpaRepository<ClienteGiroEntity, Serializable> {

	public abstract ClienteGiroEntity findByIdClienteGiro(int idClienteGiro);
}
