//CONFIGURACION DE HOST
var host = "http://localhost:8080/WebSar/controlPanel/";

// //HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

//CONFIGURACION MOMENT JS
moment.locale('es');

if(document.getElementById('appDetalle')){
    var detalleActivo=new Vue({
        el:"#appDetalle",
        mounted(){
            this.getFormData();
        },
        data:{
            catalogo:[],
            recursoActivo:[],
            fichero:[],
            newDetalle:{
                id_activo_mobiliario:'',
                id_catalogo:'',
                observaciones:'',
                titulo:'',
                urlfichero:'',
            }
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
                            $select.append('<option value='+item.id_activo_mobiliario+'>'+item.descripcion_completa+'</option>');
                        });
                        if(this.recursoActivo.length >= 1) {
                            this.newDetalle.id_activo_mobiliario = parseInt(this.recursoActivo[0].id_activo_mobiliario);
                        }
                        $('.cmbRecurso').selectpicker('refresh');
                    }
                });
            },
            getFicheroLstActivo(paramIdActivo){
               var url=host+"DetalleMant/get-fichero/"+paramIdActivo;
               axios.get(url).then(resp=>{
                   if(resp.status==200 && resp.data.respuesta){
                       this.fichero=resp.data.jsonFichero.rows[0];
                       urlfijo="http://localhost:8080/WebSar/ficheros/activoFijos/";
                       console.log(this.fichero.url_img_activo);
                       this.newDetalle.urlfichero=urlfijo+(this.fichero.url_img_activo);
                   }
               })
            }
        },
        watch:{
            'newDetalle.id_catalogo':function(val){
                if(!isNaN(val)) {
                    $('.cmbRecurso').selectpicker('refresh');
					this.getRecursoCat(val);
				}
            },
            'newDetalle.id_activo_mobiliario': function(val){
                if(!isNaN(val)){
                    this.getFicheroLstActivo(val);
                }
            }
        }
    });
}