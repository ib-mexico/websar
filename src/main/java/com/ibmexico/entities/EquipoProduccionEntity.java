package com.ibmexico.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="equiposProduccion")
public class EquipoProduccionEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idEquipoProduccion;
	
	@Column(nullable = true)
	private LocalDate renovacionFecha;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", nullable = false)
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_equipo_marca", nullable = false)
	private EquipoMarcaEntity equipoMarca;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_equipo_produccion_estatus", nullable = false)
	private EquipoProduccionEstatusEntity equipoEstatus;
		
	@Column(nullable = true)
	private String modelo;
	
	@Column(nullable = true)
	private String numeroSerie;
	
	@Lob
	@Column
	private String observaciones;
	
	@Column
	private boolean boolVenta = false;
	
	@Column
	private boolean boolRenta = false;
	
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
	public int getIdEquipoProduccion() {
		return idEquipoProduccion;
	}

	public void setIdEquipoProduccion(int idEquipoProduccion) {
		this.idEquipoProduccion = idEquipoProduccion;
	}

	public LocalDate getRenovacionFecha() {
		return renovacionFecha;
	}
	
	public String getRenovacionFechaNatural() {
		return renovacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

	public void SetRenovacionFecha(LocalDate renovacionFecha) {
		this.renovacionFecha = renovacionFecha;
	}

	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}
	
	public EquipoMarcaEntity getEquipoMarca() {
		return equipoMarca;
	}

	public void setEquipoMarca(EquipoMarcaEntity equipoMarca) {
		this.equipoMarca = equipoMarca;
	}

	public EquipoProduccionEstatusEntity getEquipoEstatus() {
		return equipoEstatus;
	}

	public void setEquipoEstatus(EquipoProduccionEstatusEntity equipoEstatus) {
		this.equipoEstatus = equipoEstatus;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public boolean isBoolVenta() {
		return boolVenta;
	}

	public void setBoolVenta(boolean boolVenta) {
		this.boolVenta = boolVenta;
	}

	public boolean isBoolRenta() {
		return boolRenta;
	}

	public void setBoolRenta(boolean boolRenta) {
		this.boolRenta = boolRenta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	
	public String getModificacionFechaNatural() {
		return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

	public void setModificacionFecha(LocalDateTime modificacionFecha) {
		this.modificacionFecha = modificacionFecha;
	}
	
	
}
