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
@Table(name="oportunidades_negocios_estatus_clasificacion")
public class OportunidadNegocioEstatusClasificacionEntity {
    
	@Id
	@Column
	@GeneratedValue
	private int idOpnNegocioEstatusClasificacion;
	
	@Column(length = 50)
	private String nombre;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;
    
	@Column(nullable = false)
	private LocalDateTime creacionFecha;
    
	@Column(nullable = false)
	private LocalDateTime modificacionFecha;
    
	@Column
    private boolean eliminado = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_oportunidad_negocio_estatus", nullable = true)
    private OportunidadNegocioEstatusEntity oportunidadNegocioEstatus;
    
	@Column(length = 10, nullable = true)
	private int orden;
    
    public int getIdOpnNegocioEstatusClasificacion() {
        return idOpnNegocioEstatusClasificacion;
    }

    public void setIdOpnNegocioEstatusClasificacion(int idOpnNegocioEstatusClasificacion) {
        this.idOpnNegocioEstatusClasificacion = idOpnNegocioEstatusClasificacion;
    }
    
    public UsuarioEntity getModificacionUsuario() {
        return modificacionUsuario;
    }

    public void setModificacionUsuario(UsuarioEntity modificacionUsuario) {
        this.modificacionUsuario = modificacionUsuario;
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

    public OportunidadNegocioEstatusEntity getOportunidadNegocioEstatus() {
        return oportunidadNegocioEstatus;
    }

    public void setIdOportunidadNegocioEstatus(OportunidadNegocioEstatusEntity oportunidadNegocioEstatus) {
        this.oportunidadNegocioEstatus = oportunidadNegocioEstatus;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}