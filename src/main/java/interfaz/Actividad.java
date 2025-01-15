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

public class Actividad extends UtilesInterfaz {

	public Actividad(Stage primaryStage, Conexion c) {
		if (c.getTipo().equalsIgnoreCase("consumidor")) {
			primaryStage.setTitle("Nuevo anuncio");
		} else {
			primaryStage.setTitle("Nueva actividad");
		}

		Scene scene = crearEscenaActividad(primaryStage, c);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.showAndWait();
		centrarPantalla(primaryStage);
	}

	public Scene crearEscenaActividad(Stage primaryStage, Conexion c) {
		cargarFuentes();
		VBox panelPrincipal = crearPanelPrincipal(20);
		BorderPane panelInicial;
		if (c.getTipo().equalsIgnoreCase("consumidor")) {
			panelInicial = crearPanelInicial(primaryStage, "NUEVO ANUNCIO");

		} else {
			panelInicial = crearPanelInicial(primaryStage, "NUEVA ACTIVIDAD");
		}
		GridPane panelCentral = crearPanelCentralActividad(primaryStage, c);

		Region espacioSuperior = new Region();
		espacioSuperior.setPrefHeight(20);

		panelPrincipal.getChildren().addAll(espacioSuperior, panelInicial, panelCentral);
		return new Scene(panelPrincipal, 700, 760);
	}

	protected void validarActividad(Stage primary, Conexion c) {
		String nom = nombreActividadText.getText();
		String desc = descripcionText.getText();
		LocalDate fecha = fechaActividadText.getValue();
		String hora = horaText.getText();
		Boolean trans = transporte.isSelected();
		String ciudadPar = ciudadPartida.getText();
		String ciudadAct = ciudadActividad.getText();
		String idioma = idiomas.getText();
		Boolean mas = mascotas.isSelected();
		String maxPersonas = maxPersonasText.getText();
		String prec = precioText.getText();
		String edad = edadRecomendadaText.getText();
		String cod = vestimentaText.getText();
		String tipo = c.getTipo().equalsIgnoreCase("ofertante") ? "actividad" : "anuncio";

		boolean coinciden = false;

		if (!trans) {
			if (!ciudadAct.isEmpty()) {
				ciudadPar = ciudadAct;
			}
		}

		boolean completo = !nom.isEmpty() && !desc.isEmpty() && fecha != null && !hora.isEmpty() && !ciudadPar.isEmpty()
				&& !ciudadAct.isEmpty() && !idioma.isEmpty() && !maxPersonas.isEmpty() && !prec.isEmpty()
				&& !edad.isEmpty() && !cod.isEmpty();

		if (completo) {
			if (fecha.isAfter(LocalDate.now())) {
				if (c.getActividadesPropias() != null) {
					for (modelo.Actividad act : c.getActividadesPropias()) {
						if (Date.valueOf(fecha).compareTo(act.getFecha()) == 0) {
							alerta("Tienes otras actividades en esta fecha.", "Coinciden fechas");
							coinciden = true;
						}
					}
				}

				if (c.getInscripciones() != null) {
					for (modelo.Actividad act : c.getInscripciones()) {
						if (Date.valueOf(fecha).compareTo(act.getFecha()) == 0) {
							alerta("Tienes otras actividades en esta fecha.", "Coinciden fechas");
							coinciden = true;
						}
					}
				}

				if (!coinciden) {
					if(!hora.matches("^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$")) {
						alerta("El formato de la hora es incorrecto.", "Hora incorrecta");
					}else if(Integer.parseInt(maxPersonas)==0){
						alerta("Debe haber por lo menos una persona.", "Cantidad personas");
					}else {
						modelo.Actividad a = new modelo.Actividad(-1, nom, tipo, desc, Date.valueOf(fecha),
								Time.valueOf(hora), trans, ciudadPar, ciudadAct, idioma, mas, Integer.parseInt(maxPersonas),
								0, Integer.parseInt(prec), Integer.parseInt(edad), cod, c.getUsuario().getId_usuario(), -1);
						c.enviarNuevaActividad(a);
						primary.close();
					}
				}
				
			} else {
				alerta("La fecha no puede estar en el pasado.", "Fecha incorrecta");
			}
		} else {
			alerta("Introduzca toda la información.", "Campos incompletos");
		}

	}

	protected void agregarBotonesActividad(GridPane panelCentral, Stage primaryStage, Conexion c) {
		Button botonAceptar = new Button("Aceptar");
		botonAceptar.setFont(fuenteTexto);
		botonAceptar.setStyle(estiloBotonFondo());
		botonAceptar.setPrefSize(250, 40);
		botonAceptar.setOnAction(e -> validarActividad(primaryStage, c));

		cambiarColorBoton(botonAceptar, ROSA, NEGRO);

		Button botonCancelar = new Button("Cancelar");
		botonCancelar.setFont(fuenteTexto);
		botonCancelar.setStyle(estiloBotonFondo());
		botonCancelar.setPrefSize(250, 40);
		botonCancelar.setOnAction(e -> primaryStage.close());

		cambiarColorBoton(botonCancelar, ROSA, NEGRO);

		HBox botones = new HBox(5);
		HBox.setHgrow(botones, Priority.ALWAYS);
		botones.getChildren().addAll(botonAceptar, botonCancelar);

		panelCentral.add(botones, 0, 13, 2, 1);
		GridPane.setMargin(botones, new Insets(40, 40, 40, 40));
		GridPane.setHalignment(botones, HPos.CENTER);

	}

	protected void agregarCamposActividad(GridPane panelCentral) {
		String[] etiquetas = { "Nombre : ", "Descripción : ", "Fecha : ", "Hora : ", "Transporte ",
				"Ciudad de partida : ", "Ciudad de la actividad : ", "Idioma : ", "Mascotas ", "Máximo de personas : ",
				"Precio : ", "Edad recomendada : ", "Código de vestimenta : " };

		nombreActividadText = new TextField();
		descripcionText = new TextArea();
		fechaActividadText = new DatePicker();
		horaText = new TextField();
		transporte = new CheckBox();
		ciudadPartida = new TextField();
		ciudadActividad = new TextField();
		idiomas = new TextField();
		mascotas = new CheckBox();
		maxPersonasText = new TextField();
		precioText = new TextField();
		edadRecomendadaText = new TextField();
		vestimentaText = new TextField();

		Label nombreActividad = crearLabel(etiquetas[0], fuenteTexto, NEGRO);
		nombreActividadText.setStyle(estiloCampoTexto());
		panelCentral.add(nombreActividad, 0, 0);
		panelCentral.add(nombreActividadText, 1, 0);

		Label descripcion = crearLabel(etiquetas[1], fuenteTexto, NEGRO);
		descripcionText.setStyle(estiloCampoTexto());
		descripcionText.setWrapText(true);
		panelCentral.add(descripcion, 0, 1);
		panelCentral.add(descripcionText, 1, 1);

		Label fecha = crearLabel(etiquetas[2], fuenteTexto, NEGRO);
		fechaActividadText = new DatePicker();
		fechaActividadText.setPromptText("dd/mm/aaaa");
		fechaActividadText.setStyle(estiloCampoTexto());
		panelCentral.add(fecha, 0, 2);
		panelCentral.add(fechaActividadText, 1, 2);

		Label hora = crearLabel(etiquetas[3], fuenteTexto, NEGRO);
		horaText.setStyle(estiloCampoTexto());
		horaText.setPromptText("hh:mm:ss");
		panelCentral.add(hora, 0, 3);
		panelCentral.add(horaText, 1, 3);

		Label ciudadPartidaLabel = crearLabel(etiquetas[5], fuenteTexto, GRIS_OSCURO);
		ciudadPartida.setStyle(estiloCampoDeshabilitado());
		ciudadPartida.setEditable(false);
		ciudadPartida.setMouseTransparent(true);
		panelCentral.add(ciudadPartidaLabel, 0, 5);
		panelCentral.add(ciudadPartida, 1, 5);

		Label transporteLabel = crearLabel(etiquetas[4], fuenteTexto, NEGRO);
		transporte.setSelected(false);
		transporte.setStyle(estiloCheckBox());
		transporte.selectedProperty().addListener(((observable, oldValue, newValue) -> {
			ciudadPartida.setEditable(newValue);
			ciudadPartida.setMouseTransparent(!newValue);
			if (newValue) {
				ciudadPartidaLabel.setTextFill(Color.web(NEGRO));
				ciudadPartida.setStyle(estiloCampoTexto());
			} else {
				ciudadPartidaLabel.setTextFill(Color.web(GRIS_OSCURO));
				ciudadPartida.setStyle(estiloCampoDeshabilitado());
				ciudadPartida.setText("");
			}
		}));
		panelCentral.add(transporteLabel, 0, 4);
		panelCentral.add(transporte, 1, 4);

		Label ciudadActividadLabel = crearLabel(etiquetas[6], fuenteTexto, NEGRO);
		ciudadActividad.setStyle(estiloCampoTexto());
		panelCentral.add(ciudadActividadLabel, 0, 6);
		panelCentral.add(ciudadActividad, 1, 6);

		Label idiomasLabel = crearLabel(etiquetas[7], fuenteTexto, NEGRO);
		idiomas.setStyle(estiloCampoTexto());
		panelCentral.add(idiomasLabel, 0, 7);
		panelCentral.add(idiomas, 1, 7);

		Label mascotasLabel = crearLabel(etiquetas[8], fuenteTexto, NEGRO);
		mascotas.setSelected(false);
		mascotas.setStyle(estiloCheckBox());
		panelCentral.add(mascotasLabel, 0, 8);
		panelCentral.add(mascotas, 1, 8);

		Label maxPersonas = crearLabel(etiquetas[9], fuenteTexto, NEGRO);
		maxPersonasText.setStyle(estiloCampoTexto());
		maxPersonasText.setTextFormatter(new TextFormatter<>(change -> {
			return change.getControlNewText().matches("\\d*") ? change : null;
		}));
		panelCentral.add(maxPersonas, 0, 9);
		panelCentral.add(maxPersonasText, 1, 9);

		Label precio = crearLabel(etiquetas[10], fuenteTexto, NEGRO);
		precioText.setStyle(estiloCampoTexto());
		precioText.setTextFormatter(new TextFormatter<>(change -> {
			return change.getControlNewText().matches("\\d*") ? change : null;
		}));
		panelCentral.add(precio, 0, 10);
		panelCentral.add(precioText, 1, 10);

		Label edad = crearLabel(etiquetas[11], fuenteTexto, NEGRO);
		edadRecomendadaText.setStyle(estiloCampoTexto());
		edadRecomendadaText.setTextFormatter(new TextFormatter<>(change -> {
			return change.getControlNewText().matches("\\d*") ? change : null;
		}));
		panelCentral.add(edad, 0, 11);
		panelCentral.add(edadRecomendadaText, 1, 11);

		Label vestimenta = crearLabel(etiquetas[12], fuenteTexto, NEGRO);
		vestimentaText.setStyle(estiloCampoTexto());
		panelCentral.add(vestimenta, 0, 12);
		panelCentral.add(vestimentaText, 1, 12);

	}

	protected GridPane crearPanelCentralActividad(Stage primaryStage, Conexion c) {
		GridPane panelCentral = new GridPane();
		panelCentral.setVgap(4);
		panelCentral.setHgap(4);
		panelCentral.setStyle("-fx-background-color: " + BLANCO + ";");
		panelCentral.setAlignment(Pos.CENTER);

		agregarCamposActividad(panelCentral);
		agregarBotonesActividad(panelCentral, primaryStage, c);

		return panelCentral;

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

}
