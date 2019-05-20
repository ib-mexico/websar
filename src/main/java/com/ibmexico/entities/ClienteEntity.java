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
@Table(name="clientes")
public class ClienteEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idCliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal", nullable = true)
	private SucursalEntity sucursal;
	
	@Column(length = 200)
	private String cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente_giro",nullable = true)
	private ClienteGiroEntity clienteGiro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_grupo_empresarial", nullable = true)
	private ClienteGrupoEmpresarialEntity clienteGrupoEmpresarial;
	
	@Column(nullable = true, length = 25)
	private String telefono;
	
	@Column(length = 10)
	private String clienteSae;
	
	@Column(length = 200)
	private String razonSocial;
	
	@Column(length = 25)
	private String rfc;
	
	@Column(length = 250)
	private String direccion;
	
	@Column(length = 100)
	private String ciudad;
	
	@Column(length = 100)
	private String estado;
	
	@Column(nullable = true, length = 5)
	private String codigoPostal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_ejecutivo", nullable = true)
	private UsuarioEntity usuarioEjecutivo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_ejecutivo_s3s", nullable = true)
	private UsuarioEntity usuarioEjecutivoS3s;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_ejecutivo_r2a", nullable = true)
	private UsuarioEntity usuarioEjecutivoR2a;
	
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

	
	
	
	
	// ACCESORS METHODS
	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public SucursalEntity getSucursal() {
		return sucursal;
	}

	public void setSucursal(SucursalEntity sucursal) {
		this.sucursal = sucursal;
	}

	public String getCliente() {
		return cliente.toUpperCase();
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public ClienteGiroEntity getClienteGiro() {
		return clienteGiro;
	}

	public void setClienteGiro(ClienteGiroEntity clienteGiro) {
		this.clienteGiro = clienteGiro;
	}
	
	public ClienteGrupoEmpresarialEntity getClienteGrupoEmpresarial() {
		
		ClienteGrupoEmpresarialEntity objGrupo = null;
		
		if(clienteGrupoEmpresarial != null) {
			objGrupo = clienteGrupoEmpresarial;
		}
		return objGrupo;
	}

	public void setClienteGrupoEmpresarial(ClienteGrupoEmpresarialEntity clienteGrupoEmpresarial) {
		this.clienteGrupoEmpresarial = clienteGrupoEmpresarial;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getClienteSae() {
		return clienteSae;
	}

	public void setClienteSae(String clienteSae) {
		this.clienteSae = clienteSae;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	
	public UsuarioEntity getUsuarioEjecutivo() {
		
		UsuarioEntity objUsuario = null; 
		
		if(usuarioEjecutivo != null) {
			objUsuario = usuarioEjecutivo;
		}
		return objUsuario;
	}

	public void setUsuarioEjecutivo(UsuarioEntity usuarioEjecutivo) {
		this.usuarioEjecutivo = usuarioEjecutivo;
	}
	
	public UsuarioEntity getUsuarioEjecutivoS3s() {
		
		UsuarioEntity objUsuario = null; 
		
		if(usuarioEjecutivoS3s != null) {
			objUsuario = usuarioEjecutivoS3s;
		}
		return objUsuario;
	}

	public void setUsuarioEjecutivoS3s(UsuarioEntity usuarioEjecutivoS3s) {
		this.usuarioEjecutivoS3s = usuarioEjecutivoS3s;
	}
	
	public UsuarioEntity getUsuarioEjecutivoR2a() {
		
		UsuarioEntity objUsuario = null; 
		
		if(usuarioEjecutivoR2a != null) {
			objUsuario = usuarioEjecutivoR2a;
		}
		return objUsuario;
	}

	public void setUsuarioEjecutivoR2a(UsuarioEntity usuarioEjecutivoR2a) {
		this.usuarioEjecutivoR2a = usuarioEjecutivoR2a;
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

	@Override
	public String toString() {
		return "ClienteEntity [idCliente=" + idCliente + ", sucursal=" + sucursal.getSucursal() + ", cliente=" + cliente
				+ ", clienteGiro=" + clienteGiro.getClienteGiro() + ", telefono=" + telefono + ", razonSocial=" + razonSocial + ", rfc="
				+ rfc + ", ciudad=" + ciudad + ", estado=" + estado + ", direccion=" + direccion + ", codigoPostal=" + codigoPostal
				+ ", creacionUsuario=" + creacionUsuario.getIdUsuario() + ", creacionFecha=" + creacionFecha + ", modificacionUsuario="
				+ modificacionUsuario.getIdUsuario() + ", modificacionFecha=" + modificacionFecha + ", eliminado=" + eliminado + "]";
	}
	
	
	
}
