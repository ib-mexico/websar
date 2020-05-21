//CONFIGURACION DE HOST
var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');

var modalOpn = Vue.component('example-modal', {
	props: ['params', 'idOportunidad', 'modalTipo','titleOpn'],
	template: '#opn-modal-template',
	mounted() {
		this.collection = this.params;
		this.tipo = this.modalTipo;
		this.dinamicTitle(this.modalTipo);
	},
	data() {
		return {
			collection: [],
			titulo: '',
			tipo: null,
		}
	},
	methods: {
		dinamicTitle(value) {
			switch(value) {
				case 1:
					this.titulo = 'Cotizaciones';
				break;

				case 2:
					this.titulo = 'Actividades';
				break;

				case 3:
					this.titulo = 'Ficheros';
				break;
				case 4:
					this.titulo='Llamada de calidad';
				break;
			}
		},
		getDate(vencimientoFecha) {
			var date = moment(vencimientoFecha, "YYYY-MM-DD").fromNow();
			return "Vencimiento " + date;
		},
		storeOpnCalidad(){
			/**Guardar un audio de calidad para las OPN de negocios */
			var formCalidad = document.getElementById('formOpnCalidad');
			var formCalidadData = new FormData(formCalidad);
			var url = host + "oportunidadesNegocios/"+this.idOportunidad+"/storeCalidad";
			axios.post(url,formCalidadData).then((resp) => {
				if (resp.status == 200 && resp.data.respuesta) {
					$("#formOpnCalidad")[0].reset();
					$("#opnModal"+this.idOportunidad).modal("hide");
					swal(resp.data.titulo, resp.data.mensaje, "success");
				}
				else{
					swal(resp.data.titulo, resp.data.mensaje, "error");
				}
			}).catch((error) => {
				swal("Algo malo pasó!",error.resp,"error")
			});
		},
	},
	watch: {
		modalTipo: function(val) {
			this.tipo = val;
			this.dinamicTitle(val);
			
		},
		params: function(val) {
			this.collection = val;
		}
	}
});

Vue.component('opn-card', {
	props: ['data', 'idOportunidad', 'tipoOportunidad', 'index','token'],
	template: '#opn-card-template',
	mounted() {
		this.identificador = this.idOportunidad;
		this.oportunidadTipo = this.tipoOportunidad;
		this.csrf = this.token;
		this.flag = this.index;
		this.getCotizacionbyOpn();
	},
	data() {
		return {
			identificador: null,
			oportunidadTipo: null,
			cotizaciones: null,
			ficheros: null,
			actividades: null,
			modalInfo: null,
			modalTipo: null,
			csrf: null,
			flag: null,
			oportunidad:null,
			/**dynamic button calidad */
			propiedades:[]
		}
	},
	methods: {
		getCotizaciones() {
			this.modalInfo = null;
			if(this.cotizaciones == null) {
				var url = host + 'oportunidadesNegocios/' + this.identificador + '/get-cotizaciones';
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.cotizaciones = response.data.dataCotizaciones.cotizaciones;
						this.modalInfo = this.cotizaciones;
					}
				});
			} else {
				this.modalInfo = this.cotizaciones;
			}
			
			this.modalTipo = 1;
		},
		getFicheros() {
			this.modalInfo = null;
			if(this.ficheros == null) {
				var url = host + 'oportunidadesNegocios/' + this.identificador + '/get-ficheros';
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.ficheros = response.data.dataFicheros.ficheros;
						this.modalInfo = this.ficheros;
					}
				});
			} else {
				this.modalInfo = this.ficheros;
			}

			this.modalTipo = 3;
		},
		getActividades() {
			this.modalInfo = null;
			if(this.actividades == null) {
				var url = host + 'oportunidadesNegocios/' + this.identificador + '/get-actividades';
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.actividades = response.data.dataActividades.actividades;
						this.modalInfo = this.actividades;
					}
				});
			} else {
				this.modalInfo = this.actividades;
			}

			this.modalTipo = 2;
		},
		/**method for get one oportunidad */
		getCalidad(){
			this.modalInfo = null;
			if (this.oportunidad == null) {
				var url=host + 'oportunidadesNegocios/' +this.identificador + '/get-oportunidad';
				axios.get(url).then((resp) => {
					this.oportunidad=resp.data.dataOportunidad.oportunidad[0];
					this.modalInfo=this.oportunidad;
				});
			} else {
				this.modalInfo=this.oportunidad;
			}
			this.modalTipo = 4;
			//DATEPICKER FECHA HORA DE LLAMADA DE CALIDAD
			$('.txtFechaHoraLlamada').bootstrapMaterialDatePicker({format: 'DD/MM/YYYY HH:mm', weekStart : 1, clearButton: true, time:true});

		},
		/**End getOportunidad */
		getCotizacionbyOpn(){
			var url=host+'oportunidadesNegocios/'+this.identificador+'/getCalidad';
			axios.get(url).then((resp) => {
				if (status == 200)
					if (resp.data.respuesta){
						return true
					}
					else{
						return false
					}
			}).catch((error) => {
				console.log("Algo salio mal", error);
			})
		},
		deleteOportunidad(param) {
			var url = host + 'oportunidadesNegocios/' + param + '/delete';

			swal({
				title	: "&iquest;Est&aacute;s seguro?",
				text	: "Una vez eliminada, la oportunidad no podra recuperarse.",
				type	: "warning",
				showCancelButton: true,
				cancelButtonText: 'Cancelar',
				confirmButtonText: 'Eliminar',
			}).then((result) => {
				if(result) {

					var data = {
						idOportunidad: this.identificador,
						_csrf: this.csrf
					}

					axios.post(url, data).then(response => {
						if(response.status == 200 && response.data.respuesta) {
							this.$parent.removeElementData(this.identificador, this.oportunidadTipo, this.flag);
							swal(response.data.titulo, response.data.mensaje,'success');
						} else {
							swal(response.data.titulo, response.data.mensaje,'error');
						}
					})
					.catch(error => {
                        swal("Algo malo pasó!", error.response.message, "error");
                    });																												
				}
			}).catch(swal.noop);
		}
	},
	watch: {
		index: function(val) {
			this.flag = val;
		}
	},
	components: {
		"example-modal": modalOpn,
	},
	computed: {
		porTipoOpn(){
			if(this.tipoOportunidad != 1 && this.tipoOportunidad!=5){
				/**OPN sin llamada de calidad */
				return true;
			}else
				/**OPN con llamadas de calidad */
				return false;
		},
		validarCalidad(){
			if (this.data.ficheroCalidad == true) {
				console.log("Retorno de verdadero");
				// this.propiedades.push({p_onclick:'getFicheros',icon:'library_music',title:'Revisar llamada de calidad'});
				return true
			}
			else{
				console.log("Retorno de falso");
				// this.propiedades.push({p_onclick:'getCalidad',icon:'call',title:'Registrar llamada de calidad'});
				return false
			}
		}
	},
});

if(document.getElementById('appOportunidades')) {
	var app1 = new Vue({
		el: '#appOportunidades',
		mounted() {
			this.idEmpresa = 1
			this.getempresa();
			// this.getOportunidades(this.idEmpresa);
		},
		data: {
			empresa : null,
			uca : false,
			isEmpty : null,
			idEmpresa: null,
			totalAbiertos: "0.00",
			totalEnCurso: "0.00",
			totalRentas: "0.00",
			totalCerrados: "0.00",
			totalPerdidos: "0.00",
			totalFinanza : "0.00",

			opnData: [],
			ibmData: null,
			s3sData: null,
			r2aData: null,
			ucaData: null,

			historicoIbmData : null,
			historicoS3sData : null,
			historicoR2aData : null,
			historicoUcaData : null,

			ibmActive: true,
			s3sActive: false,
			r2aActive: false,		
			ucaActive: false,

			historicoActive: false,

			historicoIbmActive : false,
			historicoS3sActive : false,
			historicoR2aActive : false,
			historicoUcaActive : false,
			
			activeClass: 'btn btn-default btn-lg waves-effect',
			inactiveClass: 'btn btn-indigo btn-lg waves-effect',
			token: null,
			tokenName: null,

			abiertoShow: false,
			enCursoShow: false,
			rentaShow: false,
			cerradoShow: false,
			perdidoShow: false,
			flag : null,
		},
		methods: {
			formatNumber(num) {
				return num.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
			},
			async getOportunidades(paramEmpresa) {
				var url = host + 'oportunidadesNegocios/' + paramEmpresa + '/get-oportunidades';
				await axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.opnData 		= {	...response.data.dataAbiertos.abiertos.length > 0 ? response.data.dataAbiertos : null,
												...response.data.dataEnCurso.en_curso.length > 0 ? response.data.dataEnCurso : null,
												...response.data.dataRentas.rentas.length > 0 ? response.data.dataRentas : null,
												...response.data.dataCerrados.cerrados.length > 0 ? response.data.dataCerrados : null, 
												...response.data.dataPerdidos.perdidos.length > 0 ? response.data.perdidos : null,
												...response.data.dataFinanza.financiamiento.length > 0 ? response.data.dataFinanza : null};
						this.totalAbiertos 	= this.opnData.total_abiertos;
						this.totalEnCurso 	= this.opnData.total_en_curso;
						this.totalRentas 	= this.opnData.total_rentas;
						this.totalCerrados 	= this.opnData.total_cerrados;
						this.totalPerdidos 	= this.opnData.total_perdidos;
						this.totalFinanza 	= this.opnData.total_finanza;
						this.setDataEmpresa(this.opnData);
					}
				});
				
			},

			getempresa(){
				var url = host + 'empresa/get-empresa';
				axios.get(url).then((resp) => {					
					if(resp.status == 200 && resp.data.respuesta){
						this.empresa = resp.data.jsonEmpresa.rows;
						this.uca = resp.data.opnUca;
					}
					/**Remover la empresa de UCA */
					if(this.uca == false){
						var idUca = 4;
						for (let index = 0; index < this.empresa.length; index++) {
							const element = this.empresa[index].id_empresa;
							if (element == idUca) {
								this.empresa.splice(index,1);
								break;
							}
							
						}
					}
				});
			},

			/**Se le considera para las OPN cerradas y perdidas del año pasado como historico */
			getOportunidadesHistorico(paramEmpresa) {
				
				var url = host + 'oportunidadesNegocios/' + paramEmpresa + '/get-oportunidades-Historico';
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.opnData 		= {	...response.data.dataCerrados.cerrados.length >0 ? response.data.dataCerrados : null,
												...response.data.dataPerdidos.perdidos.length >0 ? response.data.dataPerdidos : null};
						this.totalCerrados 	= this.opnData.total_cerrados;
						this.totalPerdidos 	= this.opnData.total_perdidos;
						this.setDataEmpresa(this.opnData);
					}
				});
			},
			/**End Historic */
			changeEmpresa(paramEmpresa) {
				this.idEmpresa = paramEmpresa;
				this.flag = false;
				switch(this.idEmpresa) {
					case 1:
						if(this.ibmData != null)
							this.opnData = this.ibmData;
						else
							this.getOportunidades(this.idEmpresa);
/* 						this.ibmActive = true;
						this.s3sActive = false;
						this.r2aActive = false;
						this.ucaActive = false;
						this.historicoActive = false; */
						// Se activan botones de cada empresa en version anterior del SAR.
						// document.getElementById('historicos').style.display = "none";
					break;
	
					case 2:
						if(this.s3sData != null)
							this.opnData = this.s3sData;
						else
							this.getOportunidades(this.idEmpresa);
					break;
	
					case 3:
						if(this.r2aData != null)
							this.opnData = this.r2aData;
						else
							this.getOportunidades(this.idEmpresa);
						// this.ibmActive = false;
						// this.s3sActive = false;
						// this.r2aActive = true;
						// this.ucaActive = false;
						// this.historicoActive = false;
					break;
	
					case 4:
						if(this.ucaData != null)
							this.opnData = this.ucaData;
						else
							this.getOportunidades(this.idEmpresa);
					break;
	
				}
				
				this.totalAbiertos 	= this.opnData.total_abiertos;
				this.totalEnCurso 	= this.opnData.total_en_curso;
				this.totalRentas 	= this.opnData.total_rentas;
				this.totalCerrados 	= this.opnData.total_cerrados;
				this.totalPerdidos 	= this.opnData.total_perdidos;
			},
			changeEmpresaHistorico(paramEmpresa){
				this.idEmpresa = paramEmpresa;
				this.flag = true;
				switch(this.idEmpresa){
					case 1:
						if (this.historicoIbmData !=null){
							this.totalCerrados 	= this.historicoIbmData.total_cerrados;
							this.totalPerdidos 	= this.historicoIbmData.total_perdidos;
							this.opnData = this.historicoIbmData;
						}else
							this.getOportunidadesHistorico(this.idEmpresa);
						// this.ibmActive = false;
						// this.s3sActive = false;
						// this.r2aActive = false;
						// this.ucaActive = false;
						// this.historicoIbmActive =  true;
						// this.historicoS3sActive = false;	
						// this.historicoR2aActive = false;	
						// this.historicoUcaActive = false;
						/**Estos activan botones de cada empresa, en version anterior, en caso caso */
						break;
					case 2:
						if(this.historicoS3sData != null){
							this.opnData = this.historicoS3sData;
							this.totalCerrados 	= this.historicoS3sData.total_cerrados;
							this.totalPerdidos 	= this.historicoS3sData.total_perdidos;
						}else
							this.getOportunidadesHistorico(this.idEmpresa);
						break;
					case 3:
						if(this.historicoR2aData != null){
							this.opnData = this.historicoR2aData
							this.totalCerrados 	= this.historicoR2aData.total_cerrados;
							this.totalPerdidos 	= this.historicoR2aData.total_perdidos;
						}else
							this.getOportunidadesHistorico(this.idEmpresa);
						break;
					case 4:
						if(this.historicoUcaData != null){
							this.opnData = this.historicoUcaData
							this.totalCerrados 	= this.historicoUcaData.total_cerrados;
							this.totalPerdidos 	= this.historicoUcaData.total_perdidos;
						}else
							this.getOportunidadesHistorico(this.idEmpresa);
						break;
				}
			},
			setDataEmpresa(objResponse) {
				
				console.log( "valor de flag " +this.flag)
				if(this.flag == true){
					switch (this.idEmpresa) {
						case 1:
							this.historicoIbmData = objResponse;
							this.totalCerrados 	= this.historicoIbmData.total_cerrados;
							this.totalPerdidos 	= this.historicoIbmData.total_perdidos;
							break;
						case 2:
							this.historicoS3sData = objResponse;
							this.totalCerrados 	= this.historicoS3sData.total_cerrados;
							this.totalPerdidos 	= this.historicoS3sData.total_perdidos;
							break;
						case 3:
							this.historicoR2aData = objResponse;
							this.totalCerrados 	= this.historicoR2aData.total_cerrados;
							this.totalPerdidos 	= this.historicoR2aData.total_perdidos;
							break;
						case 4:
							this.historicoUcaData = objResponse;
							this.totalCerrados 	= this.historicoUcaData.total_cerrados;
							this.totalPerdidos 	= this.historicoUcaData.total_perdidos;
							break;
					}
				}else{
					switch(this.idEmpresa) {
						case 1:
							this.ibmData = objResponse;
						break;
		
						case 2:
							this.s3sData = objResponse;
						break;
		
						case 3:
							this.r2aData = objResponse;
						break;
		
						case 4:
							this.ucaData = objResponse;
						break;
					}
				}
				
			},
			getDataEmpresa(paramEmpresa) {
				var objResponse;
				if(this.flag == true)
					switch(paramEmpresa) {
						case 1:
							objResponse = this.historicoIbmData;
						break;
		
						case 2:
							objResponse = this.historicoS3sData;
						break;
		
						case 3:
							objResponse = this.historicoR2aData;
						break;
		
						case 4:
							objResponse = this.historicoUcaData;
						break;
					}
				else
					switch(paramEmpresa) {
						case 1:
							objResponse = this.ibmData;
						break;
		
						case 2:
							objResponse = this.s3sData;
						break;
		
						case 3:
							objResponse = this.r2aData;
						break;
		
						case 4:
							objResponse = this.ucaData;
						break;
					}
				
				return objResponse;
			},
			removeElementData(paramOpn, paramTipo, index) {
				var empresaData = this.getDataEmpresa(this.idEmpresa);
				var total = 0;
				var valorRestar = 0;
	
				switch(paramTipo) {
					case 1:
						var totalParse = parseFloat(empresaData.total_abiertos.split(",").join(""));
	
						empresaData.abiertos.filter(function(value) {
							if(value.id_oportunidad == paramOpn) {
								valorRestar = parseFloat(value.ingreso_estimado.split(",").join(""));
							}
						});
	
						total = totalParse - valorRestar;
	
						empresaData.abiertos.splice(index, 1);
						empresaData.total_abiertos = this.formatNumber(total);
						this.totalAbiertos = empresaData.total_abiertos;
	
						this.opnData = empresaData;
						this.setDataEmpresa(this.opnData);
					break;
	
					case 2:
						var totalParse = parseFloat(empresaData.total_en_curso.split(",").join(""));
	
						empresaData.en_curso.filter(function(value) {
							if(value.id_oportunidad == paramOpn) {
								valorRestar = parseFloat(value.ingreso_estimado.split(",").join(""));
							}
						});
	
						total = totalParse - valorRestar;
	
						empresaData.en_curso.splice(index, 1);
						empresaData.total_en_curso = this.formatNumber(total);
						this.totalEnCurso = empresaData.total_en_curso;
	
						this.opnData = empresaData;
						this.setDataEmpresa(this.opnData);
					break;
	
					case 3:
						var totalParse = parseFloat(empresaData.total_rentas.split(",").join(""));
	
						empresaData.rentas.filter(function(value) {
							if(value.id_oportunidad == paramOpn) {
								valorRestar = parseFloat(value.ingreso_estimado.split(",").join(""));
							}
						});
	
						total = totalParse - valorRestar;
	
						empresaData.rentas.splice(index, 1);
						empresaData.total_rentas = this.formatNumber(total);
						this.totalRentas = empresaData.total_rentas;
	
						this.opnData = empresaData;
						this.setDataEmpresa(this.opnData);
					break;
	
					case 4:
						var totalParse = parseFloat(empresaData.total_cerrados.split(",").join(""));
	
						empresaData.cerrados.filter(function(value) {
							if(value.id_oportunidad == paramOpn) {
								valorRestar = parseFloat(value.ingreso_estimado.split(",").join(""));
							}
						});
	
						total = totalParse - valorRestar;
	
						empresaData.cerrados.splice(index, 1);
						empresaData.total_cerrados = this.formatNumber(total);
						this.totalCerrados = empresaData.total_cerrados;
	
						this.opnData = empresaData;
						this.setDataEmpresa(this.opnData);
					break;
	
					case 5:
						var totalParse = parseFloat(empresaData.total_perdidos.split(",").join(""));
	
						empresaData.perdidos.filter(function(value) {
							if(value.id_oportunidad == paramOpn) {
								valorRestar = parseFloat(value.ingreso_estimado.split(",").join(""));
							}
						});
	
						total = totalParse - valorRestar;
	
						empresaData.perdidos.splice(index, 1);
						empresaData.total_perdidos = this.formatNumber(total);
						this.totalPerdidos = empresaData.total_perdidos;
	
						this.opnData = empresaData;
						this.setDataEmpresa(this.opnData);
					break;
				}
	
			},
			format2(n, currency) {
                return currency + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
            },		
		},
		watch: {
			'opnData.abiertos' : function () {
				if (this.opnData.abiertos !=  undefined) {
					suma = 0;
					for (var i = 0; i < this.opnData.abiertos.length; i++) {
						suma = suma +	parseFloat(this.opnData.abiertos[i].ingreso);		
					}
					this.totalAbiertos = this.format2(suma , '$ ');					
				}
			},
			'opnData.en_curso' : function () {
				if (this.opnData.en_curso != undefined) {
					ingresoEstimado = 0;
					for (var i = 0; i < this.opnData.en_curso.length; i++) {
						ingresoEstimado = ingresoEstimado +	parseFloat(this.opnData.en_curso[i].ingreso);		
					}
					this.totalEnCurso = this.format2(ingresoEstimado , '$ ');					
				}
			},
			'opnData.rentas' : function () {
				if (this.opnData.rentas != undefined) {
					ingresoEstimado = 0;
					for (var i = 0; i < this.opnData.rentas.length; i++) {
						ingresoEstimado = ingresoEstimado +	parseFloat(this.opnData.rentas[i].ingreso);		
					}
					this.totalRentas = this.format2(ingresoEstimado , '$ ');					
				}
			},
			'opnData.cerrados' : function () {
				if (this.opnData.cerrados != undefined) {
					ingresoEstimado = 0;
					for (var i = 0; i < this.opnData.cerrados.length; i++) {
						ingresoEstimado = ingresoEstimado +	parseFloat(this.opnData.cerrados[i].ingreso);		
					}
					this.totalCerrados = this.format2(ingresoEstimado , '$ ');					
				}
			},
			'opnData.perdidos' : function () {
				if (this.opnData.perdidos != undefined) {
					ingresoEstimado = 0;
					for (var i = 0; i < this.opnData.perdidos.length; i++) {
						ingresoEstimado = ingresoEstimado +	parseFloat(this.opnData.perdidos[i].ingreso);		
					}
					this.totalPerdidos = this.format2(ingresoEstimado , '$ ');					
				}
			},
			'opnData.financiamiento' : function () {
				if (this.opnData.financiamiento != undefined) {
					ingresoEstimado = 0;
					for (var i = 0; i < this.opnData.financiamiento.length; i++) {
						ingresoEstimado = ingresoEstimado +	parseFloat(this.opnData.financiamiento[i].ingreso);		
					}
					this.totalFinanza = this.format2(ingresoEstimado , '$ ');					
				}
			}
		},
	});
}

if(document.getElementById('appCotizaciones')) {
	var app3 = new Vue({
		el: '#appCotizaciones',
		mounted() {
			this.getFormData();
		},
		data: {
			rolcotizacionAdmin: false,
			usuarioActivo: null,
			usuarios: [],
			grupos: [],
			empresas: [],
			formasPagos: [],
			monedas: [],
			clientes: [],
			contactos: [],
			proyectos: [],
			boms: [],
			rentas: [],
			newCotizacion: {
				id_usuario: 'default',
				id_empresa: 'default',
				concepto: '',
				fecha_solicitud: null,
				id_cliente: 'default',
				id_contacto: null,
				ubicacion: '',
				entrega: '',
				tiempo_entrega: '',
				vigencia_precios: '',
				id_moneda: 1,
				id_forma_pago: 1,
				dias_credito: 0,
				id_usuario_vendedor: 'default',
				id_usuario_implementador: 'default',
				rdTipoCotizacion: 'normal',
				condiciones: '',
				observaciones: '',
				chkVenta: false,
				chkImplementa: false
			},
			dataCotizacion:{}
		},
		methods: {
			getFormData() {
				var url = host + 'cotizaciones/get-cotizacion-data-form';
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.proyectos		= response.data.jsonProyectos.rows;
						this.boms			= response.data.jsonBoms.rows;
						this.rentas			= response.data.jsonRentas.rows;
						this.usuarios 		= response.data.jsonUsuarios.rows;
						this.empresas 		= response.data.jsonEmpresas.rows;
						this.formasPagos 	= response.data.jsonFormasPagos.rows;
						this.monedas 		= response.data.jsonMonedas.rows;
						this.clientes 		= response.data.jsonClientes.rows;
						this.grupos 		= response.data.jsonUsuariosGrupos.rows;						
					}
				});
			},
			loadModal() {
				if(this.rolcotizacionAdmin) {
					$('#cmbUsuario').selectpicker('refresh');
				}

				$('.cmbEmpresa').selectpicker('refresh');
				$('.cmbCliente').selectpicker('refresh');
				$('.cmbMoneda').selectpicker('refresh');
				$('.cmbFormaPago').selectpicker('refresh');
				$('.cmbVendedor').selectpicker('refresh');
				$('.cmbImplementador').selectpicker('refresh');
				$('.cmbClienteContacto').selectpicker('refresh');
			},
			getContactos(paramCliente) {
				var url = host + 'clientes/get-contactos/' + paramCliente;
				var $select = $('#cmbClienteContacto');
				
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.contactos 	= response.data.jsonContactos.rows;
						$select.find('option').remove(); 

						this.contactos.forEach(item => {
							$select.append('<option value=' + item.id_contacto + ' data-subtext="'+ item.puesto +'">' + item.contacto + '</option>');
						});

						if(this.contactos.length >= 1) {
							this.newCotizacion.id_contacto = parseInt(this.contactos[0].id_contacto);
						}

						$('.cmbClienteContacto').selectpicker('refresh');
					}
				});
			},
			validateForm() {
				var response = false;

				//VALIDATION OF USER
				if(this.rolcotizacionAdmin) {
					if(isNaN(this.newCotizacion.id_usuario)) {
						swal("Revisión!", "Debes seleccionar un usuario para continuar con la cotizacion.", "warning");
						return response;
					}
				}

				//VALIDATION OF CHECKBOX SELL
				if(this.newCotizacion.chkVenta) {
					if(isNaN(this.newCotizacion.id_usuario_vendedor)) {
						swal("Revisión!", "Debes seleccionar un vendedor para continuar con la cotizacion.", "warning");
						return response;
					}
				}

				//VALIDATION OF CHECKBOX IMPLEMENTATION
				if(this.newCotizacion.chkImplementa) {
					if(isNaN(this.newCotizacion.id_usuario_implementador)) {
						swal("Revisión!", "Debes seleccionar un implementador para continuar con la cotizacion.", "warning");
						return response;
					}
				}

				//VALIDATION OF CLIENT AND CONTACTS
				if((isNaN(this.newCotizacion.id_cliente) || isNaN(this.newCotizacion.id_contacto)) 
					|| (this.newCotizacion.id_cliente == '' || this.newCotizacion.id_contacto == '')) {
					swal("Revisión!", "El campo de cliente y contacto debe tener informacion valida.", "warning");
					return response;
				}

				//VALIDATION OF DAYS OF CREDIT
				if(isNaN(this.newCotizacion.dias_credito)) {
					swal("Revisión!", "El campo de dias de credito debe contener un valor numerico.", "warning");
					return response;
				}

				//VALIDATION OF COMPANY
				if(isNaN(this.newCotizacion.id_empresa)) {
					swal("Revisión!", "Debes seleccionar una empresa para continuar con la cotizacion.", "warning");
					return response;
				}

				return response = true;
			},
			createCotizacion() {
				if(this.validateForm()) {

					var formCotizacion = document.getElementById('formCotizacion');
					var formCotizacionData = new FormData(formCotizacion)
					var url = host + 'cotizaciones/storeAJAX';

					axios.post(url, formCotizacionData).then(response => {                        
						if(response.status == 200 && response.data.respuesta) {
		
							//CLEAN FORM
							$("#formCotizacion")[0].reset();
							$("#modalNuevaCotizacion").modal("hide");

							this.newCotizacion.id_usuario = 'default';							
							this.newCotizacion.id_empresa = 'default';
							this.newCotizacion.concepto = '';
							this.newCotizacion.id_cliente = 'default';
							this.newCotizacion.id_contacto = null;
							this.newCotizacion.ubicacion = '';
							this.newCotizacion.entrega = '';
							this.newCotizacion.tiempo_entrega = '';
							this.newCotizacion.vigencia_precios = '';
							this.newCotizacion.id_moneda = 1;
							this.newCotizacion.id_forma_pago = 1;
							this.newCotizacion.dias_credito = 0;
							this.newCotizacion.id_usuario_vendedor = 'default';
							this.newCotizacion.id_usuario_implementador = 'default';
							this.newCotizacion.rdTipoCotizacion = 'normal';
							this.newCotizacion.condiciones = '';
							this.newCotizacion.observaciones = '';
							this.newCotizacion.chkVenta = false;
							this.newCotizacion.chkImplementa = false;
							
							//CLEAN CONTACT COMBOBOX
							$('#cmbClienteContacto').find('option').remove(); 
		
							swal(response.data.titulo, response.data.mensaje, "success");
							$("#dtCotizaciones").bootstrapTable('refresh');
						}
						else {
							swal(response.data.titulo, response.data.mensaje, "error");
						}
					})
					.catch(error => {
						swal("Algo malo pasó!", error.response.data, "error");
					});
				} else {
					console.log("Error en validacion de FORM");
				}
			},

			obtenerCotizacion(idCotizacion){
                var formCalidad = document.getElementById('formCalidad');
                var formCalidadData = new FormData(formCalidad);
                var url = host + "cotizaciones/getCotizacionUnique/" + idCotizacion;
                axios.get(url, formCalidadData).then(resp => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.dataCotizacion = resp.data.jsonCotizacionUnique.jsonCotizacionSeleccionado[0];
                    }
                    console.log(this.dataCotizacion);
                }).catch((error) => {
                    console.log("Algo ha salido mal ",error);
                })
			},
			async createCalidad(){
				var formCalidad = document.getElementById('formCalidad');
                var formCalidadData = new FormData(formCalidad);
				var url = host + "cotizaciones/"+this.dataCotizacion.idCotizacion+"/calidad";

				await axios.post(url,formCalidadData).then((resp) => {
					if (resp.status == 200 && resp.data.respuesta) {
						$("#formCalidad")[0].reset();
						$("#modalCalidad").modal("hide");
						this.dataCotizacion='';
						swal(resp.data.titulo, resp.data.mensaje, "success");
						$("#dtCotizaciones").bootstrapTable('refresh');
					}
					else{
						swal(resp.data.titulo, resp.data.mensaje, "error");
					}
				}).catch((error) => {
					swal("Algo malo pasó!",error.resp,"error")
				});
			}
		},
		watch: {
			'newCotizacion.id_cliente': function(val) {
				if(!isNaN(val)) {
					this.getContactos(val);
					let approved = this.clientes.filter(data => data.id_cliente == val);
					this.newCotizacion.ubicacion = approved[0].direccion;
				}
			},
			'newCotizacion.dias_credito': function(val) {
				if(val == '') {
					this.newCotizacion.dias_credito = 0;
				}
			},
		}
	});
}

if(document.getElementById('appCotizacionesPartidas')) {
	var app2 = new Vue({
		el: '#appCotizacionesPartidas',
		data: {
			idCotizacion: null,
			newPartida: {
				txtNumeroParte: '',
				txtTiempoEntrega: 1,
				txtDescripcion: '',
				txtCantidad: 1,
				txtPrecioUnitario: 0,
				txtDescuento: 0,
			},
			total: 0,
		},
		watch: {
			'newPartida.txtTiempoEntrega': function(val) {
				if(val == '') {
					this.newPartida.txtTiempoEntrega = 0;
				}
			},
			'newPartida.txtCantidad': function(val) {
				this.calculate();
			},
			'newPartida.txtPrecioUnitario': function(val) {
				this.calculate();
			},
			'newPartida.txtDescuento': function(val) {
				this.calculate();
			},
		},
		methods: {
			calculate() {
					var cantidad = parseInt(this.newPartida.txtCantidad);
					var precio = parseFloat(this.newPartida.txtPrecioUnitario);
					var descuento = parseFloat(this.newPartida.txtDescuento);
					var total;
	
					if(cantidad > 0) {
						total = (precio - ((descuento / 100) * precio)) * cantidad;
						this.total = ((isNaN(total)) ? 0 : total);
					}
			},
			createPartida() {
				if(!isNaN(this.newPartida.txtCantidad) && !isNaN(this.newPartida.txtPrecioUnitario) 
					&& !isNaN(this.newPartida.txtDescuento) && !isNaN(this.newPartida.txtTiempoEntrega)) {
					var formPartida = document.getElementById('formPartida');
					var formPartidaData = new FormData(formPartida)
					var url = host + 'cotizaciones/' + this.idCotizacion + '/partidas';
		
					axios.post(url, formPartidaData).then(response => {                        
						if(response.status == 200 && response.data.respuesta) {
		
							$("#formPartida")[0].reset();
							$("#partidaModal").modal("hide");
		
							this.newPartida.txtNumeroParte = '';
							this.newPartida.txtTiempoEntrega = 1;
							this.newPartida.txtDescripcion = '';
							this.newPartida.txtCantidad = 1;
							this.newPartida.txtPrecioUnitario = 0;
							this.newPartida.txtDescuento = 0;
		
							swal(response.data.titulo, response.data.mensaje, "success");
							location.reload();
						}
						else {
							swal(response.data.titulo, response.data.mensaje, "error");
						}
					})
					.catch(error => {
						swal("Algo malo pasó!", error.response.data, "error");
					});
				} else {
					swal("Revisión!", "Los campos de cantidades unicamente son para numeros.", "warning");
				}
			}
		}
	});
}

if(document.getElementById("appOPN")){
    var opn = new Vue({
        el : "#appOPN",
        mounted() {
            this.obtenerUsuario();
        },
        data: {
            formInputs : [],
            idOportunidad : null,
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
                e.preventDefault();   
            }
        },

    })
}