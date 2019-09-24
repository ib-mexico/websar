package com.ibmexico.libraries.notifications;

public enum EnumException {
	
	//ACTIVOS
	ACTIVO_CREATE_001("Error en creación de activos","El objeto activo no es válido, póngase en contacto con el administrador de sistema"),
	ACTIVO_DELETE_001("Error de eliminación activo","El objeto activo no es válido, consulte a su administrador de sistema"),
	ACTIVO_FICHEROS_ADD_FILE_001("Error de fichero de Activo","Ocurrio un error con el fichero, es posible que el archivo presente errores"),
	ACTIVO_FICHEROS_ADD_FILE_002("Error de fichero de Activo","Ocurrio un error con el fichero de Activo es posible que el archivo presente erores"),
	ACTIVO_FICHEROS_ADD_FILE_003("Error de fichero de Activo","Ocurrio un error con el fichero, es posible que el archivo presente errores"),
	ACTIVO_FICHEROS_ADD_FILE_004("Error de fichero de Activo","El fichero requiere un nombre"),




	//GENERALES
	GENERAL_FORM("Error de formulario", "Es necesario completar el formulario."),
	GENERAL_COMUNICATION("Error de comunicación", "Ocurrio un error de comunicación entre el cliente y el servidor."),
	GENERAL_PARSE("Error de conversión", "se ingresó información que no coincide con el tipo de dato requerido, Verifique su información."),	
	
	//ENTITIES
	ENTITY_PASSWORD_001("Password incorrecto", "El password debe de tener mínimo 8 caracteres y máximo 60"),
	
	//LOGIN
	USUARIO_LOGIN_001("Error de inicio de sesión", "El usuario o el password son incorrectos. verifique su información."),
	USUARIO_LOGIN_002("Error de privilegios", "El usuario o el password son incorrectos o el usuario no cuenta con los suficientes privilegios."),
	USUARIO_LOGIN_003("Error de usuario", "El usuario se encuentra eliminado, para recuperar el acceso debe póngase en contacto con la administración."),
	USUARIO_LOGIN_004("Error de usuario", "El usuario no existe, póngase en contacto con la administración."),
	
	
	//USUARIOS
	USUARIOS_CREATE_001("Error en creación de usuario", "El objeto usuario no es valido, póngase en contacto con el administrador del sistema."),
	USUARIOS_CREATE_002("Error en creación de usuario", "El usuario existe y no puede duplicarse."),
	USUARIOS_CREATE_003("Error en creación de usuario", "La clave del usuario ya se encuentra en uso, debe ingresar una diferente."),

	USUARIOS_UPDATE_001("Error de modificacion de usuario", "El objeto usuario no es válido, póngase en contacto con el administrador del sistema."),
	USUARIOS_UPDATE_002("Error de modificacion de usuario", "El usuario no ha sido creado con anterioridad."),
	USUARIOS_UPDATE_003("Error de modificacion de usuario", "El usuario no existe en la base de datos."),
	USUARIOS_UPDATE_004("Error de modificacion de usuario", "El usuario no puede modificarse, ya que se encuentra eliminado/bloqueado por el administrador."),
	
	USUARIOS_ADD_FILE_001("Error de fichero de usuario", "El objeto usuario no es valido, póngase en contacto con el administrador del sistema."),
	USUARIOS_ADD_FILE_002("Error de fichero de usuario", "Es necesario proporcionar por lo menos un fichero para almacenar."),
	USUARIOS_ADD_FILE_003("Error de fichero de usuario", "Ocurrio un error con el fichero, es posible que el archivo presente errores."),
	USUARIOS_ADD_FILE_004("Error de fichero de usuario", "El fichero requiere un nombre."),
	
	USUARIOS_PASSWORD_UPDATE_001("Error de modificacion de usuario", "El password y la confirmación no coinciden, es necesario que coincidan."),
	
	USUARIOS_LOCK_001("Error en bloqueo de usuario", "El objeto usuario no existe, póngase en contacto con el administrador del sistema."),
	USUARIOS_UNLOCK_001("Error en desbloqueo de usuario", "El objeto usuario no existe, póngase en contacto con el administrador del sistema."),
	
	//CLIENTES
	CLIENTES_CREATE_001("Error en creación de cliente", "El objeto cliente no es válido, póngase en contacto con el administrador del sistema."),
	CLIENTES_UPDATE_001("Error en modificación de cliente", "El objeto cliente no existe, póngase en contacto con el administrador del sistema."),
	CLIENTES_LOCK_001("Error en bloqueo de cliente", "El objeto cliente no existe, póngase en contacto con el administrador del sistema."),
	CLIENTES_UNLOCK_001("Error en desbloqueo de cliente", "El objeto cliente no existe, póngase en contacto con el administrador del sistema."),
	
	CLIENTES_CONTACTOS_CREATE_001("Error en creación de contacto", "El objeto contacto no es válido, póngase en contacto con el administrador del sistema."),
	CLIENTES_CONTACTOS_UPDATE_001("Error en modificación de contacto", "El objeto contacto no es válido, póngase en contacto con el administrador del sistema."),
	CLIENTES_CONTACTOS_LOCK_001("Error en bloqueo de contacto", "El objeto contacto no existe, póngase en contacto con el administrador del sistema."),
	CLIENTES_CONTACTOS_UNLOCK_001("Error en desbloqueo de contacto", "El objeto contacto no existe, póngase en contacto con el administrador del sistema."),
	
	//COTIZACIONES
	COTIZACIONES_CREATE_001("Error en creación de cotización", "El objeto cotización no es válido, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_UPDATE_001("Error en modificación de cotización", "El objeto cotización no existe, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_CLONE_001("Error en clonación de cotización", "No se pudo clonar la cotización, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_CLONE_002("Error en clonación de cotización", "El objeto cotización no existe, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_CLONE_003("Error en clonación de cotización", "No se pudo clonar la partida de la cotización, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_CLONE_004("Error en clonación de cotización", "El objeto partida no existe, póngase en contacto con el administrador del sistema."),
	
	COTIZACIONES_CALCULAR_001("Error en cálculo de cotización", "No fue posible realizar el cálculo de la cotización, favor de revisar las cantidades ingresadas."),
	COTIZACIONES_CALCULAR_002("Error en cálculo de cotización", "El objeto cotización no es válido, póngase en contacto con el administrador del sistema."),
		
	
	
	COTIZACIONES_PARTIDAS_CREATE_001("Error en creación de partida", "El objeto partida no es válido, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_PARTIDAS_UPDATE_001("Error en modificación de partida", "El objeto partida no existe, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_PARTIDAS_UPDATE_002("Error en modificación de partida", "Ocurrió un error al actualizar, póngase en contacto con el administrador del sistema."),
	
	COTIZACIONES_PARTIDAS_DELETE_001("Error en eliminación de partida", "El objeto partida no existe, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_PARTIDAS_CLONE_001("Error en clonación de partida", "El objeto partida no existe, póngase en contacto con el administrador del sistema."),
	
	COTIZACIONES_PARTIDAS_SORT_001("Error en ordenamiento de partida", "Ocurrió un error al ordenar las partidas, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_PARTIDAS_SORT_002("Error en ordenamiento de partida", "El objeto cotizacion no existe, póngase en contacto con el administrador del sistema."),
	
	
	COTIZACIONES_FICHEROS_CREATE_001("Error de creacion de fichero", "El objeto fichero no es valido, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_FICHEROS_CREATE_002("Error de creacion de fichero", "El fichero debe contar con un asunto."),
	COTIZACIONES_FICHEROS_CREATE_003("Error de creacion de fichero", "El fichero debe especificar una descripción."),
	
	COTIZACIONES_FICHEROS_UPDATE_001("Error de modificación de fichero", "El objeto fichero no es valido, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_FICHEROS_UPDATE_002("Error de modificación de fichero", "El fichero debe contar con un asunto."),
	COTIZACIONES_FICHEROS_UPDATE_003("Error de modificación de fichero", "El fichero debe especificar una descripción."),
	
	COTIZACIONES_FICHEROS_ADD_FILE_001("Error de fichero de cotización", "El objeto cotización no es valido, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_FICHEROS_ADD_FILE_002("Error de fichero de cotización", "Es necesario proporcionar por lo menos un fichero para almacenar."),
	COTIZACIONES_FICHEROS_ADD_FILE_003("Error de fichero de cotización", "Ocurrio un error con el fichero, es posible que el archivo presente errores."),
	COTIZACIONES_FICHEROS_ADD_FILE_004("Error de fichero de cotización", "El fichero requiere un nombre."),
	
	COTIZACIONES_FICHEROS_DELETE_001("Error en eliminación de expediente", "El objeto expediente no existe, póngase en contacto con el administrador del sistema."),
	
	
	COTIZACIONES_ORDENES_SERVICIOS_CREATE_001("Error en creación de orden", "El objeto orden de servicio no existe, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_ORDENES_SERVICIOS_CREATE_002("Error en creación de orden", "Es necesario agregar al menos un servicio o refacción en la orden de servicio."),
	COTIZACIONES_ORDENES_SERVICIOS_CREATE_003("Error en creación de orden", "El objeto orden no es válido, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_ORDENES_SERVICIOS_UPDATE_001("Error en modificación de orden", "El objeto orden de servicio no existe, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_ORDENES_SERVICIOS_UPDATE_002("Error en creación de orden", "El objeto orden de servicio no existe, póngase en contacto con el administrador del sistema."),
	
	COTIZACIONES_ORDENES_SERVICIOS_PARTIDAS_CREATE_001("Error en creación de partida", "El objeto orden de servicio no existe, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_ORDENES_SERVICIOS_PARTIDAS_CREATE_002("Error en creación de orden", "Es necesario agregar al menos un servicio o refacción en la orden de servicio."),
	
	COTIZACIONES_ORDENES_SERVICIOS_FIRMA_CREATE_001("Error en creación de firma", "El objeto orden no es válido, póngase en contacto con el administrador del sistema."),
	COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_001("Error de fichero de elaboración", "Ocurrio un error con el fichero de elaboración, es posible que el archivo presente errores."),
	COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_002("Error de fichero de recepción", "Ocurrio un error con el fichero de recepción, es posible que el archivo presente errores."),
	COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_003("Error de fichero de orden", "Ocurrio un error con el fichero, es posible que el archivo presente errores."),
	COTIZACIONES_ORDENES_SERVICIOS_FIRMA_ADD_FILE_004("Error de fichero de revisión", "Ocurrio un error con el fichero de revisión, es posible que el archivo presente errores."),
	
	
	//COMISIONES
	COTIZACION_COMISION_CREATE_001("Error en creación de comisión", "El objeto comisión no es válido, póngase en contacto con el administrador del sistema."),
	
	
	//QUOTAS
	COTIZACION_QUOTA_CREATE_001("Error en creación de quota", "El objeto quota no es válido, póngase en contacto con el administrador del sistema."),
	COTIZACION_QUOTA_CREATE_002("Error en creación de quota", "El objeto cotización no es válido, póngase en contacto con el administrador del sistema."),
	COTIZACION_QUOTA_DELETE_001("Error en eliminación de quota", "El objeto cotizción no es válido, póngase en contacto con el administrador del sistema."),
	
	
	//OPORTUNIDADES NEGOCIOS
	OPORTUNIDADES_CREATE_001("Error en creación de oportunidad", "El objeto oportunidad no es válido, póngase en contacto con el administrador del sistema."),		
	OPORTUNIDADES_UPDATE_001("Error en modificación de oportunidad", "El objeto oportunidad no es válido, póngase en contacto con el administrador del sistema."),		
	OPORTUNIDADES_DELETE_001("Error en eliminación de oportunidad", "El objeto oportunidad no existe, póngase en contacto con el administrador del sistema."),
	OPORTUNIDADES_SHOW_001("Error en listado de oportunidades", "El objeto empresa no existe, póngase en contacto con el administrador del sistema."),
	OPORTUNIDADES_SHOW_002("Error en listado de oportunidades", "Ocurrió un error al listar las oportunidades de la empresa."),
	OPORTUNIDADES_SHOW_COTIZACIONES_001("Error en listado de cotizaciones", "El objeto oportunidad no existe, póngase en contacto con el administrador del sistema."),
	OPORTUNIDADES_SHOW_COTIZACIONES_002("Error en listado de cotizaciones", "Ocurrió un error al listar las cotizaciones de la oportunidad."),
	OPORTUNIDADES_SHOW_FICHEROS_001("Error en listado de ficheros", "El objeto oportunidad no existe, póngase en contacto con el administrador del sistema."),
	OPORTUNIDADES_SHOW_FICHEROS_002("Error en listado de ficheros", "Ocurrió un error al listar los ficheros de la oportunidad."),
	OPORTUNIDADES_SHOW_ACTIVIDADES_001("Error en listado de actividades", "El objeto oportunidad no existe, póngase en contacto con el administrador del sistema."),
	OPORTUNIDADES_SHOW_ACTIVIDADES_002("Error en listado de actividades", "Ocurrió un error al listar llas actividades de la oportunidad."),
	
	OPORTUNIDADES_FICHEROS_ADD_FILE_001("Error de fichero de oportunidad", "El objeto oportunidad no es valido, póngase en contacto con el administrador del sistema."),
	OPORTUNIDADES_FICHEROS_ADD_FILE_002("Error de fichero de oportunidad", "Es necesario proporcionar por lo menos un fichero para almacenar."),
	OPORTUNIDADES_FICHEROS_ADD_FILE_003("Error de fichero de oportunidad", "Ocurrio un error con el fichero, es posible que el archivo presente errores."),
	OPORTUNIDADES_FICHEROS_ADD_FILE_004("Error de fichero de oportunidad", "El fichero requiere un nombre."),
	
	
	//ENTREGAS
	ENTREGAS_CREATE_001("Error en creación de entrega", "El objeto entrega no es válido, póngase en contacto con el administrador del sistema."),
	ENTREGAS_UPDATE_001("Error en modificación de entrega", "El objeto entrega no es válido, póngase en contacto con el administrador del sistema."),
		
	ENTREGAS_PRODUCTOS_CREATE_001("Error en creación de producto", "El objeto producto no es válido, póngase en contacto con el administrador del sistema."),
	ENTREGAS_FICHEROS_ADD_FILE_001("Error de firma de entrega", "Ocurrio un error con el fichero, es posible que el archivo presente errores."),
	ENTREGAS_FICHEROS_ADD_FILE_002("Error de firma de entrega", "Ocurrio un error con el fichero de entrega, es posible que el archivo presente errores."),
	ENTREGAS_FICHEROS_ADD_FILE_003("Error de firma de entrega", "Ocurrio un error con el fichero de recepción, es posible que el archivo presente errores."),
	
	//RESGUARDOS
	RESGUARDOS_CREATE_001("Error en creación de resguardo", "El objeto resguardo no es válido, póngase en contacto con el administrador del sistema."),
	RESGUARDOS_UPDATE_001("Error en modificación de resguardo", "El objeto resguardo no es válido, póngase en contacto con el administrador del sistema."),
	RESGUARDOS_PARTIDAS_CREATE_001("Error en creación de partida", "El objeto partida no es válido, póngase en contacto con el administrador del sistema."),
	RESGUARDOS_FICHEROS_ADD_FILE_001("Error de firma de resguardo", "Ocurrio un error con la firma, es posible que el archivo presente errores."),
	RESGUARDOS_FICHEROS_ADD_FILE_002("Error de firma de resguardo", "Ocurrio un error con la firma de entrega, es posible que el archivo presente errores."),
	RESGUARDOS_FICHEROS_ADD_FILE_003("Error de firma de resguardo", "Ocurrio un error con la firma de recepción, es posible que el archivo presente errores."),
	
	
	//ACTIVIDADES
	ACTIVIDADES_CREATE_001("Error en creación de actividad", "El objeto actividad no es válido, póngase en contacto con el administrador del sistema."),
	ACTIVIDADES_CREATE_002("Error en creación de actividad", "Es necesario agregar al menos un producto en la orden de entrega."),
	ACTIVIDADES_UPDATE_001("Error en modificación de actividad", "El objeto actividad no es válido, póngase en contacto con el administrador del sistema."),
	
	
	//TAREAS
	TAREAS_CREATE_001("Error en creación de tarea", "El objeto tarea no es válido, póngase en contacto con el administrador del sistema."),
	TAREAS_CREATE_002("Error en creación de tarea", "No existen participantes en la solicitud, póngase en contacto con el administrador del sistema."),
	TAREAS_UPDATE_001("Error en modificación de tarea", "El objeto tarea no es válido, póngase en contacto con el administrador del sistema."),
	
	TAREAS_PARTICIPANTES_CREATE_001("Error en creación de tarea", "El objeto participante no es válido, póngase en contacto con el administrador del sistema."),
	TAREAS_PARTICIPANTES_UPDATE_001("Error en modificación de tarea", "El objeto participante no es válido, póngase en contacto con el administrador del sistema."),
	TAREAS_PARTICIPANTES_UPDATE_002("Error en modificación de tarea", "No existen participantes en la solicitud, póngase en contacto con el administrador del sistema."),
	
	//NOTICIAS
	NOTICIAS_CREATE_001("Error en creación de noticia", "El objeto noticia no es válido, póngase en contacto con el administrador del sistema."),		
	NOTICIAS_UPDATE_001("Error en modificación de noticia", "El objeto noticia no es válido, póngase en contacto con el administrador del sistema."),
	
	
	//NOTIFICADORES
	NOTIFICADORES_CREATE_001("Error en creación de notificador", "El objeto notificador no es valido, póngase en contacto con el administrador del sistema."),
	NOTIFICADORES_CREATE_002("Error en creación de notificador", "Es necesario especificar el periodo de inicio de retraso del notificador."),
	NOTIFICADORES_CREATE_003("Error en creación de notificador", "El intervalo debe ser mayor a 0."),
	NOTIFICADORES_CREATE_004("Error en creación de notificador", "Es necesario especificar el periodo de intervalo del notificador."),
	NOTIFICADORES_CREATE_005("Error en creación de notificador", "El periodo de notificación debe ser mayor a 0."),
	NOTIFICADORES_CREATE_006("Error en creación de notificador", "Es necesario especificar el tipo de periodo para el rango de notificación."),
	NOTIFICADORES_CREATE_007("Error en creación de notificador", "El tiempo de deuda debe ser mayor a 0."),
	NOTIFICADORES_CREATE_008("Error en creación de notificador", "Es necesario especificar el periodo del tiempo de deuda notificación."),
	
	
	//USUARIOS ROLES
	USUARIOS_ROLES_001("Eror de creación de usuario", "El objeto usuario no es valido, póngase en contacto con el administrador del sistema."),
	
	USUARIOS_DELETE_001("Error de eliminación de usuario", "El objeto usuario no es valido, póngase en contacto con el administrador del sistema."),
	USUARIOS_DELETE_002("Error de eliminación de usuario", "El usuario no ha sido creado con anterioridad."),
	USUARIOS_DELETE_003("Error de eliminación de usuario", "El usuario no existe en la base de datos."),
	USUARIOS_DELETE_004("Error de eliminación de usuario", "El usuario ya que se encuentra eliminado/bloqueado por el administrador."),
	

	
	//PROVEEDORES
	PROVEEDORES_CREATE_001("Error de creacion de proveedor", "El objeto proveedor no es valido, póngase en contacto con el administrador del sistema."),
	PROVEEDORES_CREATE_002("Error de creacion de proveedor", "Es necesario especificar el proveedor."),
	
	PROVEEDORES_UPDATE_001("Error de modificación de proveedor", "El objeto proveedor no es válido, póngase en contacto con el administrador del sistema."),
	
	PROVEEDORES_LOCK_001("Error en bloqueo de proveedor", "El objeto proveedor no existe, póngase en contacto con el administrador del sistema."),
	PROVEEDORES_UNLOCK_001("Error en desbloqueo de proveedor", "El objeto proveedor no existe, póngase en contacto con el administrador del sistema."),
	
	//PROVEEDORES - CONTACTO
	PROVEEDORES_CONTACTO_CREATE_001("Error de creacion de contacto", "El objeto contacto no es valido, póngase en contacto con el administrador del sistema."),
	PROVEEDORES_CONTACTO_CREATE_002("Error de creacion de contacto", "Es necesario especificar el contacto para su creación."),
	
	PROVEEDORES_CONTACTOS_LOCK_001("Error en bloqueo de contacto", "El objeto contacto no existe, póngase en contacto con el administrador del sistema."),
	PROVEEDORES_CONTACTOS_UNLOCK_001("Error en desbloqueo de contacto", "El objeto contacto no existe, póngase en contacto con el administrador del sistema."),
	
		
	//GARANTIAS
	GARANTIAS_CREATE_001("Error en creación de garantía", "El objeto garantía no es válido, póngase en contacto con el administrador del sistema."),
	GARANTIAS_UPDATE_001("Error en modificación de garantía", "El objeto garantía no existe, póngase en contacto con el administrador del sistema."),
	GARANTIAS_FICHEROS_ADD_FILE_001("Error de fichero de garantía", "Ocurrio un error con el fichero, es posible que el archivo presente errores."),
	GARANTIAS_FICHEROS_ADD_FILE_002("Error de fichero de garantía", "Ocurrio un error con el fichero de garantía, es posible que el archivo presente errores."),
	GARANTIAS_FICHEROS_ADD_FILE_003("Error de fichero de garantía", "Ocurrio un error con el fichero, es posible que el archivo presente errores."),
	GARANTIAS_FICHEROS_ADD_FILE_004("Error de fichero de garantía", "El fichero requiere un nombre."),
	
	
	//EQUIPOS PRODUCCION
	EQUIPOS_PRODUCCION_CREATE_001("Error en creación de equipo", "El objeto equipo no es válido, póngase en contacto con el administrador del sistema."),
	EQUIPOS_PRODUCCION_UPDATE_001("Error en modificación de equipo", "El objeto equipo no existe, póngase en contacto con el administrador del sistema."),
	
	;
	
	
	private String title;
	private String message; 
	private String code;
	
	private EnumException(String title, String message) {
		this.code = this.name();
		this.title = title;
		this.message = message;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getMessage() {
		return "[" + code + "] " + message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getSimpleMessage() {
		return message;
	}
}
