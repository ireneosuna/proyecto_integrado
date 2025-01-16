package interfaz;

import java.sql.Date;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UtilesInterfaz extends Application {
	
	protected static final String BLANCO = "#FFFFFF";
	protected static final String NEGRO = "#000000";
	protected static final String GRIS = "#f2f3f5";
	protected static final String ROSA = "#fea6eb";
	protected static final String ROSA_OSCURO = "#f263d3";
	protected static final String GRIS_OSCURO = "#c6c7c9";
	protected static final String GRIS_CLARO = "#fcfcfc";

	protected Font fuenteTitulo;
	protected Font fuenteSegundoTitulo;
	protected Font fuenteTexto;

	protected TextField nombreText, usuarioText, emailText, nombreActividadText, vestimentaText, horaText,
			maxPersonasText, precioText, edadRecomendadaText, ciudadPartida, ciudadActividad, idiomas;
	protected DatePicker fechaNacimientoText, fechaActividadText;
	protected PasswordField passText;
	protected ComboBox<String> seleccion;
	protected Date fechaNacimiento, fechaActividad;
	protected CheckBox transporte, mascotas;
	protected TextArea descripcionText;

	@Override
	public void start(Stage primaryStage) throws Exception {
	}

	protected void centrarPantalla(Stage primaryStage) {
		primaryStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - primaryStage.getWidth()) / 2);
		primaryStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - primaryStage.getHeight()) / 2);
	}

	protected void cargarFuentes() {
		fuenteTitulo = Font.font("Arial", FontWeight.BOLD, 62);
		fuenteSegundoTitulo = Font.font("Arial", FontWeight.BOLD, 42);
		fuenteTexto = Font.font("Arial", 18);
	}

	protected void alerta(String mensaje, String titulo) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(titulo);
		alert.setHeaderText(null);
		alert.setContentText(mensaje);
		
		Label x = new Label("✘");
		x.setStyle("-fx-text-fill: "+NEGRO+"; -fx-font-size: 30px;");
		
		alert.getDialogPane().setGraphic(x);
		alert.getDialogPane().setStyle("-fx-background-color: "+GRIS+"; -fx-text-fill: "+NEGRO+"; -fx-font-size: 15px;");
		
		alert.getDialogPane().lookupButton(ButtonType.OK).setStyle("-fx-background-color: "+ROSA+"; -fx-text-fill: "+NEGRO+";"); 

		alert.showAndWait();
	}

	protected VBox crearPanelPrincipal(int spacing) {
		VBox panel = new VBox();
		panel.setSpacing(spacing);
		panel.setStyle("-fx-background-color: " + BLANCO + ";");
		panel.setAlignment(Pos.CENTER);
		return panel;
	}

	protected String estiloCampoTexto() {
		return " -fx-background-color: " + GRIS + "; -fx-text-fill: " + NEGRO
				+ "; -fx-pref-width: 200px; -fx-font-size: 18px;";
	}

	protected String estiloCampoDeshabilitado() {
		return " -fx-background-color: " + GRIS_CLARO + "; -fx-text-fill: " + NEGRO
				+ "; -fx-pref-width: 200px; -fx-font-size: 18px;";
	}

	protected String estiloComboBox() {
		return "-fx-font-family: 'Roboto'; -fx-font-size: 18px; -fx-background-color: " + GRIS
				+ "; -fx-border-color: transparent;";
	}

	protected String estiloBotonFondo() {
		return "-fx-background-color: " + ROSA + "; -fx-text-fill: " + NEGRO + "; -fx-background-radius: 18px;";
	}

	protected String estiloCheckBox() {
		return " -fx-mark-color:" + NEGRO + "; " + "-fx-font-size: 18px; " + "-fx-background-color: " + GRIS + "; "
				+ "-fx-border-color: transparent; " + "-fx-focus-color: " + ROSA + "; " + "-fx-focus-border-color: "
				+ ROSA + "; " + "-fx-faint-focus-color: " + ROSA + "; " + "-fx-faint-focus-border-color: " + ROSA
				+ "; -fx-selected-color: "+ROSA+";";
	}

	protected void cambiarColorLetrasBoton(Button boton, String letrasActuales, String letrasTocar) {
		boton.setOnMouseEntered(event -> {
			boton.setStyle(String.format("-fx-background-color: transparent; -fx-text-fill: " + letrasTocar + ";"));
		});

		boton.setOnMouseExited(event -> {
			boton.setStyle(String.format("-fx-background-color: transparent; -fx-text-fill: " + letrasActuales + ";"));
		});
	}

	protected void cambiarColorBoton(Button boton, String colorFondo, String colorTexto) {
		boton.setOnMouseEntered(event -> boton
				.setStyle("-fx-background-color: " + colorTexto + "; -fx-text-fill: " + colorFondo + "; -fx-background-radius: 18px;"));
		boton.setOnMouseExited(event -> boton
				.setStyle("-fx-background-color: " + colorFondo + "; -fx-text-fill: " + colorTexto + "; -fx-background-radius: 18px;"));
	}

	protected Label crearLabel(String texto, Font fuente, String color) {
		Label label = new Label(texto);
		label.setFont(fuente);
		label.setTextFill(Color.web(color));
		return label;
	}
}
