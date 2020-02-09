package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import gui.util.Contraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.dao.DaoFactory;
import modelo.entidades.Departamento;

public class DepartamentoFormControlhe implements Initializable{

	@FXML
	private Button brSalvar;
	@FXML
	private Button brCancelar;
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private Label labelErro;
	
	@FXML
	public void onBtSalvarAction() {
		Departamento depart = new Departamento(null, txtNome.getText());
		DaoFactory.createDepartDao().insert(depart);
		Alerts.showAlerts("Cadastro realizado", null, "Departamento cadastrado com sucesso", AlertType.INFORMATION);
	}
	@FXML
	public void onBtCancelarAction() {
		
		Alerts.showAlerts("Cadastro Cancelado", null, "Departamento não cadastrado", AlertType.INFORMATION);
		
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InitializeNodes();
	}
	
	
	
	private void InitializeNodes() {
		Contraints.setTextFieldInteger(txtId);
		Contraints.setTextFieldMaxLength(txtNome, 30);
	}


}
