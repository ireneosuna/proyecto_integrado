package conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import interfaz.Principal;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import modelo.Actividad;
import modelo.Usuario;

public class Conexion extends Thread {

	private int puerto = 7000;
	private String host = "localhost";
	private Socket socketConexion;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String respuesta;
	private String tipo;

	private Usuario usuario;
	private ArrayList<Actividad> actividadesPropias = new ArrayList<Actividad>();
	private ArrayList<Actividad> actividades = new ArrayList<Actividad>();
	private ArrayList<Actividad> inscripciones = new ArrayList<Actividad>();
	private Principal ventana;

	public Conexion(String nombre_usuario, String contraseña, String tipo) {
		this.tipo = tipo;
		if (conectarAlServidorInicio(nombre_usuario, contraseña, tipo)) {
			mostrarVentana();
		}
	}

	public Conexion(String nombre, String nombre_usuario, String contraseña, String email, Date fecha, String tipo) {
		this.tipo = tipo;
		if (conectarAlServidorRegistro(nombre, nombre_usuario, contraseña, email, fecha, tipo)) {
			mostrarVentana();
		}
	}

	private void mostrarVentana() {
		Platform.runLater(() -> {
			ventana = new Principal(this);
			ventana.setMaximized(true);
			ventana.show();
		});
	}

	private boolean conectarAlServidorInicio(String nombre_usuario, String contraseña, String tipo) {
		try {
			socketConexion = new Socket(host, puerto);
			dos = new DataOutputStream(socketConexion.getOutputStream());
			dis = new DataInputStream(socketConexion.getInputStream());

			String datos = "inicio:" + nombre_usuario + ":" + contraseña + ":" + tipo;
			enviarDatos(datos);

			respuesta = recibirDatos();

			if (respuesta.equals("inicioincorrecto")) {
				return false;
			} else {
				usuario = new Usuario();
				procesarRespuesta(respuesta);
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al conectar con servidor.");
			return false;
		}
	}

	private boolean conectarAlServidorRegistro(String nombre, String nombre_usuario, String contraseña, String email,
			Date fecha, String tipo) {
		try {
			socketConexion = new Socket(host, puerto);
			dos = new DataOutputStream(socketConexion.getOutputStream());
			dis = new DataInputStream(socketConexion.getInputStream());

			String datos = "registro:" + nombre + ":" + nombre_usuario + ":" + contraseña + ":" + email + ":"
					+ fecha.toString() + ":" + tipo;
			enviarDatos(datos);

			respuesta = recibirDatos();

			if (respuesta.equals("registroincorrecto")) {
				return false;
			} else {
				usuario = new Usuario();
				procesarRespuesta(respuesta);
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al conectar con servidor.");
			return false;
		}
	}

	private synchronized void enviarDatos(String datos) {
		try {
			dos.writeUTF(datos);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al enviar datos desde la conexion");
		}
	}

	private String recibirDatos() {
		String datos = "";
		try {
			if (dis != null) {
				datos = dis.readUTF();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al recibir datos en el cliente.");
		}
		return datos;
	}

	private void procesarRespuesta(String respuesta) {
		String[] valores = respuesta.split("#");

		usuario.asignarDatos(valores[1]);
		this.actividadesPropias = procesarActividades(valores[2], "sin_propias");
		this.actividades = procesarActividades(valores[3], "sin_actividades");
		this.inscripciones = procesarActividades(valores[4], "sin_inscripciones");
	}

	private ArrayList<Actividad> procesarActividades(String lista, String valorComprobar) {
		ArrayList<Actividad> actividades = new ArrayList<Actividad>();

		if (!lista.equals(valorComprobar)) {
			String[] acts = lista.split("~");

			for (int i = 0; i < acts.length; i++) {
				String[] a = acts[i].split("@");
				Actividad actividad = new Actividad(Integer.parseInt(a[0]), a[1], a[2], a[3], Date.valueOf(a[4]),
						Time.valueOf(a[5]), Boolean.parseBoolean(a[6]), a[7], a[8], a[9], Boolean.parseBoolean(a[10]),
						Integer.parseInt(a[11]), Integer.parseInt(a[12]), Integer.parseInt(a[13]),
						Integer.parseInt(a[14]), a[15], Integer.parseInt(a[16]), Integer.parseInt(a[17]));
				actividades.add(actividad);
			}
		}
		return actividades;
	}

	public void enviarNuevaActividad(Actividad a) {
		String actividad = "nuevaActividad#" + a.infoActividad();
		enviarDatos(actividad);
	}

	public void enviarBaja() {
		String baja = "baja#" + this.getUsuario().getId_usuario();
		enviarDatos(baja);
	}

	public void enviarModificarDatos(String datos) {
		String modificacion = "modificarDatosUsuario#" + datos;
		enviarDatos(modificacion);
	}

	public void enviarCerrarSesion() {
		enviarDatos("cerrarSesion#" + this.getTipo());
		cerrarConexion();
		ventana.close();
	}

	public void enviarVerActividadesPropias(int id) {
		enviarDatos("verActividadesPropias#" + id);
	}

	public void enviarActualizar(int id, String tipo) {
		enviarDatos("actualizar#" + id + "#" + tipo);
	}

	public void enviarVerInscripciones(int id, String tipo) {
		enviarDatos("verInscripciones#" + id + "#" + tipo);
	}

	public void enviarModificarActividad(Actividad a) {
		enviarDatos("modificarActividad#" + a.infoActividad());
	}

	public void enviarCancelarActividad(Actividad a) {
		enviarDatos("cancelarActividad#" + a.infoActividad());
	}

	public void enviarInscripcionActividad(int id_usuario, int id_actividad, String tipo) {
		enviarDatos("inscripcion#" + id_usuario + "#" + id_actividad + "#" + tipo);
	}

	public void enviarCancelarInscripcion(int id_usuario, int id_actividad, String tipo) {
		enviarDatos("cancelarInscripcion#" + id_usuario + "#" + id_actividad + "#" + tipo);
	}

	@Override
	public void run() {
		try {
			while (!socketConexion.isClosed()) {
				String accion = recibirDatos();
				String[] datos = accion.split("#");

				switch (datos[0]) {
				case "nuevaActividad":
					procesarNuevaActividad(datos[1]);
					break;
				case "baja":
					procesarBaja();
					break;
				case "modificarDatosUsuario":
					if (!datos[1].equals("error")) {
						procesarModificarInformacionUsuario(datos[1]);
					} else {
						Platform.runLater(() -> {
							ventana.alerta("Contraseña no válida.", "Error");
						});
					}
					break;
				case "actividadesPropias":
					procesarActividadesPropias(datos[1]);
					break;
				case "actividadesNoPropias":
					procesarActividadesNoPropias(datos[1]);
					break;
				case "inscripciones":
					procesarInscripciones(datos[1]);
					break;
				case "actividadActualizada":
					procesarActividadActualizada(datos[1]);
					break;
				case "actividadCancelada":
					procesarActividadCancelada(datos[1]);
					break;
				case "inscripcion":
					procesarInscripcion(datos[1], Integer.parseInt(datos[2]));
					break;
				case "asignarOfertante":
					procesarInscripcion(datos[1], Integer.parseInt(datos[2]));
					break;
				case "inscripcionCancelada":
					procesarInscripcionCancelada(datos[1], Integer.parseInt(datos[2]));
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al procesar informacion recibida.");
		} finally {
			cerrarConexion();
		}
	}

	private synchronized void procesarNuevaActividad(String datos) {
		Actividad a = obtenerActividad(datos);
		if (this.getUsuario().getId_usuario() == a.getId_usuario_propietario()) {
			actividadesPropias.add(a);

			if (ventana.getVentana().equals("propias")) {
				if (!ventana.getActividadesMostrar().contains(a)) {
					ventana.getActividadesMostrar().add(a);
				}
				Platform.runLater(() -> {
					actualizarVentana();
				});
			}

		} else {
			actividades.add(a);

			if (ventana.getVentana().equals("noPropias")) {
				if (!ventana.getActividadesMostrar().contains(a)) {
					ventana.getActividadesMostrar().add(a);
				}

				Platform.runLater(() -> {
					actualizarVentana();
				});
			}
		}
	}

	private synchronized void procesarBaja() {
		Platform.runLater(() -> {
			ventana.getAdvertencia().setVisible(true);
			if (ventana.getVentana().equals("propias")) {
				this.enviarVerActividadesPropias(this.getUsuario().getId_usuario());
			}
			if (ventana.getVentana().equals("noPropias")) {
				this.enviarActualizar(this.getUsuario().getId_usuario(), this.getTipo());
			}
			if (ventana.getVentana().equals("inscripciones")) {
				this.enviarVerInscripciones(this.getUsuario().getId_usuario(), this.getTipo());
			}
		});
	}

	private void procesarModificarInformacionUsuario(String datos) {
		usuario.asignarDatos(datos);

		Platform.runLater(() -> {
			ventana.getIcono().setText(usuario.getNombre_usuario());
		});
	}

	private void procesarActividadesPropias(String datos) {
		procesarActividades(datos, "sin_actividades", "propias", actividadesPropias);
	}

	private void procesarActividadesNoPropias(String datos) {
		procesarActividades(datos, "sin_actividades", "noPropias", actividades);
	}

	private void procesarInscripciones(String datos) {
		procesarActividades(datos, "sin_inscripciones", "inscripciones", inscripciones);
	}

	private synchronized void procesarActividades(String datos, String comprobante, String tipoVista,
			ArrayList<Actividad> a) {
		ArrayList<Actividad> actividades = procesarActividades(datos, comprobante);
		Platform.runLater(() -> {
			ventana.setVentana(tipoVista);
			if (actividades.isEmpty()) {
				ventana.getActividadesMostrar().clear();
			} else {
				a.clear();
				a.addAll(actividades);
				ventana.setActividadesMostrar(actividades);
			}
			actualizarVentana();
		});
	}

	private synchronized void procesarActividadActualizada(String datos) {
		Actividad a = obtenerActividad(datos);
		boolean actualizar = false;

		if (this.getUsuario().getId_usuario() == a.getId_usuario_propietario()) {

			actualizarLista(actividadesPropias, a);
			ventana.setActividadesMostrar(actividadesPropias);
			Platform.runLater(() -> {
				actualizarVentana();
			});

		} else {

			actualizarLista(actividades, a);
			actualizarLista(inscripciones, a);

			if (ventana.getVentana().equals("inscripciones")) {
				if (ventana.getActividadesMostrar().isEmpty()) {
					actualizar = false;
				} else {
					for (Actividad act : ventana.getActividadesMostrar()) {
						if (a.equals(act)) {
							actualizar = true;
							break;
						}
					}
				}
			}

			if (ventana.getVentana().equalsIgnoreCase("noPropias")) {
				actualizar = true;
			}

			if (actualizar) {
				if (ventana.getVentana().equals("noPropias")) {
					ventana.setActividadesMostrar(actividades);
				}

				if (ventana.getVentana().equals("inscripciones")) {
					ventana.setActividadesMostrar(inscripciones);
				}

				Platform.runLater(() -> {
					actualizarVentana();
				});
			}
		}
	}

	private synchronized void procesarActividadCancelada(String datos) {
		Actividad a = obtenerActividad(datos);

		boolean actualizar = false;

		if (a.getId_usuario_propietario() == this.getUsuario().getId_usuario()) {
			for (int i = 0; i < actividadesPropias.size(); i++) {
				Actividad act = actividadesPropias.get(i);
				if (act.getId_actividad() == a.getId_actividad()) {
					actividadesPropias.remove(act);
					break;
				}
			}
			Platform.runLater(() -> {
				ventana.setActividadesMostrar(actividadesPropias);

				actualizarVentana();
			});
		} else {
			for (int i = 0; i < actividades.size(); i++) {
				Actividad act = actividades.get(i);
				if (act.getId_actividad() == a.getId_actividad()) {
					actividades.remove(act);
					if (ventana.getVentana().equals("noPropias")) {
						actualizar = true;
					}
					break;
				}
			}

			for (int i = 0; i < inscripciones.size(); i++) {
				Actividad act = inscripciones.get(i);
				if (act.getId_actividad() == a.getId_actividad()) {
					inscripciones.remove(act);
					if (ventana.getVentana().equals("inscripciones")) {
						actualizar = true;
					}
					break;
				}
			}

			if (actualizar) {
				Platform.runLater(() -> {
					if (ventana.getVentana().equals("inscripciones")) {
						ventana.setActividadesMostrar(inscripciones);
					}
					if (ventana.getVentana().equals("noPropias")) {
						ventana.setActividadesMostrar(actividades);
					}

					actualizarVentana();
				});
			}
		}

	}

	private synchronized void procesarInscripcion(String datos, int id_usuario) {
		Actividad a = obtenerActividad(datos);

		boolean actualizar = false;

		if (this.getUsuario().getId_usuario() == id_usuario) {
			this.getInscripciones().add(a);
		}

		actualizar = actualizarLista(ventana.getActividadesMostrar(), a);
		if (actualizar) {
			Platform.runLater(() -> {
				actualizarVentana();
			});
		}
	}

	private synchronized void procesarInscripcionCancelada(String datos, int id_usuario) {
		Actividad a = obtenerActividad(datos);
		boolean actualizar = false;

		if (this.getUsuario().getId_usuario() == id_usuario) {
			inscripciones.remove(a);

			if (ventana.getVentana().equals("noPropias")) {
				actualizar = actualizarLista(ventana.getActividadesMostrar(), a);
			}
			if (ventana.getVentana().equals("inscripciones")) {
				ventana.setActividadesMostrar(inscripciones);
				actualizar = true;
			}

		} else {
			actualizar = actualizarLista(ventana.getActividadesMostrar(), a);
		}

		if (actualizar) {
			Platform.runLater(() -> {
				actualizarVentana();
			});
		}
	}

	private void actualizarVentana() {
		ventana.getActividades().getChildren().clear();
		ventana.setActividades(ventana.mostrarActividades());

		ScrollPane scrollpane = new ScrollPane(ventana.getActividades());
		scrollpane.setFitToWidth(true);
		scrollpane.setFitToHeight(true);
		scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollpane.setStyle(
				"-fx-background-color: white; -fx-border-color: transparent; -fx-border-width: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

		ventana.getPrincipal().setCenter(scrollpane);
	}

	private Actividad obtenerActividad(String info) {
		String[] d = info.split("@");
		Actividad a = new Actividad(Integer.parseInt(d[0]), d[1], d[2], d[3], Date.valueOf(d[4]), Time.valueOf(d[5]),
				Boolean.parseBoolean(d[6]), d[7], d[8], d[9], Boolean.parseBoolean(d[10]), Integer.parseInt(d[11]),
				Integer.parseInt(d[12]), Integer.parseInt(d[13]), Integer.parseInt(d[14]), d[15],
				Integer.parseInt(d[16]), Integer.parseInt(d[17]));
		return a;
	}

	private boolean actualizarLista(ArrayList<Actividad> actividades, Actividad a) {
		for (int i = 0; i < actividades.size(); i++) {
			Actividad act = actividades.get(i);
			if (act.equals(a)) {
				actividades.set(i, a);
				return true;
			}
		}
		return false;
	}

	public void cerrarConexion() {
		try {
			if (dis != null) {
				dis.close();
			}
			if (dos != null) {
				dos.close();
			}
			if (socketConexion != null) {
				socketConexion.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al cerrar conexion.");
		}
	}

	// Getters y setters
	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public ArrayList<Actividad> getActividadesPropias() {
		return actividadesPropias;
	}

	public void setActividadesPropias(ArrayList<Actividad> actividadesPropias) {
		this.actividadesPropias = actividadesPropias;
	}

	public ArrayList<Actividad> getActividades() {
		return actividades;
	}

	public void setActividades(ArrayList<Actividad> actividades) {
		this.actividades = actividades;
	}

	public ArrayList<Actividad> getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(ArrayList<Actividad> inscripciones) {
		this.inscripciones = inscripciones;
	}

}
