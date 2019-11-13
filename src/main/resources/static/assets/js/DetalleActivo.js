var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');

Vue.component('proveedor2', {
    props: ['idservicio'],
    template: `#templateproveedor2`,
})
Vue.component('proveedor3', {
    props: ['idservicio'],
    template: `#templateproveedor3`,
})

Vue.component('proveedor', {
    props: ['idservicio'],
    template: `#templateproveedor`,
})

if (document.getElementById('appDetalle')) {
    var detalleActivo = new Vue({
        el: "#appDetalle",
        mounted() {
            this.getFormData();
        },
        data: {
            catalogo: [],
            recursoActivo: [],
            fichero: [],
            urlcompletofichero: '',
            servicio: [],
            gastoAproximado: [],
            precio_estimado: '',
            totalgasto: '',
            newDetalle: {
                id_activo_mobiliario: '',
                id_catalogo: '',
                observaciones: '',
                titulo: '',
                urlfichero: '',
            },
            datafiltrado: [],
            proveedorServicio: [],

            editdatafiltrado: [],
            //Edicion ActivoManto.
            editDetalleManto: {},
            editproveedorServicio: [],

            checkdatafiltrado:[],
            estatus:[],
            changeestatus:'',
            pagos:[],
            pagados:''
        },
        methods: {
            getFormData() {
                var url = host + 'DetalleMant/get-recurso-data-form';
                axios.get(url).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.catalogo = resp.data.jsonCatalogoActivo.rows;
                        // this.recursoActivo=resp.data.jsonRecursoActivo.rows;
                        // this.fichero=resp.data.jsonRecursoFichero.rows;
                    }
                }).catch(resp => {
                    console.log(resp);
                });
            },
            loadModal() {
                $('.cmbCatalogo').selectpicker('refresh');
                $('.cmbRecurso').selectpicker('refresh');
                $('.cmbRecurso2').selectpicker('refresh');
            },
            validatevalidation(){
                var response=false
                var checkboxes = document.getElementsByName('txtAcept');
                    var vals = [];
                    for (var i=0, n=checkboxes.length;i<n;i++) 
                    {
                        if (checkboxes[i].checked) 
                        {
                            vals += checkboxes[i].value;
                        }
                    }
                    if(vals.length<=0){
                        swal("Revisión!","Deberia de validar al menos un proveedor","warning");
                        return response;
                    }

                    if(!this.changeestatus){
                        swal("Revisión!","Selecciona en que condiciones valida, este servicio", "warning");
                        return response;
                    }

                    var comentarios=document.getElementById("txtComentarios");
                    if(!comentarios.value){
                        swal("Revisión!","Describe el motivo de su decision","warning");
                        return response;
                    }
                return response=true;
            },
            validateForm() {
                var response=false;

                if(!(this.newDetalle.id_catalogo)) {
                    swal("Revisión!", "Debes seleccionar un Tipo de activo para continuar con el registro.", "warning");
                    return response;
				} 
                if(!(this.newDetalle.id_activo_mobiliario)){
                    swal("Revisión!","Seleccione un activo, para poder continuar","warning");
                    return response;
                }
                if(!this.newDetalle.observaciones){
                    swal("Revisión!", "Rellena el campo observaciónes", "warning");
					return response;
                }

                var checkboxes = document.getElementsByClassName('checkServicio');
                var vals = [];
                for (var i=0, n=checkboxes.length;i<n;i++) 
                {
                    if (checkboxes[i].checked) 
                    {
                        vals += checkboxes[i].value;
                    }
                }
                if(vals.length<=0){
                    swal("Revisión!","Seleccione por lo menos un servicio","warning");
                    return response;
                }
                return response = true;
            },
            getRecursoCat(paramCatalogo) {
                var url = host + "BienActivo/get-activos/" + paramCatalogo;
                var $select = $('.cmbRecurso');
                axios.get(url).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.recursoActivo = resp.data.jsonActivoCatalogo.rows;
                        $select.find('option').remove();
                        this.recursoActivo.forEach(item => {
                            $select.append('<option value=' + item.id_recurso_activo + '>' + item.descripcion_completa + '</option>');
                        });
                        if (this.recursoActivo.length >= 1) {
                            this.newDetalle.id_activo_mobiliario = parseInt(this.recursoActivo[0].id_activo_mobiliario);
                        }
                        $('.cmbRecurso').selectpicker('refresh');
                    }
                });
            },
            getServicioIdTipoActivo(paramIdTipo) {
                var url = host + "DetalleMant/get-servicio/" + paramIdTipo;
                axios.get(url).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.servicio = resp.data.jsonServicio.rows;
                        this.fillcheck();
                    }
                });
            },
            /**Obtener imagen del activo mediante el id del selector */
            getFicheroLstActivo(paramIdActivo) {
                var url = host + "DetalleMant/get-fichero/" + paramIdActivo;
                axios.get(url).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.fichero = resp.data.jsonFichero.rows[0];
                        urlfijo = "http://localhost:8080/WebSar/ficheros/activoFijos/";
                        console.log(this.fichero.url_img_activo);
                        this.urlcompletofichero = urlfijo + (this.fichero.url_img_activo);
                    }
                })
            },

            async getProveedorServicio(paramIdActivo, paramIdSevicio) {
                var url = host + "DetalleMant/get-proveedor/" + paramIdActivo + "/" + paramIdSevicio;
                await axios.get(url).then(resp => {
                    if (resp.status == 200) {
                        this.proveedorServicio = [...resp.data.jsonServicioProveedor.rows];
                    }
                });
                return this.proveedorServicio;
            },
            async getProveedorServicioedit(paramIdActivo, paramIdSevicio) {
                var url = host + "DetalleMant/get-proveedor/" + paramIdActivo + "/" + paramIdSevicio;
                await axios.get(url).then(resp => {
                    if (resp.status == 200) {
                        this.editproveedorServicio = [...resp.data.jsonServicioProveedor.rows];
                    }
                });
                return this.editproveedorServicio;
            },

            async ObtenerIdServicio(index) {
                if ($('#' + index + '').is(':checked') == true) {
                    valor = $('#' + index + '').val();
                    // idTipoActivo=$('select').find('option:selected').val();
                    idTipoActivo = $("select#cmbCatalogo option:checked").val();
                    this.proveedorServicio = await this.getProveedorServicio(idTipoActivo, index);
                    if (this.proveedorServicio.length > 0) {
                        this.datafiltrado.push(this.proveedorServicio);
                    }
                    /*Add gasto aproximado if checked service */
                    this.servicio.forEach(item => {
                        if(index==item.id_servicio){
                            this.gastoAproximado.push({"precio":item.precio_estimado,"id_servicio":item.id_servicio});
                        }
                    });
                } else
                if ($('#' + index + '').is(':checked') == false) {
                    for (i = 0; i < this.datafiltrado.length; i++) {
                        capacidad = this.datafiltrado[i].length;
                        if (this.datafiltrado[i].length != null && this.datafiltrado[i]) {
                            for (j = 0; j < capacidad; j++) {
                                tamanio = this.datafiltrado[i].length;
                                if (this.datafiltrado[i][j].id_servicio === index) {
                                    this.datafiltrado.splice(i, 1);
                                }
                                j = tamanio;
                            }
                        }
                    }
                    /*Delete gasto if unchecked */
                    for (var i = 0; i < this.gastoAproximado.length; i++) {
                        if (this.gastoAproximado[i].id_servicio===index) {
                            this.gastoAproximado.splice(i,1);
                        }
                    }

                }
            },
            async ObtenerIdServicioedit(index) {
                if ($('#serv' + index + '').is(':checked') == true) {
                    valor = $('#' + index + '').val();
                    idTipoActivo = detalleActivo.editDetalleManto.id_tipo_activo;
                    this.editproveedorServicio = await this.getProveedorServicioedit(idTipoActivo, index);
                    if (this.editproveedorServicio.length > 0) {
                        this.editdatafiltrado.push(this.editproveedorServicio);
                    }
                    
                    /*Add gasto aproximado if checked service */
                    this.servicio.forEach(item => {
                        if(index==item.id_servicio){
                            this.gastoAproximado.push({"precio":item.precio_estimado,"id_servicio":item.id_servicio});
                        }
                    });


                } else
                if ($('#serv' + index + '').is(':checked') == false) {
                    for (i = 0; i < this.editdatafiltrado.length; i++) {
                        capacidad = this.editdatafiltrado[i].length;
                        if (this.editdatafiltrado[i].length != null && this.editdatafiltrado[i]) {
                            for (j = 0; j < capacidad; j++) {
                                tamanio = this.editdatafiltrado[i].length;
                                if (this.editdatafiltrado[i][j].id_servicio === index) {
                                    this.editdatafiltrado.splice(i, 1);
                                }
                                j = tamanio;
                            }
                        }
                    }
                    //end for
                    /*Delete gasto if unchecked */
                    for (var i = 0; i < this.gastoAproximado.length; i++) {
                        if (this.gastoAproximado[i].id_servicio===index) {
                            this.gastoAproximado.splice(i,1);
                        }
                    }
                }
            },
            createActivoManto() {
                console.log(this.validateForm() +" validateform final");
                if(this.validateForm()) {
                    var formActivo = document.getElementById('formActivoManto');
                    var formActivoData = new FormData(formActivo);
                    var url = host + 'DetalleMant/storeAjaxServicioProveeedor';
                    axios.post(url, formActivoData).then(response => {
                        if (response.status == 200 && response.data.respuesta) {
                            $("#formActivoManto")[0].reset();
                            $("#modalNuevoActivoManto").modal("hide");
                            this.newDetalle.id_activo_mobiliario = '';
                                this.newDetalle.id_catalogo = '';
                                this.newDetalle.observaciones = '';
                                this.newDetalle.titulo = '';
                                this.newDetalle.urlfichero = '';
                                //Edicion ActivoManto.
                                this.recursoActivo = [];
                                this.servicio = [];
                                this.fichero = [];
                                this.gastoAproximado = [];
                                this.proveedorServicio = [];
                                this.datafiltrado = [];
                                this.urlcompletofichero='';
                                swal(response.data.titulo, response.data.mensaje, "success");
                            $("#dtDetalle").bootstrapTable('refresh');
                        } else {
                            console.log(response);
                            swal(response.data.titulo, response.data.mensaje, "error");
                        }
                    }).catch(error => {
                        swal("Algo malo pasó!", error.response.data, "error");
                        console.log(error);
                    });
                }
            },

            /**All methods for edit manto */
            /**Comprobar value de los checks */
            fillcheck() {
                if (!this.servicio.isNaN) {
                    if(this.checkdatafiltrado.length>0){
                        for (x = 0; x < this.checkdatafiltrado.length; x++) {
                            this.findCheckedvalidator(this.checkdatafiltrado[x][0].id_servicio);
                        }
                    }else
                    if (this.editdatafiltrado.length > 0) {
                        for (x = 0; x < this.editdatafiltrado.length; x++) {
                            this.findChecked(this.editdatafiltrado[x][0].id_servicio);
                        }
                    }
                }
            },
            findChecked(idServicio) {
                if ($('#serv' + idServicio).val() == idServicio) {
                    document.getElementById('serv' + idServicio).checked = true;
                    // $('#serv'+idServicio).prop("checked",true);
                    
                    /**En este apartado se necesita hacer el calculo del gasto aproximado en modo edición */
                    this.servicio.forEach(item => {
                        if(idServicio==item.id_servicio){
                            this.gastoAproximado.push({"precio":item.precio_estimado,"id_servicio":item.id_servicio});
                        }
                    });
                }
            },
            findCheckedvalidator(idServicio) {
                if ($('#validator' + idServicio).val() == idServicio) {
                    document.getElementById('validator' + idServicio).checked = true;
                    // $('#serv'+idServicio).prop("checked",true);
                    
                    /**En este apartado se necesita hacer el calculo del gasto aproximado en modo edición */
                    this.servicio.forEach(item => {
                        if(idServicio==item.id_servicio){
                            this.gastoAproximado.push({"precio":item.precio_estimado,"id_servicio":item.id_servicio});
                        }
                    });
                }
            },
             editActivoManto(idDetalleManto) {
                var formEditActivoManto = document.getElementById('formEditActivoManto');
                var formEditActivoMantoData = new FormData(formEditActivoManto);
                var url = host + "DetalleMant/get-servicio-proveedor/" + idDetalleManto;
                 axios.get(url, formEditActivoMantoData).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.editDetalleManto = resp.data.jsonBienDetalleManto.bienDetalleManto[0];
                        let data = resp.data.jsonServicioProveedorManto.rows;
                        this.recursoActivo = resp.data.jsonActivoCatalogo.rows;
                        // Agrupacion de los proveedores-servicios por el tipo servicio
                        this.editdatafiltrado = _.chain(data).groupBy("id_servicio").toArray().value();
                        //Obtencion del tipo Activo
                        this.getServicioIdTipoActivo(this.editDetalleManto.id_tipo_activo);

                        $('.cmbCatalogo').selectpicker('val', this.editDetalleManto.id_tipo_activo);
                        $('.cmbCatalogo').selectpicker('render');
                    }
                })
                $('.cmbRecurso2').selectpicker('val', this.editDetalleManto.id_bien_activo);
                $('.cmbRecurso2').selectpicker('render');
                this.loadModal();
            },

            async validacion(idDetalleManto) {
                var formModalValidacion = document.getElementById('formModalValidacion');
                var formModalValidacionData = new FormData(formModalValidacion);
                var url = host + "DetalleMant/get-servicio-proveedor/" + idDetalleManto;
                await axios.get(url, formModalValidacionData).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.editDetalleManto = resp.data.jsonBienDetalleManto.bienDetalleManto[0];
                        let data = resp.data.jsonServicioProveedorManto.rows;
                        this.recursoActivo = resp.data.jsonActivoCatalogo.rows;
                        this.estatus=resp.data.jsonEstatus.estatus;
                        // Agrupacion de los proveedores-servicios por el tipo servicio
                        this.checkdatafiltrado = _.chain(data).groupBy("id_servicio").toArray().value();
                        //Obtencion del tipo Activo
                        this.getServicioIdTipoActivo(this.editDetalleManto.id_tipo_activo);

                        $('.cmbCatalogo').selectpicker('val', this.editDetalleManto.id_tipo_activo);
                        $('.cmbCatalogo').selectpicker('render');
                    }
                })
                $('.cmbRecurso2').selectpicker('val', this.editDetalleManto.id_bien_activo);
                $('.cmbRecurso2').selectpicker('render');
                this.loadModal();
                /**Formateando precio */
                // $(".txtPrecio").on({
                //     "focus": function(event) {
                //         $(event.target).select();
                //     },
                //     "keyup": function(event) {
                //         $(event.target).val(function(index, value) {
                //         return value.replace(/\D/g, "")
                //             .replace(/([0-9])([0-9]{2})$/, '$1.$2')
                //             .replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ",");
                //         });
                //     }
                // });
            },
            sendvalidation(param){
                var url=host+'DetalleMant/'+param+'/sendvalidate';
				swal({
					title	: "Enviar",
					text	: "Seguro que desea enviar esto a validación.",
					type	: "success",
					showCancelButton: true,
					cancelButtonText: 'Cancelar',
					confirmButtonText: 'Enviar',
				}).then((result)=>{
					if(result){
						axios.post(url).then(resp=>{
							if(resp.status==200 && resp.data.respuesta){
								swal(resp.data.titulo, resp.data.mensaje, "success");
								$("#dtDetalle").bootstrapTable('refresh');
							}else{
								swal(resp.data.titulo, resp.data.mensaje, "error");
							}
						}).catch(error=>{
							swal("Algo malo pasó!", error.resp.data, "error");
						});
					}
				}).catch(swal.noop);
			}
           ,
/*----------------------------------------------------------------------------------------------------------------------*/
            UpdateValidacion() {
                if(this.validatevalidation()){
                    var formModalValidacion = document.getElementById('formModalValidacion');
                    var formModalValidacionData = new FormData(formModalValidacion);
                    var url = host + 'DetalleMant/'+this.editDetalleManto.id_detalle_manto+'/validate'  ;
                    axios.post(url, formModalValidacionData).then(resp => {
                        if (resp.status == 200 && resp.data.respuesta) {
                                $("#formModalValidacion")[0].reset();
                                $("#modalValidacion").modal("hide");
                                this.editDetalleManto={};
                                this.editdatafiltrado=[];
                                this.checkdatafiltrado=[];
                                    //Edicion ActivoManto.
                                    this.recursoActivo = [],
                                    this.servicio = [],
                                    this.fichero = [],
                                    this.proveedorServicio = [],
                                    this.datafiltrado = [],
                                    this.gastoAproximado=[];
                            
                            swal(resp.data.titulo, resp.data.mensaje, "success");
                            $("#dtDetalle").bootstrapTable('refresh');
                        }else{
                            console.log(response);
                            swal(resp.data.titulo, resp.data.mensaje, "error");
                        }
                    }).catch(error => {
                            swal("Algo malo pasó!", error.resp.data, "error");
                            console.log(error);
                        });
                }
            },
/*----------------------------------------------------------------------------------------------------------------*/
            async getdataPagar(idDetalleManto){
                var formModalPago = document.getElementById('formModalPago');
                var formModalPagoData = new FormData(formModalPago);
                var url = host + "DetalleMant/get-servicioAceptado/" + idDetalleManto;
                await axios.get(url, formModalPagoData).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.editDetalleManto = resp.data.jsonBienDetalleManto.bienDetalleManto[0];
                        this.pagos = resp.data.jsonServicioAceptado.aceptado;
                        this.recursoActivo = resp.data.jsonActivoCatalogo.rows;
                        //Obtencion del tipo Activo
                        this.getServicioIdTipoActivo(this.editDetalleManto.id_tipo_activo);
                        suma=0;
                        this.pagos.forEach(item => {
                            suma+=item.precio_servicio_proveedor;
                        });
                        this.totalgasto=suma;
                        $('.cmbCatalogo').selectpicker('val', this.editDetalleManto.id_tipo_activo);
                        $('.cmbCatalogo').selectpicker('render');
                    }
                })
                $('.cmbRecurso2').selectpicker('val', this.editDetalleManto.id_bien_activo);
                $('.cmbRecurso2').selectpicker('render');
                this.loadModal();
                $(function () {
                    //Datetimepicker plugin
                    $('.datetimepicker').bootstrapMaterialDatePicker({
                        format: 'DD/MM/YYYY',
                        clearButton: true,
                        weekStart: 1,
                        time:false
                    });
                });
                /**Definir las opciones dinamicas de pago */
                    let totalpagado=false;
                    this.pagos.forEach(element => {
                        let status=(element.pagado);
                        totalpagado=status;

                    });
                    this.pagados=totalpagado;
            },

            PagarServicios(){
                var formModalPago = document.getElementById('formModalPago');
                var formModalPagoData = new FormData(formModalPago);
                var url = host + 'DetalleMant/storePagos';
                    axios.post(url, formModalPagoData).then(response => {
                        if (response.status == 200 && response.data.respuesta) {
                            $("#formModalPago")[0].reset();
                            $("#modalpago").modal("hide");
                            this.pagados='';
                            // this.newDetalle.id_activo_mobiliario = '';
                            //     this.newDetalle.id_catalogo = '';
                            //     this.newDetalle.observaciones = '';
                            //     this.newDetalle.titulo = '';
                            //     this.newDetalle.urlfichero = '';
                            //     //Edicion ActivoManto.
                            //     this.recursoActivo = [];
                            //     this.servicio = [];
                            //     this.fichero = [];
                            //     this.gastoAproximado = [];
                            //     this.proveedorServicio = [];
                            //     this.datafiltrado = [];
                            //     this.urlcompletofichero='';
                            this.pagados='';
                            swal(response.data.titulo, response.data.mensaje, "success");
                            $("#dtDetalle").bootstrapTable('refresh');
                        } else {
                            console.log(response);
                            swal(response.data.titulo, response.data.mensaje, "error");
                        }
                    }).catch(error => {
                        swal("Algo malo pasó!", error.response.data, "error");
                        console.log(error);
                    });
            },
            updateManto() {
                var formEditManto = document.getElementById('formEditActivoManto');
                var formEditMantoData = new FormData(formEditManto);
                var url = host + 'DetalleMant/' + this.editDetalleManto.id_detalle_manto + '/update';
                axios.post(url, formEditMantoData).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                            $("#formEditActivoManto")[0].reset();
                            $("#modalEditActivoManto").modal("hide");
                            this.editDetalleManto={};
                                //Edicion ActivoManto.
                                this.recursoActivo = [],
                                this.servicio = [],
                                this.fichero = [],
                                this.proveedorServicio = [],
                                this.datafiltrado = [],
                                this.gastoAproximado=[];
                        
                        swal(resp.data.titulo, resp.data.mensaje, "success");
                        $("#dtDetalle").bootstrapTable('refresh');

                    }else{
                        console.log(resp);
                        swal(resp.data.titulo, resp.data.mensaje, "error");
                    }
                }).catch(error => {
                        swal("Algo malo pasó!", error.resp.data, "error");
                        console.log(error);
                    });
            },
        },
        watch: {
            'newDetalle.id_catalogo': function (val) {
                if(val>0){
                    if (!isNaN(val)) {
                        $('.cmbRecurso').selectpicker('refresh');        
                            this.getRecursoCat(val);
                            this.getServicioIdTipoActivo(val);
                        } 
                }
            },
            'newDetalle.id_activo_mobiliario': function (val) {
                if(val>0){
                    if (!isNaN(val)) {
                        this.getFicheroLstActivo(val);
                    }
                }
            },
            "gastoAproximado": function () {
                if (!this.gastoAproximado.isNaN) {
                    suma = 0;
                this.gastoAproximado.forEach(total => {
                    suma+=total.precio;
                });
                    this.totalgasto = suma;
                }
            },

            //Watch for editDetalleManto
            'editDetalleManto.id_tipo_activo': function (val) {
                if (!isNaN(val)) {
                    console.log(val + "valor de idtipoactivo");
                    // this.getRecursoCat(val);
                    this.getServicioIdTipoActivo(val);
                }
            },
            'editDetalleManto.id_bien_activo': function (val) {
                if (!isNaN(val)) {
                    console.log("fichero" + val);
                    this.getFicheroLstActivo(val);
                }
            },


        }
    });
}