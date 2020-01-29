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

@Entity
@Table(name="oportunidades_negocios_colaborador")
public class OpnNegocioColaboradorEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idOportunidadNegocioColaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oportunidad_negocio", nullable = true)
    private OportunidadNegocioEntity opnNegocio;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_colaborador", nullable = true)
	private UsuarioEntity usuarioColaborador;

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
    private boolean eliminado = false;

    public int getIdOportunidadNegocioColaborador() {
        return idOportunidadNegocioColaborador;
    }

    public void setIdOportunidadNegocioColaborador(int idOportunidadNegocioColaborador) {
        this.idOportunidadNegocioColaborador = idOportunidadNegocioColaborador;
    }

    public OportunidadNegocioEntity getOpnNegocio() {
        return opnNegocio;
    }

    public void setOpnNegocio(OportunidadNegocioEntity opnNegocio) {
        this.opnNegocio = opnNegocio;
    }

    public UsuarioEntity getUsuarioColaborador() {
        return usuarioColaborador;
    }

    public void setUsuarioColaborador(UsuarioEntity usuarioColaborador) {
        this.usuarioColaborador = usuarioColaborador;
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
    
    
}