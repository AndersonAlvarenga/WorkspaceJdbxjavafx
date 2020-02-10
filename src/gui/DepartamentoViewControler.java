package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeres.DataChangeListerner;
import gui.util.Alerts;
import gui.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.entidades.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentoViewControler implements Initializable, DataChangeListerner {
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
	private TableColumn<Departamento, Departamento> tableColumnEDIT;

	@FXML
	public void onBtNewDepartamentoAction(ActionEvent action) {
		Stage parent = Util.palcoAtual(action);
		Departamento dep = new Departamento();
		createDialogForm(dep, "/gui/DepartamentoForm.fxml", parent);
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
		initEditButtons();
	}

	public void createDialogForm(Departamento dep, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DepartamentoFormControlhe controle = loader.getController();
			controle.setDepartamento(dep);
			controle.instanciacaoDepartamentoService(new DepartamentoService());
			controle.carregaListerner(this);
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

	@Override
	public void onDataChange() {
		UpdateTableDepartamento();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Departamento obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartamentoForm.fxml", Util.palcoAtual(event)));
			}
		});
	}

}
