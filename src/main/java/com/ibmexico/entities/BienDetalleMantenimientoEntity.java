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
@Table(name="bien_detalle_mantenimiento")
public class BienDetalleMantenimientoEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idDetalleMantenimiento;

    @Column(nullable = false, length = 5)
    private int cantidad;

    @Column(length = 150,nullable = false)
    private String titulo;
    
    @Lob
    @Column(nullable = false)
    private String diagnostico;

    @Lob
    @Column(nullable = false)
    private String falla;

    @Column(nullable = false)
    private LocalDateTime fechaMantenimiento;

    @Column(nullable = true)
    private LocalDateTime creacionFecha;
    
    @Column(nullable = true)
    private LocalDateTime modificacionFecha;

    @Column(nullable = false)
    private boolean finalizado;

    @Column
    private boolean estatus_recordatorio;

    //Relations
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
    private UsuarioEntity modificacionUsuario;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_bien_activo",nullable = false)
    private BienActivoEntity idBienActivo;

    public int getIdDetalleMantenimiento() {
        return idDetalleMantenimiento;
    }

    public void setIdDetalleMantenimiento(int idDetalleMantenimiento) {
        this.idDetalleMantenimiento = idDetalleMantenimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getFalla() {
        return falla;
    }

    public void setFalla(String falla) {
        this.falla = falla;
    }

    public LocalDateTime getFechaMantenimiento() {
        return fechaMantenimiento;
    }
    // formateo de la fecha 
    public String getFechaMantenimientoFechaNatural() {
        return fechaMantenimiento.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
    }
    
    public void setFechaMantenimiento(LocalDateTime fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
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
    // formateo de la fecha 
    public String getModificacionFechaNatural() {
         return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
    }
        
    public void setModificacionFecha(LocalDateTime modificacionFecha) {
        this.modificacionFecha = modificacionFecha;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public boolean isEstatus_recordatorio() {
        return estatus_recordatorio;
    }

    public void setEstatus_recordatorio(boolean estatus_recordatorio) {
        this.estatus_recordatorio = estatus_recordatorio;
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

    public BienActivoEntity getIdBienActivo() {
        return idBienActivo;
    }

    public void setIdBienActivo(BienActivoEntity idBienActivo) {
        this.idBienActivo = idBienActivo;
    }



    
}