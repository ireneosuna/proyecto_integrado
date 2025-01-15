package interfaz;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import conexion.Conexion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModificarActividad extends UtilesInterfaz {

	private TextField nombreActividadText;
	private TextArea descripcionText;
	private DatePicker fechActividadText;
	private TextField horaText;
	private CheckBox transporte;
	private TextField ciudadPartidaText;
	private TextField ciudadActividadText;
	private TextField idiomaText;
	private CheckBox mascotasText;
	private TextField maxPersonasText;
	private TextField precioText;
	private TextField edadText;
	private TextField codText;

	public ModificarActividad(Stage primaryStage, Conexion c, modelo.Actividad a) {
		if (c.getTipo().equalsIgnoreCase("consumidor")) {
			primaryStage.setTitle("Modificar anuncio");
		} else {
			primaryStage.setTitle("Modificar actividad");
		}

		Scene scene = crearEscenaModificarActividad(primaryStage, c, a);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.showAndWait();
		centrarPantalla(primaryStage);
	}

	protected Scene crearEscenaModificarActividad(Stage primaryStage, Conexion c, modelo.Actividad a) {
		cargarFuentes();
		VBox panelPrincipal = crearPanelPrincipal(20);

		BorderPane panelInicial;
		if (c.getTipo().equalsIgnoreCase("consumidor")) {
			panelInicial = crearPanelInicial(primaryStage, "MODIFICAR ANUNCIO");

		} else {
			panelInicial = crearPanelInicial(primaryStage, "MODIFICAR ACTIVIDAD");

		}
		GridPane panelCentral = crearPanelCentral(primaryStage, c, a);

		Region espacioSuperior = new Region();
		espacioSuperior.setPrefHeight(20);

		panelPrincipal.getChildren().addAll(espacioSuperior, panelInicial, panelCentral);

		return new Scene(panelPrincipal, 700, 760);
	}

	protected BorderPane crearPanelInicial(Stage primaryStage, String label) {
		BorderPane panelInicial = new BorderPane();
		panelInicial.setStyle("-fx-background-color: " + BLANCO + ";");

		Text titulo = new Text(label);
		titulo.setFont(fuenteSegundoTitulo);
		titulo.setStyle("-fx-fill: " + ROSA + "; -fx-stroke: " + NEGRO + "; -fx-stroke-width: 0.5px;");

		panelInicial.setCenter(titulo);

		return panelInicial;
	}

	protected GridPane crearPanelCentral(Stage primaryStage, Conexion c, modelo.Actividad a) {
		GridPane panelCentral = new GridPane();
		panelCentral.setVgap(4);
		panelCentral.setHgap(4);
		panelCentral.setAlignment(Pos.CENTER);

		agregarCamposTexto(panelCentral, c, a);
		agregarBotones(panelCentral, primaryStage, c, a);

		return panelCentral;
	}

	protected void agregarCamposTexto(GridPane panel, Conexion c, modelo.Actividad a) {
		String[] etiquetas = { "Nombre : ", "Descripcion : ", "Fecha : ", "Hora : ", "Transporte : ",
				"Ciudad Partida :", "Ciudad Actividad : ", "Idioma : ", "Mascotas : ", "Máximo de personas : ",
				"Precio : ", "Edad recomendada : ", "Código de vestimenta : " };

		nombreActividadText = new TextField(a.getNombre_actividad());
		nombreActividadText.setFocusTraversable(false);
		descripcionText = new TextArea(a.getDescripcion());
		descripcionText.setFocusTraversable(false);
		fechActividadText = new DatePicker(a.getFecha().toLocalDate());
		horaText = new TextField(a.getHora().toString());
		transporte = new CheckBox();
		transporte.setSelected(a.isTransporte());
		ciudadPartidaText = new TextField(a.getCiudad_partida());
		ciudadActividadText = new TextField(a.getCiudad_actividad());
		idiomaText = new TextField(a.getIdioma());
		mascotasText = new CheckBox();
		mascotasText.setSelected(a.isMascotas());
		maxPersonasText = new TextField(a.getCapacidad_personas() + "");
		precioText = new TextField(a.getPrecio() + "");
		edadText = new TextField(a.getEdad_recomendada() + "");
		codText = new TextField(a.getCodigo_vestimenta());

		Label nombreActividad = crearLabel(etiquetas[0], fuenteTexto, NEGRO);
		nombreActividadText
				.setStyle("-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");
		panel.add(nombreActividad, 0, 0);
		panel.add(nombreActividadText, 1, 0);

		Label descripcion = crearLabel(etiquetas[1], fuenteTexto, NEGRO);
		descripcionText.setStyle(estiloCampoTexto());
		descripcionText.setWrapText(true);
		panel.add(descripcion, 0, 1);
		panel.add(descripcionText, 1, 1);

		Label fecha = crearLabel(etiquetas[2], fuenteTexto, NEGRO);
		fechActividadText.setStyle(estiloCampoTexto());
		panel.add(fecha, 0, 2);
		panel.add(fechActividadText, 1, 2);

		Label hora = crearLabel(etiquetas[3], fuenteTexto, NEGRO);
		horaText.setStyle(estiloCampoTexto());
		panel.add(hora, 0, 3);
		panel.add(horaText, 1, 3);

		Label ciudadPartidaLabel = crearLabel(etiquetas[5], fuenteTexto, NEGRO);
		if (transporte.isSelected()) {
			ciudadPartidaLabel.setTextFill(Color.web(NEGRO));
			ciudadPartidaText.setStyle(estiloCampoTexto());
		} else {
			ciudadPartidaLabel.setTextFill(Color.web(GRIS_OSCURO));
			ciudadPartidaText.setStyle(estiloCampoDeshabilitado());
			ciudadPartidaText.setText("");
		}

		panel.add(ciudadPartidaLabel, 0, 5);
		panel.add(ciudadPartidaText, 1, 5);

		Label transporteLabel = crearLabel(etiquetas[4], fuenteTexto, NEGRO);
		transporte.setStyle(estiloCheckBox());
		transporte.selectedProperty().addListener(((observable, oldValue, newValue) -> {
			ciudadPartidaText.setEditable(newValue);
			ciudadPartidaText.setMouseTransparent(!newValue);

			if (newValue) {
				ciudadPartidaLabel.setTextFill(Color.web(NEGRO));
				ciudadPartidaText.setStyle(estiloCampoTexto());
			} else {
				ciudadPartidaLabel.setTextFill(Color.web(GRIS_OSCURO));
				ciudadPartidaText.setStyle(estiloCampoDeshabilitado());
				ciudadPartidaText.setText("");
			}
		}));
		panel.add(transporteLabel, 0, 4);
		panel.add(transporte, 1, 4);

		Label ciudadActividadLabel = crearLabel(etiquetas[6], fuenteTexto, NEGRO);
		ciudadActividadText.setStyle(estiloCampoTexto());
		panel.add(ciudadActividadLabel, 0, 6);
		panel.add(ciudadActividadText, 1, 6);

		Label idiomasLabel = crearLabel(etiquetas[7], fuenteTexto, NEGRO);
		idiomaText.setStyle(estiloCampoTexto());
		panel.add(idiomasLabel, 0, 7);
		panel.add(idiomaText, 1, 7);

		Label mascotasLabel = crearLabel(etiquetas[8], fuenteTexto, NEGRO);
		mascotasText.setSelected(false);
		mascotasText.setStyle(estiloCheckBox());
		panel.add(mascotasLabel, 0, 8);
		panel.add(mascotasText, 1, 8);

		Label maxPersonas = crearLabel(etiquetas[9], fuenteTexto, NEGRO);
		maxPersonasText.setStyle(estiloCampoTexto());
		maxPersonasText.setTextFormatter(new TextFormatter<>(change -> {
			return change.getControlNewText().matches("\\d*") ? change : null;
		}));
		panel.add(maxPersonas, 0, 9);
		panel.add(maxPersonasText, 1, 9);

		Label precio = crearLabel(etiquetas[10], fuenteTexto, NEGRO);
		precioText.setStyle(estiloCampoTexto());
		precioText.setTextFormatter(new TextFormatter<>(change -> {
			return change.getControlNewText().matches("\\d*") ? change : null;
		}));
		panel.add(precio, 0, 10);
		panel.add(precioText, 1, 10);

		Label edad = crearLabel(etiquetas[11], fuenteTexto, NEGRO);
		edadText.setStyle(estiloCampoTexto());
		edadText.setTextFormatter(new TextFormatter<>(change -> {
			return change.getControlNewText().matches("\\d*") ? change : null;
		}));
		panel.add(edad, 0, 11);
		panel.add(edadText, 1, 11);

		Label vestimenta = crearLabel(etiquetas[12], fuenteTexto, NEGRO);
		codText.setStyle(estiloCampoTexto());
		panel.add(vestimenta, 0, 12);
		panel.add(codText, 1, 12);

	}

	protected void agregarBotones(GridPane panel, Stage primaryStage, Conexion c, modelo.Actividad a) {
		Button botonAceptar = new Button("Aceptar");
		botonAceptar.setFont(fuenteTexto);
		botonAceptar.setStyle(estiloBotonFondo());
		botonAceptar.setPrefSize(250, 40);
		botonAceptar.setOnAction(e -> validarModificarActividad(primaryStage, c, a));

		cambiarColorBoton(botonAceptar, ROSA, NEGRO);

		Button botonCancelar = new Button("Cancelar");
		botonCancelar.setFont(fuenteTexto);
		botonCancelar.setStyle(estiloBotonFondo());
		botonCancelar.setPrefSize(250, 40);
		botonCancelar.setOnAction(e -> primaryStage.close());

		cambiarColorBoton(botonCancelar, ROSA, NEGRO);

		HBox botones = new HBox(25);
		HBox.setHgrow(botones, Priority.ALWAYS);
		botones.getChildren().addAll(botonAceptar, botonCancelar);

		panel.add(botones, 0, 13, 2, 1);
		GridPane.setMargin(botones, new Insets(40, 40, 40, 40));
		GridPane.setHalignment(botones, HPos.CENTER);
	}

	protected void validarModificarActividad(Stage primaryStage, Conexion c, modelo.Actividad a) {
		String nombre = nombreActividadText.getText();
		String descripcion = descripcionText.getText();
		LocalDate fecha = fechActividadText.getValue();
		String hora = horaText.getText();
		boolean trans = transporte.isSelected();
		String ciudadPart = ciudadPartidaText.getText();
		String ciudadAct = ciudadActividadText.getText();
		String idioma = idiomaText.getText();
		boolean mas = mascotasText.isSelected();
		String maxPersonas = maxPersonasText.getText();
		String precio = precioText.getText();
		String edad = edadText.getText();
		String codigo = codText.getText();

		boolean coinciden = false;

		if (!trans) {
			if (!ciudadAct.isEmpty()) {
				ciudadPart = ciudadAct;
			}
		}

		boolean completa = !nombre.isEmpty() && !descripcion.isEmpty() && !hora.isEmpty() && fecha != null
				&& !ciudadPart.isEmpty() && !ciudadAct.isEmpty() && !idioma.isEmpty() && !maxPersonas.isEmpty()
				&& !precio.isEmpty() && !edad.isEmpty() && !codigo.isEmpty();

		if (completa) {
			if (fecha.isAfter(LocalDate.now())) {
				for (modelo.Actividad act : c.getActividadesPropias()) {
					if (Date.valueOf(fecha).compareTo(act.getFecha()) == 0) {
						if (act.getId_actividad() != a.getId_actividad()) {
							alerta("Tienes otras actividades en esta fecha.", "Coinciden fechas");
							coinciden = true;
						}
					}
				}
				for (modelo.Actividad act : c.getInscripciones()) {
					if (act.getId_ofertante() == c.getUsuario().getId_usuario()) {
						if (Date.valueOf(fecha).compareTo(act.getFecha()) == 0) {
							alerta("Inscrito a otras actividades en esta fecha.", "Coinciden fechas");
							coinciden = true;
						}
					}
				}

				if (!coinciden) {
					if (!hora.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$")) {
						alerta("El formato de la hora es incorrecto.", "Hora incorrecta");
					} else if (Integer.parseInt(maxPersonas) == 0) {
						alerta("Debe haber por lo menos una persona.", "Cantidad personas");
					} else {
						modelo.Actividad activ = new modelo.Actividad(a.getId_actividad(), nombre, a.getTipo(),
								descripcion, Date.valueOf(fecha), Time.valueOf(hora), trans, ciudadPart, ciudadAct,
								idioma, mas, Integer.parseInt(maxPersonas), a.getPersonas_actuales(),
								Integer.parseInt(precio), Integer.parseInt(edad), codigo, a.getId_usuario_propietario(),
								a.getId_ofertante());
						c.enviarModificarActividad(activ);
					}
				}
				primaryStage.close();
			}

		} else {
			alerta("Introduzca toda la información.", "Campos incompletos");
		}

	}
}
