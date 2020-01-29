var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');


if (document.getElementById("appEmpresa")) {
    var empresa = new Vue({
        el:"#appEmpresa",
        data:{
            title: 'Registro de una nueva empresa',
            nombreEmpresa: '',
            claveEmpresa: '',
            direccionEmpresa: '',
            dominioEmpresa: '',
            razonSocial: '',
            editEmpresa: {}
        },

        methods: {
            validateForm(){
                var response=false;
                if(!(this.nombreEmpresa) || !this.claveEmpresa || !this.direccionEmpresa || !this.dominioEmpresa || !this.razonSocial) {
                    swal("Revisión!", "Rellene todos los campos del formulario por favor", "warning");
                    return response;
                }
                
                var fileInput= document.getElementById('idFileLogotipo').files.length;
				if(!fileInput){
					swal("Revisión!","Sube una imagen con formato png de logo para la empresa.","warning");
					return response;
                }

                return response = true;
            },

            createEnpresa(){
                if(this.validateForm()){
                    var formEmpresa = document.getElementById('formEmpresa');
                    var formEmpresaData = new FormData(formEmpresa);
                    var url = host + 'empresa/storeEmpresa';
                    axios.post(url,formEmpresaData).then(resp=>{
                        console.log(resp);
                        if (resp.status==200 && resp.data.respuesta) {
                            $("#formEmpresa")[0].reset();
                            $("#modalNuevaEmpresa").modal("hide");
                            this.nombreEmpresa= '',
                            this.claveEmpresa= '',
                            this.direccionEmpresa= '',
                            this.dominioEmpresa= '',
                            swal(resp.data.titulo, resp.data.mensaje, "success");
                            $("#dtEmpresa").bootstrapTable('refresh');
                        }else{
                            swal(resp.data.titulo, resp.data.mensaje, "error");
                        }
                    }).catch(error=>{
                        swal("Algo ha salido mal!",error.resp,"error");
                    })
                }
            },

            editEmpresaID(idEmpresa){
                var url = host + "empresa/get-empresa/"+idEmpresa;
                axios.get(url).then(resp=>{
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.editEmpresa = resp.data.jsonEmpresa.rows[0];
                    }
                });
            },
            
            updateEmpresa(){
                var formEditEmpresa = document.getElementById('formEditEmpresa');
                var formEditEmpresaData = new FormData(formEditEmpresa);
                var url = host + 'empresa/'+this.editEmpresa.idEmpresa+'/updateEmpresa';
                axios.post(url, formEditEmpresaData).then(resp=>{
                    if(resp.status == 200 && resp.data.respuesta){
                        $("#formEditEmpresa")[0].reset();
                        $("#modalEditEmpresa").modal("hide");
                        this.editEmpresa = {};
                        swal(resp.data.titulo, resp.data.mensaje, "success");
                        $("#dtEmpresa").bootstrapTable('refresh');
                    }else{
                        swal(resp.data.titulo, resp.data.mensaje,"error");
                    }
                }).catch(error=>{
                    swal("Algo ha salido mal!",error.resp,"error");
                })  
            },

            deleteEmpresa(param){
                var url = host+'empresa/'+param+'/eliminar';
				swal({
					title	: "Eliminar Empresa",
					text	: "Estas seguro! de eliminar esta empresa.",
					type	: "warning",
					showCancelButton: true,
					cancelButtonText: 'Cancelar',
					confirmButtonText: 'Eliminar',
				}).then((result)=>{
					if(result){
						axios.post(url).then(resp=>{
							if(resp.status == 200 && resp.data.respuesta){
								swal(resp.data.titulo, resp.data.mensaje, "success");
								$("#dtEmpresa").bootstrapTable('refresh');
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
