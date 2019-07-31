package com.ibmexico.entities;

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

import org.hibernate.annotations.Formula;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="resguardos")
public class ResguardoEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idResguardo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_resguardo_tipo", nullable = true)
	private ResguardoTipoEntity resguardoTipo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = true)
	private EmpresaEntity empresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_entrega", nullable = false)
	private UsuarioEntity usuarioEntrega;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_recibe", nullable = false)
	private UsuarioEntity usuarioRecibe;
	
	@Formula(value = "(select concat('RES-',e.clave,'-',lpad(id_resguardo, 5, '0')) from empresas e where e.id_empresa = id_empresa)")
	private String folio;
	
	@Lob
	@Column(nullable = true)
	private String urlFirmaEntrega;
	
	@Lob
	@Column(nullable = true)
	private String urlFirmaRecibe;
	
	@Column(nullable = true)
	private String observaciones;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@Column(nullable = false)
	private LocalDateTime creacionFecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;

	@Column(nullable = true)
	private LocalDateTime modificacionFecha;

	@Column
	private boolean eliminado = false;

	
	
	
	//ACCESORS METHODS
	public int getIdResguardo() {
		return idResguardo;
	}
	
	public void setIdResguardo(int idResguardo) {
		this.idResguardo = idResguardo;
	}
	
	public ResguardoTipoEntity getResguardoTipo() {
		return resguardoTipo;
	}
	
	public void setResguardoTipo(ResguardoTipoEntity resguardoTipo) {
		this.resguardoTipo = resguardoTipo;
	}
	
	public UsuarioEntity getUsuarioEntrega() {
		return usuarioEntrega;
	}
	
	public void setUsuarioEntrega(UsuarioEntity usuarioEntrega) {
		this.usuarioEntrega = usuarioEntrega;
	}
	
	public UsuarioEntity getUsuarioRecibe() {
		return usuarioRecibe;
	}
	
	public void setUsuarioRecibe(UsuarioEntity usuarioRecibe) {
		this.usuarioRecibe = usuarioRecibe;
	}
	
	public EmpresaEntity getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaEntity empresa) {
		this.empresa = empresa;
	}
	
	public String getFolio() {
		return folio;
	}
	
	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public String getUrlFirmaEntrega() {
		return urlFirmaEntrega;
	}
	
	public void setUrlFirmaEntrega(String urlFirmaEntrega) {
		this.urlFirmaEntrega = urlFirmaEntrega;
	}
	
	public String getUrlFirmaRecibe() {
		String valor = "";
		if(urlFirmaRecibe != null) {
			valor = urlFirmaRecibe;
		}
		return valor;
	}
	
	public void setUrlFirmaRecibe(String urlFirmaRecibe) {
		this.urlFirmaRecibe = urlFirmaRecibe;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		if(observaciones.equals("")) {
			this.observaciones = null;
		} else {			
			this.observaciones = observaciones;
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
	
	public boolean isEliminado() {
		return eliminado;
	}
	
	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
	
	
	
}
