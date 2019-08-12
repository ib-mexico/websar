<template>
	<div class="card">
		<div class="header">
			<h2>{{ nombre_oportunidad }}</h2>
			<small class="usuario">{{ nombre_vendedor }}</small>
			<br />
			<small class="opn-id">{{ '#' + id_oportunidad }}</small>
			<br />
			<small>{{ '&Uacute;ltima modificaci&oacute;n: ' + ultima_modificacion }}</small>
			<ul class="header-dropdown m-r--5">
				<li class="dropdown">
					<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						<i class="material-icons">more_vert</i>
					</a>
					<ul class="dropdown-menu pull-right">
						<li><a :href="link_crear_actividad">Planificar Actividad</a></li>
						<li><a :href="link_cargar_archivo" class="opn-adjuntar" th:attr="data-id_oportunidad=${itemAbierto.getIdOportunidadNegocio()}">Adjuntar Archivo</a></li>
						<li><a :href="link_crear_cotizacion">Crear Cotizaci&oacute;n</a></li>
						<li><a :href="link_reporte_opn" target="_blank">Reporte OPN</a></li>
						<li><a :href="link_editar_opn">Editar</a></li>
						<li><a :href="'javascript:void(0);'" class="opn-eliminar" th:attr="data-id_oportunidad=${itemAbierto.getIdOportunidadNegocio()}">Eliminar</a></li>
					</ul>
				</li>
			</ul>
		</div>

		<div class="body">
			<div class="row cliente-div">
				<div class="col-md-12 cliente-text">
					Cliente: {{ nombre_cliente }}								
				</div>
				
				<div class="col-md-12">
					Ingreso estimado: {{ ingreso_estimado }}								
				</div>
			</div>

			<div class="row">
				<div class="col-md-6 align-left">						
					<div v-if="valor_prioridad == 0" style="color:gold;"><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i></div>
					<div v-if="valor_prioridad == 1" style="color:gold;"><i class="fas fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i></div>
					<div v-if="valor_prioridad == 2" style="color:gold;"><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="far fa-star"></i></div>
					<div v-if="valor_prioridad == 3" style="color:gold;"><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i></div>
				</div>

				<div class="col-md-6 align-right">
					<a class="popover-component" title="Cotizaciones" data-trigger="focus" data-toggle="popover" data-container="body" data-placement="right" type="button" data-html="true" :href="'javascript:void(0);'" :id="'c' + id_oportunidad">
						<i class="material-icons">view_list</i>
					</a>
				
					<a class="popover-component" title="Actividades" data-trigger="focus" data-toggle="popover" data-container="body" data-placement="right" type="button" data-html="true" :href="'javascript:void(0);'" :id="'p' + id_oportunidad">
						<i class="material-icons">query_builder</i>
					</a>
					
					<a class="popover-component" title="Ficheros" data-trigger="focus" data-toggle="popover" data-container="body" data-placement="right" type="button" data-html="true" :href="'javascript:void(0);'" :id="'f' + id_oportunidad">
						<i class="material-icons">archive</i>
					</a>

					<!-- COTIZACIONES -->
					<div :id="'popover-content-c' + id_oportunidad" class="hide">
						<ul class="list-group">
							<li class="list-group-item" v-for="item in cotizaciones" :key="item.id_cotizacion">								 												 				
								<div class="row clearfix pop-header">
									<div class="col-md-12 align-left">														
										<strong>{{ item.folio }}</strong>
										<br />
										<small :if="item.bool_maestra == 1">Cotizaci&oacute;n Maestra</small>
										<small :if="item.bool_renta == 1">Cotizaci&oacute;n de Renta</small>
										<small :if="item.bool_normal == 1">Cotizaci&oacute;n Normal</small>
									</div>					 				
								</div>								 				
								<div class="row pop-body">
									<i class="material-icons">person</i> <span>{{ item.usuario.correo }}</span><br />
									<i class="material-icons">monetization_on</i> <span>{{ item.total }}</span><br />
									<i class="material-icons">public</i> <span>{{ item.cliente.cliente }}</span><br />
									<i class="material-icons">contacts</i> <span>{{ item.contacto.contacto }}</span><br />
									<div class="col-md-12 align-right"><a :href="link_cotizacion_partidas + '/partidas'">Ver Partidas</a></div>								 					
								</div>									 												 					
							</li>
						</ul>
					</div>

					<!-- ACTIVIDADES -->
					<div :id="'popover-content-p' + id_oportunidad" class="hide">
						<ul class="list-group">
							<li class="list-group-item" v-for="item in actividades" :key="item.id_actividad">								 												 				
								<div class="row clearfix pop-header">
									<div class="col-md-8 align-left">														
										<strong>{{ item.actividadTipo.actividadTipo }}</strong>
									</div>	
									
									<div class="col-md-4 align-right">														
										<span v-if="item.finalizado == 1"><i class="material-icons">check_box</i></span>
										<span v-else><i class="material-icons">check_box_outline_blank</i></span>
									</div>					 				
								</div>								 				
								<div class="row pop-body">
									<i class="material-icons">person</i> <span>{{ item.usuario.correo }}</span><br />
									<div :if="item.finalizado != 1">
										<i class="material-icons">query_builder</i> <span class="date-format" :data-fecha_vencimiento="item.fecha_vencimiento">test</span><br />
									</div>
									<div class="col-md-12 align-right"><a :href="link_ver_actividad + '/edit-actividad'">Ver Actividad</a></div>								 					
								</div>									 												 					
							</li>
						</ul>
					</div>

					<!-- FICHEROS -->
					<div :id="'popover-content-f' + id_oportunidad" class="hide">
						<ul class="list-group">
							<li class="list-group-item" v-for="item in ficheros" :key="item.id_oportunidad_negocio_fichero">								 												 				
								<div class="row clearfix pop-header">
									<div class="col-md-12 text-center">														
										<strong>{{ item.titulo }}</strong>
										<p>
											{{ item.descripcion }}
										</p>
									</div>					 				
								</div>								 				
								<div class="row pop-body">
									<div class="col-md-12 align-right"><a :href="link_ficheros + item.url" target="_blank" role="button" class="btn btn-primary waves-effect" >Ver Fichero</a></div>								 					
								</div>									 												 					
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	export default {
		props: ['nombre_oportunidad', 'nombre_vendedor', 'id_oportunidad', 'ultima_modificacion', 'link_crear_actividad', 'link_cargar_archivo', 'link_crear_cotizacion', 'link_reporte_opn',
			'link_editar_opn', 'nombre_cliente', 'ingreso_estimado', 'valor_prioridad', 'cotizaciones', 'link_cotizacion_partidas', 'actividades', 'link_ver_actividad', 'link_ficheros'],
		methods : {

		}
	}
</script>