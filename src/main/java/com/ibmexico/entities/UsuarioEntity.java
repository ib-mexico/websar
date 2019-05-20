package com.ibmexico.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.UsuarioRolEntity;


@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

	// FIELDS
	@Id
	@Column(name = "id_usuario")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idUsuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal", nullable = true)
	private SucursalEntity sucursal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_grupo", nullable = true)
	private UsuarioGrupoEntity usuarioGrupo;

	@Column(unique = true, nullable = false, length = 100)
	private String correo;
	
	@Column(unique = true, nullable = false, length = 100)
	private String username;

	@Column(nullable = true, length = 60)
	private String password;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 100)
	private String apellidoPaterno;

	@Column(length = 100)
	private String apellidoMaterno;
	
	@Formula(value = " concat(nombre, ' ', apellido_paterno, ' ', apellido_materno) ")
	private String nombreCompleto;
	
	@Column(unique = true, nullable = false, length = 25)
	private String clave;
	
	@Column(length = 25)
	private String puesto;
	
	@Column(length = 100)
	private String direccion;
	
	@Column(length = 25)
	private String telefono;

	@Column(length = 25)
	private String celular;
	
	@Lob
	@Column
	private String imagen;
	
	@Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal quota;

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
	
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<UsuarioRolEntity> usuarioRoles = new ArrayList<UsuarioRolEntity>();
	
	
	
	
	// ACCESORS METHODS
	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	public SucursalEntity getSucursal() {
		return sucursal;
	}

	public void setSucursal(SucursalEntity sucursal) {
		this.sucursal = sucursal;
	}
	
	public UsuarioGrupoEntity getUsuarioGrupo() {
		
		UsuarioGrupoEntity objGrupo = null;
		
		if(usuarioGrupo != null) {
			objGrupo = usuarioGrupo;
		}
		return objGrupo;
	}

	public void setUsuarioGrupo(UsuarioGrupoEntity usuarioGrupo) {
		this.usuarioGrupo = usuarioGrupo;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		
		if(correo == null || correo.trim() == "") {
			throw new IllegalArgumentException("Correo no válido");
		}

		this.correo = correo;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		
		if(username == null || username.trim() == "") {
			throw new IllegalArgumentException("Usuario no válido");
		}

		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if(password == null || password.trim() == "") {
			throw new ApplicationException(EnumException.ENTITY_PASSWORD_001);
		} else if(password.length() < 8 || password.length() > 60) {
			throw new ApplicationException(EnumException.ENTITY_PASSWORD_001);
		} else {
			BCryptPasswordEncoder cryptPassword = new BCryptPasswordEncoder();
			this.password = cryptPassword.encode(password);
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		if(nombre == null || nombre.trim() == "") {
			throw new IllegalArgumentException("El campo Nombre es obligatorio");
		} else {
			this.nombre = nombre;
		}
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		if(apellidoPaterno == null || nombre.trim() == "") {
			throw new IllegalArgumentException("El campo Apellido Paterno es obligatorio");
		} else {
			this.apellidoPaterno = apellidoPaterno;
		}
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}
	
	public String getAlias() {
		
		String objReturn = this.correo;
		
		if(	nombreCompleto != null
			&& !nombreCompleto.trim().isEmpty()) {
			objReturn = nombre + " " + apellidoPaterno;
		}
		
		return objReturn;
	}
	
	public String getAliasCorreo() {
		
		String objReturn = this.correo;
		
		if(objReturn != null || objReturn != "") {
			
			String[] arrCorreo = objReturn.split("@");
			objReturn = arrCorreo[0];
		}
		
		return objReturn;
	}
	
	public String getClave() {
		return clave;
	}
	
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getPuesto() {
		return puesto;
	}
	
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
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

	public List<UsuarioRolEntity> getUsuarioRoles() {
		return usuarioRoles;
	}

	public void setUsuarioRoles(List<UsuarioRolEntity> usuarioRoles) {
		this.usuarioRoles = usuarioRoles;
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
	
	public BigDecimal getQuota() {
		return quota;
	}
	
	public String getQuotaNatural() {
		String valor = "";
		
		if(quota != null) {		
			valor = "$" + GeneralConfiguration.getInstance().getNumberFormat().format(quota);
		}
		return valor;
	}

	public void setQuota(BigDecimal quota) {
		this.quota = quota;
	}

	@Override
	public String toString() {
		return "UsuarioEntity [idUsuario=" + idUsuario + ", sucursal=" + sucursal.getSucursal() + ", correo=" + correo + ", password=" + password + ", nombre="
				+ nombre + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno
				+ ", nombreCompleto=" + nombreCompleto + ", clave=" + clave + ", puesto=" + puesto + ", direccion="
				+ direccion + ", telefono=" + telefono + ", celular=" + celular + ", creacionUsuario=" + creacionUsuario.getIdUsuario()
				+ ", creacionFecha=" + creacionFecha + ", modificacionUsuario=" + modificacionUsuario.getIdUsuario()
				+ ", modificacionFecha=" + modificacionFecha + ", eliminado=" + eliminado + ", usuarioRoles=" + usuarioRoles.size() + "]";
	}
	
	
	
}
