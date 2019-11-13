package com.ibmexico.repositories;

import java.io.Serializable;

import com.ibmexico.entities.FacturaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("factura_gastos")
public interface IFacturaRepository extends JpaRepository<FacturaEntity ,Serializable>{
    public abstract FacturaEntity findByIdFactura(int idFactura);
}