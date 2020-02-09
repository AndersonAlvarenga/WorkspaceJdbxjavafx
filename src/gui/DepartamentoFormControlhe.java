package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeres.DataChangeListerner;
import gui.util.Alerts;
import gui.util.Contraints;
import gui.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import modelo.entidades.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentoFormControlhe implements Initializable {
	private Departamento departamentoIstanci;

	private DepartamentoService depService;

	public void instanciacaoDepartamentoService(DepartamentoService depService) {
		this.depService = depService;
	}
	private List<DataChangeListerner> listaListener=new ArrayList<DataChangeListerner>();
	
	public void carregaListerner(DataChangeListerner listerner) {
		listaListener.add(listerner);
	}

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
	public void onBtSalvarAction(ActionEvent event) {
		try {
			Departamento depart = new Departamento(Util.parseToInt(txtId.getText()), txtNome.getText());
			depService.saveNewDepartmento(depart);
			Alerts.showAlerts("Cadastro realizado", null, "Departamento cadastrado com sucesso", AlertType.INFORMATION);
			notifiqueList();
			Util.palcoAtual(event).close();
		} catch (DbException e) {
			Alerts.showAlerts("Erro ao salvar no banco de dados", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifiqueList() {
		for(DataChangeListerner lister:listaListener) {
			lister.onDataChange();
		}
		
	}

	@FXML
	public void onBtCancelarAction(ActionEvent event) {

		Alerts.showAlerts("Cadastro Cancelado", null, "Departamento não cadastrado", AlertType.INFORMATION);
		Util.palcoAtual(event).close();
	}

	public void setDepartamento(Departamento departamentoInstanc) {
		this.departamentoIstanci = departamentoInstanc;
	}

	public void updateFormTextFild() {
		txtId.setText(String.valueOf(departamentoIstanci.getId()));
		txtNome.setText(String.valueOf(departamentoIstanci.getNome()));
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
