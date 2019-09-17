//CONFIGURACION DE HOST
var host = "http://localhost:8080/WebSar/controlPanel/";

// //HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

//CONFIGURACION MOMENT JS
moment.locale('es');
if (document.getElementById('appCatalogoActivos')) {
	var catalogoVue=new Vue({
		el:"#appCatalogoActivos",
		data:{
			nombre:"",
			descripcion:""
		},
		methods:{

			validateForm(){
				var response=false;

				//Validacion de los campos
				if((!this.nombre || !this.descripcion)) {
					swal("Revisión!", "Debes  de terminar de rellenar todos los campos.", "warning");
					return response;
				}
				return response=true;
			}
			,
			//started method
			createCatalogo(){
				if(this.validateForm()){
				var formCatalogoActivo=document.getElementById('formCatalogoActivo');
				var formCatalogoActivoData=new FormData(formCatalogoActivo);
				var url=host+'catalogoActivo/storeCatalogoAjax';
				axios.post(url,formCatalogoActivoData).then(response=>{
					if(response.status==200 && response.data.respuesta){
						//Clean form
						$("#formCatalogoActivo")[0].reset();
						$("#modalNuevaCategoria").modal("hide");
						this.nombre='';
						this.descripcion='';

						swal(response.data.titulo, response.data.mensaje, "success");
						$("#dtActivos").bootstrapTable('refresh');

					}else{
						console.log(response);
						swal(response.data.titulo, response.data.mensaje, "error");
					}
				})
				.catch(error=>{
					swal("Algo malo pasó!", error.response.data, "error");
					console.log(error);
				});
			}
			else {
				console.log("Error en validacion de FORM");
			}
		}
		//finish method createCatalogo
	
		}
	});
}


if(document.getElementById('appActivos')){
	var NuevoActivo=new Vue({
		el:"#appActivos",
		data:{
			catalogo:[],
			empresas:[],
			newActivo:{
				nombre:"",
				marca:"",
				modelo:"",
				color:"",
				serie:"",
				observaciones:"",
				id_empresa:'default',
				id_activo:'default'
			},
			editData:{},
			elegido:""
		
		}
		,methods:{
			getFormData() {
				var url=host+'BienActivo/get-catalogo-data-form';
				axios.get(url).then(response=>{
					if(response.status==200 && response.data.respuesta){
						this.catalogo=response.data.jsonCatalogoActivo.rows;
						this.empresas 		= response.data.jsonEmpresas.rows;					
					}
				})
			}
			,loadModal(){
				$('.cmbCatalogo').selectpicker('refresh');
				$('.cmbEmpresa').selectpicker('refresh');
			}
			,
			validateForm(){
				var response = false;

				if(!this.newActivo.nombre || !this.newActivo.marca){
					swal("Revisión!", "Debes  de terminar de rellenar todos los campos.", "warning");
					return response;
				}

				//VALIDATION DE CATALOGO
					if(isNaN(this.newActivo.id_activo)) {
						swal("Revisión!", "Debes seleccionar un catalogo para continuar con el registro.", "warning");
						return response;
					}
					if(isNaN(this.newActivo.id_empresa)){
						swal("Revisión!","Debes seleccionar una empresa para continuar con el registro.","warning");
						return response;
					}
					return response = true;
			},
			createActivo(){
				if(this.validateForm()) {
					var formActivo=document.getElementById('formActivo');
					var formActivoData=new FormData(formActivo);
					var url=host+'BienActivo/storeActivoAjax';

					axios.post(url,formActivoData).then(response=>{
						if(response.status==200 && response.data.respuesta){
							//Clean form
							$("#formActivo")[0].reset();
							$("#modalNuevoActivo").modal("hide");
							this.newActivo.nombre='';
							this.newActivo.marca='',
							this.newActivo.modelo='',
							this.newActivo.color='',
							this.newActivo.serie='',
							this.newActivo.observaciones='default',
							this.newActivo.id_activo='default'
							this.newActivo.id_empresa='default'

							swal(response.data.titulo, response.data.mensaje, "success");
							$("#dtActivo").bootstrapTable('refresh');

						}else{
							console.log(response);
							swal(response.data.titulo, response.data.mensaje, "error");
						}
					})
					.catch(error=>{
						swal("Algo malo pasó!", error.response.data, "error");
						console.log(error);
					});
				}
			},
			editActivo(param){

				 var formEditActivo=document.getElementById('formEditActivo');
				 var formEditActivoData=new FormData(formEditActivo);
				var url=host+'BienActivo/'+param+'/edit';
				axios.get(url, formEditActivoData).then(resp=>{
					console.log(resp);
					var data=resp.data.dataActivo.Activos[0];
					//con el indice al final
					this.editData=data;
					this.elegido=this.editData.id_empresa;
					$('.cmbEmpresa').selectpicker('val', NuevoActivo.editData.id_empresa);
					$('.cmbEmpresa').selectpicker('render');
		
					$('.cmbCatalogo').selectpicker('val', NuevoActivo.editData.id_activo);
					$('.cmbCatalogo').selectpicker('render');
		
					this.loadModal();
				// 	this.editData.nombre=data[0].nombre;
				// 	this.editData.marca=data[0].marca;
				// 	this.editData.modelo=data[0].modelo;
				// 	this.editData.color=data[0].color;
				// 	this.editData.serie=data[0].serie;
				// 	this.editData.observaciones=data[0].observaciones;
				// 	this.editData.id_empresa=data[0].id_empresa;
				// 	this.editData.id_activo=data[0].id_activo;
				// 	this.editData.id_activo_mobiliario=data[0].id_activo_mobiliario;
				}).catch(error=>{
					console.log(error);
				});
			},
			update(){
				var formEditActivo=document.getElementById('formEditActivo');
				var formEditActivoData=new FormData(formEditActivo);
				var url=host+'BienActivo/'+this.editData.id_activo_mobiliario+'/update';
				axios.post(url,formEditActivoData).then(resp=>{
					if(resp.status==200 && resp.data.respuesta){
						$("#formEditActivo")[0].reset();
						$("#modalEditActivo").modal("hide");
							this.editData.nombre="";
							this.editData.marca="";
							this.editData.modelo="";
							this.editData.color="";
							this.editData.serie="";
							this.editData.observaciones="";
							this.editData.id_empresa='default';
							this.editData.id_activo='default';
							this.editData.id_activo_mobiliario='default';
					
						swal(resp.data.titulo, resp.data.mensaje, "success");
							$("#dtActivo").bootstrapTable('refresh');
					}else{
						console.log(resp);
							swal(resp.data.titulo, resp.data.mensaje, "error");
					}
				}).catch(error=>{
					swal("Algo malo pasó!", error.resp.data, "error");
						console.log(error);
				})

			}
		}
		,mounted(){
			this.getFormData();
		}
	})
}