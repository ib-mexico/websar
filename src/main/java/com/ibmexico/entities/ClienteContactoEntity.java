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
@Table(name="clientes_contactos")
public class ClienteContactoEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idClienteContacto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", nullable = true)
	private ClienteEntity cliente;
	
	@Column(length = 200)
	private String contacto;
	
	@Column(length = 200)
	private String puesto;
	
	@Column(length = 100)
	private String correo;
	
	@Column(nullable = true, length = 25)
	private String telefono;
	
	@Column(nullable = true, length = 25)
	private String celular;
	
	@Column
	private boolean administrador = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@Column(nullable = false)
	private LocalDateTime creacionFecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;

	@Column(nullable = false)
	private LocalDateTime modificacionFecha;

	@Column
	private boolean eliminado = false;
	
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "id_puesto", referencedColumnName = "idPuesto" ,nullable = true)
	// private PuestoEntity puestos;
	
	// @OneToOne(fetch = FetchType.LAZY, optional = true)
    // @JoinColumn(name = "id_puesto", nullable = true)
	// private PuestoEntity puestos;
	
	// @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "id_puesto", referencedColumnName = "id_puesto")
	// private PuestoEntity puestos;
	@ManyToOne
    @JoinColumn(name="id_puesto", nullable = true)
    private PuestoEntity puestoContacto;


	//ACCESORS METHODS
	public int getIdClienteContacto() {
		return idClienteContacto;
	}

	public void setIdClienteContacto(int idClienteContacto) {
		this.idClienteContacto = idClienteContacto;
	}

	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getPuesto() {
		return puesto;
	}

	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
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

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	public PuestoEntity getPuestoContacto() {
		return puestoContacto;
	}

	public void setPuestoContacto(PuestoEntity puestoContacto) {
		this.puestoContacto = puestoContacto;
	}

	@Override
	public String toString() {
		return "ClienteContactoEntity [administrador=" + administrador + ", celular=" + celular + ", cliente=" + cliente
				+ ", contacto=" + contacto + ", correo=" + correo + ", creacionFecha=" + creacionFecha
				+ ", creacionUsuario=" + creacionUsuario + ", eliminado=" + eliminado + ", idClienteContacto="
				+ idClienteContacto + ", modificacionFecha=" + modificacionFecha + ", modificacionUsuario="
				+ modificacionUsuario + ", puesto=" + puesto + ", puestoContacto=" + puestoContacto + ", telefono="
				+ telefono + "]";
	}


	
	
}
