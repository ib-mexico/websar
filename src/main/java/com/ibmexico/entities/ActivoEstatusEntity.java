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
@Table(name = "activo_estatus")
public class ActivoEstatusEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idActivoEstatus;

    @Column(length = 50)
    private String activoEstatus;
    
    @Column
    private boolean eliminado = false;
    
    //usuario y fecha cuando crean y modifican
    @Column(nullable = true)
    private LocalDateTime creacionFecha;
                
    @Column(nullable = true)
    private LocalDateTime modificacionFecha;
            
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creacion_id_usuario", nullable = false)
    private UsuarioEntity creacionUsuario;
            
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modificacion_id_usuario", nullable = false)
    private UsuarioEntity modificacionUsuario;

    public int getIdActivoEstatus() {
        return idActivoEstatus;
    }

    public void setIdActivoEstatus(int idActivoEstatus) {
        this.idActivoEstatus = idActivoEstatus;
    }


    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
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

    public String getModificacionFechaNatural() {
		return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

    public LocalDateTime getModificacionFecha() {
        return modificacionFecha;
    }

    public void setModificacionFecha(LocalDateTime modificacionFecha) {
        this.modificacionFecha = modificacionFecha;
    }

    public UsuarioEntity getCreacionUsuario() {
        return creacionUsuario;
    }

    public void setCreacionUsuario(UsuarioEntity creacionUsuario) {
        this.creacionUsuario = creacionUsuario;
    }

    public UsuarioEntity getModificacionUsuario() {
        return modificacionUsuario;
    }

    public void setModificacionUsuario(UsuarioEntity modificacionUsuario) {
        this.modificacionUsuario = modificacionUsuario;
    }

    public String getActivoEstatus() {
        return activoEstatus;
    }

    public void setActivoEstatus(String activoEstatus) {
        this.activoEstatus = activoEstatus;
    }

}