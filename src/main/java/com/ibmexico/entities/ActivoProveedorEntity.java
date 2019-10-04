package com.ibmexico.entities;

import java.time.LocalDateTime;
import java.util.Set;

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

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name = "activo_proveedor")
public class ActivoProveedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idActivoProveedor;
    
    @Column
    private String proveedor;

    @Column
    private String razonSocial;
    
    @Column
    private String rfc;

    @Column
    private String ciudad;

    @Column
    private String direccion;

    @Column
    private int numeroTelefonico;

 
   @OneToMany(mappedBy = "idProveedor", cascade = CascadeType.ALL)
   Set<ActivoServicioProveedorEntity> idProveedor;

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

    public int getIdActivoProveedor() {
        return idActivoProveedor;
    }

    public void setIdActivoProveedor(int idActivoProveedor) {
        this.idActivoProveedor = idActivoProveedor;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getNumeroTelefonico() {
        return numeroTelefonico;
    }

    public void setNumeroTelefonico(int numeroTelefonico) {
        this.numeroTelefonico = numeroTelefonico;
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

    public LocalDateTime getModificacionFecha() {
        return modificacionFecha;
    }

    public String getModificacionFechaNatural() {
		return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
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

    public Set<ActivoServicioProveedorEntity> getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Set<ActivoServicioProveedorEntity> idProveedor) {
        this.idProveedor = idProveedor;
    }




    
}