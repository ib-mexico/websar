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
@Table(name="bienActivo")
public class BienActivoEntity{

    // Campos generales
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idRecursoActivo;

    @Column(nullable = true)
    private String numeroEconomico;

    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = true)
    private String marca;

    @Column(nullable = true)
    private String modelo;

    @Column(nullable = true)
    private String serie;

    @Column(nullable = true)
    private String color;

    @Column(nullable = true)
    private LocalDate fechaCompra;

    //definir garantia(meses)  y obsolencia
    @Column(nullable = true)
    private Integer garantiaMes;
    @Column(nullable = true)
    private LocalDate obsolecensia;

    @Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
    private BigDecimal costo;


    @Lob
    @Column(nullable = true)
    private String observaciones;

    //definir un chekbox donde pregunte requiere mantenimiento? periodo de mantenimiento estimado
    @Column(nullable = true)
    private boolean requiereMantenimiento=false;
    @Column(nullable = true)
    private LocalDate fechaDeMantenimiento;
    @Column(nullable = true)
    private Integer periodoMantEstimado;


    //Relacion con la entidad departamento, definir en que depa esta asignado
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_departamento", nullable = true)
    private DepartamentoEntity idDepartamento;
    //Relacion con la entidad usuario , definir usuario a resguardo
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
    private UsuarioEntity usuario;
    //Relacion con la entidad empresa
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = true)
    private EmpresaEntity empresa;
    //Relacion con el catalogo de activos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_activo", nullable = false)
    private CatalogoActivoEntity idActivo;
     //Relacion con la entidad Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_proveedor", nullable = true)
    private ProveedorEntity proveedor;

    //campos uso general
    @Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
    private BigDecimal costoPromedioMantenimiento;
    @Column(nullable = true)
    private LocalDate fechaUltimoMantenimiento;

    //Campos VHC
    @Column(nullable = true)
    private String placa;

    @Column(nullable = true)
    private String tipoVehiculo;

    //Campos HUR

    //Campos EAC
    @Column(nullable = true)
    private String serieEvaporadora;
    @Column(nullable = true)
    private String serieCondensadora;
    @Column
    private boolean boolControlRemoto=false;


    //Campos HME
    // @Column(nullable = true)
    // private LocalDate fechaUltimaCalibracion;
    // @Column(nullable = true)
    // private Double costoEstimadoCalibracion;


    //CAMPOS HTC
    //fecha ultimo mantenimiento al parecer hay duplicidad
    @Column(nullable = true)
    private LocalDate fechaEntregaMovil;
    @Column(nullable = true)
    private Integer imei;
    @Column(nullable = true)
    private int almacenamientoExterna;


    //Campos ECP

    @Column(nullable = true)
    private String tipoEquipo;
    @Column(nullable = true)
    private Integer capacidadMemoriaRam;
    @Column(nullable = true)
    private String tipoMemoriaRam;
    @Column(nullable = true)
    private Integer capacidadProcesador;
    @Column(nullable = true)
    private String tipoProcesador;
    @Column(nullable = true)
    private String marcaProcesador;
    @Column(nullable = true)
    private Integer capacidadInternoHdd;
    @Column(nullable = true)
    private String tipoHdd;

    //check if CUENTAMONITOR is true generate next atributes
    @Column
    private boolean cuentaMonitor;
    @Column(nullable = true)
    private Integer tamanio;
    @Column(nullable = true)
    private String colorMonitor;
    @Column(nullable = true)
    private String modeloMonitor;
    @Column(nullable = true)
    private String numeroParte;

    //define el estatus del recurso
    @Column
    private boolean estatus=true;
    
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public DepartamentoEntity getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(DepartamentoEntity idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public String getNumeroEconomico() {
        return numeroEconomico;
    }

    public void setNumeroEconomico(String numeroEconomico) {
        this.numeroEconomico = numeroEconomico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }
    
    public String getFechaCompraNatural(){
        String fecha="";
        if(fechaCompra!=null){
            fecha=fechaCompra.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
        }
        return fecha;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Integer getGarantiaMes() {
        return garantiaMes;
    }

    public void setGarantiaMes(Integer garantiaMes) {
        this.garantiaMes = garantiaMes;
    }

    public LocalDate getObsolecensia() {
        return obsolecensia;
    }
    public String getObsolecensiaFechaNatural(){
        String fecha="";
        if(obsolecensia!=null){
            fecha=obsolecensia.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
        }
        return fecha;
    }

    public void setObsolecensia(LocalDate obsolecensia) {
        this.obsolecensia = obsolecensia;
    }

    

    public boolean isRequiereMantenimiento() {
        return requiereMantenimiento;
    }

    public void setRequiereMantenimiento(boolean requiereMantenimiento) {
        this.requiereMantenimiento = requiereMantenimiento;
    }

    public Integer getPeriodoMantEstimado() {
        return periodoMantEstimado;
    }

    public void setPeriodoMantEstimado(Integer periodoMantEstimado) {
        this.periodoMantEstimado = periodoMantEstimado;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }

    public BigDecimal getCostoPromedioMantenimiento() {
        return costoPromedioMantenimiento;
    }

    public void setCostoPromedioMantenimiento(BigDecimal costoPromedioMantenimiento) {
        this.costoPromedioMantenimiento = costoPromedioMantenimiento;
    }

    public LocalDate getFechaUltimoMantenimiento() {
        return fechaUltimoMantenimiento;
    }

    public String getFechaUltimoMantenimientoNatural(){
        String fecha="";
        if(fechaUltimoMantenimiento!=null){
            fecha=fechaUltimoMantenimiento.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
        }
        return fecha;
    }

    public void setFechaUltimoMantenimiento(LocalDate fechaUltimoMantenimiento) {
        this.fechaUltimoMantenimiento = fechaUltimoMantenimiento;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getSerieEvaporadora() {
        return serieEvaporadora;
    }

    public void setSerieEvaporadora(String serieEvaporadora) {
        this.serieEvaporadora = serieEvaporadora;
    }

    public String getSerieCondensadora() {
        return serieCondensadora;
    }

    public void setSerieCondensadora(String serieCondensadora) {
        this.serieCondensadora = serieCondensadora;
    }

    public boolean isBoolControlRemoto() {
        return boolControlRemoto;
    }

    public void setBoolControlRemoto(boolean boolControlRemoto) {
        this.boolControlRemoto = boolControlRemoto;
    }

    public LocalDate getFechaEntregaMovil() {
        return fechaEntregaMovil;
    }

    public String getFechaEntregaMovilNatural(){
        String fecha="";
        if(fechaEntregaMovil!=null){
            fecha=fechaEntregaMovil.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
        }
        return fecha;
    }

    public void setFechaEntregaMovil(LocalDate fechaEntregaMovil) {
        this.fechaEntregaMovil = fechaEntregaMovil;
    }

    public Integer getImei() {
        return imei;
    }

    public void setImei(Integer imei) {
        this.imei = imei;
    }



    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public Integer getCapacidadMemoriaRam() {
        return capacidadMemoriaRam;
    }

    public void setCapacidadMemoriaRam(Integer capacidadMemoriaRam) {
        this.capacidadMemoriaRam = capacidadMemoriaRam;
    }

    public String getTipoMemoriaRam() {
        return tipoMemoriaRam;
    }

    public void setTipoMemoriaRam(String tipoMemoriaRam) {
        this.tipoMemoriaRam = tipoMemoriaRam;
    }


    public String getTipoProcesador() {
        return tipoProcesador;
    }

    public void setTipoProcesador(String tipoProcesador) {
        this.tipoProcesador = tipoProcesador;
    }

    public String getMarcaProcesador() {
        return marcaProcesador;
    }

    public void setMarcaProcesador(String marcaProcesador) {
        this.marcaProcesador = marcaProcesador;
    }

    public Integer getCapacidadInternoHdd() {
        return capacidadInternoHdd;
    }

    public void setCapacidadInternoHdd(Integer capacidadInternoHdd) {
        this.capacidadInternoHdd = capacidadInternoHdd;
    }

    public String getTipoHdd() {
        return tipoHdd;
    }

    public void setTipoHdd(String tipoHdd) {
        this.tipoHdd = tipoHdd;
    }

    public boolean isCuentaMonitor() {
        return cuentaMonitor;
    }

    public void setCuentaMonitor(boolean cuentaMonitor) {
        this.cuentaMonitor = cuentaMonitor;
    }


    public String getColorMonitor() {
        return colorMonitor;
    }

    public void setColorMonitor(String colorMonitor) {
        this.colorMonitor = colorMonitor;
    }

    public String getModeloMonitor() {
        return modeloMonitor;
    }

    public void setModeloMonitor(String modeloMonitor) {
        this.modeloMonitor = modeloMonitor;
    }

    public String getNumeroParte() {
        return numeroParte;
    }

    public void setNumeroParte(String numeroParte) {
        this.numeroParte = numeroParte;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public int getIdRecursoActivo() {
        return idRecursoActivo;
    }

    public void setIdRecursoActivo(int idRecursoActivo) {
        this.idRecursoActivo = idRecursoActivo;
    }

    public int getAlmacenamientoExterna() {
        return almacenamientoExterna;
    }

    public void setAlmacenamientoExterna(int almacenamientoExterna) {
        this.almacenamientoExterna = almacenamientoExterna;
    }

    public Integer getCapacidadProcesador() {
        return capacidadProcesador;
    }

    public void setCapacidadProcesador(Integer capacidadProcesador) {
        this.capacidadProcesador = capacidadProcesador;
    }

    public Integer getTamanio() {
        return tamanio;
    }

    public void setTamanio(Integer tamanio) {
        this.tamanio = tamanio;
    }

    public LocalDate getFechaDeMantenimiento() {
        return fechaDeMantenimiento;
    }

    public String getFechaDeMantenimientoNatural(){
        String fecha="";
        if(fechaDeMantenimiento!=null){
            fecha=fechaDeMantenimiento.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
        }
        return fecha;
    }

    public void setFechaDeMantenimiento(LocalDate fechaDeMantenimiento) {
        this.fechaDeMantenimiento = fechaDeMantenimiento;
    }

}