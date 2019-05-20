package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.FormaPagoEntity;

@Repository("formaPagoRepository")
public interface IFormaPagoRepository extends JpaRepository<FormaPagoEntity, Serializable> {

	public abstract FormaPagoEntity findByIdFormaPago(int idFormaPago);
}