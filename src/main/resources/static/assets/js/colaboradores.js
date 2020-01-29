//CONFIGURACION DE HOST
var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');

if(document.getElementById("appOPN")){
    var opn = new Vue({
        el : "#appOPN",
        mounted() {
            this.obtenerUsuario();
        },
        data: {
            formInputs : [],
            idOportunidad : null,  
            usuario: []
          
        },
        methods: {
            obtenerUsuario(){
                var url = host + 'opnNegocioColaborador/get-users';
                axios.get(url).then((resp) => {
                    if(resp.status == 200 && resp.data.respuesta);
                    this.usuario=resp.data.jsonUsuario.rows;
                })
            },
            addUser : function(e){                
                var values = $('.cmbUsuario').selectpicker('val');
                for (var index = 0; index < values.length; index++) {
                    var idUsuario = parseInt(values[index]);
                    var id = this.usuario.findIndex(x => x.id_usuario == idUsuario);
                    if (this.formInputs.length >= 1) {
                        if (this.formInputs.some(item => item.id_usuario === idUsuario)) {
                        }else{
                            this.formInputs.push(this.usuario[id]);
                        }
                    }else{
                        this.formInputs.push(this.usuario[id]);
                    }
                }
                e.preventDefault();
            },
            deleteUser: function(index){
                if(this.formInputs[index].idOpn != undefined){
                    this.eliminarFichero(this.formInputs[index].idCotizacionFichero,this.formInputs[index].id_cotizacion, index);
                }else{
                    //console.log(`Removing index: ${index}`);
                    this.formInputs.splice(index,1);
                    //console.log(`New Array: ${JSON.stringify(this.formInputs)}`);
                }
            },

            editOpn(idOPN){
                var url = host +'opnNegocioColaborador/get-Colaboradores/'+idOPN;
                axios.get(url).then((resp) => {
                    if(resp.status == 200 && resp.data.respuesta) {
                        this.formInputs = resp.data.jsonColaboradores.jsonColaboradores;
                        console.log(this.formInputs);
                        
                        valores = [];
                        for ( var i = 0; i < this.formInputs.length; i++) {
                            valores.push(this.formInputs[i].id_usuario);
                        }
                        console.log(valores,"valores de seleccion");
                        $('.cmbUsuario').selectpicker('val',valores);
                        $('.cmbUsuario').selectpicker('render');
                    }
                });
            },
            removeColaborador: function(index){
                if(this.formInputs[index].idOpnColaborador != undefined){
                    this.deleteColaborador(this.formInputs[index].idOpnColaborador);
                }else{
                    //console.log(`Removing index: ${index}`);
                    this.formInputs.splice(index,1);
                    //console.log(`New Array: ${JSON.stringify(this.formInputs)}`);
                }
            },

            deleteColaborador(idColaborador){
                var url=host+'opnNegocioColaborador/'+idColaborador+'/eliminar';
				swal({
					title	: "Eliminar Colaborador",
					text	: "Estas seguro! de eliminar tu colaborador.",
					type	: "warning",
					showCancelButton: true,
					cancelButtonText: 'Cancelar',
					confirmButtonText: 'Eliminar',
				}).then((result)=>{
					if(result){
						axios.post(url).then(resp=>{
							if(resp.status==200 && resp.data.respuesta){
								swal(resp.data.titulo, resp.data.mensaje, "success");
							}else{
								swal(resp.data.titulo, resp.data.mensaje, "error");
							}
						}).catch(error=>{
							swal("Algo malo pasó!", error.resp, "error");
						});
					}
				}).catch(swal.noop);
            },

        },

    })
}