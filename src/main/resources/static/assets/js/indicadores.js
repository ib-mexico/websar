var host = "http://localhost:8080/WebSar/controlPanel/";

//HEADERS EN AXIOS
let token = document.head.querySelector('meta[name="_csrf"]');
axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content; // for all requests

if(document.getElementById("appIndicadores")){
    var indicadores=new  Vue({
        el:"#appIndicadores",
        mounted() {
            this.getDataEjecutivo();
            this.getDataUsuarioGrupo();
        },
        data:{
            title:"INDICADORES DE PRODUCCIÓN",
            dataUsuarioGrupo:[],
            dataEjecutivo:[],

            dataProduccion:[],
            dataProduccionArea:[],

            idUsuario: '',
            idGrupoVenta: '',
            usuarioObtenido:'',
            grupoVentaObtenido: '',
                        
            dataIndicadoresGanador:[],

            titulo : [], 
            total : [] , 
            percent : [],
            monto : [],
            ganaGrupoVentas: []

        },
        methods: {
            
            loadComponent(){
                $('.cmbEjecutivo').selectpicker('refresh');
                $('.cmbVenta').selectpicker('refresh');
            },
            validateForm(){
                var response = false;  
                if((this.idUsuario) == null || this.idUsuario == 'selected') {
                    swal("Revisión!", "Debes seleccionar un Ejecutivo para continuar con los indicativos.", "warning");
                    return response;
                }
                return response=true;
            },

            validateForm2(){
                var response=false;
                
                if((this.idGrupoVenta) == null || this.idGrupoVenta == 'selected') {
                    swal("Revisión!", "Debes seleccionar un Grupo de venta para continuar con los indicativos.", "warning");
                    return response;
                }
                return response=true;
            },

            async getDataEjecutivo(){
                var url = host+"Indicadores/get-ejecutivos";
                await axios.get(url).then((resp)=>{
                    if(resp.status == 200 && resp.data.respuesta){
                        this.dataEjecutivo = resp.data.jsonEjecutivos.rows;
                    }
                })
            },
            async getDataUsuarioGrupo(){
                var url = host+"Indicadores/get-usuarioGrupo";
                await axios.get(url).then((resp) => {
                    if (resp.status == 200 && resp.data.respuesta) {
                        this.dataUsuarioGrupo = resp.data.jsonUsuarioGrupo.jsonGrupo;
                    }
                }).catch((error) => {
                    console.log(error);
                });
            }
            ,
            obtenerUsuarioSeleccionado(paramEjecutivo){
                var ejecutivo = this.dataEjecutivo.filter(user => user.id_usuario == paramEjecutivo);
                this.usuarioObtenido = ejecutivo[0].nombre_completo;
            },
            obtenerUsuarioGrupoSeleccionado(paramGrupo){
                var usuarioGrupo = this.dataUsuarioGrupo.filter(grupo => grupo.idUsuarioGrupo == paramGrupo);
                this.grupoVentaObtenido = usuarioGrupo[0].nombre_completo;
            },

            async getDataProduccion(paramEjecutivo, paramFechaInicio, paramFechaFin){
                // $("#bar_chart").empty();
                this.dataProduccion = [];

                this.titulo = [];
                this.total = [];
                this.percent = [];
                this.monto = [];

                // $('#graph1').highcharts().destroy();

                $('[data-toggle="tooltip"]').tooltip()

                var url = host+"Indicadores/get-indicadores-produccion/"+paramEjecutivo+"/"+paramFechaInicio+"/"+paramFechaFin;
                if(this.validateForm()){
                    await axios.get(url).then((resp) => {
                        var dataIndicadora = [];
                        dataIndicadora.push(resp.data.jsonIndicadores.jsonCotNuevas.jsonCotizacionesNuevas[0]);
                        dataIndicadora.push(resp.data.jsonIndicadores.jsonCotAprobadas.jsonCotizacionesAceptadas[0]);
                        dataIndicadora.push(resp.data.jsonIndicadores.jsonCotFacturadas.jsonCotizacionesFacturadas[0]);
                        dataIndicadora.push(resp.data.jsonIndicadores.jsonCotCobradasMas90.jsonCotCobradasMas90Dias[0]);
                        dataIndicadora.push(resp.data.jsonIndicadores.jsonCotCobradasMenos90.jsonCotCobradasMenos90Dias[0]);
                        for (let i = 0; i < dataIndicadora.length; i++) {
                            this.dataProduccion.push({
                                "titulo":dataIndicadora[i].titulo,
                                "total":dataIndicadora[i].total,
                                "montoCotizacion":this.format2(parseFloat(dataIndicadora[i].montoCotizacion),'$ '),
                                "meta":dataIndicadora[i].meta,
                                "porcentaje":dataIndicadora[i].porcentaje,
                                "monto":dataIndicadora[i].montoCotizacion,
                                "color":"#"+i+"A66E"+i
                            });
                        }
                        
                        // idGrafo= document.getElementById("bar_chart");
                        // this.getMorris('bar',idGrafo, this.dataProduccion);

                        this.obtenerUsuarioSeleccionado(paramEjecutivo);
                        this.myChart();
                    }).catch((error) => {
                        console.log(error)
                    });
                }
            },
            
            async getDataProduccionArea(paramIdArea,paramFechaInicio, paramFechaFin){
                this.dataIndicadoresGanador = [];
                var url=host+"Indicadores/get-indicadoresArea-produccion/"+paramIdArea+"/"+paramFechaInicio+"/"+paramFechaFin;
                if(this.validateForm2()){
                    await axios.get(url).then((resp) => {
                        if (resp.status == 200 && resp.data.respuesta) {
                            this.obtenerUsuarioGrupoSeleccionado(paramIdArea);
                            var dataProdArea = resp.data.jsonIndicadoresArea.jsonCotArea;
                            var dataProdCobradaArea = resp.data.jsonIndicadorPagada.jsonCotMenos90Dias;
                            var dataOpnGeneradaArea = resp.data.jsonOpnArea.jsonOpns;
                            var dataOpnCerradaArea = resp.data.jsonOpnCerrada.jsonOpnsClose;
                           
                            // this.dataProduccionArea=_.reject(dataProdArea,function(o){return o.idUsuario});

                            /** Tratar la DATA de Produccion de cotizaciones facturadas */
                            if (dataProdArea.length!=null) {
                                var produccionArea=_.chain(dataProdArea).groupBy("idUsuario").toArray().value();
                                var dataFinalFactura = [] ,dataFinalIngreso = [];

                                /**Iteracion para el mayor numero de facturas y mayor ingreso de facturas */
                                for (let ejecutivo = 0; ejecutivo < produccionArea.length; ejecutivo++) {
                                    let suma = 0 , aux=0;
                                    for (let y = aux; y < produccionArea[ejecutivo].length; y++) {
                                        suma = suma+parseFloat(produccionArea[ejecutivo][y].subtotal); 
                                    }
                                    dataFinalFactura.push(
                                        {   
                                            "titulo":"Ejecutivo con Mayor Número de Facturas", "usuario":produccionArea[ejecutivo][aux].nombreCompleto,
                                            "numeroFactura":produccionArea[ejecutivo].length, "monto": this.format2(suma,'$ ')
                                        }
                                    );
                                    dataFinalIngreso.push(
                                        {
                                            "titulo":"Ejecutivo con Mayor Ingreso en Facturación", "usuario":produccionArea[ejecutivo][aux].nombreCompleto,
                                            "numeroFactura":produccionArea[ejecutivo].length, "monto":this.format2(suma,'$ '),"recurso":suma
                                        }
                                    )
                                }
    
                                //Ordenar los arreglos para de facturas y monto.
                                var dataFacturaOrdenado = dataFinalFactura.sort((a, b) => {return (b.numeroFactura - a.numeroFactura)});
                                var dataMontoOrdenado = dataFinalIngreso.sort((a, b) => {return (b.recurso - a.recurso)});
    
                                //this.dataIndicadoresGanador = []; Buscar el mayor numero de facturas
                                this.dataIndicadoresGanador.push(dataFacturaOrdenado[0]);
                                for (let ivendedor = 1; ivendedor < dataFacturaOrdenado.length; ivendedor++) {
                                    if (dataFacturaOrdenado[0].numeroFactura === dataFacturaOrdenado[ivendedor].numeroFactura) {
                                        this.dataIndicadoresGanador.push(dataFacturaOrdenado[ivendedor]);
                                    }else{
                                        break;
                                    }
                                }

                                /**Buscar con el mayor monto */
                                this.dataIndicadoresGanador.push(dataMontoOrdenado[0]);
                                for (let ivendedor = 1; ivendedor < dataMontoOrdenado.length; ivendedor++) {
                                    if (dataMontoOrdenado[0].monto===dataMontoOrdenado[ivendedor].monto) {
                                        this.dataIndicadoresGanador.push(dataMontoOrdenado[ivendedor]);
                                    }else{
                                        break;
                                    }
                                }

                                // var dataGanador=_.maxBy(dataFinal, function(x) { return x.mayor==x.mayor; });
                            }

                            if (dataProdCobradaArea!=null) {
                                var produccionCobrada =_.chain(dataProdCobradaArea).groupBy("idUsuario").toArray().value();
                                var dataCobradaMenos90 = [];
                                /**Iteracion para el mayor numero de facturas y mayor ingreso de facturas */
                                for (let ejecutivo = 0; ejecutivo < produccionCobrada.length; ejecutivo++) {
                                    let suma = 0 , aux=0;
                                    for (let y = aux; y < produccionCobrada[ejecutivo].length; y++) {
                                        suma = suma+parseFloat(produccionCobrada[ejecutivo][y].subtotal); 
                                    }
                                    dataCobradaMenos90.push(
                                        {   
                                            "titulo":"Ejecutivo con Mayor Recuperacion de Cobranza antes de los 90 dias",
                                            "usuario":produccionCobrada[ejecutivo][aux].nombreCompleto,
                                            "numeroFactura":produccionCobrada[ejecutivo].length, "monto":this.format2(suma,'$ ')
                                        }
                                    );
                                }
                                //Ordenar facturas cobradas con menos de 90 dias
                                var dataFacturaCobradaMenos = dataCobradaMenos90.sort((a, b) => {return (b.numeroFactura - a.numeroFactura)});
                                
                                this.dataIndicadoresGanador.push(dataFacturaCobradaMenos[0]);
                                for (let ivendedor = 1; ivendedor < dataFacturaCobradaMenos.length; ivendedor++) {
                                    if (dataFacturaCobradaMenos[0].numeroFactura === dataFacturaCobradaMenos[ivendedor].numeroFactura) {
                                        this.dataIndicadoresGanador.push(dataFacturaCobradaMenos[ivendedor]);
                                    }else{
                                        break;
                                    }
                                }
                            }
    
                            /** Agrupacion, ordenamiento, maximo de todas las OPN nuevas generadas */
                            if (dataOpnGeneradaArea!=null && dataOpnGeneradaArea.length>0) {
                                var dataOPNGen=_.chain(dataOpnGeneradaArea).groupBy("idUsuario").toArray().value();
                                console.log('OPN Generadas',dataOPNGen);
                                var dataOPNGenerado=[];
                                /**Iteracion para el mayor numero de facturas y mayor ingreso de facturas */
                                for (let ejecutivo = 0; ejecutivo < dataOPNGen.length; ejecutivo++) {
                                    let suma = 0 , aux=0;
                                    for (let y = aux; y < dataOPNGen[ejecutivo].length; y++) {
                                        suma = suma+parseFloat(dataOPNGen[ejecutivo][y].subtotal); 
                                    }
                                    dataOPNGenerado.push(
                                        {   
                                            "titulo":"Ejecutivo con Mayor Numero de OPN Generadas",
                                            "usuario":dataOPNGen[ejecutivo][aux].nombreCompleto,
                                            "numeroFactura":dataOPNGen[ejecutivo].length, "monto":this.format2(suma,'$ ')
                                        }
                                    );
                                }
                                //Ordenar las opns generadas 
                                var dataOPNGenOrden = dataOPNGenerado.sort((a, b) => {return (b.numeroFactura - a.numeroFactura)});
                                
                                this.dataIndicadoresGanador.push(dataOPNGenOrden[0]);
                                for (let ivendedor = 1; ivendedor < dataOPNGenOrden.length; ivendedor++) {
                                    if (dataOPNGenOrden[0].numeroFactura === dataOPNGenOrden[ivendedor].numeroFactura) {
                                        this.dataIndicadoresGanador.push(dataOPNGenOrden[ivendedor]);
                                    }else{
                                        break;
                                    }
                                }
                            }
                            else{
                                var dataOpnVacio = [];
                                dataOpnVacio.push(
                                    {   
                                        "titulo": "Ejecutivo con Mayor Numero de OPN Generadas",
                                        "usuario": "N/A",
                                        "numeroFactura": "0", "monto": "0"
                                    }
                                );
                                this.dataIndicadoresGanador.push(dataOpnVacio[0]);
                            }
    
                            /**Agrupacion, ordenamiento de todos las OPN ganadas y cerradas exitosamente */
                            if (dataOpnCerradaArea != null && dataOpnCerradaArea.length > 0) {
                                var dataOPNCerrado=_.chain(dataOpnCerradaArea).groupBy("idUsuario").toArray().value();
                                var dataOPNclose = [];
                                /**Iteracion para el mayor numero de facturas y mayor ingreso de facturas */
                                for (let ejecutivo = 0; ejecutivo < dataOPNCerrado.length; ejecutivo++) {
                                    let suma = 0 , aux=0;
                                    for (let y = aux; y < dataOPNCerrado[ejecutivo].length; y++) {
                                        suma = suma + parseFloat(dataOPNCerrado[ejecutivo][y].subtotal); 
                                    }
                                    dataOPNclose.push(
                                        {   
                                            "titulo": "Ejecutivo con Mayor Numero de OPN Ganadas o Cerradas",
                                            "usuario": dataOPNCerrado[ejecutivo][aux].nombreCompleto,
                                            "numeroFactura": dataOPNCerrado[ejecutivo].length,
                                            "monto": this.format2(suma,'$ ')
                                        }
                                    );
                                }
                                //Ordenar dataFinalFactura
                                var dataOPNcloseOrder = dataOPNclose.sort((a, b) => {return (b.numeroFactura - a.numeroFactura)});
                                
                                //this.dataIndicadoresGanador=[];
                                this.dataIndicadoresGanador.push(dataOPNcloseOrder[0]);
                                for (let ivendedor = 1; ivendedor < dataOPNcloseOrder.length; ivendedor++) {
                                    if (dataOPNcloseOrder[0].numeroFactura === dataOPNcloseOrder[ivendedor].numeroFactura) {
                                        this.dataIndicadoresGanador.push(dataOPNcloseOrder[ivendedor]);
                                    }else{
                                        break;
                                    }
                                }
                            }
                            else{
                                var dataVacio=[];
                                dataVacio.push(
                                    {   
                                        "titulo":"Ejecutivo con Mayor Numero de OPN Ganadas o Cerradas",
                                        "usuario":"N/A",
                                        "numeroFactura":"0", "monto":"0"
                                    }
                                );
                                this.dataIndicadoresGanador.push(dataVacio[0]);
                            }
                                
                        }
                    })
                }
                // var groupVentas=_.chain(this.dataIndicadoresGanador).groupBy("titulo").toArray().value();
                // this.ganaGrupoVentas = groupVentas;
                console.log(groupVentas);
                
                this.myChartGrupo();
            },

            format2(n, currency) {
                return currency + n.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,');
            },
            
            getMorris(type,element,value){
                console.log(value)
                var data=this.dataProduccion;
                if (type === 'bar') {
                    Morris.Bar({
                        element: element,    
                        data: [
                            {t:data[0].titulo,a:data[0].total,b:data[0].meta,c:data[0].monto},
                            {t:data[1].titulo,a:data[1].total,b:data[1].meta,c:data[1].monto},
                            {t:data[2].titulo,a:data[2].total,b:data[2].meta,c:data[2].monto},
                            {t:data[3].titulo,a:data[3].total,b:data[3].meta,c:data[3].monto},
                            {t:data[4].titulo,a:data[4].total,b:data[4].meta,c:data[4].monto},
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
            
            myChart(){
                
                for (var index = 0; index < this.dataProduccion.length; index++) {
                    this.titulo.push(this.dataProduccion[index].titulo);
                    this.total.push(this.dataProduccion[index].total);
                    this.percent.push(this.dataProduccion[index].porcentaje);
                    this.monto.push(this.dataProduccion[index].monto);
                }

                // Highcharts.chart('graph1', {

                //     title: {
                //         text: this.usuarioObtenido
                //     },
                
                //     subtitle: {
                //         text: "Indicadores de producción"
                //     },
                
                //     xAxis: {
                //         categories: this.titulo
                //     },
                //     yAxis: {
                //         title: {
                //             text: 'Numero de Cotizaciones'
                //         }
                //     },
                
                //     series: [{
                //         type: 'column',
                //         colorByPoint: true,
                //         data: this.total,
                //         monto : this.monto,
                //         showInLegend: false
                //     }]
                
                // });


                 Highcharts.chart('graph1', {
                     chart: {
                         type: 'column'
                     },
                     title: {
                         text: this.usuarioObtenido
                     },
                     xAxis: {
                         categories: this.titulo
                     },
                     yAxis: {
                         title: {
                             text: 'Numero de Cotizaciones'
                         }
                     },
                     plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                                format: '{point.y:.0f}'
                            }
                        }
                    },
                     tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.title}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> Cotizaciones<br/>'
                    },
                     series: [
                         {
                            title: this.titulo,
                            name : this.usuarioObtenido,
                            colorByPoint: true,
                            data: this.total,
                            monto : this.monto,
                            y : this.percent
                        }]
                 });
             
            },
            
            myChartGrupo(){


                Highcharts.chart('graph2', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: this.grupoVentaObtenido
                    },
                    subtitle: {
                        text: 'Estimacion de produccion por area'
                    },
                    xAxis: {
                        categories: [
                            'Jan',
                            'Feb',
                            'Mar',
                            'Nov'
                        ],
                        crosshair: true
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Rainfall (mm)'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                            '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            pointPadding: 0.2,
                            borderWidth: 0
                        }
                    },
                    series: [{
                        name: 'Tokyo',
                        data: [49.9, 71.5, 106.4, 129.2]
                
                    }, {
                        name: 'New York',
                        data: [83.6, 78.8, 98.5, 93.4]
                
                    }, {
                        name: 'London',
                        data: [48.9, 38.8, 39.3, 41.4]
                
                    }, {
                        name: 'Berlin',
                        data: [42.4, 33.2, 34.5, 39.7]
                
                    }]
                });

            }
        }
    })
}