package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.MonedaEntity;

@Repository("monedaRepository")
public interface IMonedaRepository extends JpaRepository<MonedaEntity, Serializable> {

	public abstract MonedaEntity findByIdMoneda(int idMoneda);
}
