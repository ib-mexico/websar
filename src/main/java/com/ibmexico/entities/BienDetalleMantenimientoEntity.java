package com.ibmexico.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    
    @Column(nullable = false)
    private LocalDate fechaMantenimientoProgramada;

    @Column(nullable = false)
    private boolean finalizado;

    @Lob
    @Column(nullable = true)
    private String observaciones;
    
    @Column
    private boolean estatus_recordatorio;
    
    @Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
    private BigDecimal gastoAproximado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_activo_estatus", nullable = false)
    private ActivoEstatusEntity activoEstatus;
     
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_bien_activo",nullable = false)
    private BienActivoEntity bienActivo;

    //Relations
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
    private UsuarioEntity modificacionUsuario;
    
    @Column(nullable = true)
    private LocalDateTime creacionFecha;
    
    @Column(nullable = true)
    private LocalDateTime modificacionFecha;


    public int getIdDetalleMantenimiento() {
        return idDetalleMantenimiento;
    }

    public void setIdDetalleMantenimiento(int idDetalleMantenimiento) {
        this.idDetalleMantenimiento = idDetalleMantenimiento;
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


    public LocalDate getFechaMantenimientoProgramada() {
        return fechaMantenimientoProgramada;
    }

    // formateo de la fecha 
    public String getFechaMantenimientoProgramadaFechaNatural() {
        return fechaMantenimientoProgramada.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
    }
    
    public void setFechaMantenimientoProgramada(LocalDate fechaMantenimientoProgramada) {
        this.fechaMantenimientoProgramada = fechaMantenimientoProgramada;
    }

    public ActivoEstatusEntity getActivoEstatus() {
        return activoEstatus;
    }

    public void setActivoEstatus(ActivoEstatusEntity activoEstatus) {
        this.activoEstatus = activoEstatus;
    }

    public BienActivoEntity getBienActivo() {
        return bienActivo;
    }

    public void setBienActivo(BienActivoEntity bienActivo) {
        this.bienActivo = bienActivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getGastoAproximado() {
        return gastoAproximado;
    }

    public void setGastoAproximado(BigDecimal gastoAproximado) {
        this.gastoAproximado = gastoAproximado;
    }

}