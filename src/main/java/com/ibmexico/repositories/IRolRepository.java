package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.RolEntity;

@Repository("rolRepository")
public interface IRolRepository extends JpaRepository<RolEntity, Serializable> {
	
	public abstract RolEntity findByIdRol(int idRol);
	public abstract RolEntity findByRol(String rol);
	
	public abstract List<RolEntity> findByIdRolIn(List<Integer> lstIdRol);

}
