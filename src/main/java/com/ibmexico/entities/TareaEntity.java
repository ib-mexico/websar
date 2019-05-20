package com.ibmexico.entities;

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

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="tareas")
public class TareaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idTarea;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private UsuarioEntity usuario;
	
	@Column
	private String tarea;
	
	@Lob
	@Column
	private String descripcion;
	
	@Column
	private String color;
	
	@Column
	private String lugar;
	
	@Column(nullable = true)
	private LocalDateTime inicioFecha;
	
	@Column(nullable = true)
	private LocalDateTime finFecha;
	
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
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "tarea", cascade = CascadeType.ALL)
	private List<TareaParticipanteEntity> tareasParticipantes = new ArrayList<TareaParticipanteEntity>();

	
	
	//ACCESORS METHODS
	public int getIdTarea() {
		return idTarea;
	}

	public void setIdTarea(int idTarea) {
		this.idTarea = idTarea;
	}

	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}

	public String getTarea() {
		return tarea;
	}

	public void setTarea(String tarea) {
		this.tarea = tarea;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDateTime getInicioFecha() {
		return inicioFecha;
	}
	
	public String getInicioFechaNatural() {
		return inicioFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}
	
	public String getInicioHoraNatural() {
		return inicioFecha.format(GeneralConfiguration.getInstance().getTimeFormatterNatural());
	}

	public void setInicioFecha(LocalDateTime inicioFecha) {
		this.inicioFecha = inicioFecha;
	}
	
	public LocalDateTime getFinFecha() {
		return finFecha;
	}
	
	public String getFinFechaNatural() {
		return finFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}
	
	public String getFinHoraNatural() {
		return finFecha.format(GeneralConfiguration.getInstance().getTimeFormatterNatural());
	}

	public void setFinFecha(LocalDateTime finFecha) {
		this.finFecha = finFecha;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
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

	public List<TareaParticipanteEntity> getTareasParticipantes() {
		return tareasParticipantes;
	}

	public void setTareasParticipantes(List<TareaParticipanteEntity> tareasParticipantes) {
		this.tareasParticipantes = tareasParticipantes;
	}
	
	

	
}
