package modelo;

import java.sql.Date;

public class Usuario {
	private int id_usuario;
	private String nombre_completo;
	private String nombre_usuario;
	private String contrasena;
	private String email;
	private Date fecha_nacimiento;
	private String rol;

	//Constructor registro
	public Usuario(String nombre_completo, String nombre_usuario, String contrasena, String email,
			Date fecha_nacimiento, String rol) {
		super();
		this.nombre_completo = nombre_completo;
		this.nombre_usuario = nombre_usuario;
		this.contrasena = contrasena;
		this.email = email;
		this.fecha_nacimiento = fecha_nacimiento;
		this.rol = rol;
	}

	//Constructor inicio sesión
	public Usuario(String nombre_usuario, String contrasena, String rol) {
		super();
		this.nombre_usuario = nombre_usuario;
		this.contrasena = contrasena;
		this.rol = rol;
	}

	public Usuario() {
		super();
	}

	// Getters y setters
	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getNombre_completo() {
		return nombre_completo;
	}

	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}

	public String getNombre_usuario() {
		return nombre_usuario;
	}

	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(Date fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
}
