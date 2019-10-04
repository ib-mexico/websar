//CONFIGURACION DE HOST
var host = "http://localhost:8080/WebSar/controlPanel/";

// //HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');

var proveedor= Vue.component('proveedor',{
    props:['idServicio'],
    template:`#templateproveedor`,
})



if(document.getElementById('appDetalle')){
    var detalleActivo=new Vue({
        el:"#appDetalle",
        components:{
            'proveedor': proveedor,
        },
        mounted(){
            this.getFormData();
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
            ,datafiltrado:[]
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

            ObtenerIdServicio(index){

                // this.servicio=this.servicio.filter(x=> !x.checked);
                // alert(index +"valor de chekbox");
                if($('.checkServicio').is(':checked')){
                    alert("seleccionado"+index);
                   this.filtrado(index);
                }
                this.datafiltrado= index;

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