package modelo;

import java.sql.Date;
import java.sql.Time;

public class Actividad {
	private int id_actividad;
	private String nombre_actividad;
	private String tipo;
	private String descripcion;
	private Date fecha;
	private Time hora;
	private boolean transporte;
	private String ciudad_partida;
	private String ciudad_actividad;
	private String idioma;
	private boolean mascotas;
	private int capacidad_personas;
	private int personas_actuales;
	private int precio;
	private int edad_recomendada;
	private String codigo_vestimenta;
	private int id_usuario_propietario;
	private int id_ofertante;
	
	public Actividad(int id_actividad, String nombre_actividad, String tipo, String descripcion, Date fecha,
			Time hora, boolean transporte, String ciudad_partida, String ciudad_actividad, String idioma,
			boolean mascotas, int capacidad_personas, int personas_actuales, int precio, int edad_recomendada,
			String codigo_vestimenta, int id_usuario_propietario, int id_ofertante) {
		super();
		this.id_actividad = id_actividad;
		this.nombre_actividad = nombre_actividad;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.hora = hora;
		this.transporte = transporte;
		this.ciudad_partida = ciudad_partida;
		this.ciudad_actividad = ciudad_actividad;
		this.idioma = idioma;
		this.mascotas = mascotas;
		this.capacidad_personas = capacidad_personas;
		this.personas_actuales = personas_actuales;
		this.precio = precio;
		this.edad_recomendada = edad_recomendada;
		this.codigo_vestimenta = codigo_vestimenta;
		this.id_usuario_propietario = id_usuario_propietario;
		this.id_ofertante = id_ofertante;
	}
	
	public String infoActividad() {
		return id_actividad + "@" + nombre_actividad + "@" + tipo + "@" + descripcion + "@" + fecha + "@" + hora
				+ "@" + transporte + "@" + ciudad_partida + "@" + ciudad_actividad + "@" + idioma + "@" + mascotas + "@"
				+ capacidad_personas + "@" + personas_actuales + "@" + precio + "@" + edad_recomendada + "@"
				+ codigo_vestimenta + "@" + id_usuario_propietario +"@"+id_ofertante;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actividad other = (Actividad) obj;
		return id_actividad == other.id_actividad;
	}

	//	Getters y setters
	public int getId_actividad() {
		return id_actividad;
	}

	public void setId_actividad(int id_actividad) {
		this.id_actividad = id_actividad;
	}

	public String getNombre_actividad() {
		return nombre_actividad;
	}

	public void setNombre_actividad(String nombre_actividad) {
		this.nombre_actividad = nombre_actividad;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Time getHora() {
		return hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public boolean isTransporte() {
		return transporte;
	}

	public void setTransporte(boolean transporte) {
		this.transporte = transporte;
	}

	public String getCiudad_partida() {
		return ciudad_partida;
	}

	public void setCiudad_partida(String ciudad_partida) {
		this.ciudad_partida = ciudad_partida;
	}

	public String getCiudad_actividad() {
		return ciudad_actividad;
	}

	public void setCiudad_actividad(String ciudad_actividad) {
		this.ciudad_actividad = ciudad_actividad;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public boolean isMascotas() {
		return mascotas;
	}

	public void setMascotas(boolean mascotas) {
		this.mascotas = mascotas;
	}

	public int getCapacidad_personas() {
		return capacidad_personas;
	}

	public void setCapacidad_personas(int capacidad_personas) {
		this.capacidad_personas = capacidad_personas;
	}

	public int getPersonas_actuales() {
		return personas_actuales;
	}

	public void setPersonas_actuales(int personas_actuales) {
		this.personas_actuales = personas_actuales;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public int getEdad_recomendada() {
		return edad_recomendada;
	}

	public void setEdad_recomendada(int edad_recomendada) {
		this.edad_recomendada = edad_recomendada;
	}

	public String getCodigo_vestimenta() {
		return codigo_vestimenta;
	}

	public void setCodigo_vestimenta(String codigo_vestimenta) {
		this.codigo_vestimenta = codigo_vestimenta;
	}

	public int getId_usuario_propietario() {
		return id_usuario_propietario;
	}

	public void setId_usuario_propietario(int id_usuario_propietario) {
		this.id_usuario_propietario = id_usuario_propietario;
	}

	public int getId_ofertante() {
		return id_ofertante;
	}

	public void setId_ofertante(int id_ofertante) {
		this.id_ofertante = id_ofertante;
	}
	
}
