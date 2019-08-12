//CONFIGURACION DE HOST
var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');

var modalOpn = Vue.component('example-modal', {
	props: ['params', 'idOportunidad', 'modalTipo'],
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
			}
		},
		getDate(vencimientoFecha) {
			var date = moment(vencimientoFecha, "YYYY-MM-DD").fromNow();
			return "Vencimiento " + date;
		}
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
	}
});

if(document.getElementById('appOportunidades')) {
	var app1 = new Vue({
		el: '#appOportunidades',
		mounted() {
			this.idEmpresa = 1
			this.getOportunidades(this.idEmpresa);
		},
		data: {
			idEmpresa: null,
			totalAbiertos: "0.00",
			totalEnCurso: "0.00",
			totalRentas: "0.00",
			totalCerrados: "0.00",
			totalPerdidos: "0.00",
			opnData: [],
			ibmData: null,
			s3sData: null,
			r2aData: null,
			ucaData: null,
			ibmActive: true,
			s3sActive: false,
			r2aActive: false,
			ucaActive: false,
			activeClass: 'btn btn-default btn-lg waves-effect',
			inactiveClass: 'btn btn-indigo btn-lg waves-effect',
			token: null,
			tokenName: null,
		},
		methods: {
			formatNumber(num) {
				return num.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
			},
			getOportunidades(paramEmpresa) {
				var url = host + 'oportunidadesNegocios/' + paramEmpresa + '/get-oportunidades';
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
						this.opnData 		= {...response.data.dataAbiertos, ...response.data.dataEnCurso, ...response.data.dataRentas, ...response.data.dataCerrados, ...response.data.dataPerdidos};
						this.totalAbiertos 	= this.opnData.total_abiertos;
						this.totalEnCurso 	= this.opnData.total_en_curso;
						this.totalRentas 	= this.opnData.total_rentas;
						this.totalCerrados 	= this.opnData.total_cerrados;
						this.totalPerdidos 	= this.opnData.total_perdidos;
	
						this.setDataEmpresa(this.opnData);
					}
				});
			},
			changeEmpresa(paramEmpresa) {
				this.idEmpresa = paramEmpresa;
	
				switch(this.idEmpresa) {
					case 1:
						if(this.ibmData != null)
							this.opnData = this.ibmData;
						else
							this.getOportunidades(this.idEmpresa);
	
						this.ibmActive = true;
						this.s3sActive = false;
						this.r2aActive = false;
						this.ucaActive = false;
					break;
	
					case 2:
						if(this.s3sData != null)
							this.opnData = this.s3sData;
						else
							this.getOportunidades(this.idEmpresa);
	
						this.ibmActive = false;
						this.s3sActive = true;
						this.r2aActive = false;
						this.ucaActive = false;
					break;
	
					case 3:
						if(this.r2aData != null)
							this.opnData = this.r2aData;
						else
							this.getOportunidades(this.idEmpresa);
	
						this.ibmActive = false;
						this.s3sActive = false;
						this.r2aActive = true;
						this.ucaActive = false;
					break;
	
					case 4:
						if(this.ucaData != null)
							this.opnData = this.ucaData;
						else
							this.getOportunidades(this.idEmpresa);
	
						this.ibmActive = false;
						this.s3sActive = false;
						this.r2aActive = false;
						this.ucaActive = true;
					break;
	
				}
				
				this.totalAbiertos 	= this.opnData.total_abiertos;
				this.totalEnCurso 	= this.opnData.total_en_curso;
				this.totalRentas 	= this.opnData.total_rentas;
				this.totalCerrados 	= this.opnData.total_cerrados;
				this.totalPerdidos 	= this.opnData.total_perdidos;
			},
			setDataEmpresa(objResponse) {
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
			},
			getDataEmpresa(paramEmpresa) {
				var objResponse;
	
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
	
			}		
		}
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
			}
		},
		methods: {
			getFormData() {
				var url = host + 'cotizaciones/get-cotizacion-data-form';
				axios.get(url).then(response => {
					if(response.status == 200 && response.data.respuesta) {
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
							$select.append('<option value=' + item.id_contacto + ' data-subtext="'+ item.correo +'">' + item.contacto + '</option>');
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
			}
		},
		watch: {
			'newCotizacion.id_cliente': function(val) {
				if(!isNaN(val)) {
					this.getContactos(val);
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