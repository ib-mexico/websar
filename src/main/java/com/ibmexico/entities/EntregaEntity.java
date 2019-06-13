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

import org.hibernate.annotations.Formula;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="entregas")
public class EntregaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idEntrega;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = true)
	private EmpresaEntity empresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_entrega", nullable = false)
	private UsuarioEntity usuarioEntrega;
	
	@Column(nullable = true)
	private String personaRecibe;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", nullable = false)
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente_contacto", nullable = false)
	private ClienteContactoEntity clienteContacto;
	
	@Formula(value = "(select concat('ENT-',e.clave,'-',u.clave,'-', lpad(id_entrega, 7, '0')) from usuarios u, empresas e where u.id_usuario = id_usuario_entrega AND e.id_empresa = id_empresa)")
	private String folio;
	
	@Column(nullable = true)
	private String horaEntrada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion", nullable = true)
	private CotizacionEntity cotizacion;
	
	@Column(nullable = true)
	private String ordenCompra;
	
	@Lob
	@Column(nullable = true)
	private String urlFirmaEntrega;
	
	@Lob
	@Column(nullable = true)
	private String urlFirmaRecibe;
	
	@Column(nullable = true)
	private String observaciones;
	
	@Column
	private boolean prestamo = false;
	
	@Column(nullable = true)
	private LocalDate regresoFecha;
	
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
	public int getIdEntrega() {
		return idEntrega;
	}
	
	public void setIdEntrega(int idEntrega) {
		this.idEntrega = idEntrega;
	}
	
	public UsuarioEntity getUsuarioEntrega() {
		return usuarioEntrega;
	}
	
	public EmpresaEntity getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaEntity empresa) {
		this.empresa = empresa;
	}
	
	public void setUsuarioEntrega(UsuarioEntity usuarioEntrega) {
		this.usuarioEntrega = usuarioEntrega;
	}
	
	public String getPersonaRecibe() {
		
		String persona = "";
		
		if(personaRecibe != null) {
			persona = personaRecibe;
		}
		
		return persona;
	}
	
	public void setPersonaRecibe(String personaRecibe) {
		this.personaRecibe = personaRecibe;
	}
	
	public ClienteEntity getCliente() {
		return cliente;
	}
	
	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}
	
	public ClienteContactoEntity getClienteContacto() {
		return clienteContacto;
	}
	
	public void setClienteContacto(ClienteContactoEntity clienteContacto) {
		this.clienteContacto = clienteContacto;
	}		
	
	public String getFolio() {
		return folio;
	}
	
	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public String getHoraEntrada() {
		
		String hora = "";
		
		if(personaRecibe != null) {
			hora = horaEntrada;
		}
		
		return hora;
	}
	
	public void setHoraEntrada(String horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	
	public CotizacionEntity getCotizacion() {
		
		CotizacionEntity objCotizacion = null;
		
		if(cotizacion != null) {
			objCotizacion = cotizacion;
		}
		
		return objCotizacion;
	}
	
	public void setCotizacion(CotizacionEntity cotizacion) {
		this.cotizacion = cotizacion;
	}
	
	public String getOrdenCompra() {
		return ordenCompra;
	}
	
	public void setOrdenCompra(String ordenCompra) {
		this.ordenCompra = ordenCompra;
	}
	
	public String getUrlFirmaEntrega() {
		return urlFirmaEntrega;
	}
	
	public void setUrlFirmaEntrega(String urlFirmaEntrega) {
		this.urlFirmaEntrega = urlFirmaEntrega;
	}
	
	public String getUrlFirmaRecibe() {
		return urlFirmaRecibe;
	}
	
	public void setUrlFirmaRecibe(String urlFirmaRecibe) {
		this.urlFirmaRecibe = urlFirmaRecibe;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public boolean isPrestamo() {
		return prestamo;
	}
	
	public void setPrestamo(boolean prestamo) {
		this.prestamo = prestamo;
	}
	
	public LocalDate getRegresoFecha() {
		return regresoFecha;
	}
	
	public String getRegresoFechaNatural() {
		String fecha = "";
		
		if(regresoFecha != null) {			
			fecha = regresoFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;
	}
	
	public void setRegresoFecha(LocalDate regresoFecha) {
		this.regresoFecha = regresoFecha;
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
