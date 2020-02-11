package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import modelo.service.DepartamentoService;
import modelo.service.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/SellerLista.fxml", (SellerListControler seller) -> {
			seller.setServiceSeller(new SellerService());
			seller.UpdateTableSeller();
		});
	}

	public void onMenuItemDepartamentoAction() {
		loadView("/gui/DepartamentoLista.fxml", (DepartamentoListControler dep) -> {
			dep.setServiceDepartamento(new DepartamentoService());
			dep.UpdateTableDepartamento();
		});
	}

	public void onMenuItemAboutAction() {
		loadView("/gui/AboutView.fxml", x -> {
		});
	}

	public synchronized <T> void loadView(String nomeView, Consumer<T> expressao) {
		try {
			FXMLLoader newView = new FXMLLoader(getClass().getResource(nomeView));
			VBox newVBox = newView.load();
			Scene mainScene = Main.getMainScene();
			VBox mainsVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainsVBox.getChildren().get(0);
			mainsVBox.getChildren().clear();
			mainsVBox.getChildren().add(mainMenu);
			mainsVBox.getChildren().addAll(newVBox.getChildren());
			T dep = newView.getController();
			expressao.accept(dep);
		} catch (IOException e) {
			Alerts.showAlerts("IOException", "Error", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		// TODO Auto-generated method stub

	}

}
