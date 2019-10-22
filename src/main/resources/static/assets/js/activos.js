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
			descripcion:"",
			clave:"",
			file_name:"",
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
			},
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
						this.nombre=''
						this.descripcion=''
						this.clave=''
						this.file_name=''
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
			departamento:[],
			usuario:[],
			newActivo:{
				descripcion:"",
				marca:"",
				modelo:"",
				serie:"",
				color:"",
				fecha_compra:"",
				costo_recurso:"",
				garantia_mes:"",
				obsolencia:"",
				//coste fecha mantenimiento
				periodo_mant_estimado:"",
				// costo_prom_mantenimiento:"",
				// fechaUltimoMantenimiento:"",

				//asignacion de recurso
				id_empresa:'',
				id_departamento:'',
				id_usuario_asignado:'',
				id_catalogo:'',
				//VHC
				placa:"",
				tipo_vehiculo:"",
				//EAC
				serie_evaporadora:"",
				serie_condensadora:"",
				control_remoto:false,
				//HTC
				fecha_entrega:"",
				imei:"",
				almacenamientoExterno:"",
				//ECP
				tipoEquipo:"",
				tipoMemRam:"",
				capacidadRam:"",
				tipoProcesador:"",
				marcaProcesador:"",
				capacidadProcesador:"",
				tipoHDD:"",
				capacidadHDD:"",
				tieneMonitor:false,
					tamanioMonitor:"",
					colorMonitor:"",
					modeloMonitor:"",
					numParte:"",
				observaciones:"",
				fichero:'',
				// requiereMantenimiento:false,
				// fecha_mantenimiento:''
			},
			editData:{},
			elegido:''
		}
		,methods:{
			getFormData() {
				var url=host+'BienActivo/get-catalogo-data-form';
				axios.get(url).then(response=>{
					if(response.status==200 && response.data.respuesta){
						this.catalogo=response.data.jsonCatalogoActivo.rows;
						this.empresas 		= response.data.jsonEmpresas.rows;
						this.departamento =	 response.data.jsonDepartamento.rows;
						this.usuario =		response.data.jsonUsuarios.rows;			
					}
				})
			}
			,loadModal(){
				$('.cmbCatalogo').selectpicker('refresh');
				$('.cmbEmpresa').selectpicker('refresh');
				$('.cmbDepartamento').selectpicker('refresh');
				$('.cmbUsuario').selectpicker('refresh');
	
				// $('#cmbCatalogo').on('change',function(){
				// 	if($(this).val() ==1){
				// 		$('.cmbDepartamento').show();
				// 		$('.cmbUsuario').show();
				// 	}
				// });
			},
			validateForm(){
				var response = false;
				if(!this.newActivo.descripcion || !this.newActivo.marca || !this.newActivo.modelo ||  !this.newActivo.serie ||
					 !this.newActivo.color || !this.newActivo.costo_recurso){
					swal("Revisión!", "Debes  de terminar de rellenar todos los campos.", "warning");
					return response;
				}
				if(!this.newActivo.garantia_mes){
					swal("Revisión!","Escriba cuanta garantia vigente tiene el activo.","warning");
					return response;
				}
				/* if(!this.newActivo.periodo_mant_estimado){
				// 	swal("Revisión!","Defina los dias que se pretende dar a mant.");
				 }*/

				//VALIDATION DE CATALOGO
					if(isNaN(this.newActivo.id_catalogo)) {
						swal("Revisión!", "Debes seleccionar un catalogo para continuar con el registro.", "warning");
						return response;
					}
					if(isNaN(this.newActivo.id_empresa)){
						swal("Revisión!","Debes seleccionar una empresa para continuar con el registro.","warning");
						return response;
					}
					if(isNaN(this.newActivo.id_departamento)){
						swal("Revisión!","Eliga un departamento para continuar","warning");
						return response;
					}

				var fileInput= document.getElementById('idFileActivo').files.length;
				if(!fileInput){
					swal("Revisión!","Sube alguna imagen de referencia al activo");
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
							this.newActivo.descripcion="",
							this.newActivo.marca="",
							this.newActivo.modelo="",
							this.newActivo.serie="",
							this.newActivo.color="",
							this.newActivo.fecha_compra="",
							this.newActivo.costo_recurso="",
							this.newActivo.garantia_mes="",
							this.newActivo.obsolencia="",
							this.newActivo.periodo_mant_estimado="",
							this.newActivo.id_empresa='',
							this.newActivo.id_departamento='',
							this.newActivo.id_usuario_asignado='',
							this.newActivo.id_catalogo='',
							//VHC
							this.newActivo.placa="",
							this.newActivo.tipo_vehiculo="",
							//EAC
							this.newActivo.serie_evaporadora="",
							this.newActivo.serie_condensadora="",
							this.newActivo.control_remoto=false,
							//HTC
							this.newActivo.fecha_entrega="",
							this.newActivo.imei="",
							this.newActivo.almacenamientoExterno="",
							//ECP
							this.newActivo.tipoEquipo="",
							this.newActivo.tipoMemRam="",
							this.newActivo.capacidadRam="",
							this.newActivo.tipoProcesador="",
							this.newActivo.marcaProcesador="",
							this.newActivo.capacidadProcesador="",
							this.newActivo.tipoHDD="",
							this.newActivo.capacidadHDD="",
							this.newActivo.tieneMonitor=false,
								this.newActivo.tamanioMonitor="",
								this.newActivo.colorMonitor="",
								this.newActivo.modeloMonitor="",
								this.newActivo.numParte="",
							this.newActivo.observaciones="",
							this.newActivo.fichero='',

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
		
					$('.cmbCatalogo').selectpicker('val', NuevoActivo.editData.id_tipo_activo);
					$('.cmbCatalogo').selectpicker('render');

					$('.cmbDepartamento').selectpicker('val', NuevoActivo.editData.id_departamento);
					$('.cmbDepartamento').selectpicker('render');
					if(NuevoActivo.editData.id_usuario!=0){
						$('.cmbUsuario').selectpicker('val', NuevoActivo.editData.id_usuario);
						$('.cmbUsuario').selectpicker('render');
					}

					var date = $('.txtFechaCompra').data('txtFechaCompra');
					//DATEPICKER
					$('.txtFechaCompra').bootstrapMaterialDatePicker({format: 'DD/MM/YYYY', weekStart : 1, clearButton: false, time:false, minDate : new Date()});
					if(date != null && date != '') {
						var arrDate = date.split("-");				
						$('.txtFechaCompra').bootstrapMaterialDatePicker('setDate', arrDate[2]+"/"+arrDate[1]+"/"+arrDate[0]);
					}

					var dateObsolecencia = $('.txtObsolecencia').data('txtObsolecencia');
					$('.txtObsolecencia').bootstrapMaterialDatePicker({format: 'DD/MM/YYYY', weekStart : 1, clearButton: false, time:false, minDate : new Date()});
					if(dateObsolecencia != null && dateObsolecencia != '') {
						var arrDateObsolencia = date.split("-");				
						$('.txtObsolecencia').bootstrapMaterialDatePicker('setDate', arrDateObsolencia[2]+"/"+arrDateObsolencia[1]+"/"+arrDateObsolencia[0]);
					}

					var fechaentrega = $('.txtFechaEntrega').data('txtFechaEntrega');
					$('.txtFechaEntrega').bootstrapMaterialDatePicker({format: 'DD/MM/YYYY', weekStart : 1, clearButton: false, time:false, minDate : new Date()});
					
					if(fechaentrega != null && fechaentrega != '') {
						var arrDateFechaEntrega = date.split("-");				
						$('.txtFechaEntrega').bootstrapMaterialDatePicker('setDate', arrDateFechaEntrega[2]+"/"+arrDateFechaEntrega[1]+"/"+arrDateFechaEntrega[0]);
					}
					this.loadModal();
				// 	this.editData.nombre=data[0].nombre;
				// 	this.editData.marca=data[0].marca;
				// 	this.editData.id_empresa=data[0].id_empresa;
				}).catch(error=>{
					console.log(error);
				});
			},
			update(){
				var formEditActivo=document.getElementById('formEditActivo');
				var formEditActivoData=new FormData(formEditActivo);
				
				var url=host+'BienActivo/'+this.editData.id_activo+'/update';
				axios.post(url,formEditActivoData).then(resp=>{
					if(resp.status==200 && resp.data.respuesta){
						$("#formEditActivo")[0].reset();
						$("#modalEditActivo").modal("hide");
						this.editData.descripcion="",
						this.editData.marca="",
						this.editData.modelo="",
						this.editData.serie="",
						this.editData.color="",
						this.editData.fecha_compra="",
						this.editData.costo_recurso="",
						this.editData.garantia_mes="",
						this.editData.obsolencia="",
						this.editData.periodo_mant_estimado="",
						this.editData.id_empresa='',
						this.editData.id_departamento='',
						this.editData.id_usuario='',
						this.editData.id_catalogo='',
						//VHC
						this.editData.placa="",
						this.editData.tipo_vehiculo="",
						//EAC
						this.editData.serie_evaporadora="",
						this.editData.serie_condensadora="",
						this.editData.control_remoto=false,
						//HTC
						this.editData.fecha_entrega="",
						this.editData.imei="",
						this.editData.almacenamientoExterno="",
						//ECP
						this.editData.tipoEquipo="",
						this.editData.tipoMemRam="",
						this.editData.capacidadRam="",
						this.editData.tipoProcesador="",
						this.editData.marcaProcesador="",
						this.editData.capacidadProcesador="",
						this.editData.tipoHDD="",
						this.editData.capacidadHDD="",
						this.editData.tieneMonitor=false,
							this.editData.tamanioMonitor="",
							this.editData.colorMonitor="",
							this.editData.modeloMonitor="",
							this.editData.numParte="",
						this.editData.observaciones="",
						this.editData.fichero='',
					
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
			,deleteActivo(param){
				var url=host+'BienActivo/'+param+'/delete';
				swal({
					title	: "Eliminar Activo",
					text	: "Estas seguro de eliminar un activo en el sistema.",
					type	: "warning",
					showCancelButton: true,
					cancelButtonText: 'Cancelar',
					confirmButtonText: 'Eliminar',
				}).then((result)=>{
					if(result){
						axios.post(url).then(resp=>{
							if(resp.status==200 && resp.data.respuesta){
								swal(resp.data.titulo, resp.data.mensaje, "success");
								$("#dtActivo").bootstrapTable('refresh');
							}else{
								// console.log(resp);
								swal(resp.data.titulo, resp.data.mensaje, "error");
							}
						}).catch(error=>{
							swal("Algo malo pasó!", error.resp.data, "error");
								// console.log(error);
						});
					}
				}).catch(swal.noop);
			}
		}
		,mounted(){
			this.getFormData();
		},
		watch:{
			// 'newActivo.id_catalogo':function(val){
			// 	console.log(val+"hola val");
			// 	if(val==1){
			// 		$('.cmbUsuario').selectpicker('refresh');
            //         $('.cmbDepartamento').selectpicker('refresh');
			// 	}
            // },
		}
	})
}