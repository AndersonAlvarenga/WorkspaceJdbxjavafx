package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.entidades.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentoViewControler implements Initializable {
	private DepartamentoService serviceDepart;
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	@FXML
	private TableColumn<Departamento, Integer> tableColunaId;
	@FXML
	private TableColumn<Departamento, String> tableColunaNome;
	@FXML
	private Button btNewDepartamento;
	private ObservableList<Departamento> obsList;

	@FXML
	public void onBtNewDepartamentoAction(ActionEvent action) {
		Stage parent = Util.palcoAtual(action);
		Departamento dep = new Departamento();
		createDialogForm(dep,"/gui/DepartamentoForm.fxml", parent);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		InitializableNodes();

	}

	private void InitializableNodes() {
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());

	}

	public void setServiceDepartamento(DepartamentoService service) {
		this.serviceDepart = service;
	}

	public void UpdateTableDepartamento() {
		if (serviceDepart == null) {
			throw new IllegalStateException("Nao foi criado depencia com o DepartamentoService ");
		}
		List<Departamento> list = serviceDepart.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}

	public void createDialogForm(Departamento dep, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartamentoFormControlhe controle = loader.getController();
			controle.setDepartamento(dep);
			controle.updateFormTextFild();

			Stage stage = new Stage();
			stage.setTitle("");
			stage.setScene(new Scene(pane));
			stage.setResizable(false);
			stage.initOwner(parentStage);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlerts("Erro", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
		}
	}

}
