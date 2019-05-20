package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.EmpresaEntity;

@Repository("empresaRepository")
public interface IEmpresaRepository extends JpaRepository<EmpresaEntity, Serializable> {

	public abstract EmpresaEntity findByIdEmpresa(int idEmpresa);
	
	@Query("SELECT objEmpresa FROM EmpresaEntity objEmpresa")
	public abstract List<EmpresaEntity> listEmpresas();
}
