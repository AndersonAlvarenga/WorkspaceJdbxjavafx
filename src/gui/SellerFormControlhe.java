package gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import modelo.Exception.ValidacaoExeption;
import modelo.entidades.Departamento;
import modelo.entidades.Seller;
import modelo.service.DepartamentoService;
import modelo.service.SellerService;

public class SellerFormControlhe implements Initializable {
	private Seller sellerInstanci;

	public void instaciacaoSeller(Seller seller) {
		this.sellerInstanci = seller;
	}

	private SellerService sellerService;
	private DepartamentoService depService;

	public void instanciacaoServices(SellerService sellerService, DepartamentoService depService) {
		this.sellerService = sellerService;
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
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Departamento> cbDepartamento;
	@FXML
	private Label labelErro;
	@FXML
	private Label labelErroEmail;
	@FXML
	private Label labelErroData;
	@FXML
	private Label labelErroSalario;
	private ObservableList<Departamento> obsList;

	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		try {
			sellerInstanci = getFormData();
			// Seller depart = new Seller(Util.parseToInt(txtId.getText()),
			// txtNome.getText());
			sellerService.saveNewDepartmento(sellerInstanci);
			Alerts.showAlerts("Cadastro realizado", null, "Seller cadastrado com sucesso", AlertType.INFORMATION);
			notifiqueList();
			Util.palcoAtual(event).close();
		} catch (ValidacaoExeption e) {
			setError(e.getErro());

		} catch (DbException e) {
			e.getStackTrace();
			Alerts.showAlerts("Erro ao salvar no banco de dados", null, e.getMessage(), AlertType.ERROR);
		}

	}

	public void loadComboBox() {
		if (depService == null) {
			throw new IllegalStateException("DepService igual a null");
		}
		List<Departamento> listaDepartamento = depService.findAll();
		obsList = FXCollections.observableArrayList(listaDepartamento);
		cbDepartamento.setItems(obsList);
	}

	public Seller getFormData() {
		Seller obj = new Seller();
		ValidacaoExeption validExeption = new ValidacaoExeption("Erro de validação");

		obj.setId(Util.parseToInt(txtId.getText()));
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			validExeption.addErro("Nome", "Campo não pode ser vazio");
		}
		obj.setName(txtNome.getText());
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

		Alerts.showAlerts("Cadastro Cancelado", null, "Seller não cadastrado", AlertType.INFORMATION);
		Util.palcoAtual(event).close();
	}

	public void setSeller(Seller sellerInstanc) {
		this.sellerInstanci = sellerInstanc;
	}

	public void updateFormTextFild() {
		txtId.setText(String.valueOf(sellerInstanci.getId()));
		txtNome.setText(String.valueOf(sellerInstanci.getName()));
		txtEmail.setText(String.valueOf(sellerInstanci.getEmail()));
		txtBaseSalary.setText(String.format("%.2f", sellerInstanci.getBaseSalary()));
		if (sellerInstanci.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDateTime
					.ofInstant(sellerInstanci.getBirthDate().toInstant(), ZoneId.systemDefault()).toLocalDate());
		}
		if (cbDepartamento != null) {
			cbDepartamento.setValue(sellerInstanci.getDepartment());
		}
		// cbDepartamento.setValue();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InitializeNodes();
	}

	private void InitializeNodes() {
		Contraints.setTextFieldInteger(txtId);
		Contraints.setTextFieldMaxLength(txtNome, 70);
		if (txtBaseSalary != null) {
			Contraints.setTextFieldDouble(txtBaseSalary);
		}
		Contraints.setTextFieldMaxLength(txtEmail, 60);
		Util.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartamento();
	}

	private void initializeComboBoxDepartamento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		cbDepartamento.setCellFactory(factory);
		cbDepartamento.setButtonCell(factory.call(null));
	}

}
