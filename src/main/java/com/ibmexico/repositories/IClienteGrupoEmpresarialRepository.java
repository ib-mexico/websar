package com.ibmexico.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.ClienteGrupoEmpresarialEntity;

@Repository("clienteGrupoEmpresarialRepository")
public interface IClienteGrupoEmpresarialRepository extends JpaRepository<ClienteGrupoEmpresarialEntity, Serializable> {

	public abstract ClienteGrupoEmpresarialEntity findByIdGrupoEmpresarial(int idGrupoEmpresarial);
}
