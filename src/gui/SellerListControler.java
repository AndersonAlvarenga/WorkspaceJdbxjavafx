package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegritExeption;
import gui.listeres.DataChangeListerner;
import gui.util.Alerts;
import gui.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.entidades.Seller;
import modelo.service.SellerService;

public class SellerListControler implements Initializable, DataChangeListerner {
	private SellerService serviceSeller;
	@FXML
	private TableView<Seller> tableViewSeller;
	@FXML
	private TableColumn<Seller, Integer> tableColunaId;
	@FXML
	private TableColumn<Seller, String> tableColunaNome;
	@FXML
	private TableColumn<Seller, String> tableColunaEmail;
	@FXML
	private TableColumn<Seller, Double> tableColunaBaseSalary;
	@FXML
	private TableColumn<Seller,Date> tableColunaBirthDate;
	@FXML
	private Button btNewSeller;
	private ObservableList<Seller> obsList;
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	@FXML
	private TableColumn<Seller, Seller> tableColumnDelete;

	@FXML
	public void onBtNewSellerAction(ActionEvent action) {
		Stage parent = Util.palcoAtual(action);
		Seller dep = new Seller();
		createDialogForm(dep, "/gui/SellerForm.fxml", parent);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		InitializableNodes();

	}

	private void InitializableNodes() {
		tableColunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColunaEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
		tableColunaBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Util.formatTableColumnDouble(tableColunaBaseSalary, 2);
		tableColunaBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Util.formatTableColumnDate(tableColunaBirthDate, "dd/MM/yyyy");
		

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

	}

	public void setServiceSeller(SellerService service) {
		this.serviceSeller = service;
	}

	public void UpdateTableSeller() {
		if (serviceSeller == null) {
			throw new IllegalStateException("Nao foi criado depencia com o SellerService ");
		}
		List<Seller> list = serviceSeller.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initDeleteButtons();
	}

	public void createDialogForm(Seller dep, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//
//			SellerFormControlhe controle = loader.getController();
//			controle.setSeller(dep);
//			controle.instanciacaoSellerService(new SellerService());
//			controle.carregaListerner(this);
//			controle.updateFormTextFild();
//
//			Stage stage = new Stage();
//			stage.setTitle("");
//			stage.setScene(new Scene(pane));
//			stage.setResizable(false);
//			stage.initOwner(parentStage);
//			stage.initModality(Modality.WINDOW_MODAL);
//			stage.showAndWait();
//		} catch (IOException e) {
//			Alerts.showAlerts("Erro", "Erro ao carregar tela", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void onDataChange() {
		UpdateTableSeller();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Util.palcoAtual(event)));
			}
		});
	}

	private void initDeleteButtons() {
		tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnDelete.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("Delete");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeDepart(obj));
			}
		});
	}

	private void removeDepart(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmaticao("Confirmação",
				"Deseja delatar o Seller selecionado:");
		if (result.get() == ButtonType.OK) {
			if (serviceSeller == null) {
				throw new IllegalStateException("SellerService nao instanciado");
			}
			try {
				serviceSeller.removeDepart(obj);
				UpdateTableSeller();

			} catch (DbIntegritExeption e) {
				Alerts.showAlerts("Erro ao deletar", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}
}
