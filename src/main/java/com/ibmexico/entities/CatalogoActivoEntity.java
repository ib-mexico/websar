package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name = "catalogoActivo")
public class CatalogoActivoEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idCatalogoActivo;
	
	@Column(length = 200, nullable = false)
	private String nombre;
	
	@Column(length = 250, nullable = false)
	private String descripcion;
	
	@Column
	private boolean eliminado = false;
	
	@Column(nullable = false)
	private LocalDateTime fechaRegistro;


	@Column(nullable=false)
	private LocalDateTime fechaModificacion;
	

	
	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}


	

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public int getIdCatalogoActivo() {
		return idCatalogoActivo;
	}

	public void setIdCatalogoActivo(int idCatalogoActivo) {
		this.idCatalogoActivo = idCatalogoActivo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public String getCreacionFechaNatural() {
		return fechaRegistro.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

	public String getModificacionFechaNatural() {
		return fechaModificacion.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}
	@Override
	public String toString() {
		return "CatalogoActivoEntity [idCatalogoActivo=" + idCatalogoActivo + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", fechaRegistro=" + fechaRegistro + "]";
	}



}
