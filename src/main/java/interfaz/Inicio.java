package interfaz;

import conexion.Conexion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Inicio extends UtilesInterfaz {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Inicio de sesión");
		Scene scene = crearEscenaInicio(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		centrarPantalla(primaryStage);

	}

	public Scene crearEscenaInicio(Stage primaryStage) {
		cargarFuentes();
		VBox panelPrincipal = crearPanelPrincipal(80);

		BorderPane panelInicial = crearPanelInicial(primaryStage, "INICIAR SESION");
		GridPane panelCentral = crearPanelCentralInicio(primaryStage);

		panelPrincipal.getChildren().addAll(panelInicial, panelCentral);
		return new Scene(panelPrincipal, 500, 550);
	}

	protected BorderPane crearPanelInicial(Stage primaryStage, String label) {
		BorderPane panelInicial = new BorderPane();
		panelInicial.setStyle("-fx-background-color: " + BLANCO + ";");

		Text titulo = new Text(label);
		titulo.setFont(fuenteTitulo);
		titulo.setStyle("-fx-fill: " + ROSA + "; -fx-stroke: " + NEGRO + "; -fx-stroke-width: 0.5px;");

		panelInicial.setCenter(titulo);

		return panelInicial;
	}

	protected GridPane crearPanelCentralInicio(Stage primaryStage) {
		GridPane panelCentral = new GridPane();
		panelCentral.setVgap(10);
		panelCentral.setHgap(10);
		panelCentral.setStyle("-fx-background-color: " + BLANCO + ";");
		panelCentral.setAlignment(Pos.CENTER);

		agregarCamposTextoInicio(panelCentral);
		agregarComboBox(panelCentral, 2);
		agregarBotonInicio(panelCentral, primaryStage, 3, 80);

		return panelCentral;
	}

	protected void agregarCamposTextoInicio(GridPane panelCentral) {
		String[] etiquetas = { "Nombre usuario : ", "Contraseña : " };

		nombreText = new TextField();
		passText = new PasswordField();

		TextField[] camposTexto = { nombreText, passText };

		for (int i = 0; i < etiquetas.length; i++) {
			Label etiqueta = crearLabel(etiquetas[i], fuenteTexto, NEGRO);
			camposTexto[i]
					.setStyle("-fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO + "; -fx-font-size: 18px;");

			panelCentral.add(etiqueta, 0, i);
			panelCentral.add(camposTexto[i], 1, i);
		}
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

	protected void agregarBotonInicio(GridPane panelCentral, Stage primaryStage, int pos, int insets) {
		Button botonConfirmar = new Button("Aceptar");
		botonConfirmar.setFont(fuenteTexto);
		botonConfirmar.setStyle(estiloBotonFondo());
		botonConfirmar.setPrefWidth(200);
		botonConfirmar.setPrefHeight(40);
		botonConfirmar.setOnAction(e -> validarFormularioInicio(primaryStage));

		cambiarColorBoton(botonConfirmar, ROSA, NEGRO);

		panelCentral.add(botonConfirmar, 0, pos, 2, 1);
		GridPane.setMargin(botonConfirmar, new Insets(insets, 0, 0, 0));
		GridPane.setHalignment(botonConfirmar, HPos.CENTER);

		Button botonRegistrarse = new Button("Registrarse");
		botonRegistrarse.setStyle(
				"-fx-background-color: transparent; -fx-text-fill: " + NEGRO + "; -fx-border-color: transparent;");
		botonRegistrarse.setFont(fuenteTexto);
		botonRegistrarse.setPrefWidth(200);
		botonRegistrarse.setPrefHeight(40);

		cambiarColorLetrasBoton(botonRegistrarse, NEGRO, ROSA);

		botonRegistrarse.setOnAction(e -> {
			Registro registro = new Registro();
			try {
				Scene r = registro.crearEscenaRegistro(primaryStage);
				primaryStage.setTitle("Registro");
				primaryStage.setScene(r);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		panelCentral.add(botonRegistrarse, 0, 4, 2, 1);
		GridPane.setHalignment(botonRegistrarse, HPos.CENTER);

	}

	protected void validarFormularioInicio(Stage primaryStage) {
		String nom = nombreText.getText();
		String con = passText.getText();
		String tipo = seleccion.getValue();

		if (nom.isEmpty() && con.isEmpty()) {
			alerta("Introduzca su nombre y contraseña.", "Campos incompletos");
		} else if (nom.isEmpty()) {
			alerta("Introduzca su nombre.", "Campo incompleto");
		} else if (con.isEmpty()) {
			alerta("Introduzca su contraseña.", "Campo incompleto");
		} else if (con.length() > 100 || nom.length() > 100) {
			alerta("La credenciales no pueden tener demasiada longitud.", "Error longitud credenciales");
		} else {
			Conexion c = new Conexion(nom, con, tipo);
			if (c.getRespuesta().equals("inicioincorrecto")) {
				alerta("Credenciales no válidas.", "Error inicio de sesión");
			} else {
				c.start();
				primaryStage.close();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
