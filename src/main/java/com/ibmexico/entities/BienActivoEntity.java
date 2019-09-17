package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="bienActivo")
public class BienActivoEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idActivoMobiliario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = true)
    private String marca;

    @Column(nullable = true)
    private String modelo;

    @Column(nullable = true)
    private String serie;

	@Lob
	@Column
	private String observaciones;

    @Column(nullable = true)
    private String color;

    @Column
    private boolean estatus=true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_activo", nullable = false)
    private CatalogoActivoEntity idActivo;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = true)
    private EmpresaEntity empresa;
    



    
    public int getIdActivoMobiliario() {
        return idActivoMobiliario;
    }

    public void setIdActivoMobiliario(int idActivoMobiliario) {
        this.idActivoMobiliario = idActivoMobiliario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public CatalogoActivoEntity getIdActivo() {
        return idActivo;
    }

    public void setIdActivo(CatalogoActivoEntity idActivo) {
        this.idActivo = idActivo;
    }

    public LocalDateTime getCreacionFecha() {
        return creacionFecha;
    }

    // formateo de la fecha 
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

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EmpresaEntity getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaEntity empresa) {
        this.empresa = empresa;
    }



}