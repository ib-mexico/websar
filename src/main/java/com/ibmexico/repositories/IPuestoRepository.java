package com.ibmexico.repositories;

import java.io.Serializable;

import com.ibmexico.entities.PuestoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("puesto_repository")
public interface IPuestoRepository extends JpaRepository<PuestoEntity, Serializable>{

    public abstract PuestoEntity findByIdPuesto(int idPuesto);

    @Query("SELECT objPuesto from PuestoEntity objPuesto WHERE objPuesto.eliminado = false")
    public abstract java.util.List<PuestoEntity> puestosAll();
}