package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.DepartamentoEntity;

@Repository("departamentoRepository")
public interface IDepartamentoRepository extends JpaRepository<DepartamentoEntity, Serializable> {

	public abstract DepartamentoEntity findByIdDepartamento(int idDepartamento);
}
