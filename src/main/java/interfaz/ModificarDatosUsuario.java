package interfaz;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import conexion.Conexion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModificarDatosUsuario extends UtilesInterfaz {

	protected TextField nombreText;
	protected TextField usuarioText;
	protected TextField emailText;
	protected PasswordField pasAct;
	protected PasswordField pasCam;
	protected DatePicker fechaNacimientoText;

	public ModificarDatosUsuario(Stage primaryStage, Conexion c) {
		primaryStage.setTitle("Modificar información");
		Scene scene = crearEscenaModificarDatosUsuario(primaryStage, c);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.initModality(Modality.APPLICATION_MODAL);
		primaryStage.showAndWait();
		centrarPantalla(primaryStage);
	}

	protected Scene crearEscenaModificarDatosUsuario(Stage primaryStage, Conexion c) {
		cargarFuentes();
		VBox panelPrincipal = crearPanelPrincipal(35);

		BorderPane panelInicial = crearPanelInicial(primaryStage, "MODIFICAR", "INFORMACIÓN");
		GridPane panelCentral = crearPanelCentral(primaryStage, c);

		panelPrincipal.getChildren().addAll(panelInicial, panelCentral);
		return new Scene(panelPrincipal, 600, 650);
	}

	protected BorderPane crearPanelInicial(Stage primaryStage, String label1, String label2) {
		BorderPane panelInicial = new BorderPane();
		panelInicial.setStyle("-fx-background-color: " + BLANCO + ";");

		Text titulo1 = new Text(label1);
		titulo1.setFont(fuenteSegundoTitulo);
		titulo1.setStyle("-fx-fill: " + ROSA + "; -fx-stroke: " + NEGRO + "; -fx-stroke-width: 0.5px;");

		Text titulo2 = new Text(label2);
		titulo2.setFont(fuenteSegundoTitulo);
		titulo2.setStyle("-fx-fill: " + ROSA + "; -fx-stroke: " + NEGRO + "; -fx-stroke-width: 0.5px;");

		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(titulo1, titulo2);

		panelInicial.setTop(vbox);

		return panelInicial;
	}

	protected GridPane crearPanelCentral(Stage primaryStage, Conexion c) {
		GridPane panelCentral = new GridPane();
		panelCentral.setVgap(10);
		panelCentral.setHgap(10);
		panelCentral.setAlignment(Pos.CENTER);

		agregarCamposTexto(panelCentral, c);
		agregarBotones(panelCentral, primaryStage, c);

		return panelCentral;
	}

	protected void agregarCamposTexto(GridPane panel, Conexion c) {
		String[] etiquetas = { "Nombre : ", "Usuario : ", "Email : ", "Fecha de nacimiento : ", "Contraseña actual : ",
				"Contraseña cambiada :" };

		nombreText = new TextField(c.getUsuario().getNombre_completo());
		nombreText.setFocusTraversable(false);
		usuarioText = new TextField(c.getUsuario().getNombre_usuario());
		usuarioText.setFocusTraversable(false);
		emailText = new TextField(c.getUsuario().getEmail());
		emailText.setFocusTraversable(false);
		pasAct = new PasswordField();
		pasCam = new PasswordField();

		TextField[] camposTexto = { nombreText, usuarioText, emailText, pasAct, pasCam };

		for (int i = 0; i < etiquetas.length; i++) {
			if (i == 3) {
				Label etiqueta = crearLabel(etiquetas[i], fuenteTexto, NEGRO);
				fechaNacimientoText = new DatePicker();
				fechaNacimientoText.setStyle(
						"-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");
				Date fecha = c.getUsuario().getFecha_nacimiento();
				LocalDate localDate = fecha.toLocalDate();

				fechaNacimientoText.setValue(localDate);
				fechaNacimientoText.setPromptText("dd/mm/aaaa");

				panel.add(etiqueta, 0, i);
				panel.add(fechaNacimientoText, 1, i);
			} else {
				if (i > 3) {
					Label etiqueta = crearLabel(etiquetas[i], fuenteTexto, NEGRO);
					camposTexto[i - 1].setStyle(
							"-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");
					panel.add(etiqueta, 0, i);
					panel.add(camposTexto[i - 1], 1, i);
				} else {
					Label etiqueta = crearLabel(etiquetas[i], fuenteTexto, NEGRO);
					camposTexto[i].setStyle(
							"-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");
					panel.add(etiqueta, 0, i);
					panel.add(camposTexto[i], 1, i);
				}
			}
		}

	}

	protected void agregarBotones(GridPane panel, Stage primaryStage, Conexion c) {
		Button botonAceptar = new Button("Aceptar");
		botonAceptar.setFont(fuenteTexto);
		botonAceptar.setStyle(estiloBotonFondo());
		botonAceptar.setPrefWidth(200);
		botonAceptar.setPrefHeight(40);
		botonAceptar.setOnAction(e -> validarModificarDatosUsuario(primaryStage, c));

		cambiarColorBoton(botonAceptar, ROSA, NEGRO);

		Button botonCancelar = new Button("Cancelar");
		botonCancelar.setFont(fuenteTexto);
		botonCancelar.setStyle(estiloBotonFondo());
		botonCancelar.setPrefWidth(200);
		botonCancelar.setPrefHeight(40);
		botonCancelar.setOnAction(e -> primaryStage.close());

		cambiarColorBoton(botonCancelar, ROSA, NEGRO);

		HBox botones = new HBox(25);
		HBox.setHgrow(botones, Priority.ALWAYS);
		botones.getChildren().addAll(botonAceptar, botonCancelar);

		panel.add(botones, 0, 7, 2, 1);
		GridPane.setMargin(botones, new Insets(40, 40, 40, 40));
		GridPane.setHalignment(botones, HPos.CENTER);
	}

	protected void validarModificarDatosUsuario(Stage primaryStage, Conexion c) {
		String nombre = nombreText.getText();
		String usuario = usuarioText.getText();
		String email = emailText.getText();
		LocalDate fecha = fechaNacimientoText.getValue();
		String pass1 = pasAct.getText();
		String pass2 = pasCam.getText();

		boolean completa = !nombre.isEmpty() && !usuario.isEmpty() && !email.isEmpty() && fecha != null
				&& !pass1.isEmpty() && !pass2.isEmpty();

		if (!completa) {
			alerta("Por favor, rellene todos los campos.", "Campos incompletos");
		} else if (nombre.length() > 100 || usuario.length() > 100 || email.length() > 100 || pass1.length() > 100
				|| pass2.length() > 100) {
			alerta("La credenciales no pueden tener demasiada longitud.", "Error longitud credenciales");
		} else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			alerta("El correo electrónico no tiene un formato válido.", "Error en correo electrónico");
		} else if (fecha.isAfter(LocalDate.now())) {
			alerta("La fecha de nacimiento no puede ser en el futuro.", "Fecha inválida");
		} else if (Period.between(fecha, LocalDate.now()).getYears() < 18) {
			alerta("Debe tener al menos 18 años para registrarse.", "Edad insuficiente");
		} else {
			String datos = c.getUsuario().getId_usuario() + "/" + nombre + "/" + usuario + "/" + email + "/" + fecha
					+ "/" + pass1 + "/" + pass2;
			c.enviarModificarDatos(datos);
			primaryStage.close();
		}

	}

}
