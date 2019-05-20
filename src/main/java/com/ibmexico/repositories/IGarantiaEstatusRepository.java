package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.GarantiaEstatusEntity;

@Repository("garantiaEstatusRepository")
public interface IGarantiaEstatusRepository extends JpaRepository<GarantiaEstatusEntity, Serializable> {
	
	public abstract GarantiaEstatusEntity findByIdGarantiaEstatus(int idGarantiaEstatus);

}
