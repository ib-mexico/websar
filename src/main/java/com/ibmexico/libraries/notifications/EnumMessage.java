package com.ibmexico.libraries.notifications;

public enum EnumMessage {

	//GENERALES
	GENERAL_001("Acción completada", "La operación se llevó a cabo satisfactoriamente."),
	
	//USUARIOS
	USUARIOS_CREATE_001("Creación de usuario", "El usuario se creó satisfactoriamente."),
	USUARIOS_UPDATE_001("Modificación de usuario", "El usuario se modificó satisfactoriamente."),
	USUARIOS_PASSWORD_UPDATE_001("Modificación de usuario", "Se modificó el password del usuario satisfactoriamente."),
	USUARIOS_DELETE_001("Eliminacion de usuario", "Se eliminó el usuario satisfactoriamente."),
	
	
	//CLIENTES
	CLIENTES_CREATE_001("Creación de cliente", "El cliente se creó satisfactoriamente."),
	CLIENTES_UPDATE_001("Modificación de cliente", "El cliente se modificó satisfactoriamente."),
	
	CLIENTES_CONTACTOS_CREATE_001("Creación de contacto", "El contacto del cliente se creó satisfactoriamente."),
	CLIENTES_CONTACTOS_UPDATE_001("Modificación de contacto", "El contacto del cliente se creó satisfactoriamente."),
	
	
	//COTIZACIONES
	COTIZACIONES_CREATE_001("Creación de cotización", "La cotización se creó satisfactoriamente."),
	COTIZACIONES_PARTIDAS_CREATE_001("Creación de partida", "La partida de la cotización se creó satisfactoriamente."),
	COTIZACIONES_ORDENES_SERVICIOS_CREATE_001("Creación de orden", "La orden de servicio se creó satisfactoriamente."),
	COTIZACIONES_ORDENES_SERVICIOS_REVISION_UPDATE_001("Modificación de orden", "La orden de servicio se revisó satisfactoriamente."),
	
	COTIZACIONES_UPDATE_001("Modificación de cotización", "La cotización se modificó satisfactoriamente."),
	COTIZACIONES_PARTIDAS_UPDATE_001("Modificación de partida", "La partida se modificó satisfactoriamente."),
	COTIZACIONES_FICHEROS_UPDATE_001("Modificación de expediente", "El expediente se modificó satisfactoriamente."),
	
	
	//OPORTUNIDADES NEGOCIOS
	OPORTUNIDADES_CREATE_001("Creación de oportunidad", "La oportunidad se creó satisfactoriamente."),
	OPORTUNIDADES_UPDATE_001("Modificación de oportunidad", "La oportunidad se modificó satisfactoriamente."),
	
	OPORTUNIDADES_FICHEROS_CREATE_001("Creación de fichero", "El fichero se cargó satisfactoriamente."),
	
	
	
	//ENTREGAS
	ENTREGAS_CREATE_001("Creación de entrega", "La entrega de producto se creó satisfactoriamente."),
	ENTREGAS_EDIT_001("Modificación de entrega", "La entrega de producto se modificó satisfactoriamente."),
	ENTREGAS_FIRMA_001("Entrega de Producto", "La entrega de producto se realizó satisfactoriamente."),
	
	
	//GARANTIAS
	GARANTIAS_CREATE_001("Creación de garantía", "La garantía del producto se creó satisfactoriamente."),
	GARANTIAS_EDIT_001("Modificación de garantía", "La garantía del producto se modificó satisfactoriamente."),
	GARANTIAS_FIRMA_001("Entrega de Producto", "La entrega de producto se realizó satisfactoriamente."),
	
	
	//ACTIVIDADES
	ACTIVIDADES_CREATE_001("Creación de actividad", "La actividad se creó satisfactoriamente."),
	ACTIVIDADES_UPDATE_001("Modificación de actividad", "La actividad se modificó satisfactoriamente."),
	
	
	//PROVEEDORES
	PROVEEDORES_CREATE_001("Creación de proveedor", "El proveedor se creó satisfactoriamente."),
	PROVEEDORES_UPDATE_001("Modificación de proveedor", "El proveedor se modificó satisfactoriamente."),
	
	PROVEEDORES_CONTACTOS_CREATE_001("Creación de contacto", "El contacto del proveedor se creó satisfactoriamente."),
	PROVEEDORES_CONTACTOS_UPDATE_001("Modificación de contacto", "El contacto del proveedor se creó satisfactoriamente."),
	
	
	
	//NOTICIAS
	NOTICIAS_CREATE_001("Creación de noticia", "La noticia se creó satisfactoriamente."),
	NOTICIAS_UPDATE_001("Modificación de noticia", "La noticia se modificó satisfactoriamente."),
	
	//TAREAS
	TAREAS_CREATE_001("Creación de tarea", "La tarea se creó satisfactoriamente."),
	TAREAS_UPDATE_001("Modificación de tarea", "La tarea se modificó satisfactoriamente."),
	;
	
	
	private String title;
	private String message; 
	private String code;
	
	private EnumMessage(String title, String message) {
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
