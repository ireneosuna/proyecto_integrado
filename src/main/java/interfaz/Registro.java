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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Registro extends UtilesInterfaz {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Registro");
		Scene scene = crearEscenaRegistro(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		centrarPantalla(primaryStage);
	}

	protected Scene crearEscenaRegistro(Stage primaryStage) {
		cargarFuentes();
		VBox panelPrincipal = crearPanelPrincipal(25);

		BorderPane panelInicial = crearPanelInicial(primaryStage, "REGISTRARSE");
		GridPane panelCentral = crearPanelCentralRegistro(primaryStage);

		panelPrincipal.getChildren().addAll(panelInicial, panelCentral);
		return new Scene(panelPrincipal, 500, 550);
	}

	protected BorderPane crearPanelInicial(Stage primaryStage, String label) {
		BorderPane panelInicial = new BorderPane();
		panelInicial.setStyle("-fx-background-color: " + BLANCO + ";");

		Button botonVolver = crearBotonVolver(primaryStage);

		Text titulo = new Text(label);
		titulo.setFont(fuenteTitulo);
		titulo.setStyle("-fx-fill: " + ROSA + "; -fx-stroke: " + NEGRO + "; -fx-stroke-width: 0.5px;");

		HBox hbox = new HBox();
		hbox.getChildren().addAll(botonVolver, titulo);
		hbox.setSpacing(0);
		hbox.setAlignment(Pos.CENTER_LEFT);
		panelInicial.setTop(hbox);

		return panelInicial;
	}

	protected GridPane crearPanelCentralRegistro(Stage primaryStage) {
		GridPane panelCentral = new GridPane();
		panelCentral.setVgap(10);
		panelCentral.setHgap(10);
		panelCentral.setStyle("-fx-background-color: " + BLANCO + ";");
		panelCentral.setAlignment(Pos.CENTER);

		agregarCamposTextoRegistro(panelCentral);
		agregarComboBox(panelCentral, 5);
		agregarBotonConfirmarRegistro(panelCentral, primaryStage, 6, 40);

		return panelCentral;
	}

	protected Button crearBotonVolver(Stage primaryStage) {
		Button botonVolverInicio = new Button("◀");
		botonVolverInicio.setPrefWidth(0);
		botonVolverInicio.setStyle("-fx-background-color: transparent; -fx-text-fill: " + NEGRO
				+ "; -fx-border-color: transparent; -fx-font-size: 20px;");

		botonVolverInicio.setOnAction(e -> {
			Inicio inicio = new Inicio();
			try {
				Scene escenaInicio = inicio.crearEscenaInicio(primaryStage);
				primaryStage.setTitle("Inicio de sesión");
				primaryStage.setScene(escenaInicio);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		return botonVolverInicio;
	}

	protected void agregarCamposTextoRegistro(GridPane panelCentral) {
		String[] etiquetas = { "Nombre :", "Usuario :", "Contraseña :", "Email :", "Fecha nacimiento :" };

		nombreText = new TextField();
		usuarioText = new TextField();
		passText = new PasswordField();
		emailText = new TextField();

		TextField[] camposTexto = { nombreText, usuarioText, passText, emailText };

		for (int i = 0; i < etiquetas.length; i++) {
			if (i == 4) {
				Label etiqueta = crearLabel(etiquetas[i], fuenteTexto, NEGRO);
				fechaNacimientoText = new DatePicker();
				fechaNacimientoText.setPromptText("dd/mm/aaaa");
				fechaNacimientoText.setStyle(
						"-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");
				panelCentral.add(etiqueta, 0, i);
				panelCentral.add(fechaNacimientoText, 1, i);
			} else {
				Label etiqueta = crearLabel(etiquetas[i], fuenteTexto, NEGRO);
				camposTexto[i].setStyle(
						"-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");
				panelCentral.add(etiqueta, 0, i);
				panelCentral.add(camposTexto[i], 1, i);
			}

		}

		fechaNacimientoText = new DatePicker();
		fechaNacimientoText.setPromptText("dd/mm/aaaa");
		fechaNacimientoText
				.setStyle("-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");
		panelCentral.add(fechaNacimientoText, 1, 4);
	}

	protected void agregarComboBox(GridPane panelCentral, int num) {
		seleccion = new ComboBox<>();
		seleccion.getItems().addAll("Consumidor", "Ofertante");
		seleccion.setValue("Consumidor");
		seleccion.setStyle(estiloComboBox());
		seleccion.setPrefWidth(200);
		seleccion.setPrefHeight(40);

		seleccion.setCellFactory(param -> new ListCell<String>() {
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

		panelCentral.add(seleccion, 0, num, 2, 1);
		GridPane.setHalignment(seleccion, HPos.CENTER);
	}

	protected void agregarBotonConfirmarRegistro(GridPane panelCentral, Stage primaryStage, int pos, int insets) {
		Button botonConfirmar = new Button("Aceptar");
		botonConfirmar.setFont(fuenteTexto);
		botonConfirmar.setStyle(estiloBotonFondo());
		botonConfirmar.setPrefWidth(200);
		botonConfirmar.setPrefHeight(40);
		botonConfirmar.setOnAction(e -> validarFormularioRegistro(primaryStage));

		cambiarColorBoton(botonConfirmar, ROSA, NEGRO);

		panelCentral.add(botonConfirmar, 0, pos, 2, 1);
		GridPane.setMargin(botonConfirmar, new Insets(insets, 0, 25, 0));
		GridPane.setHalignment(botonConfirmar, HPos.CENTER);
	}

	protected void validarFormularioRegistro(Stage primaryStage) {
		// Obtener valores de los campos
		String nom = nombreText.getText();
		String user = usuarioText.getText();
		String con = passText.getText();
		String email = emailText.getText();
		LocalDate fecha = fechaNacimientoText.getValue();
		Date fechaNacimiento = (fecha != null) ? Date.valueOf(fecha) : null;
		String tipo = seleccion.getValue();

		// Validación de campos
		if (nom.isEmpty() || con.isEmpty() || user.isEmpty() || email.isEmpty() || fecha == null || tipo == null) {
			alerta("Por favor, rellene todos los campos.", "Campos incompletos");
		} else if (nom.length() > 100 || con.length() > 100 || user.length() > 100 || email.length() > 100) {
			alerta("La credenciales no pueden tener demasiada longitud.", "Error longitud credenciales");
		} else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			alerta("El correo electrónico no tiene un formato válido.", "Error en correo electrónico");
		} else if (fecha.isAfter(LocalDate.now())) {
			alerta("La fecha de nacimiento no puede ser en el futuro.", "Fecha inválida");
		} else if (Period.between(fecha, LocalDate.now()).getYears() < 18) {
			alerta("Debe tener al menos 18 años para registrarse.", "Edad insuficiente");
		} else {
			Conexion c = new Conexion(nom, user, con, email, fechaNacimiento, tipo);

			if (c.getRespuesta().equals("registroincorrecto")) {
				alerta("Credenciales incorrectas.", "Error de registro");
			} else {
				primaryStage.close();
				c.start();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
