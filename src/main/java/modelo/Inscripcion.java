package modelo;

import java.sql.Date;

public class Inscripcion {
	private int id_actividad;
	private int id_usuario;
	private Date fecha_inscripcion;

	public Inscripcion(int id_actividad, int id_usuario, Date fecha_inscripcion) {
		super();
		this.id_actividad = id_actividad;
		this.id_usuario = id_usuario;
		this.fecha_inscripcion = fecha_inscripcion;
	}

	public Inscripcion() {
	}

	// Getters y setters
	public int getId_actividad() {
		return id_actividad;
	}

	public void setId_actividad(int id_actividad) {
		this.id_actividad = id_actividad;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public Date getFecha_inscripcion() {
		return fecha_inscripcion;
	}

	public void setFecha_inscripcion(Date fecha_inscripcion) {
		this.fecha_inscripcion = fecha_inscripcion;
	}
}
