var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

if(document.getElementById("appIndicadores")){
    var indicadores=new  Vue({
        el:"#appIndicadores",
        mounted() {
            this.getDataEjecutivo();
        },
        data:{
            title:"INDICADORES DE PRODUCCIÓN",
            idUsuario:null,
            dataProduccion:[],
            dataEjecutivo:[],
            usuarioObtenido:''
        },
        methods: {
            loadComponent(){
                $('.cmbEjecutivo').selectpicker('refresh');
            },
            validateForm(){
                var response=false;
                
                if((this.idUsuario) == null || this.idUsuario == 'selected') {
                    swal("Revisión!", "Debes seleccionar un Ejecutivo para continuar con los indicativos.", "warning");
                    return response;
                }
                console.log(response)
                return response=true;
            },
            async getDataProduccion(paramEjecutivo, paramFechaInicio, paramFechaFin){
                $("#bar_chart").empty();
                this.dataProduccion = [];

                $('[data-toggle="tooltip"]').tooltip()
                var url=host+"Indicadores/get-indicadores-produccion/"+paramEjecutivo+"/"+paramFechaInicio+"/"+paramFechaFin;
                if(this.validateForm()){
                    await axios.get(url).then((resp) => {
                        this.dataProduccion.push(resp.data.jsonIndicadores.jsonCotNuevas.jsonCotizacionesNuevas[0]);
                        this.dataProduccion.push(resp.data.jsonIndicadores.jsonCotAprobadas.jsonCotizacionesAceptadas[0]);
                        this.dataProduccion.push(resp.data.jsonIndicadores.jsonCotFacturadas.jsonCotizacionesFacturadas[0]);
                        this.dataProduccion.push(resp.data.jsonIndicadores.jsonCotCobradasMas90.jsonCotCobradasMas90Dias[0]);
                        this.dataProduccion.push(resp.data.jsonIndicadores.jsonCotCobradasMenos90.jsonCotCobradasMenos90Dias[0]);
                        idGrafo= document.getElementById("bar_chart");
                        this.getMorris('bar',idGrafo, this.dataProduccion);
                        this.obtenerUsuarioSeleccionado(paramEjecutivo);
                    }).catch((error) => {
                        console.log(error)
                    });
                }
            },

            getMorris(type,element,value){
                console.log(value)
                var data=this.dataProduccion;
                if (type === 'bar') {
                    Morris.Bar({
                        element: element,    
                        data: [
                            {t:data[0].titulo,a:data[0].total,b:data[0].meta,c:data[0].montoCotizacion},
                            {t:data[1].titulo,a:data[1].total,b:data[1].meta,c:data[1].montoCotizacion},
                            {t:data[2].titulo,a:data[2].total,b:data[2].meta,c:data[2].montoCotizacion},
                            {t:data[3].titulo,a:data[3].total,b:data[3].meta,c:data[3].montoCotizacion},
                            {t:data[4].titulo,a:data[4].total,b:data[4].meta,c:data[4].montoCotizacion},
                        ],
                        xkey: 't',
                        ykeys: ['a','b'],
                        xLabelAngle: 0,
                        xLabelWidth:5,
                        xlabelColor: '#1A66E1',
                        xlabelSize:12,
                        labels: ['Total Cotizaciones','Objetivo'],
                        barColors: ['rgb(26, 102, 225 )','rgb(150,40,195)']
                    });
                }
            },
            
            getDataEjecutivo(){
                var url=host+"Indicadores/get-ejecutivos";
                axios.get(url).then((resp)=>{
                    if(resp.status==200 && resp.data.respuesta){
                        this.dataEjecutivo=resp.data.jsonEjecutivos.rows;
                    }
                })
            },
            
            obtenerUsuarioSeleccionado(paramEjecutivo){
                var ejecutivo=this.dataEjecutivo.filter(user=>user.id_usuario ==paramEjecutivo);
                this.usuarioObtenido=ejecutivo[0].nombre_completo;
            }
        },
        watch: {
            
        },
    })
}