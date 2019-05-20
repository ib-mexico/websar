package com.ibmexico.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tareas_participantes")
public class TareaParticipanteEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idTareaParticipante;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tarea", nullable = false)
	private TareaEntity tarea;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private UsuarioEntity usuario;

	
	//ACCESORS METHODS
	public int getIdTareaParticipante() {
		return idTareaParticipante;
	}

	public void setIdTareaParticipante(int idTareaParticipante) {
		this.idTareaParticipante = idTareaParticipante;
	}

	public TareaEntity getTarea() {
		return tarea;
	}

	public void setTarea(TareaEntity tarea) {
		this.tarea = tarea;
	}

	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}
	
	
}
