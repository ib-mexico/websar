//CONFIGURACION DE HOST
var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');

Vue.component('proveedor',{
    props:['idservicio'],
    template:`#templateproveedor`,
})

if(document.getElementById('appDetalle')){
    var detalleActivo=new Vue({
        el:"#appDetalle",
 
        mounted(){
            this.getFormData();
            // this.getProveedorServicio(1,2);
        },
        data:{
            catalogo:[],
            recursoActivo:[],
            fichero:[],
            servicio:[],
            checked:false,
            gastoAproximado:[],
            totalgasto:'',
            newDetalle:{
                id_activo_mobiliario:'',
                id_catalogo:'',
                observaciones:'',
                titulo:'',
                urlfichero:'',
            }
            ,datafiltrado:[],
            proveedorServicio:[]
        },
        methods:{
            getFormData(){
                var url=host+'DetalleMant/get-recurso-data-form';
                axios.get(url).then(resp=>{
                    if(resp.status==200 && resp.data.respuesta){
                        this.catalogo=resp.data.jsonCatalogoActivo.rows;
                        // this.recursoActivo=resp.data.jsonRecursoActivo.rows;
                        // this.fichero=resp.data.jsonRecursoFichero.rows;
                    }
                }).catch(resp=>{
                    console.log(resp);
                });
            },
            loadModal(){
                $('#cmbCatalogo').selectpicker('refresh');
                $('.cmbRecurso').selectpicker('refresh');
            },
            validateForm(){

            },
            getRecursoCat(paramCatalogo){
                var url=host+"BienActivo/get-activos/"+paramCatalogo;
                var $select=$('#cmbRecurso');
                axios.get(url).then(resp=>{
                    if(resp.status==200 && resp.data.respuesta){
                        this.recursoActivo=resp.data.jsonActivoCatalogo.rows;
                        $select.find('option').remove();

                        this.recursoActivo.forEach(item => {
                            $select.append('<option value='+item.id_recurso_activo+'>'+item.descripcion_completa+'</option>');
                        });
                        if(this.recursoActivo.length >= 1) {
                            this.newDetalle.id_activo_mobiliario = parseInt(this.recursoActivo[0].id_activo_mobiliario);
                        }
                        $('.cmbRecurso').selectpicker('refresh');
                    }
                });
            },
            getServicioIdTipoActivo(paramIdTipo){
                var url=host+"DetalleMant/get-servicio/"+paramIdTipo;
                axios.get(url).then(resp=>{
                    if(resp.status==200 && resp.data.respuesta){
                        this.servicio=resp.data.jsonServicio.rows;
                    }
                });
            },
            getFicheroLstActivo(paramIdActivo){
               var url=host+"DetalleMant/get-fichero/"+paramIdActivo;
               axios.get(url).then(resp=>{
                   if(resp.status==200 && resp.data.respuesta){
                       this.fichero=resp.data.jsonFichero.rows[0];
                       urlfijo="http://localhost:8080/WebSar/ficheros/activoFijos/";
                       alert(this.fichero.url_img_activo);
                       console.log(this.fichero.url_img_activo);
                       this.newDetalle.urlfichero=urlfijo+(this.fichero.url_img_activo);
                   }
               })
            },

            async getProveedorServicio(paramIdActivo, paramIdSevicio) {
                var url=host+"DetalleMant/get-proveedor/"+paramIdActivo+"/"+paramIdSevicio;
                await axios.get(url).then(resp=>{
                    if(resp.status==200){
                        this.proveedorServicio = [...resp.data.jsonServicioProveedor.rows];
                    }
                });
                 return this.proveedorServicio;
            },

            async ObtenerIdServicio(index){
                if($('#'+index+'').is(':checked')==true){
                     valor = $('#'+index+'').val();  
                    // idTipoActivo=$('select').find('option:selected').val();
                    idTipoActivo=$( "select#cmbCatalogo option:checked" ).val();
                    this.proveedorServicio = await this.getProveedorServicio(idTipoActivo,index);
                    if(this.proveedorServicio.length<=0){
                        // alert(this.proveedorServicio.length);
                    }else{
                        this.datafiltrado.push(this.proveedorServicio);
                    }
                }else
                    if($('#'+index+'').is(':checked')==false){
                        for ( i = 0; i < this.datafiltrado.length; i++) {
                            for ( j = 0; j < this.datafiltrado[i].length; j++) {
                                alert(this.datafiltrado[i][j].id_servicio+ "  ->idservicio");
                                if(this.datafiltrado[i][j].id_servicio===index){
                                    this.datafiltrado.splice(i,1);
                                }
                                j=this.datafiltrado[i].length;
                                alert(j+"valor de j");
                            }  
                        }

                        // for ( i = 0; i < this.datafiltrado.length; i++) {
                        //     alert(this.datafiltrado.length +"1er for");
                        //     for (j = 0; j < this.datafiltrado[i].length; j++) {
                        //         alert(this.datafiltrado[i].length +"2do for");
                        //         if (this.datafiltrado[i][j].id_servicio===index) {
                        //             this.datafiltrado.splice(i,1);                                   
                        //         }
                        //         j=this.datafiltrado[i].length;
                        //     }
                        //     this.datafiltrado.splice(i,1);                                   
                        //     i--;
                        // }
                    }
            },

            createActivo(){
                var formActivo=document.getElementById('formActivo');
				var formActivoData=new FormData(formActivo);
                var url=host+'DetalleMant/storeAjaxServicioProveeedor';
                axios.post(url,formActivoData).then(response=>{
                    if(response.status==200 && response.data.respuesta){
                        $("#formActivo")[0].reset();
                        $("#modalNuevoActivo").modal("hide");
                        
                        swal(response.data.titulo, response.data.mensaje, "success");
                        $("#dtDetalle").bootstrapTable('refresh');
                    }else{
                        console.log(response);
						swal(response.data.titulo, response.data.mensaje, "error");
                    }
                }).catch(error=>{
                    swal("Algo malo pasó!", error.response.data, "error");
                    console.log(error);
                });
              
            }

            },

            
        watch:{
            'newDetalle.id_catalogo':function(val){
                if(!isNaN(val)) {
                    $('.cmbRecurso').selectpicker('refresh');
                    this.getRecursoCat(val);
                    this.getServicioIdTipoActivo(val);
				}
            },
            'newDetalle.id_activo_mobiliario': function(val){
                if(!isNaN(val)){
                    this.getFicheroLstActivo(val);
                }
            },
            "gastoAproximado": function(){
                
                if(!this.gastoAproximado.isNaN){
                    suma=0;
                    this.gastoAproximado.forEach(function(element){
                        suma+=element;
                    })
                    this.totalgasto=suma;
                }
            }

        }
    });
}