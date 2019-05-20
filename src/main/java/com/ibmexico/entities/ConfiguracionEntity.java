package com.ibmexico.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="configuraciones")
public class ConfiguracionEntity {
	
	@Id
	@Column(unique = true)
	private String variable;
	
	@Column(nullable = false)
	private String valor;
	
	@Column(nullable = false)
	private String tipo;
	

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}