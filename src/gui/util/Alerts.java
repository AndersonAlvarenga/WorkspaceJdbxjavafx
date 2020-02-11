package gui.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {

	public static void showAlerts(String title, String header, String content, AlertType type) {

		Alert alerta = new Alert(type);
		alerta.setTitle(title);
		alerta.setHeaderText(header);
		alerta.setContentText(content);
		alerta.show();
	}
	public static Optional<ButtonType> showConfirmaticao(String titulo,String conteudo) {
		Alert alerta = new Alert(AlertType.CONFIRMATION);
		alerta.setTitle(titulo);
		alerta.setContentText(conteudo);
		return alerta.showAndWait();
	}

}
