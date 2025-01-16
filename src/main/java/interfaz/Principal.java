package interfaz;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import conexion.Conexion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Principal extends Stage {

	private String filtroActividad = "";
	private String busquedaActividad = "";
	private VBox actividades;
	private Conexion conexion;
	private BorderPane principal;
	private Label advertencia;
	private Button icono;
	private ArrayList<modelo.Actividad> actividadesMostrar = new ArrayList<modelo.Actividad>();
	private HBox busqueda;
	private String ventana = "noPropias";

	protected static final String BLANCO = "#FFFFFF";
	protected static final String NEGRO = "#000000";
	protected static final String GRIS = "#f2f3f5";
	protected static final String ROSA = "#fea6eb";
	protected static final String ROSA_OSCURO = "#b00b8c";
	protected static final String GRIS_OSCURO = "#c6c7c9";
	protected static final String GRIS_CLARO = "#fcfcfc";
	protected static final String VERDE = "#7ed957";
	protected static final String ROJO = "#ff3131";

	protected Font fuenteTitulo;
	protected Font fuenteSegundoTitulo;
	protected Font fuenteTexto;
	protected Font fuenteTexto2;
	protected Font fuenteAdvertencia;

	public Principal(Conexion c) {

		cargarFuentes();

		principal = new BorderPane();
		principal.setStyle("-fx-background-color: " + BLANCO);
		conexion = c;
		actividadesMostrar = conexion.getActividades();

		actividades = mostrarActividades();

		ScrollPane scrollpane = new ScrollPane(actividades);
		scrollpane.setFitToWidth(true);
		scrollpane.setFitToHeight(true);
		scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		scrollpane.setStyle("-fx-background-color: " + BLANCO
				+ "; -fx-border-color: transparent; -fx-border-width: 0; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");

		principal.setCenter(scrollpane);

		BorderPane inicial = new BorderPane();
		inicial.setPadding(new Insets(10));

		HBox info = new HBox(20);

		Button recargar = new Button("⭮");
		recargar.setStyle("-fx-background-color: " + ROSA + "; -fx-text-fill: " + NEGRO
				+ "; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 50%; -fx-min-width: 50px; -fx-min-height: 50px;");

		recargar.setOnAction(e -> {
			conexion.enviarActualizar(conexion.getUsuario().getId_usuario(), conexion.getTipo());
		});

		StackPane centrarRecargar = new StackPane();
		centrarRecargar.getChildren().add(recargar);

		info.getChildren().add(centrarRecargar);
		advertencia = new Label("Se ha producido una baja, compruebe sus actividades, pueden haberse cancelado.");
		advertencia.setFont(fuenteAdvertencia);
		info.getChildren().add(advertencia);
		advertencia.setVisible(false);
		advertencia.setStyle("-fx-text-fill: " + ROSA_OSCURO);
		principal.setOnMouseClicked(event -> advertencia.setVisible(false));

		inicial.setLeft(info);

		icono = new Button(conexion.getUsuario().getNombre_usuario());
		icono.setStyle("-fx-background-color: " + ROSA + "; -fx-text-fill: " + NEGRO
				+ "; -fx-font-size: 16px; -fx-background-radius: 50%; -fx-min-width: 100px; -fx-min-height: 100px;");

		Button baja = new Button("Dar de baja");
		baja.setStyle(estiloBotonFondo());
		baja.setOnAction(e -> {
			alertaConfirmacionBaja();
		});

		Button modificarDatos = new Button("Modificar información");
		modificarDatos.setStyle(estiloBotonFondo());
		modificarDatos.setOnAction(e -> {
			Stage modificarStage = new Stage();
			ModificarDatosUsuario modificar = new ModificarDatosUsuario(modificarStage, conexion);
		});
		Button cerrarSesion = new Button("Cerrar sesión");
		cerrarSesion.setStyle(estiloBotonFondo());
		cerrarSesion.setOnAction(e -> {
			alertaConfirmacionCerrarSesion();
		});

		BorderPane acciones = new BorderPane();
		VBox accion = new VBox(15, baja, modificarDatos);
		accion.setAlignment(Pos.CENTER);
		acciones.setTop(accion);
		acciones.setPadding(new Insets(45, 5, 5, 5));

		StackPane stackPane1 = new StackPane();
		stackPane1.getChildren().add(cerrarSesion);
		acciones.setBottom(stackPane1);

		acciones.setVisible(false);
		icono.setOnAction(e -> {
			if (acciones.isVisible()) {
				acciones.setVisible(false);
				principal.setRight(null);
			} else {
				acciones.setVisible(true);
				principal.setRight(acciones);
			}
		});

		inicial.setRight(icono);

		principal.setTop(inicial);

		BorderPane izquierda = new BorderPane();
		Button crear = new Button();
		crear.setStyle(estiloBotonFondo());
		if (conexion.getTipo().equalsIgnoreCase("consumidor")) {
			crear.setText("Nuevo anuncio");
		} else {
			crear.setText("Nueva actividad");
		}
		crear.setOnAction(e -> {
			Stage actividadStage = new Stage();
			interfaz.Actividad actividad = new interfaz.Actividad(actividadStage, conexion);
		});

		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(crear);

		izquierda.setTop(stackPane);

		HBox propios = new HBox(10);

		Button verActividadesPropias = new Button();
		verActividadesPropias.setStyle(estiloBotonFondo());
		if (conexion.getTipo().equalsIgnoreCase("consumidor")) {
			verActividadesPropias.setText("Mis anuncios");
		} else {
			verActividadesPropias.setText("Mis actividades");
		}

		verActividadesPropias.setOnAction(e -> {
			conexion.enviarVerActividadesPropias(conexion.getUsuario().getId_usuario());
		});

		Button inscripciones = new Button();
		inscripciones.setStyle(estiloBotonFondo());
		if (conexion.getTipo().equalsIgnoreCase("consumidor")) {
			inscripciones.setText("Inscripciones");
		} else {
			inscripciones.setText("Anuncios");
		}

		inscripciones.setOnAction(e -> {
			conexion.enviarVerInscripciones(conexion.getUsuario().getId_usuario(), conexion.getTipo());
		});

		propios.getChildren().addAll(verActividadesPropias, inscripciones);
		izquierda.setBottom(propios);
		izquierda.setPadding(new Insets(45, 5, 5, 5));

		principal.setLeft(izquierda);

		Scene scene = new Scene(principal);
		setScene(scene);

		this.setOnCloseRequest(event -> {
			conexion.cerrarConexion();
		});
	}

	private GridPane crearPanelActividad(modelo.Actividad actividad) {
		boolean inscrito = false;
		if (conexion.getInscripciones() != null) {
			for (modelo.Actividad a : conexion.getInscripciones()) {
				if (a.equals(actividad)) {
					inscrito = true;
					break;
				}
			}
		}

		GridPane panelActividad = new GridPane();
		panelActividad.setHgap(5);
		panelActividad.setVgap(5);
		panelActividad.setPadding(new Insets(20, 20, 20, 20));
		panelActividad.setStyle("-fx-background-color: " + GRIS);
		Label titulo = new Label();
		titulo.setFont(fuenteTexto2);

		if (actividad.getTipo().equals("anuncio")) {
			titulo.setText("Se busca ofertante para: " + actividad.getNombre_actividad());
		} else {
			titulo.setText(actividad.getNombre_actividad());
		}

		panelActividad.add(titulo, 0, 0);

		Label descripcion = new Label(actividad.getDescripcion());
		descripcion.setFont(fuenteTexto2);
		panelActividad.add(descripcion, 0, 1);

		Label asignado = new Label();
		asignado.setFont(fuenteTexto2);

		switch (actividad.getTipo()) {
		case "anuncio":
			if (actividad.getId_ofertante() == -1) {
				asignado.setText("Sin asignar");
				asignado.setStyle("-fx-text-fill:  " + ROJO + ";");
			} else {
				asignado.setText("Asignado");
				asignado.setStyle("-fx-text-fill:  " + VERDE + ";");
			}
			break;
		case "actividad":
			if (actividad.getCapacidad_personas() > actividad.getPersonas_actuales()) {
				asignado.setText("Sin completar");
				asignado.setStyle("-fx-text-fill:  " + ROJO + ";");
			} else {
				asignado.setText("Completa");
				asignado.setStyle("-fx-text-fill:  " + VERDE + ";");
			}
			break;
		}
		panelActividad.add(asignado, 1, 0);

		Label precio = new Label();
		precio.setFont(fuenteTexto2);
		if (actividad.getPrecio() <= 0) {
			precio.setText("Gratis");
		} else {
			precio.setText(actividad.getPrecio() + " €");
		}
		panelActividad.add(precio, 2, 0);
		GridPane.setHalignment(precio, HPos.RIGHT);

		LocalDate f = actividad.getFecha().toLocalDate();
		DateTimeFormatter formatear = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		Label fecha = new Label("Fecha: " + f.format(formatear));
		fecha.setFont(fuenteTexto2);
		panelActividad.add(fecha, 0, 2);

		Label hora = new Label("Hora: " + actividad.getHora());
		hora.setFont(fuenteTexto2);
		panelActividad.add(hora, 0, 3);

		String transporte = actividad.isTransporte() ? "(Transporte incluido)" : "(Sin transporte incluido)";

		Label ciudad = new Label();
		ciudad.setFont(fuenteTexto2);
		if (!actividad.isTransporte()) {
			ciudad.setText(actividad.getCiudad_actividad() + " " + transporte);
		} else {
			ciudad.setText(actividad.getCiudad_partida() + " > " + actividad.getCiudad_actividad() + " " + transporte);
		}
		panelActividad.add(ciudad, 0, 4);

		Label idioma = new Label("Idioma: " + actividad.getIdioma());
		idioma.setFont(fuenteTexto2);
		panelActividad.add(idioma, 0, 5);

		Label capacidadActual = new Label();
		capacidadActual.setFont(fuenteTexto2);

		if (actividad.getTipo().equals("anuncio")) {
			capacidadActual.setText(actividad.getCapacidad_personas()
					+ (actividad.getCapacidad_personas() == 1 ? " persona" : " personas"));
		} else {
			capacidadActual.setText(actividad.getPersonas_actuales() + "/" + actividad.getCapacidad_personas()
					+ (actividad.getCapacidad_personas() == 1 ? " persona" : " personas"));
		}

		panelActividad.add(capacidadActual, 2, 3);
		GridPane.setHalignment(capacidadActual, HPos.RIGHT);

		String edad = "Edad recomendada: " + actividad.getEdad_recomendada()
				+ (actividad.getEdad_recomendada() == 1 ? " año" : " años"),
				ropa = "Código vestimenta: " + actividad.getCodigo_vestimenta(),
				mascotas = actividad.isMascotas() ? "Mascotas permitidas" : "Mascotas no permitidas";

		ComboBox<String> infoAdicional = new ComboBox<String>();
		infoAdicional.getItems().addAll(edad, ropa, mascotas);
		infoAdicional.setStyle(estiloComboBox());
		infoAdicional.setValue("Información adicional");
		infoAdicional.setEditable(false);

		infoAdicional.setCellFactory(lv -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item);
					setFont(fuenteTexto2);
					setDisable(true);
				}
			}
		});

		infoAdicional.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null || !newValue.equals("Información adicional")) {
				infoAdicional.setValue("Información adicional");
			}
		});

		panelActividad.add(infoAdicional, 0, 6);

		if (actividad.getId_usuario_propietario() == conexion.getUsuario().getId_usuario()) {
			HBox funciones = new HBox(10);

			Button editar = new Button("Editar");
			editar.setStyle(estiloBotonFondo());
			editar.setPrefWidth(100);

			editar.setOnAction(e -> {
				if (actividad.getPersonas_actuales() == 0) {
					Stage modificarStage = new Stage();
					ModificarActividad modificar = new ModificarActividad(modificarStage, conexion, actividad);
				} else {
					alerta("No es posible editar una actividad con consumidores asociados.",
							"Actividad con consumidores");
				}
			});

			Button cancelar = new Button("Cancelar");
			cancelar.setStyle(estiloBotonFondo());
			cancelar.setPrefWidth(100);

			cancelar.setOnAction(e -> {
				alertaConfirmacion(actividad);
			});

			if (LocalDate.parse(actividad.getFecha().toString()).isBefore(LocalDate.now())) {
				editar.setDisable(true);
				cancelar.setDisable(true);
			}

			funciones.getChildren().addAll(editar, cancelar);
			funciones.setAlignment(Pos.CENTER_RIGHT);
			panelActividad.add(funciones, 2, 6);
			GridPane.setHalignment(funciones, HPos.RIGHT);
		} else {
			if (inscrito && conexion.getTipo().equalsIgnoreCase("consumidor")) {

				Button cancelar = new Button("Cancelar");
				cancelar.setStyle(estiloBotonFondo());
				cancelar.setPrefWidth(100);
				panelActividad.add(cancelar, 2, 6);
				GridPane.setHalignment(cancelar, HPos.RIGHT);

				cancelar.setOnAction(e -> {
					conexion.enviarCancelarInscripcion(conexion.getUsuario().getId_usuario(),
							actividad.getId_actividad(), conexion.getTipo());
				});

				if (LocalDate.parse(actividad.getFecha().toString()).isBefore(LocalDate.now())) {
					cancelar.setDisable(true);
				}

			} else {
				Button aceptar = new Button("Aceptar");
				aceptar.setStyle(estiloBotonFondo());
				aceptar.setPrefWidth(100);
				panelActividad.add(aceptar, 2, 6);
				GridPane.setHalignment(aceptar, HPos.RIGHT);

				aceptar.setOnAction(e -> {
					conexion.enviarInscripcionActividad(conexion.getUsuario().getId_usuario(),
							actividad.getId_actividad(), conexion.getTipo());
				});

				if (LocalDate.parse(actividad.getFecha().toString()).isBefore(LocalDate.now())) {
					aceptar.setDisable(true);
				}
				if (actividad.getTipo().equals("anuncio")) {
					if (actividad.getId_ofertante() > 0) {
						aceptar.setDisable(true);
					}
				} else {
					if (actividad.getPersonas_actuales() == actividad.getCapacidad_personas()) {
						aceptar.setDisable(true);
					}
				}
			}
		}

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50);
		col1.setHgrow(Priority.ALWAYS);

		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(25);
		col2.setHgrow(Priority.ALWAYS);

		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(25);
		col3.setHgrow(Priority.ALWAYS);
		panelActividad.getColumnConstraints().addAll(col1, col2, col3);

		return panelActividad;
	}

	private HBox crearBarraBusqueda() {
		ComboBox<String> filtro = new ComboBox<String>();
		filtro.setStyle(estiloComboBox());
		filtro.getItems().addAll("Filtro", "Fecha", "Hora", "Transporte", "Ciudad", "Idioma", "Mascotas", "Capacidad",
				"Precio", "Edad");
		filtro.setValue("Filtro");

		filtro.setCellFactory(param -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(item);
				setFont(fuenteTexto);
				setStyle("-fx-background-color: " + GRIS + "; -fx-text-fill:" + ROSA + ";");

				setOnMouseEntered(event -> {
					setStyle("-fx-background-color: " + ROSA + "; -fx-text-fill: " + BLANCO + ";");
				});

				setOnMouseExited(event -> {
					setStyle("-fx-background-color: " + BLANCO + "; -fx-text-fill: " + ROSA + ";");
				});
			}
		});

		TextField busqueda = new TextField();
		busqueda.setStyle(estiloCampoTexto());
		busqueda.setPromptText("Ingrese el término de búsqueda");
		HBox.setHgrow(busqueda, Priority.ALWAYS);

		Button buscar = new Button("▾");
		buscar.setStyle(estiloBotonFondo());

		buscar.setOnAction(e -> {
			setFiltroActividad(filtro.getValue());
			setBusquedaActividad(busqueda.getText());

			if (actividades.getChildren().size() > 1) {
				actividades.getChildren().remove(1, actividades.getChildren().size());
			}
			actividades.getChildren().addAll(mostrarActividades());

			filtro.setValue("Filtro");
			busqueda.setText("");
		});

		return new HBox(10, filtro, busqueda, buscar);
	}

	private boolean filtrarActividad(modelo.Actividad a) {
		switch (filtroActividad) {
		case "Fecha":
			String validoFecha = "^([0-2][0-9]|(3)[0-1])-(0[1-9]|1[0-2])-(\\d{4})$";

			if (busquedaActividad.matches(validoFecha)) {
				SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
				Date fechaBuscada = null;
				try {
					fechaBuscada = formato.parse(busquedaActividad);
				} catch (ParseException e) {
					e.printStackTrace();
					return false;
				}
				return a.getFecha().equals(fechaBuscada);
			} else {
				alerta("La fecha no tiene un formato válido.", "Formato inválido");
				return false;
			}

		case "Hora":
			String validoHora = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9]):([0-5]?[0-9])$";

			if (busquedaActividad.matches(validoHora)) {
				LocalTime horaBuscada = null, horaActividadLocalTime = null;
				try {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					horaBuscada = LocalTime.parse(busquedaActividad, formatter);

					Time horaActividad = a.getHora();
					horaActividadLocalTime = horaActividad.toLocalTime();

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return horaActividadLocalTime.equals(horaBuscada);
			} else {
				alerta("La hora no tiene un formato válido.", "Formato inválido");
				return false;
			}

		case "Transporte":
			boolean transporte = true;
			String busquedaActividadLower = busquedaActividad.toLowerCase();
			if ((busquedaActividadLower.contains("no") || busquedaActividadLower.contains("sin"))) {
				transporte = false;
			}
			return a.isTransporte() == transporte;

		case "Ciudad":
			if (a.getCiudad_actividad().equalsIgnoreCase(busquedaActividad)
					|| a.getCiudad_partida().equalsIgnoreCase(busquedaActividad)) {
				return true;
			}
			return false;
		case "Idioma":
			return a.getIdioma().equalsIgnoreCase(busquedaActividad);
		case "Mascotas":
			boolean mascotas = true;
			if ((busquedaActividad.toLowerCase().contains("no") || busquedaActividad.toLowerCase().contains("sin"))) {
				mascotas = false;
			}
			return a.isMascotas() == mascotas;
		case "Capacidad":
			if (esNumeroValido(busquedaActividad)) {
				return a.getCapacidad_personas() == Integer.parseInt(busquedaActividad);
			}
			return false;
		case "Precio":
			if (esNumeroValido(busquedaActividad)) {
				return a.getPrecio() == Integer.parseInt(busquedaActividad);
			}
			return false;
		case "Edad":
			if (esNumeroValido(busquedaActividad)) {
				return a.getEdad_recomendada() == Integer.parseInt(busquedaActividad);
			}
			return false;
		default:
			return false;
		}
	}

	public VBox mostrarActividades() {
		actividades = new VBox(15);
		actividades.setMaxWidth(Double.MAX_VALUE);
		actividades.setStyle("-fx-background-color: " + BLANCO);

		if (busqueda == null) {
			busqueda = crearBarraBusqueda();
		}

		actividades.getChildren().add(busqueda);

		if (!actividadesMostrar.isEmpty()) {
			Collections.sort(actividadesMostrar, new Comparator<modelo.Actividad>() {
				@Override
				public int compare(modelo.Actividad a1, modelo.Actividad a2) {
					return Integer.compare(a2.getId_actividad(), a1.getId_actividad());
				}
			});

			if (!filtroActividad.isEmpty() && !busquedaActividad.isEmpty()) {
				for (modelo.Actividad a : actividadesMostrar) {
					if (filtrarActividad(a)) {
						añadirActividad(a);
					}
				}
			} else {
				for (modelo.Actividad a : actividadesMostrar) {
					añadirActividad(a);
				}
			}
			
			setFiltroActividad("");
			setBusquedaActividad("");
		} else {
			VBox panel = new VBox();
			Label alerta = new Label("Aún no hay información para mostrar.");
			alerta.setFont(fuenteAdvertencia);
			alerta.setStyle("-fx-text-fill: " + ROSA_OSCURO);
			panel.getChildren().add(alerta);
			actividades.getChildren().add(panel);
		}
		return actividades;
	}
	
	private void añadirActividad(modelo.Actividad a) {
		GridPane act = crearPanelActividad(a);
		act.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(act, Priority.ALWAYS);
		GridPane.setVgrow(act, Priority.ALWAYS);
		act.setStyle("-fx-border-color: " + ROSA
				+ "; -fx-border-width: 1; -fx-padding: 10; -fx-border-radius: 15px;");
		actividades.getChildren().add(act);
	}

	private String estiloCampoTexto() {
		return " -fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO
				+ "; -fx-pref-width: 200px; -fx-font-size: 15px;";
	}

	private String estiloComboBox() {
		return "-fx-font-family: 'Roboto'; -fx-font-size: 15px; -fx-background-color: " + GRIS
				+ "; -fx-border-color: transparent;";
	}

	private String estiloBotonFondo() {
		return "-fx-background-color: " + ROSA + "; -fx-text-fill: " + NEGRO
				+ ";-fx-font-size: 14px; -fx-background-radius: 15px; ";
	}

	private void cargarFuentes() {
		fuenteTitulo = Font.font("Arial", FontWeight.BOLD, 62);
		fuenteSegundoTitulo = Font.font("Arial", FontWeight.BOLD, 42);
		fuenteTexto = Font.font("Arial", 18);
		fuenteTexto2 = Font.font("Arial", 15);
		fuenteAdvertencia = Font.font("Arial", 12);
	}

	public void alerta(String mensaje, String titulo) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);

		Label x = new Label("✘");
		x.setStyle("-fx-text-fill: " + NEGRO + "; -fx-font-size: 30px;");

		alert.getDialogPane().setGraphic(x);
		alert.getDialogPane()
				.setStyle("-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 15px;");

		alert.getDialogPane().lookupButton(ButtonType.OK)
				.setStyle("-fx-background-color: " + ROSA + "; -fx-text-fill: " + NEGRO + ";");
		alert.showAndWait();
	}

	private void alertaConfirmacion(modelo.Actividad actividad) {
		Alert alert = crearAlerta("Confirmación", "¿Estás seguro de que deseas cancelar la actividad?", "Se perderán toda la información.");

		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				conexion.enviarCancelarActividad(actividad);
			}
		});
	}

	private void alertaConfirmacionBaja() {
		Alert alert = crearAlerta("Dar de baja", "¿Estás seguro de que deseas dar de baja?", "Esta acción no puede deshacerse.");
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				conexion.enviarBaja();
				conexion.cerrarConexion();
				this.close();
			}
		});
	}

	private void alertaConfirmacionCerrarSesion() {
		Alert alert = crearAlerta("Cerrar sesión", "¿Estás seguro de que deseas cerrar sesión?", "Esta acción no puede deshacerse.");
		alert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				conexion.enviarCerrarSesion();
				this.close();
			}
		});
	}
	
	private Alert crearAlerta(String titulo, String header, String content) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(titulo);
		alert.setHeaderText(header);
		alert.setContentText(content);

		alert.getDialogPane().setGraphic(null);

		alert.getDialogPane()
				.setStyle("-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 15px;");

		alert.getDialogPane().lookupButton(ButtonType.OK)
				.setStyle("-fx-background-color: " + ROSA + "; -fx-text-fill: " + NEGRO + ";");
		alert.getDialogPane().lookupButton(ButtonType.CANCEL)
				.setStyle("-fx-background-color: " + ROSA + "; -fx-text-fill: " + NEGRO + ";");

		ButtonType buttonAceptar = ButtonType.OK;
		ButtonType buttonCancelar = ButtonType.CANCEL;

		alert.getDialogPane().lookupButton(buttonAceptar).setAccessibleText("Aceptar");
		alert.getDialogPane().lookupButton(buttonCancelar).setAccessibleText("Cancelar");
		
		return alert;
	}

	// Getters y setters
	public boolean esNumeroValido(String busquedaActividad) {
		return busquedaActividad.matches("\\d+");
	}

	public String getFiltroActividad() {
		return filtroActividad;
	}

	public void setFiltroActividad(String filtroActividad) {
		this.filtroActividad = filtroActividad;
	}

	public String getBusquedaActividad() {
		return busquedaActividad;
	}

	public void setBusquedaActividad(String busquedaActividad) {
		this.busquedaActividad = busquedaActividad;
	}

	public VBox getActividades() {
		return actividades;
	}

	public void setActividades(VBox actividades) {
		this.actividades = actividades;
	}

	public BorderPane getPrincipal() {
		return principal;
	}

	public void setPrincipal(BorderPane principal) {
		this.principal = principal;
	}

	public Label getAdvertencia() {
		return advertencia;
	}

	public void setAdvertencia(Label advertencia) {
		this.advertencia = advertencia;
	}

	public Conexion getConexion() {
		return conexion;
	}

	public void setConexion(Conexion conexion) {
		this.conexion = conexion;
	}

	public Button getIcono() {
		return icono;
	}

	public void setIcono(Button icono) {
		this.icono = icono;
	}

	public ArrayList<modelo.Actividad> getActividadesMostrar() {
		return actividadesMostrar;
	}

	public void setActividadesMostrar(ArrayList<modelo.Actividad> actividadesMostrar) {
		this.actividadesMostrar = actividadesMostrar;
	}

	public String getVentana() {
		return ventana;
	}

	public void setVentana(String ventana) {
		this.ventana = ventana;
	}
}
