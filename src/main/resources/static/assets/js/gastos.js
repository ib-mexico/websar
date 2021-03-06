var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');
Vue.component('gastos',{
    props:['cotizaciones'],
    template:`#templategastos`
})


if (document.getElementById("appGastos")) {
    var gasto = new Vue({
        el:"#appGastos",
        mounted() {
            this.getDataGastos();
            this.loadModal();
        },
        data:{
            title: 'Gasto relacionados a una o varias Cotizaciones',
            formInputs: [
            ],
            formCotizaciones: [],
            opcionGasto: '',
            estatus1: '',
            estatus2: '',

            lugares: [],
            empresa: [],
            proveedor: [],
            facturaNota: '',
            clasificacionPrincipalGasto: [],
            tipoGasto: [],
            clasificacionGasto: [],
            cotizacion: [],
            tipoFichero: [],

            usuario: [],
            nuevoPago:{
                id_empresa: '',
                id_proveedor: '',
                facturaNota: '',

                idPrincipalGasto:  '',
                id_tipoGasto: '',
                id_clasificacionGasto: '',

                id_cotizacion: '',
                id_usuario: '',
                checkCotizacion:false,
                id_cotizacion: '',
                txtCotizacion: '',
                observaciones: '',
                id_tipoFichero: '',
                depa: '',
                sucursal: '',
                subtotal: ''
            }
            ,editGastoData: [],
            editUsuario:{
                id_usuario: '',
                departamento: '',
                sucursal: '',
            },
        },

        methods: {

            addInput: function(e){
                e.preventDefault();
                if($("#modalGasto").is(':visible')){
                    var values=$('select[name=cmbCotizacion]').val();
                }else{
                    var values=$('.cmbCotizacion').selectpicker('val');
                }      
                for(var index = 0; index < values.length; index++) {
                    var item = values[index];
                    var arrFolio = item.split("-");
                    var numberFolio = parseInt(arrFolio[3]);

                    if(this.formInputs.length>=1){
                        if(this.formInputs.some(item=>item.id_cotizacion === numberFolio)){
                            console.log("no se puede duplicar");
                        }else{
                            this.formInputs.push({
                                name:           item,
                                id_cotizacion:  numberFolio,
                                subTotal:       ''
                            });
                        }
                    }
                    else{
                        this.formInputs.push({
                            name: item,
                            id_cotizacion:numberFolio,
                            subTotal:''
                        });
                    }
                }
              },
              
            removeInput: function(index){
                if(this.formInputs[index].idCotizacionFichero != undefined){
                    this.eliminarFichero(this.formInputs[index].idCotizacionFichero,this.formInputs[index].id_cotizacion, index);
                }else{
                    //console.log(`Removing index: ${index}`);
                    this.formInputs.splice(index,1);
                    //console.log(`New Array: ${JSON.stringify(this.formInputs)}`);
                }
            },
            eliminarFichero(idCotizacionFichero, idCotizacion, index){
                var url=host+'Gastos/'+idCotizacion+'/'+idCotizacionFichero+'/eliminarFichero';
				swal({
                    type: 'warning',
					title: 'Advertencia!',
					text: 'Este registro sera eliminado del sistema permanentemente. Si la cotización se encuentra Facturada o Pagada, afectará los cálculos de cuotas y comisiones.',
					showCancelButton: true,
					cancelButtonText: 'Cancelar',
                    confirmButtonText: 'Eliminar',
                    showLoaderOnConfirm: true,
				}).then((result)=>{
					if(result){
						axios.post(url).then(resp=>{
							if(resp.status==200 && resp.data.respuesta){
                                this.formInputs.splice(index, 1);
								swal(resp.data.titulo, resp.data.mensaje, "success");
							}else{
								// console.log(resp);
								swal(resp.data.titulo, resp.data.mensaje, "error");
							}
						}).catch(error=>{
							swal("Algo malo pasó!", error.resp, "error");
								// console.log(error);
						});
					}
				}).catch(swal.noop);
            },

            gastoTotal(){
            	// $('#members').data('max-options', count)
                $('.cmbCotizacion').selectpicker({
                    maxOptions:1
                });
                $('.cmbCotizacion').selectpicker('refresh');
            },
            gastoParcial(){
                $('.cmbCotizacion').selectpicker({
                    maxOptions:4
                });
                $('.cmbCotizacion').selectpicker('refresh');
            },

            loadModal(){
                $('.cmbEmpresa').selectpicker('refresh');
                $('.cmbProveedor').selectpicker('refresh');
                $('.cmbClasificacionPrincipalGasto').selectpicker('refresh');
                $('.cmbTipogasto').selectpicker('refresh');
                $('.cmbClasificacion').selectpicker('refresh');
                $('.cmbUsuario').selectpicker('refresh');
                $('.cmbCotizacion').selectpicker('refresh');
                $('.cmbTipoFichero').selectpicker('refresh');
            },

            validateForm(){
                var response=false;
                if(!(this.nuevoPago.id_empresa)) {
                    swal("Revisión!", "Seleccione alguna empresa en particular.", "warning");
                    return response;
				} 
                if(!(this.nuevoPago.id_proveedor)){
                    swal("Revisión!","Seleccione un proveedor, para poder continuar","warning");
                    return response;
                }
                if(!(this.nuevoPago.id_tipoFichero)){
                    swal("Revisión!","Seleccione un Tipo de Fichero, para continuar","warning");
                    return response;
                }
                if(!(this.nuevoPago.idPrincipalGasto)){
                    swal("Revisión!","Seleccione una clasificacion de gasto, para poder continuar","warning");
                    return response;
                }
                if(!(this.nuevoPago.id_tipoGasto)){
                    swal("Revisión!","Seleccione un uso de gasto, para poder continuar","warning");
                    return response;
                }
                if(!(this.nuevoPago.id_clasificacionGasto)){
                    swal("Revisión!","Seleccione el tipo de gasto, para poder continuar","warning");
                    return response;
                }
                if(!this.nuevoPago.facturaNota){
                    swal("Revisión!", "Completa el campo factura-Nota", "warning");
                    return response;
                }
                if(!this.nuevoPago.observaciones){
                    swal("Revisión!", "Rellena el campo de observaciones", "warning");
					return response;
                }
                if(!this.nuevoPago.subtotal){
                    swal("Revisión!", "Completa el campo Subtotal del gasto","warning");
                    return response;
                }

                var fileInput= document.getElementById('idFicheroFactura').files.length;
				if(!fileInput){
					swal("Revisión!","Sube la factura para evidenciar gastos.","warning");
					return response;
                }
                // if(this.nuevoPago.checkCotizacion==true){

                // }else{
                //     var cotizacion=$("input[name=txtCotizacion]").val();
                //     console.log(cotizacion);
                    
                //     var ficheroCotizacion= document.getElementById('idficheroCotizacion').files.length;
                //     if(!ficheroCotizacion){
                //         swal("Revisión!","Sube la evidencia de esta cotización","warning");
                //         return response;
                //     }
                // }



                return response = true;
            },

            getDataGastos(){
                var url=host+'Gastos/get-recurso-data-form';
                axios.get(url).then(resp=>{
                    if(resp.status==200 && resp.data.respuesta){
                        this.empresa=resp.data.jsonEmpresa.rows;
                        this.proveedor=resp.data.jsonProveedor.proveedores;
                        this.clasificacionPrincipalGasto = resp.data.jsonClasificacionPrincipalGasto.rows;
                        this.tipoGasto=resp.data.jsonTipoGasto.tipo_gasto;
                        this.usuario=resp.data.jsonUsuario.rows;
                        this.cotizacion=resp.data.jsonCotizacion.rows;
                        this.tipoFichero=resp.data.jsonTipoFichero.rows;
                    }
                }).catch(resp => {
                    console.log(resp);
                });
            },
            async obtenerClasificacionTipoGasto(idTipoGasto){
                var url = host + "Gastos/get-clasificacion/" + idTipoGasto;
                var $select = $('.cmbClasificacion');
                await axios.get(url).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.clasificacionGasto = resp.data.jsonClasificacionGasto.rows;
                        $select.find('option').remove();
                        this.clasificacionGasto.forEach(item => {
                            $select.append('<option value=' + item.id_tipogasto + '>' + item.nombre + '</option>');
                        });
                        if (this.clasificacionGasto.length >= 1) {
                            this.nuevoPago.id_clasificacionGasto = parseInt(this.clasificacionGasto[0].id_tipo_gasto);
                        }
                        if(Array.isArray(this.editGastoData) && this.editGastoData.length){
                            if(this.clasificacionGasto.length>=1){
                                this.editGastoData.id_clasificacion = parseInt(this.clasificacionGasto[0].id_tipo_gasto);
                            }
                        }
                        $('.cmbClasificacion').selectpicker('refresh');
                    }
                });
            },
            
            createGasto(){
                if(this.validateForm()){
                    var formGasto = document.getElementById('formGasto');
                    var formGastoData = new FormData(formGasto);
                    var url = host + 'Gastos/storeGastos';
                    axios.post(url,formGastoData).then(resp => {
                        console.log(resp);
                        if (resp.status == 200 && resp.data.respuesta) {
                            $("#formGasto")[0].reset();
                            $("#modalGasto").modal("hide");
                            this.nuevoPago.id_empresa = '',
                            this.nuevoPago.id_proveedor = '',
                            this.nuevoPago.facturaNota = '',

                            this.nuevoPago.idPrincipalGasto = '',
                            this.nuevoPago.id_tipoGasto = '',
                            this.nuevoPago.id_clasificacionGasto = '',

                            this.nuevoPago.id_cotizacion = '',
                            this.nuevoPago.id_usuario = '',
                            this.nuevoPago.checkCotizacion=false,
                            this.nuevoPago.txtCotizacion = '',
                            this.nuevoPago.depa = '',
                            this.nuevoPago.sucursal = ''
                            this.formInputs = '';
                            swal(resp.data.titulo, resp.data.mensaje, "success");
                            $("#dtGasto").bootstrapTable('refresh');
                            console.log("refrescando la tabla");
                        }else{
                            swal(resp.data.titulo, resp.data.mensaje, "error");
                        }
                    }).catch(error => {
                        swal("Algo ha salido mal!",error.resp,"error");
                    })
                }
            },

            editGasto(idGasto){
                var url = host + "Gastos/get-gasto/"+idGasto;
                axios.get(url).then(resp=>{
                    if (resp.status==200 && resp.data.respuesta) {
                        this.editGastoData=resp.data.jsonGasto.gastoSelect[0];
                        this.formInputs=resp.data.jsonCotizacionFichero.rows;
                        $('.cmbEmpresa').selectpicker('val', this.editGastoData.id_empresa);
                        $('.cmbEmpresa').selectpicker('render');
                        $('.cmbProveedor').selectpicker('val', this.editGastoData.id_proveedor);
                        $('.cmbProveedor').selectpicker('render');
                        $('.cmbTipoFichero').selectpicker('val', this.editGastoData.id_tipoFichero);
                        $('.cmbTipoFichero').selectpicker('render');
                        /* Agregar clasificacion Principal */
                        $('.cmbClasificacionPrincipalGasto').selectpicker('val', this.editGastoData.idPrincipalGasto);
                        $('.cmbClasificacionPrincipalGasto').selectpicker('render');

                        $('.cmbTipogasto').selectpicker('val', this.editGastoData.id_tipoGasto);
                        $('.cmbTipogasto').selectpicker('render');

                        $('.cmbUsuario').selectpicker('val', this.editGastoData.usuario);
                        $('.cmbUsuario').selectpicker('render');
                        if(this.editGastoData.checkGastoParcial==true){
                            this.opcionGasto=2;
                            this.estatus1=true;
                            this.estatus2=false;
                            this.gastoParcial();
                        }else{
                            this.opcionGasto=1;
                            this.estatus2=true;
                            this.estatus1=false;
                            this.gastoTotal();
                        }
                        valores=[];
                        for (var i = 0; i < this.formInputs.length; i++) {
                            valores.push(this.formInputs[i].name);
                        }
                        console.log(valores);
                        this.editUsuario.id_usuario=this.editGastoData.usuario;
                        if (this.editGastoData.checkCotizacion==true) {
                            $('.cmbCotizacion').selectpicker('val', valores);
                            $('.cmbCotizacion').selectpicker('render');                        
                        }
                        this.obtenerClasificacionTipoGasto(this.editGastoData.id_tipoGasto);
                    }
                });
                $('.cmbClasificacion').selectpicker('val', this.editGastoData.id_clasificacion);
                $('.cmbClasificacion').selectpicker('render');
                this.loadModal();
            },
            
            updateGasto(){
                var formEditGasto = document.getElementById('formEditGasto');
                var formEditGastoData = new FormData(formEditGasto);
                var url = host + 'Gastos/'+this.editGastoData.id_gasto+'/actualizar';
                //`${host}Gastos/${this.editGastoData.id_gasto}/actualizar`

                axios.post(url, formEditGastoData).then(resp=>{
                    if(resp.status==200 && resp.data.respuesta){
                        $("#formEditGasto")[0].reset();
                        $("#modalEditGasto").modal("hide");
                        this.editGastoData={};
                        this.estatus1='';
                        this.estatus2='';
                        swal(resp.data.titulo, resp.data.mensaje, "success");
                        $("#dtGasto").bootstrapTable('refresh');
                    }else{
                        swal(resp.data.titulo, resp.data.mensaje,"error");
                    }
                }).catch(error=>{
                    swal("Algo ha salido mal!",error.resp,"error");
                })  
            },

            deleteGasto(param){
                var url=host+'Gastos/'+param+'/eliminar';
				swal({
					title	: "Eliminar Gasto",
					text	: "Estas seguro! si esta asociado a una cotización comisiones y cuotas seran recalculadas.",
					type	: "warning",
					showCancelButton: true,
					cancelButtonText: 'Cancelar',
					confirmButtonText: 'Eliminar',
				}).then((result)=>{
					if(result){
						axios.post(url).then(resp=>{
							if(resp.status==200 && resp.data.respuesta){
								swal(resp.data.titulo, resp.data.mensaje, "success");
								$("#dtGasto").bootstrapTable('refresh');
							}else{
								swal(resp.data.titulo, resp.data.mensaje, "error");
							}
						}).catch(error=>{
							swal("Algo malo pasó!", error.resp, "error");
						});
					}
				}).catch(swal.noop);
            },
            
            validateReporte(){
                var response=false;
                var valorEmpresa=$('.cmbReporteEmpresa').selectpicker('val');
                var reporteFechaInicio=$('#txtFechaReporteInicio').val();
                var reporteFechaFin=$('#txtFechaReporteFin').val();
                if(valorEmpresa=='default'){
					swal("Revision!","Por favor eliga una empresa.","warning");
                    return response;
                }
                if(reporteFechaInicio=='' && reporteFechaFin==''){
					swal("Revisión!","Seleccione las fechas correspondientes.","warning");
                    return response;
                }
               return response=true;
            },

            GenerarPDF(){  
                var urlRedirect = '';
                var valorEmpresa=$('.cmbReporteEmpresa').selectpicker('val');
                var valorTipoGasto=$('.cmbReporteTipoGasto').selectpicker('val');
                var valorUsuario=$('.cmbReporteUsuario').selectpicker('val');
                var reporteFechaInicio=$('#txtFechaReporteInicio').val();
                var reporteFechaFin=$('#txtFechaReporteFin').val();
                console.log(valorEmpresa);
                console.log(reporteFechaInicio);
                console.log(reporteFechaFin);
                console.log(this.validateReporte())
                if(this.validateReporte()){
                    if( valorUsuario!='default' && valorUsuario!=null){
                        if (valorTipoGasto!='default' && valorTipoGasto!=null) {
                            urlRedirect=host+'Gastos/reporte-gasto-pdf/'+reporteFechaInicio+'/'+reporteFechaFin+'/'+valorEmpresa+'/'+valorTipoGasto+'/'+valorUsuario;
                        }
                        else{
                            urlRedirect=host+'Gastos/reporte-gasto-pdf/'+reporteFechaInicio+'/'+reporteFechaFin+'/'+valorEmpresa+'/'+valorUsuario;
                        }
                    }
                    if(valorUsuario=='default' && valorTipoGasto!=null && valorTipoGasto!='default'){
                        urlRedirect=host+'Gastos/reporte-gasto-pdf/'+reporteFechaInicio+'/'+reporteFechaFin+'/'+valorEmpresa+'/'+valorTipoGasto+"/tipoGasto";
                    }else if(valorTipoGasto=='default' && valorUsuario=='default'){
                        urlRedirect=host+'Gastos/reporte-gasto-pdf/'+reporteFechaInicio+'/'+reporteFechaFin+'/'+valorEmpresa;
                    }
                    $('#txtFechaReporteInicio').val("");
                    $("#txtFechaReporteFin").val("");
                    $('.cmbReporteEmpresa').val('default')
                    $('.cmbReporteEmpresa').selectpicker('refresh');
                    $('.cmbReporteTipoGasto').val('default')
                    $('.cmbReporteTipoGasto').selectpicker('refresh');
                    $('.cmbReporteUsuario').val('default');
                    $('.cmbReporteUsuario').selectpicker('refresh');
                    window.open(urlRedirect);
                }
            },

        },

        computed: {          
            buscarTipoGasto(){
                lst = this.tipoGasto;
                if(this.nuevoPago.idPrincipalGasto == 4){
                    console.log("prueba del level 4 computed");
                    // return this.nuevoPago.idPrincipalGasto == 4 ? this.tipoGasto.filter((c) => c.nombre_gasto.includes("COMPRAS")) : this.tipoGasto;
                    lst = this.tipoGasto.filter((c) => c.nombre_gasto.includes("COMPRAS"));
                }else if(this.nuevoPago.idPrincipalGasto == 6){
                    console.log("prueba del level 6 computed");
                    lst = this.tipoGasto.filter((c) => (c.id_tipo_gasto >= 7 && c.id_tipo_gasto <= 9));
                }
                return lst;
            }
        },
        watch: {
            buscarTipoGasto: function(val) {
                console.log(val.length, "tamanio");
                if(val.length){
                    $('.cmbTipoGasto').selectpicker('render');
                    this.loadModal();
                }
                console.log("prueba 2");
            },

            'nuevoPago.id_tipoGasto': function (val) {
                if(val>0){
                    if (!isNaN(val)) {
                        // $('.cmbClasificacion').selectpicker('refresh');        
                        this.obtenerClasificacionTipoGasto(val);
                    } 
                }
            },
            
            'nuevoPago.checkCotizacion': function () {
                if(this.nuevoPago.checkCotizacion==true){
                    $('.cmbCotizacion').selectpicker('refresh');
                }
            },
            
            'nuevoPago.id_usuario':function (val){
                console.log(val);
                if(val>0){
                    if(!isNaN(val)){
                        for (let i = 0; i < this.usuario.length; i++) {
                            if(val==this.usuario[i].id_usuario){
                                this.nuevoPago.depa=this.usuario[i].departamento;
                                this.nuevoPago.sucursal=this.usuario[i].sucursal;
                            }
                        }
                    }
                }
            },
            'editGastoData.id_tipoGasto': function (val) {
                if(val>0){
                    if (!isNaN(val)) {    
                        this.obtenerClasificacionTipoGasto(val);
                    }
                }
            },

            'editUsuario.id_usuario':function (val){
                console.log(val);
                if(val>0){
                    if(!isNaN(val)){
                        for (let i = 0; i < this.usuario.length; i++) {
                            if(val==this.usuario[i].id_usuario){
                                this.editUsuario.departamento=this.usuario[i].departamento!=='' ? this.usuario[i].departamento : "N/A";
                                this.editUsuario.sucursal=this.usuario[i].sucursal!=='' ? this.usuario[i].departamento : "N/A";
                            }
                        }
                    }
                }
            },

            'nuevoPago.lugar': function (val) {           
                if (val.length >0) {
                    if(this.lugares.length>0){
                        this.nuevoPago.ciudad=this.lugares[0].long_name;
                        this.nuevoPago.estado=this.lugares[1].long_name;
                        this.nuevoPago.pais=this.lugares[2].long_name;
                    }
                    this.lugares=[];
                }
            },

            'opcionGasto':function(val){
                console.log(val +"valor de val");
                if(val==2){
                    console.log("cambio de opcion de gasto");
                    document.getElementById("addCotizacionGasto").disabled=false;
                    document.getElementById("addCotizacionGastoEdit").disabled=false;
                }
                else if(val==1){
                    if(this.formInputs.length>1){
                        this.formInputs=[];
                        $('.cmbCotizacion').selectpicker('val',[]);
                        $('.cmbCotizacion').selectpicker('render');
                    }
                    if(this.formInputs.length>=1){
                        document.getElementById("addCotizacionGasto").disabled=true;
                        document.getElementById("addCotizacionGastoEdit").disabled=true;
                    }
                }
            }
            ,

            'formInputs.length':function(val){
                if(this.opcionGasto==1){
                    console.log(val +"valor en el formInputs");
                    if(val>=1){
                        document.getElementById("addCotizacionGasto").disabled=true;
                        document.getElementById("addCotizacionGastoEdit").disabled=true;

                    }
                    if(val==0){
                        document.getElementById("addCotizacionGasto").disabled=false;
                        document.getElementById("addCotizacionGastoEdit").disabled=false;
                    }
                }else if(this.opcionGasto==2){
                    document.getElementById("addCotizacionGasto").disabled=false;
                    document.getElementById("addCotizacionGastoEdit").disabled=false;
                }
            }


        }

    });
}
