var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');


if (document.getElementById("appSucursales")) {
    var sucursal = new Vue({
        el:"#appSucursales",
        mounted() {
            this.getEmpresa();
        },
        data:{
            title: 'Registro de una nueva Sucursal',
            empresa:[],
            idEmpresa: '',
            nombreSucursal: '',
            codigoPostal: '',
            telefono: '',
            domicilioSucursal: '',
            coloniaSucursal: '',
            municipio: '',
            ciudad: '',
            estado: '',
            editSucursal: {}
        },

        methods: {
            loadModal(){
                $('.cmbEmpresa').selectpicker('refresh');
            },
            validateForm(){
                var response=false;
                if(!(this.nombreSucursal) || !this.codigoPostal || !this.telefono || !this.domicilioSucursal || !this.coloniaSucursal
                || !this.municipio || !this.ciudad || !this.estado) {
                    swal("Revisión!", "Rellene todos los campos del formulario por favor", "warning");
                    return response;
                }
                if(!(this.idEmpresa)) {
                    swal("Revisión!", "Asocia con alguna empresa en particular.", "warning");
                    return response;
				} 

                return response = true;
            },
            getEmpresa(){
                var url = host+'sucursales/getEmpresa';
                axios.get(url).then(resp=>{
                    if(resp.status == 200 && resp.data.respuesta){
                        this.empresa = resp.data.jsonEmpresa.rows;
                    }
                }).catch(resp => {
                    console.log(resp);
                });
            },

            createSucursal(){
                if(this.validateForm()){
                    var formSucursal = document.getElementById('formSucursal');
                    var formSucursalData = new FormData(formSucursal);
                    var url = host + 'sucursales/storeSucursal';
                    axios.post(url,formSucursalData).then(resp=>{
                        console.log(resp);
                        if (resp.status==200 && resp.data.respuesta) {
                            $("#formSucursal")[0].reset();
                            $("#modalNuevaSucursal").modal("hide");
                            this.idEmpresa = '',
                            this.nombreSucursal = '',
                            this.codigoPostal = '',
                            this.telefono = '',
                            this.domicilioSucursal = '',
                            this.coloniaSucursal = '',
                            this.municipio = '',
                            this.ciudad = '',
                            this.estado = '',
                            swal(resp.data.titulo, resp.data.mensaje, "success");
                            $("#dtSucursales").bootstrapTable('refresh');
                        }else{
                            swal(resp.data.titulo, resp.data.mensaje, "error");
                        }
                    }).catch(error=>{
                        swal("Algo ha salido mal!",error.resp,"error");
                    })
                }
            },

            editSucursalID(idSucursal){
                var url = host + "sucursales/getSucursal/"+idSucursal;
                axios.get(url).then(resp=>{
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.editSucursal = resp.data.jsonSucursal.rows[0];
                        $('.cmbEmpresa').selectpicker('val', this.editSucursal.idEmpresa);
                        $('.cmbEmpresa').selectpicker('render');
                    }
                });
                this.loadModal();
            },
            
            updateSucursal(){
                var formEditSucursal = document.getElementById('formEditSucursal');
                var formEditSucursalData = new FormData(formEditSucursal);
                var url = host + 'sucursales/'+this.editSucursal.idSucursal+'/updateSucursal';
                axios.post(url, formEditSucursalData).then(resp=>{
                    if(resp.status == 200 && resp.data.respuesta){
                        $("#formEditSucursal")[0].reset();
                        $("#modalEditSucursal").modal("hide");
                        this.editSucursal = {};
                        swal(resp.data.titulo, resp.data.mensaje, "success");
                        $("#dtSucursales").bootstrapTable('refresh');
                    }else{
                        swal(resp.data.titulo, resp.data.mensaje,"error");
                    }
                }).catch(error=>{
                    swal("Algo ha salido mal!",error.resp,"error");
                })  
            },

            deleteSucursal(param){
                var url = host+'sucursales/'+param+'/eliminar';
				swal({
					title	: "Eliminar Sucursal",
					text	: "Estas seguro! de eliminar esta sucursal.",
					type	: "warning",
					showCancelButton: true,
					cancelButtonText: 'Cancelar',
					confirmButtonText: 'Eliminar',
				}).then((result)=>{
					if(result){
						axios.post(url).then(resp=>{
							if(resp.status == 200 && resp.data.respuesta){
								swal(resp.data.titulo, resp.data.mensaje, "success");
								$("#dtSucursales").bootstrapTable('refresh');
							}else{
								swal(resp.data.titulo, resp.data.mensaje, "error");
							}
						}).catch(error=>{
							swal("Algo malo pasó!", error.resp, "error");
						});
					}
				}).catch(swal.noop);
            },
        }

    });
}
