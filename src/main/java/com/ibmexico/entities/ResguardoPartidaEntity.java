package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="resguardos_partidas")
public class ResguardoPartidaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idResguardoPartida;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_resguardo", nullable = false)
	private ResguardoEntity resguardo;
	
	@Column
	private int cantidad;
	
	@Column
	private String marca;
	
	@Column
	private String descripcion;
	
	@Column
	private String numeroSerie;
	
	@Column
	private String modelo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@Column(nullable = true)
	private LocalDateTime creacionFecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;

	@Column(nullable = true)
	private LocalDateTime modificacionFecha;

	
	
	
	
	//ACCESORS METHODS
	public int getIdResguardoPartida() {
		return idResguardoPartida;
	}

	public void setIdResguardoPartida(int idResguardoPartida) {
		this.idResguardoPartida = idResguardoPartida;
	}

	public ResguardoEntity getResguardo() {
		return resguardo;
	}

	public void setResguardo(ResguardoEntity resguardo) {
		this.resguardo = resguardo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		if(marca.equals("")) {
			this.marca = null;
		} else {			
			this.marca = marca;
		}
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}
	
	public void setModelo(String modelo) {
		if(modelo.equals("")) {
			this.modelo = null;
		} else {			
			this.modelo = modelo;
		}
	}

	public String getModelo() {
		return modelo;
	}

	public void setNumeroSerie(String numeroSerie) {
		if(numeroSerie.equals("")) {
			this.numeroSerie = null;
		} else {			
			this.numeroSerie = numeroSerie;
		}
	}

	public UsuarioEntity getCreacionUsuario() {
		return creacionUsuario;
	}

	public void setCreacionUsuario(UsuarioEntity creacionUsuario) {
		this.creacionUsuario = creacionUsuario;
	}

	public LocalDateTime getCreacionFecha() {
		return creacionFecha;
	}

	public String getCreacionFechaNatural() {
		return creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}
	
	public void setCreacionFecha(LocalDateTime creacionFecha) {
		this.creacionFecha = creacionFecha;
	}
	
	public UsuarioEntity getModificacionUsuario() {
		return modificacionUsuario;
	}
	
	public void setModificacionUsuario(UsuarioEntity modificacionUsuario) {
		this.modificacionUsuario = modificacionUsuario;
	}
	
	public LocalDateTime getModificacionFecha() {
		return modificacionFecha;
	}
	
	public void setModificacionFecha(LocalDateTime modificacionFecha) {
		this.modificacionFecha = modificacionFecha;
	}
}
