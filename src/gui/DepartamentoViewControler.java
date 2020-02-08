package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.entidade.Departamento;

public class DepartamentoViewControler implements Initializable {

	@FXML
	private TableView<Departamento> tableViewDepartamento;
	@FXML
	private TableColumn<Departamento, Integer> tableColunaId;
	@FXML
	private TableColumn<Departamento, String> tableColunaNome;
	@FXML
	private Button btNewDepartamento;
	
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
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage =(Stage) Main.getMainScne().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
		
	}




	
	
	

}
