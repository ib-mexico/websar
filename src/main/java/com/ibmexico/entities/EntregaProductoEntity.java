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
@Table(name="entregas_productos")
public class EntregaProductoEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idEntregaProducto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_entrega", nullable = false)
	private EntregaEntity entrega;
	
	@Column
	private int cantidad;
	
	@Column
	private String numeroParte;
	
	@Column
	private String descripcion;
	
	@Column
	private String numeroSerie;
	
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
	public int getIdEntregaProducto() {
		return idEntregaProducto;
	}

	public void setIdEntregaProducto(int idEntregaProducto) {
		this.idEntregaProducto = idEntregaProducto;
	}

	public EntregaEntity getEntrega() {
		return entrega;
	}

	public void setEntrega(EntregaEntity entrega) {
		this.entrega = entrega;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getNumeroParte() {
		return numeroParte;
	}

	public void setNumeroParte(String numeroParte) {
		this.numeroParte = numeroParte;
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

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
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
