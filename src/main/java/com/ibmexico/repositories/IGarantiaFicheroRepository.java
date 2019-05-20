package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.GarantiaFicheroEntity;


@Repository("garantiaFicheroRepository")
public interface IGarantiaFicheroRepository extends JpaRepository<GarantiaFicheroEntity, Serializable> {

	public abstract List<GarantiaFicheroEntity> findByGarantia_IdGarantia(int idGarantia);

}
