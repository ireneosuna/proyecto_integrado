package servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import modelo.Actividad;
import modelo.Inscripcion;
import modelo.Usuario;

public class ConexionBBDD {

	// Conexion con base de datos
	private static final String usuario = "irene";
	private static final String pass = "irene";
	private static final String conexion = "jdbc:mysql://localhost:3306/proyecto_irene";

	/**
	 * Obtiene el id del usuario a partir de sus credenciales
	 * 
	 * @param nombre
	 * @param con
	 * @param tipo
	 * @return el id
	 */
	public int obtenerIdUsuario(String nombre, String con) {
		int id = -1;
		Connection connection = null;
		String consulta = "SELECT u.ID_usuario FROM usuario u WHERE u.nombre_usuario COLLATE utf8mb4_bin  = ? AND u.contrasena COLLATE utf8mb4_bin  = ?;";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setString(1, nombre);
			ps.setString(2, con);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				id = resultSet.getInt("ID_usuario");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener id del usuario a partir de sus credenciales.");
		}
		return id;
	}

	/**
	 * Comprueba si existe un usuario registrado con el nombre pasado por
	 * parámetros
	 * 
	 * @param nombre
	 * @return -1 o id del usuario
	 */
	public int obtenerExisteUsuario(String nombre) {
		int id = -1;
		Connection connection = null;
		String consulta = "SELECT u.ID_usuario FROM usuario u  WHERE u.nombre_usuario COLLATE utf8mb4_bin = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setString(1, nombre);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				id = resultSet.getInt("ID_usuario");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al comprobar si existe un usuario con nombre pasado.");
		}
		return id;
	}

	/**
	 * Obtiene los datos personales de un usuario
	 * 
	 * @param id
	 * @return String con los datos obtenidos
	 */
	public String obtenerDatosUsuario(int id) {
		Connection connection = null;
		String consulta = "SELECT u.nombre_completo, u.nombre_usuario, u.email, u.fecha_nacimiento FROM usuario u  WHERE u.ID_usuario = ?",
				datos = "";
		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				datos += resultSet.getString("nombre_completo") + "/";
				datos += resultSet.getString("nombre_usuario") + "/";
				datos += resultSet.getString("email") + "/";
				datos += resultSet.getDate("fecha_nacimiento");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener información usuario.");
		}
		return datos;
	}

	/**
	 * Comprueba si la contraseña es correcta
	 * 
	 * @param id
	 * @param pass1
	 * @return true o false
	 */
	public boolean passwordCorrecta(int id, String pass1) {
		Connection connection = null;
		String consulta = "SELECT u.contrasena FROM usuario u WHERE ID_usuario = ?";
		String passObtenida = "";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				passObtenida = resultSet.getString("contrasena");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al comprobar contraseña.");
		}
		return passObtenida.equals(pass1);
	}

	/**
	 * Actualiza la información personal del usuario
	 * 
	 * @param datos
	 * @return true o false
	 */
	public boolean actualizarInformacionUsuario(String datos) {
		Connection connection = null;
		String[] d = datos.split("/");

		String actualizar = "UPDATE usuario SET nombre_completo = ?,nombre_usuario = ?,contrasena = ?,email = ?,fecha_nacimiento = ? WHERE ID_usuario = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(actualizar);
			ps.setString(1, d[1]);
			ps.setString(2, d[2]);
			ps.setString(3, d[6]);
			ps.setString(4, d[3]);
			ps.setDate(5, java.sql.Date.valueOf(d[4]));
			ps.setInt(6, Integer.parseInt(d[0]));

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al actualizar información usuario.");
			return false;
		}
		return true;
	}

	/**
	 * Actualiza los datos de una actividad
	 * 
	 * @param a
	 * @return true o false
	 */
	public boolean actualizarActividad(modelo.Actividad a) {
		Connection connection = null;
		String actualizar = "UPDATE Actividad SET nombre_actividad = ?, descripcion = ?, fecha = ?, hora = ?, transporte = ?, ciudad_partida = ?, ciudad_actividad = ?, \r\n"
				+ "idioma = ?,  mascotas = ?, capacidad_personas = ?, personas_actuales = ?, precio = ?, edad_recomendada = ?, codigo_vestimenta = ?\r\n"
				+ "WHERE ID_actividad = ?;";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(actualizar);
			ps.setString(1, a.getNombre_actividad());
			ps.setString(2, a.getDescripcion());
			ps.setDate(3, a.getFecha());
			ps.setTime(4, a.getHora());
			ps.setBoolean(5, a.isTransporte());
			ps.setString(6, a.getCiudad_partida());
			ps.setString(7, a.getCiudad_actividad());
			ps.setString(8, a.getIdioma());
			ps.setBoolean(9, a.isMascotas());
			ps.setInt(10, a.getCapacidad_personas());
			ps.setInt(11, a.getPersonas_actuales());
			ps.setInt(12, a.getPrecio());
			ps.setInt(13, a.getEdad_recomendada());
			ps.setString(14, a.getCodigo_vestimenta());
			ps.setInt(15, a.getId_actividad());

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al actualizar actividad.");
			return false;
		}
		return true;
	}

	/**
	 * Obtiene el id del rol pasado por parámetro
	 * 
	 * @param rol
	 * @return
	 */
	public int obtenerIdRol(String rol) {
		Connection connection = null;
		String consulta = "SELECT r.ID_rol FROM rol r WHERE r.valor = ?";
		int id_rol = -1;

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setString(1, rol);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				id_rol = resultSet.getInt("ID_rol");
			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			System.out.println("Error al obtener rol por id.");
		}
		return id_rol;
	}

	/**
	 * Obtiene las actividades de un usuario por id
	 * 
	 * @param id
	 * @return ArrayList<Actividad>
	 */
	public ArrayList<Actividad> obtenerActividadesUsuario(int id) {
		Connection connection = null;
		String consulta = "SELECT * FROM Actividad a WHERE a.ID_usuario_propietario = ?";
		ArrayList<Actividad> actividadesPropias = new ArrayList<Actividad>();

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Actividad actividad = new Actividad();
				actividad.setId_actividad(resultSet.getInt("ID_actividad"));
				actividad.setNombre_actividad(resultSet.getString("nombre_actividad"));
				actividad.setTipo(resultSet.getString("tipo"));
				actividad.setDescripcion(resultSet.getString("descripcion"));
				actividad.setFecha(resultSet.getDate("fecha"));
				actividad.setHora(resultSet.getTime("hora"));
				actividad.setTransporte(resultSet.getBoolean("transporte"));
				actividad.setCiudad_partida(resultSet.getString("ciudad_partida"));
				actividad.setCiudad_actividad(resultSet.getString("ciudad_actividad"));
				actividad.setIdioma(resultSet.getString("idioma"));
				actividad.setMascotas(resultSet.getBoolean("mascotas"));
				actividad.setCapacidad_personas(resultSet.getInt("capacidad_personas"));
				actividad.setPersonas_actuales(resultSet.getInt("personas_actuales"));
				actividad.setPrecio(resultSet.getInt("precio"));
				actividad.setEdad_recomendada(resultSet.getInt("edad_recomendada"));
				actividad.setCodigo_vestimenta(resultSet.getString("codigo_vestimenta"));
				actividad.setId_usuario_propietario(resultSet.getInt("ID_usuario_propietario"));
				actividad.setId_ofertante(resultSet.getInt("ID_ofertante"));

				actividadesPropias.add(actividad);
			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener actividades de un usuario.");
		}

		return actividadesPropias;
	}

	/**
	 * Obtiene las actividades que no pertenecen a un usuario pasado por parámetro
	 * 
	 * @param id
	 * @param tipo
	 * @return ArrayList<Actividad>
	 */
	public ArrayList<Actividad> obtenerActividadesNoPropias(int id, String tipo) {
		Connection connection = null;
		String consulta = "";

		if (tipo.equalsIgnoreCase("consumidor")) {
			consulta = "SELECT a.* FROM Actividad a WHERE a.ID_usuario_propietario != ? AND a.fecha > ? AND a.personas_actuales < a.capacidad_personas AND a.tipo = ?;";
		} else {
			consulta = "SELECT * FROM Actividad a WHERE a.ID_usuario_propietario != ? AND a.fecha > ? AND a.ID_ofertante = ? AND  a.tipo = ?";
		}

		ArrayList<Actividad> actividades = new ArrayList<Actividad>();

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);

			if (tipo.equalsIgnoreCase("consumidor")) {
				ps.setInt(1, id);
				ps.setDate(2, (java.sql.Date.valueOf(LocalDate.now())));
				ps.setString(3, "actividad");
			} else {
				ps.setInt(1, id);
				ps.setDate(2, (java.sql.Date.valueOf(LocalDate.now())));
				ps.setInt(3, -1);
				ps.setString(4, "anuncio");
			}

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Actividad actividad = new Actividad();
				actividad.setId_actividad(resultSet.getInt("ID_actividad"));
				actividad.setNombre_actividad(resultSet.getString("nombre_actividad"));
				actividad.setTipo(resultSet.getString("tipo"));
				actividad.setDescripcion(resultSet.getString("descripcion"));
				actividad.setFecha(resultSet.getDate("fecha"));
				actividad.setHora(resultSet.getTime("hora"));
				actividad.setTransporte(resultSet.getBoolean("transporte"));
				actividad.setCiudad_partida(resultSet.getString("ciudad_partida"));
				actividad.setCiudad_actividad(resultSet.getString("ciudad_actividad"));
				actividad.setIdioma(resultSet.getString("idioma"));
				actividad.setMascotas(resultSet.getBoolean("mascotas"));
				actividad.setCapacidad_personas(resultSet.getInt("capacidad_personas"));
				actividad.setPersonas_actuales(resultSet.getInt("personas_actuales"));
				actividad.setPrecio(resultSet.getInt("precio"));
				actividad.setEdad_recomendada(resultSet.getInt("edad_recomendada"));
				actividad.setCodigo_vestimenta(resultSet.getString("codigo_vestimenta"));
				actividad.setId_usuario_propietario(resultSet.getInt("ID_usuario_propietario"));
				actividad.setId_ofertante(resultSet.getInt("ID_ofertante"));

				actividades.add(actividad);

			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener actividades no propias.");
		}
		return actividades;
	}

	/**
	 * Obtiene las inscripciones de un usuario
	 * 
	 * @param id
	 * @param tipo
	 * @return ArrayList<Actividad>
	 */
	public ArrayList<Actividad> obtenerIncripciones(int id, String tipo) {
		Connection connection = null;
		String consulta;
		if (tipo.equalsIgnoreCase("consumidor")) {
			consulta = "SELECT ID_actividad FROM Inscripcion WHERE ID_usuario = ?";
		} else {
			consulta = "SELECT * FROM Actividad a WHERE a.ID_ofertante = ?";
		}
		ArrayList<Actividad> actividades = new ArrayList<Actividad>();

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				if (tipo.equalsIgnoreCase("consumidor")) {
					Actividad a = obtenerActividad(resultSet.getInt("ID_actividad"));
					actividades.add(a);
				} else {
					Actividad a = new Actividad();
					a.setId_actividad(resultSet.getInt("ID_actividad"));
					a.setNombre_actividad(resultSet.getString("nombre_actividad"));
					a.setTipo(resultSet.getString("tipo"));
					a.setDescripcion(resultSet.getString("descripcion"));
					a.setFecha(resultSet.getDate("fecha"));
					a.setHora(resultSet.getTime("hora"));
					a.setTransporte(resultSet.getBoolean("transporte"));
					a.setCiudad_partida(resultSet.getString("ciudad_partida"));
					a.setCiudad_actividad(resultSet.getString("ciudad_actividad"));
					a.setIdioma(resultSet.getString("idioma"));
					a.setMascotas(resultSet.getBoolean("mascotas"));
					a.setCapacidad_personas(resultSet.getInt("capacidad_personas"));
					a.setPersonas_actuales(resultSet.getInt("personas_actuales"));
					a.setPrecio(resultSet.getInt("precio"));
					a.setEdad_recomendada(resultSet.getInt("edad_recomendada"));
					a.setCodigo_vestimenta(resultSet.getString("codigo_vestimenta"));
					a.setId_usuario_propietario(resultSet.getInt("ID_usuario_propietario"));
					a.setId_ofertante(resultSet.getInt("ID_ofertante"));

					actividades.add(a);
				}
			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener actividades a las que está inscrito un usuario.");
		}

		return actividades;
	}

	/**
	 * Obtiene los roles a los que está registrado un usuario
	 * 
	 * @param usu
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> obtenerIdRolesUsuario(Usuario usu) {
		ArrayList<Integer> roles = new ArrayList<Integer>();
		Connection connection = null;
		String consulta = "SELECT * FROM proyecto_irene.usuariorol WHERE ID_usuario = ?;";
		int id_rol;
		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, usu.getId_usuario());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				id_rol = resultSet.getInt("ID_rol");
				roles.add(id_rol);
			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener roles del usuario.");
		}
		return roles;
	}

	/**
	 * Obtiene una Actividad a partir del id
	 * 
	 * @param id_actividad
	 * @return Actividad
	 */
	public Actividad obtenerActividad(int id_actividad) {
		Connection connection = null;
		String consulta = "SELECT * FROM Actividad a WHERE a.ID_actividad = ?";
		Actividad actividad = new Actividad();

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id_actividad);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				actividad.setId_actividad(resultSet.getInt("ID_actividad"));
				actividad.setNombre_actividad(resultSet.getString("nombre_actividad"));
				actividad.setTipo(resultSet.getString("tipo"));
				actividad.setDescripcion(resultSet.getString("descripcion"));
				actividad.setFecha(resultSet.getDate("fecha"));
				actividad.setHora(resultSet.getTime("hora"));
				actividad.setTransporte(resultSet.getBoolean("transporte"));
				actividad.setCiudad_partida(resultSet.getString("ciudad_partida"));
				actividad.setCiudad_actividad(resultSet.getString("ciudad_actividad"));
				actividad.setIdioma(resultSet.getString("idioma"));
				actividad.setMascotas(resultSet.getBoolean("mascotas"));
				actividad.setCapacidad_personas(resultSet.getInt("capacidad_personas"));
				actividad.setPersonas_actuales(resultSet.getInt("personas_actuales"));
				actividad.setPrecio(resultSet.getInt("precio"));
				actividad.setEdad_recomendada(resultSet.getInt("edad_recomendada"));
				actividad.setCodigo_vestimenta(resultSet.getString("codigo_vestimenta"));
				actividad.setId_usuario_propietario(resultSet.getInt("ID_usuario_propietario"));
				actividad.setId_ofertante(resultSet.getInt("ID_ofertante"));
			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener actividad.");
		}
		return actividad;
	}

	/**
	 * Inserta un nuevo usuario
	 * 
	 * @param usu
	 */
	public void insertarNuevoUsuario(Usuario usu) {
		Connection connection = null;
		String insert = "INSERT INTO usuario (nombre_completo, nombre_usuario, contrasena, email, fecha_nacimiento) VALUES (?,?,?,?,?);";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			connection.setAutoCommit(false);

			try (PreparedStatement ps = connection.prepareStatement(insert)) {
				ps.setString(1, usu.getNombre_completo());
				ps.setString(2, usu.getNombre_usuario());
				ps.setString(3, usu.getContrasena());
				ps.setString(4, usu.getEmail());
				ps.setDate(5, usu.getFecha_nacimiento());

				ps.executeUpdate();
				connection.commit();

			} catch (Exception e) {
				e.printStackTrace();
				connection.rollback();
				System.out.println("Fallo al insertar nuevo usuario.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al insertar nuevo usuario.");
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * Asigna el rol correspondiente a cada usuario
	 * 
	 * @param id_rol
	 * @param id_usuario
	 * @return true o false
	 */
	public boolean asignarRolUsuario(int id_rol, int id_usuario) {
		Connection connection = null;

		String insert = "INSERT INTO usuariorol (ID_usuario, ID_rol) VALUES (?,?);";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			connection.setAutoCommit(false);

			try (PreparedStatement ps = connection.prepareStatement(insert)) {
				ps.setInt(1, id_usuario);
				ps.setInt(2, id_rol);

				ps.executeUpdate();
				ps.close();
				connection.commit();

			} catch (Exception e) {
				e.printStackTrace();
				connection.rollback();
				System.out.println("Fallo al asignar rol a usuario.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al asignar rol a usuario.");
			return false;
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
					connection.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * Elimina un usuario pasado por parametro
	 * 
	 * @param usu
	 */
	public void eliminarUsuario(int id_usuario) {
		Connection connection = null;
		String eliminar = "DELETE FROM Usuario WHERE ID_usuario = ?;";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(eliminar);
			ps.setInt(1, id_usuario);

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al eliminar usuario.");
		}
	}

	/**
	 * Crea una nueva actividad con los datos pasados por parámetro
	 * 
	 * @param a
	 * @return -1 o id actividad
	 */
	public int crearNuevaActividad(Actividad a) {
		Connection connection = null;
		String insert = "INSERT INTO Actividad (nombre_actividad, tipo, descripcion, fecha, hora, transporte, ciudad_partida, ciudad_actividad, idioma, "
				+ "mascotas, capacidad_personas, personas_actuales, precio, edad_recomendada, codigo_vestimenta, ID_usuario_propietario, ID_ofertante) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, a.getNombre_actividad());
			ps.setString(2, a.getTipo());
			ps.setString(3, a.getDescripcion());
			ps.setDate(4, a.getFecha());
			ps.setTime(5, a.getHora());
			ps.setBoolean(6, a.isTransporte());
			ps.setString(7, a.getCiudad_partida());
			ps.setString(8, a.getCiudad_actividad());
			ps.setString(9, a.getIdioma());
			ps.setBoolean(10, a.isMascotas());
			ps.setInt(11, a.getCapacidad_personas());
			ps.setInt(12, a.getPersonas_actuales());
			ps.setInt(13, a.getPrecio());
			ps.setInt(14, a.getEdad_recomendada());
			ps.setString(15, a.getCodigo_vestimenta());
			ps.setInt(16, a.getId_usuario_propietario());
			ps.setInt(17, a.getId_ofertante());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al crear nueva actividad.");
		}
		return -1;
	}

	/**
	 * Inscribe a un usuario en una actividad
	 * 
	 * @param id_usuario
	 * @param id_actividad
	 */
	public void inscribir(int id_usuario, int id_actividad) {
		Connection connection = null;

		String insert = "INSERT INTO Inscripcion (ID_actividad ,ID_usuario, fecha_inscripcion) VALUES (?,?,?)";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(insert);

			ps.setInt(1, id_actividad);
			ps.setInt(2, id_usuario);
			ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al inscribir usuario en actividad");
		}
	}

	/**
	 * Asigna ofertante a un anuncio
	 * 
	 * @param id_usuario
	 * @param id_actividad
	 */
	public void ofertante(int id_usuario, int id_actividad) {
		Connection connection = null;

		String actualizar = "UPDATE Actividad SET ID_ofertante = ? WHERE ID_actividad = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(actualizar);
			ps.setInt(1, id_usuario);
			ps.setInt(2, id_actividad);

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al asignar ofertante");
		}
	}

	/**
	 * Da de baja a un usuario, eliminando toda su información
	 * 
	 * @param id
	 */
	public void darDeBaja(int id) {
		if (comprobarExisteUsuario(id)) {
			ArrayList<Actividad> actPropias = obtenerActividadesUsuario(id);

			for (Actividad a : actPropias) {
				ArrayList<Inscripcion> inscripciones = obtenerIncripcionesActividad(a);
				for (Inscripcion i : inscripciones) {
					eliminarInscripcion(i);
				}
				eliminarActividad(a);
			}

			ArrayList<Inscripcion> inscripcionesUsuario = obtenerIncripcionesUsuario(id);
			for (Inscripcion i : inscripcionesUsuario) {
				Actividad a = obtenerActividad(i.getId_actividad());
				if (a.getFecha().after(new Date())) {
					borrarPersonasActuales(i.getId_actividad());
				}
				eliminarInscripcion(i);
			}

			actualizarOfertantesAnuncios(id);
			eliminarUsuarioRol(id);
			eliminarUsuario(id);
		}
	}

	/**
	 * Comprueba si existe el usuario
	 * 
	 * @param id
	 * @return true o false
	 */
	private boolean comprobarExisteUsuario(int id) {
		Connection connection = null;
		String consulta = "SELECT u.nombre_usuario FROM Usuario u WHERE u.ID_usuario = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();

			return resultSet.next();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al comprobar si existe un usuario");
		}
		return false;

	}

	/**
	 * Obtiene las inscripciones a una actividad
	 * 
	 * @param a
	 * @return ArrayList<Inscripcion>
	 */
	public ArrayList<Inscripcion> obtenerIncripcionesActividad(Actividad a) {
		Connection connection = null;
		ArrayList<Inscripcion> inscripciones = new ArrayList<Inscripcion>();
		String consulta = "SELECT * FROM Inscripcion WHERE ID_actividad = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, a.getId_actividad());
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Inscripcion i = new Inscripcion();
				i.setId_actividad(a.getId_actividad());
				i.setId_usuario(resultSet.getInt("ID_usuario"));
				i.setFecha_inscripcion(resultSet.getDate("fecha_inscripcion"));

				inscripciones.add(i);
			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener inscripciones de una actividad.");
		}
		return inscripciones;
	}

	/**
	 * Obtiene las inscripciones de un usuario
	 * 
	 * @param id_usuario
	 * @return ArrayList<Inscripcion>
	 */
	public ArrayList<Inscripcion> obtenerIncripcionesUsuario(int id_usuario) {
		Connection connection = null;
		ArrayList<Inscripcion> inscripciones = new ArrayList<Inscripcion>();
		String consulta = "SELECT * FROM Inscripcion WHERE ID_usuario = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id_usuario);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Inscripcion i = new Inscripcion();
				i.setId_actividad(resultSet.getInt("ID_actividad"));
				i.setId_usuario(resultSet.getInt("ID_usuario"));
				i.setFecha_inscripcion(resultSet.getDate("fecha_inscripcion"));

				inscripciones.add(i);
			}

			resultSet.close();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener inscripciones de un usuario.");
		}

		return inscripciones;
	}

	/**
	 * Elimina la inscripción pasada por parámetro
	 * 
	 * @param i
	 */
	public void eliminarInscripcion(Inscripcion i) {
		Connection connection = null;
		String eliminar = "DELETE FROM Inscripcion WHERE ID_actividad = ? AND ID_usuario = ?;";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(eliminar);
			ps.setInt(1, i.getId_actividad());
			ps.setInt(2, i.getId_usuario());

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al eliminar inscripcion.");
		}
	}

	/**
	 * Elimina la actividad pasada por parámetro
	 * 
	 * @param a
	 */
	public void eliminarActividad(Actividad a) {
		Connection connection = null;
		String eliminar = "DELETE FROM Actividad WHERE ID_actividad = ?;";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(eliminar);
			ps.setInt(1, a.getId_actividad());

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al eliminar actividad.");
		}
	}

	/**
	 * Borra a una persona de la actividad
	 * 
	 * @param id_actividad
	 */
	public void borrarPersonasActuales(int id_actividad) {
		Connection connection = null;
		int personas = obtenerPersonasActualesActividad(id_actividad) - 1;

		String actualizar = "UPDATE Actividad SET personas_actuales = ? WHERE ID_actividad = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(actualizar);
			ps.setInt(1, personas);
			ps.setInt(2, id_actividad);

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al actualizar personas actuales.");
		}
	}

	/**
	 * Añade una persona a la actividad
	 * 
	 * @param id_actividad
	 */
	public void addPersonasActividad(int id_actividad) {
		Connection connection = null;
		int personas = obtenerPersonasActualesActividad(id_actividad) + 1;

		String actualizar = "UPDATE Actividad SET personas_actuales = ? WHERE ID_actividad = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(actualizar);
			ps.setInt(1, personas);
			ps.setInt(2, id_actividad);

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al actualizar personas actuales.");
		}
	}

	/**
	 * Obtiene las personas actuales de la actividad
	 * 
	 * @param id_actividad
	 * @return
	 */
	private int obtenerPersonasActualesActividad(int id_actividad) {
		Connection connection = null;
		String consulta = "SELECT personas_actuales FROM Actividad WHERE ID_actividad = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);
			PreparedStatement ps = connection.prepareStatement(consulta);
			ps.setInt(1, id_actividad);
			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt("personas_actuales");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al obtener personas actuales actividad.");
		}

		return -1;
	}

	/**
	 * Actualiza el ofertante de un anuncio a -1
	 * 
	 * @param id_usuario
	 */
	public void actualizarOfertantesAnuncios(int id_usuario) {
		Connection connection = null;
		String actualizar = "UPDATE Actividad SET ID_ofertante = -1 WHERE ID_ofertante = ?";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(actualizar);
			ps.setInt(1, id_usuario);

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al actualizar id del ofertante");
		}
	}

	/**
	 * Elimina rol asociado a usuario
	 * 
	 * @param id_usuario
	 */
	public void eliminarUsuarioRol(int id_usuario) {
		Connection connection = null;
		String eliminar = "DELETE FROM UsuarioRol WHERE ID_usuario = ?;";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(eliminar);
			ps.setInt(1, id_usuario);

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al eliminar rol asociado a usuario.");
		}
	}

	/**
	 * Elimina inscripción
	 * @param id_usuario
	 * @param id_actividad
	 */
	public void eliminarInscripcion(int id_usuario, int id_actividad) {
		Connection connection = null;
		String eliminar = "DELETE FROM Inscripcion WHERE ID_usuario = ? AND ID_actividad = ?;";

		try {
			connection = DriverManager.getConnection(conexion, usuario, pass);

			PreparedStatement ps = connection.prepareStatement(eliminar);
			ps.setInt(1, id_usuario);
			ps.setInt(2, id_actividad);

			ps.executeUpdate();
			ps.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al eliminar inscripción.");
		}
	}
}
