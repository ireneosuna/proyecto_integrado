package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;

import modelo.Actividad;
import modelo.Usuario;

public class Conexion extends Thread {

	private Servidor servidor;
	private Socket socket;
	private InputStream is;
	private DataInputStream dis;
	private OutputStream os;
	private DataOutputStream dos;
	private Usuario usuario;

	public Conexion(Socket socket) {
		this.socket = socket;
		this.servidor = Servidor.getInstancia();
	}

	public void run() {
		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);

			String recibido = recibir();

			String orden = ordenRecibida(recibido);
			if (orden.equals("inicio")) {
				procesarInicioSesion(recibido);
				if (servidor.comprobarInicioSesion(this)) {
					recibirAccionRealizar();
				}
				socket.close();

			} else {
				procesarRegistro(recibido);
				if (servidor.comprobarRegistro(this)) {
					recibirAccionRealizar();
				}
				socket.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al iniciar conexión.");
		}
	}

	private String ordenRecibida(String cadena) {
		String[] orden = cadena.split(":");
		if (orden.length > 0) {
			return orden[0];
		}
		return "";
	}

	private void procesarInicioSesion(String cadena) {
		String[] dato = cadena.split(":");
		String nombreUsuario = dato[1];
		String contrasena = dato[2];
		String tipo = dato[3];
		this.usuario = new Usuario(nombreUsuario, contrasena, tipo);
	}

	private void procesarRegistro(String cadena) throws ParseException {
		String[] dato = cadena.split(":");
		String nombreCompleto = dato[1];
		String nombreUsuario = dato[2];
		String contrasena = dato[3];
		String email = dato[4];
		Date fecha = Date.valueOf(dato[5]);
		String tipo = dato[6];

		this.usuario = new Usuario(nombreCompleto, nombreUsuario, contrasena, email, fecha, tipo);
	}

	public synchronized void enviar(String envio) {
		try {
			dos.writeUTF(envio);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al enviar información al cliente desde el servidor");
		}
	}

	private String recibir() {
		String recibido = "";
		try {
			recibido = dis.readUTF().trim();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al recibir orden de la conexión al servidor.");
		}
		return recibido;
	}

	public void cerrarConexion() {
		try {
			if (dis != null) {
				dis.close();
			}
			if (dos != null) {
				dos.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al cerrar conexion.");
		}
	}

	public void recibirAccionRealizar() throws IOException {
		try {
			while (true) {
				String accion = recibir();
				String[] datos = accion.split("#");

				if (accion == null || accion.isEmpty()) {
					break;
				}

				switch (datos[0]) {
				case "nuevaActividad":
					procesarNuevaActividad(datos[1]);
					break;
				case "baja":
					procesarBajaUsuario(Integer.parseInt(datos[1]));
					break;
				case "modificarDatosUsuario":
					procesarModificarDatosUsuario(datos[1]);
					break;
				case "verActividadesPropias":
					procesarVerActividadesPropias(Integer.parseInt(datos[1]));
					break;
				case "actualizar":
					procesarActualizar(Integer.parseInt(datos[1]), datos[2]);
					break;
				case "verInscripciones":
					procesarVerInscripciones(Integer.parseInt(datos[1]), datos[2]);
					break;
				case "modificarActividad":
					procesarModificarActividad(datos[1]);
					break;
				case "cancelarActividad":
					procesarCancelarActividad(datos[1]);
					break;
				case "inscripcion":
					procesarInscripcion(Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), datos[3]);
					break;
				case "cancelarInscripcion":
					procesarCancelarInscripcion(Integer.parseInt(datos[1]), Integer.parseInt(datos[2]), datos[3]);
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al recibir accion realizable del servidor.");
		} finally {
			// Desconectar usuario y actualizar
			if (this.isAlive()) {
				servidor.actualizarUsuariosDesconectados(this);
				this.socket.close();
			}
		}
	}

	private void procesarNuevaActividad(String info) {
		Actividad a = obtenerActividad(info);
		a.setId_actividad(servidor.nuevaActividad(a));

		for (Conexion c : servidor.getConexionesServidor()) {
			c.enviarNuevaActividad(a);
		}
	}

	private void procesarBajaUsuario(int id) {
		servidor.baja(id);

		for (Conexion c : servidor.getConexionesServidor()) {
			c.enviarBaja();
		}
	}

	private void procesarModificarDatosUsuario(String datos) {
		boolean modificado = servidor.modificarInformacionUsuario(datos);
		enviarModificacion(modificado, datos);
	}

	private void procesarVerActividadesPropias(int id) {
		ArrayList<Actividad> actividadesPropias = servidor.verActividadesPropias(id);
		enviarActividadesPropias(actividadesPropias);
	}

	private void procesarActualizar(int id, String tipo) {
		ArrayList<Actividad> actividades = servidor.verActividadesNoPropias(id, tipo);
		enviarActividadesNoPropias(actividades);
	}

	private void procesarVerInscripciones(int id, String tipo) {
		ArrayList<Actividad> actividades = servidor.verInscripciones(id, tipo);
		enviarInscripciones(actividades);
	}

	private void procesarModificarActividad(String datos) {
		Actividad a = obtenerActividad(datos);
		boolean actualizada = servidor.enviarActualizarActividad(a);

		for (Conexion c : servidor.getConexionesServidor()) {
			if (actualizada) {
				c.enviarActividadActualizada(a);
			}
		}
	}

	private void procesarCancelarActividad(String datos) {
		Actividad a = obtenerActividad(datos);
		servidor.enviarCancelarActividad(a);

		for (Conexion c : servidor.getConexionesServidor()) {
			c.enviarCancelarActividad(a);
		}
	}

	private void procesarInscripcion(int id_usuario, int id_actividad, String tipo) {
		if (tipo.equalsIgnoreCase("consumidor")) {
			String datos = servidor.inscripcion(id_usuario, id_actividad);
			for (Conexion c : servidor.getConexionesServidor()) {
				c.enviarInscripcion(datos);
			}
		} else {
			String datos = servidor.ofertante(id_usuario, id_actividad);
			for (Conexion c : servidor.getConexionesServidor()) {
				c.enviarAsigancionOfertante(datos);
			}
		}
	}

	private void procesarCancelarInscripcion(int id_usuario, int id_actividad, String tipo) {
		if (tipo.equalsIgnoreCase("consumidor")) {
			String datos = servidor.cancelarInscripcion(id_usuario, id_actividad);

			for (Conexion c : servidor.getConexionesServidor()) {
				c.enviar("inscripcionCancelada#" + datos);
			}
		}
	}

	private void enviarNuevaActividad(Actividad a) {
		String info = "nuevaActividad#" + a.infoActividad();
		enviar(info);
	}

	private void enviarBaja() {
		enviar("baja");
	}

	private void enviarModificacion(boolean realizado, String datos) {
		String info = "modificarDatosUsuario#";
		if (realizado) {
			info += datos;
		} else {
			info += "error";
		}
		enviar(info);
	}

	private void enviarActividadesPropias(ArrayList<Actividad> actividades) {
		enviarActividades("actividadesPropias", actividades, "sin_actividades");
	}

	private void enviarActividadesNoPropias(ArrayList<Actividad> actividades) {
		enviarActividades("actividadesNoPropias", actividades, "sin_actividades");
	}

	private void enviarInscripciones(ArrayList<Actividad> actividades) {
		enviarActividades("inscripciones", actividades, "sin_inscripciones");
	}

	private void enviarActividades(String tipoActividad, ArrayList<Actividad> actividades, String mensajeVacio) {
		String info = tipoActividad + "#";

		if (actividades.size() == 0) {
			info += mensajeVacio;
		} else {
			for (int i = 0; i < actividades.size(); i++) {
				info += actividades.get(i).infoActividad();
				if (i < actividades.size() - 1) {
					info += "~";
				}
			}
		}

		enviar(info);
	}

	private void enviarActividadActualizada(Actividad a) {
		enviar("actividadActualizada#" + a.infoActividad());
	}

	private void enviarCancelarActividad(Actividad a) {
		for (Conexion c : servidor.getConexionesServidor()) {
			c.enviar("actividadCancelada#" + a.infoActividad());
		}
	}

	private void enviarInscripcion(String datos) {
		for (Conexion c : servidor.getConexionesServidor()) {
			c.enviar("inscripcion#" + datos);
		}
	}

	private void enviarAsigancionOfertante(String datos) {
		for (Conexion c : servidor.getConexionesServidor()) {
			c.enviar("asignarOfertante#" + datos);
		}
	}

	private Actividad obtenerActividad(String info) {
		String[] d = info.split("@");
		Actividad a = new Actividad(Integer.parseInt(d[0]), d[1], d[2], d[3], Date.valueOf(d[4]), Time.valueOf(d[5]),
				Boolean.parseBoolean(d[6]), d[7], d[8], d[9], Boolean.parseBoolean(d[10]), Integer.parseInt(d[11]),
				Integer.parseInt(d[12]), Integer.parseInt(d[13]), Integer.parseInt(d[14]), d[15],
				Integer.parseInt(d[16]), Integer.parseInt(d[17]));
		return a;
	}

	// Getters y setters
	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
