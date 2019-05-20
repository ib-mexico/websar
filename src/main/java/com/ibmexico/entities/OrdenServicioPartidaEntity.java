package com.ibmexico.entities;

import java.math.BigDecimal;
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
@Table(name="ordenes_servicios_partidas")
public class OrdenServicioPartidaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idOrdenServicioPartida;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_orden_servicio", nullable = false)
	private OrdenServicioEntity ordenServicio;
	
	@Column
	private int cantidad;
	
	@Column
	private String descripcion;
	
	@Column
	private String numeroParte;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal precioUnitario;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal importe;
	
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
	public int getIdOrdenServicioPartida() {
		return idOrdenServicioPartida;
	}

	public void setIdOrdenServicioPartida(int idOrdenServicioPartida) {
		this.idOrdenServicioPartida = idOrdenServicioPartida;
	}

	public OrdenServicioEntity getOrdenServicio() {
		return ordenServicio;
	}

	public void setOrdenServicio(OrdenServicioEntity ordenServicio) {
		this.ordenServicio = ordenServicio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumeroParte() {
		return numeroParte;
	}

	public void setNumeroParte(String numeroParte) {
		this.numeroParte = numeroParte;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	
	public String getPrecioUnitarioNatural() {
		return "$" + GeneralConfiguration.getInstance().getNumberFormat().format(precioUnitario);
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getImporte() {
		return importe;
	}
	
	public String getImporteNatural() {
		return "$" + GeneralConfiguration.getInstance().getNumberFormat().format(importe);
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
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
