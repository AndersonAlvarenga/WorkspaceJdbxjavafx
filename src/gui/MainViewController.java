package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemVendedorAction() {
		System.out.println("teste");
	}

	public void onMenuItemDepartamentoAction() {
		System.out.println("teste");
	}

	public void onMenuItemAboutAction() {
		System.out.println("teste");
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		// TODO Auto-generated method stub

	}

}
