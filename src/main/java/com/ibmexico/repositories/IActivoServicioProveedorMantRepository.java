package com.ibmexico.repositories;

import java.io.Serializable;

import com.ibmexico.entities.ActivoServicioProveedorMantEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("activo_servicio_proveedor_mant_repo")
public interface IActivoServicioProveedorMantRepository extends JpaRepository<ActivoServicioProveedorMantEntity, Serializable>{


}