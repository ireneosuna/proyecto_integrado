package servidor;

import java.util.ArrayList;
import modelo.Actividad;
import modelo.Inscripcion;
import modelo.Usuario;

public class Servidor {

	private static Servidor instancia;
	private ArrayList<Conexion> conexionesServidor = new ArrayList<Conexion>();
	private ConexionBBDD bbdd = new ConexionBBDD();

	private Servidor() {
	}

	public static Servidor getInstancia() {
		if (instancia == null) {
			instancia = new Servidor();
		}
		return instancia;
	}

	public synchronized boolean comprobarInicioSesion(Conexion c) {
		Usuario u = c.getUsuario();

		int id = existeUsuario(u.getNombre_usuario(), u.getContrasena());
		boolean existe = false;
		if (id != -1) {
			u.setId_usuario(id);
			existe = true;
		}

		boolean tieneRol = registradoARol(u, u.getRol());
		boolean conectado = conexionesServidor.stream()
				.anyMatch(conexion -> conexion.getUsuario().getId_usuario() == u.getId_usuario());

		String respuesta;

		if (existe && !conectado && tieneRol) {
			respuesta = informacionUsuario("iniciocorrecto#", id, u.getRol());
			conexionesServidor.add(c);
		} else {
			respuesta = "inicioincorrecto";
		}
		c.enviar(respuesta);

		return existe && !conectado && tieneRol;
	}

	public synchronized boolean comprobarRegistro(Conexion c) {
		boolean registroValido = false;
		String respuesta;
		Usuario u = c.getUsuario();

		registroValido = (nombreRegistrado(u.getNombre_usuario()) == -1);

		if (registroValido) {
			registrarNuevoUsuario(u);
			asignarRolUsuario(u);
			respuesta = informacionUsuario("registrocorrecto#", u.getId_usuario(), u.getRol());
			conexionesServidor.add(c);
		} else {
			respuesta = "registroincorrecto";
		}
		c.enviar(respuesta);

		return registroValido;
	}

	private String informacionUsuario(String acceso, int id_usuario, String tipo) {
		String info = acceso + id_usuario + "/" + bbdd.obtenerDatosUsuario(id_usuario) + "#";

		ArrayList<Actividad> actividadesPropias = bbdd.obtenerActividadesUsuario(id_usuario);
		ArrayList<Actividad> actividades = bbdd.obtenerActividadesNoPropias(id_usuario, tipo);
		ArrayList<Inscripcion> inscripciones = bbdd.obtenerIncripcionesUsuario(id_usuario);

		ArrayList<Actividad> actividadesInscripcion = new ArrayList<Actividad>();
		for (Inscripcion i : inscripciones) {
			Actividad a = bbdd.obtenerActividad(i.getId_actividad());
			actividadesInscripcion.add(a);
		}

		info += agregarActividades(actividadesPropias, "sin_propias#", false);
		info += agregarActividades(actividades, "sin_actividades#", false);
		info += agregarActividades(actividadesInscripcion, "sin_inscripciones", true);
		return info;
	}

	private String agregarActividades(ArrayList<Actividad> actividades, String mensaje, boolean fin) {
		String info = "";
		if (actividades.isEmpty()) {
			info += mensaje;
		} else {
			int contador = 0;
			for (Actividad act : actividades) {
				info += act.infoActividad();
				if (contador < actividades.size() - 1) {
					info += "~";
				} else if (!fin) {
					info += "#";
				}
				contador++;
			}
		}
		return info;
	}

	public synchronized void actualizarUsuariosDesconectados(Conexion c) {
		conexionesServidor.remove(c);
	}

	private int existeUsuario(String nombreUsuario, String contrasena) {
		return bbdd.obtenerIdUsuario(nombreUsuario, contrasena);
	}

	private int nombreRegistrado(String nombreUsuario) {
		return bbdd.obtenerExisteUsuario(nombreUsuario);
	}

	private boolean registradoARol(Usuario usu, String rol) {
		return bbdd.obtenerIdRolesUsuario(usu).contains(bbdd.obtenerIdRol(rol));
	}

	public void registrarNuevoUsuario(Usuario usuario) {
		bbdd.insertarNuevoUsuario(usuario); // Insertar usuario
		int id = bbdd.obtenerIdUsuario(usuario.getNombre_usuario(), usuario.getContrasena());
		usuario.setId_usuario(id);
	}

	public void asignarRolUsuario(Usuario usuario) {
		int id_rol = bbdd.obtenerIdRol(usuario.getRol());
		bbdd.asignarRolUsuario(id_rol, usuario.getId_usuario()); // Asignar rol
	}

	public int nuevaActividad(Actividad a) {
		return bbdd.crearNuevaActividad(a);
	}

	public void baja(int id) {
		bbdd.darDeBaja(id);
	}

	public ArrayList<Actividad> verActividadesPropias(int id) {
		return bbdd.obtenerActividadesUsuario(id);
	}

	public ArrayList<Actividad> verActividadesNoPropias(int id, String tipo) {
		return bbdd.obtenerActividadesNoPropias(id, tipo);
	}

	public ArrayList<Actividad> verInscripciones(int id, String tipo) {
		return bbdd.obtenerIncripciones(id, tipo);
	}

	public boolean modificarInformacionUsuario(String datos) {
		String[] d = datos.split("/");
		boolean passCorrecta = bbdd.passwordCorrecta(Integer.parseInt(d[0]), d[5]);
		if (passCorrecta) {
			return bbdd.actualizarInformacionUsuario(datos);
		} else {
			return false;
		}
	}

	public boolean enviarActualizarActividad(modelo.Actividad a) {
		return bbdd.actualizarActividad(a);
	}

	public void enviarCancelarActividad(modelo.Actividad a) {
		ArrayList<Inscripcion> inscripciones = bbdd.obtenerIncripcionesActividad(a);
		for (Inscripcion i : inscripciones) {
			bbdd.eliminarInscripcion(i);
		}
		bbdd.eliminarActividad(a);
	}

	public String inscripcion(int id_usuario, int id_actividad) {
		bbdd.addPersonasActividad(id_actividad);
		bbdd.inscribir(id_usuario, id_actividad);
		Actividad a = bbdd.obtenerActividad(id_actividad);
		return a.infoActividad() + "#" + id_usuario;
	}

	public String ofertante(int id_usuario, int id_actividad) {
		bbdd.ofertante(id_usuario, id_actividad);
		Actividad a = bbdd.obtenerActividad(id_actividad);
		return a.infoActividad() + "#" + id_usuario;
	}

	public String cancelarInscripcion(int id_usuario, int id_actividad) {
		bbdd.eliminarInscripcion(id_usuario, id_actividad);
		bbdd.borrarPersonasActuales(id_actividad);
		Actividad a = bbdd.obtenerActividad(id_actividad);
		return a.infoActividad() + "#" + id_usuario;
	}

	public synchronized ArrayList<Conexion> getConexionesServidor() {
		return conexionesServidor;
	}

	public void setConexionesServidor(ArrayList<Conexion> conexionesServidor) {
		this.conexionesServidor = conexionesServidor;
	}

}
