package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cotizaciones_clasificacion")
public class CotizacionClasificacionEntity {

    @Id
	@Column
	@GeneratedValue
	private int idCotizacionClasificacion;
	
	@Column(length = 50)
	private String cotizacionClasificacion;
	
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

	@Column(length = 10, nullable = true)
    private int orden;

    public int getIdCotizacionClasificacion() {
        return idCotizacionClasificacion;
    }

    public void setIdCotizacionClasificacion(int idCotizacionClasificacion) {
        this.idCotizacionClasificacion = idCotizacionClasificacion;
    }

    public String getCotizacionClasificacion() {
        return cotizacionClasificacion;
    }

    public void setCotizacionClasificacion(String cotizacionClasificacion) {
        this.cotizacionClasificacion = cotizacionClasificacion;
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

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
    
}