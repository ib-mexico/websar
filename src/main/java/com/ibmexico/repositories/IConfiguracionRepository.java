package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ConfiguracionEntity;

@Repository("configuracionRepository")
public interface IConfiguracionRepository extends JpaRepository<ConfiguracionEntity, Serializable> {

	public abstract ConfiguracionEntity findByVariable(String variable);
	
}
