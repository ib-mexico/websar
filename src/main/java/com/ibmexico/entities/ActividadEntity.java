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
@Table(name="actividades")
public class ActividadEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idActividad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private UsuarioEntity usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_actividad_tipo", nullable = true)
	private ActividadTipoEntity actividadTipo;
	
	@Column
	private String resumen;
	
	@Column
	private String color;
	
	@Column(nullable = true)
	private LocalDate vencimientoFecha;	
	
	@Lob
	@Column
	private String notas;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_oportunidad_negocio", nullable = true)
	private OportunidadNegocioEntity oportunidad;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion", nullable = true)
	private CotizacionEntity cotizacion;
	
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
	
	@Column
	private boolean finalizado = false;

	
	
	//ACCESORS METHODS
	public int getIdActividad() {
		return idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}

	public ActividadTipoEntity getActividadTipo() {
		return actividadTipo;
	}

	public void setActividadTipo(ActividadTipoEntity actividadTipo) {
		this.actividadTipo = actividadTipo;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public LocalDate getVencimientoFecha() {
		return vencimientoFecha;
	}
	
	public String getVencimientoFechaNatural() {
		return vencimientoFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

	public void setVencimientoFecha(LocalDate vencimientoFecha) {
		this.vencimientoFecha = vencimientoFecha;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public OportunidadNegocioEntity getOportunidad() {
		return oportunidad;
	}

	public void setOportunidad(OportunidadNegocioEntity oportunidad) {
		this.oportunidad = oportunidad;
	}

	public CotizacionEntity getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(CotizacionEntity cotizacion) {
		this.cotizacion = cotizacion;
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

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	
	

	
}
