package com.ibmexico.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roles_tipos")
public class RolTipo {

	@Id
	@GeneratedValue
	private int idRolTipo;
	
	@Column(length=100, nullable =false)
	private String rolTipo;

	//ACCESORS METHODS
	public int getIdRolTipo() {
		return idRolTipo;
	}

	public void setIdRolTipo(int idRolTipo) {
		this.idRolTipo = idRolTipo;
	}

	public String getRolTipo() {
		return rolTipo;
	}

	public void setRolTipo(String rolTipo) {
		this.rolTipo = rolTipo;
	}

	//METHODS
	@Override
	public String toString() {
		return "RolTipo [idRolTipo=" + idRolTipo + ", rolTipo=" + rolTipo + "]";
	}
	
}
