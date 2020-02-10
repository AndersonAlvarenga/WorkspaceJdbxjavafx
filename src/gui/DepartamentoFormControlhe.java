package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import modelo.Exception.ValidacaoExeption;
import modelo.entidades.Departamento;
import modelo.service.DepartamentoService;

public class DepartamentoFormControlhe implements Initializable {
	private Departamento departamentoIstanci;

	public void instaciacaoDepartamento(Departamento dep) {
		this.departamentoIstanci = dep;
	}

	private DepartamentoService depService;

	public void instanciacaoDepartamentoService(DepartamentoService depService) {
		this.depService = depService;
	}

	private List<DataChangeListerner> listaListener = new ArrayList<DataChangeListerner>();

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
			departamentoIstanci = getFormData();
			// Departamento depart = new Departamento(Util.parseToInt(txtId.getText()),
			// txtNome.getText());
			depService.saveNewDepartmento(departamentoIstanci);
			Alerts.showAlerts("Cadastro realizado", null, "Departamento cadastrado com sucesso", AlertType.INFORMATION);
			notifiqueList();
			Util.palcoAtual(event).close();
		}catch(ValidacaoExeption e){
			setError(e.getErro());
			
		}catch (DbException e) {
			Alerts.showAlerts("Erro ao salvar no banco de dados", null, e.getMessage(), AlertType.ERROR);
		}

	}

	public Departamento getFormData() {
		Departamento obj = new Departamento(); 
		ValidacaoExeption validExeption = new ValidacaoExeption("Erro de validação");
		
		obj.setId(Util.parseToInt(txtId.getText()));
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			validExeption.addErro("Nome", "Campo não pode ser vazio");
		}
		obj.setNome(txtNome.getText());
		if (validExeption.getErro().size() > 0) {
			throw validExeption;
		}
		return obj;
	}

	private void setError(Map<String, String> erro) {
		Set<String> listaSet = erro.keySet();
		if (listaSet.contains("Nome")) {
			labelErro.setText(erro.get("Nome"));
		}
	}

	private void notifiqueList() {
		for (DataChangeListerner lister : listaListener) {
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
