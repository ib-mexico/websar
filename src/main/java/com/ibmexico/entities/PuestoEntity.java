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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "puestos")
public class PuestoEntity{

    @Id
    @Column(name = "id_puesto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPuesto;

    @Column(nullable = true, length = 200)
    private String cargo;

    @Column(nullable =  true, length = 100)
    private String clave;

    @Column(nullable = true,length = 200)
    private String descripcion;

    @Column(nullable = false)
	private LocalDateTime creacionFecha;
    
	@Column(nullable = false)
	private LocalDateTime modificacionFecha;

	@Column
    private boolean eliminado = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "puestoContacto", cascade = CascadeType.ALL)
    private List<ClienteContactoEntity> clienteContacto = new ArrayList<ClienteContactoEntity>();


    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }


    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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

    
    public List<ClienteContactoEntity> getClienteContacto() {
        return clienteContacto;
    }

    public void setClienteContacto(List<ClienteContactoEntity> clienteContacto) {
        this.clienteContacto = clienteContacto;
    }

}