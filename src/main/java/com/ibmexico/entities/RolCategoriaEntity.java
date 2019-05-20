package com.ibmexico.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles_categorias")
public class RolCategoriaEntity {

	// FIELDS
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idRolCategoria;

	@Column(length = 100)
	private String rolCategoria;
	
	@Column(length = 100)
	private String icono;

	// ACCESORS METHODS
	public int getIdRolCategoria() {
		return idRolCategoria;
	}

	public void setIdRolCategoria(int idRolCategoria) {
		this.idRolCategoria = idRolCategoria;
	}

	public String getRolCategoria() {
		return rolCategoria;
	}

	public void setRolCategoria(String rolCategoria) {
		this.rolCategoria = rolCategoria;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	// METHODS
	@Override
	public String toString() {
		return "RolCategoriaEntity [idRolCategoria=" + idRolCategoria + ", rolCategoria=" + getRolCategoria() + ", icono=" + getIcono() + "]";
	}
}