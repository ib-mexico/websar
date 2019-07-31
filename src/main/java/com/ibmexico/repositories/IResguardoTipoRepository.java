package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ResguardoTipoEntity;

@Repository("resguardoTipoRepository")
public interface IResguardoTipoRepository extends JpaRepository<ResguardoTipoEntity, Serializable> {
	
	public abstract ResguardoTipoEntity findByIdResguardoTipo(int idResguardoTipo);

}
