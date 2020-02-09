package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private ObservableList<Departamento>obsList;

	@FXML
	public void onBtNewDepartamentoAction() {
		System.out.println("Teste");
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
		if(serviceDepart==null) {
			throw new IllegalStateException("Nao foi criado depencia com o DepartamentoService ");
		}
		List<Departamento>list = serviceDepart.findAll();
		obsList=FXCollections.observableArrayList(list);
		tableViewDepartamento.setItems(obsList);
	}

}
