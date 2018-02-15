package javafx.controller;

import app.Main;
import database.entity.Province;
import database.entity.Trade;
import database.exception.DataTooLongViolationException;
import database.exception.NameUniqueViolationException;
import database.service.OfficeService;
import javafx.CustomMessageBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

@Controller
public class ModifyTradesAndProvincesController implements Initializable {
    private OfficeService officeService;
    private ObservableList<Trade> tradeObservableList = FXCollections.observableArrayList();
    private ObservableList<Province> provinceObservableList = FXCollections.observableArrayList();
    private CustomMessageBox customMessageBox;

    @FXML
    private Label labelHeader, labelProvince;
    @FXML
    private TextField textFieldAddTrade, textFieldModifyTrade, textFieldAddProvince, textFieldModifyProvince;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextArea textAreaTradeDialog, textAreaProvinceDialog;
    @FXML
    private TableView<Trade> tableViewTrades;
    @FXML
    private TableColumn<Trade, String> tableColumnTradeName;
    @FXML
    private TableView<Province> tableViewProvinces;
    @FXML
    private TableColumn<Province, String> tableColumnProvinceName;

    public void setFrameObjects(OfficeService officeService) {
        this.officeService = officeService;
        initTradeTableView();
        initProvinceTableView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences pref = Preferences.userRoot();
        labelHeader.setText(pref.get("header",
                "Inter Art Marcin Rogal, ul. Wiktorowska 34, Wapiennik, 42-120 Miedźno, Polska"));
        customMessageBox = new CustomMessageBox("image/icon.png");
    }

    public String getTextOfProvinceLabel() {
        return labelProvince.getText();
    }

    public void changeProvinceLabel(String province) {
        labelProvince.setText(province);
    }

    @FXML
    void buttonAddProvince_onAction() {
        clearDialogTextAreas();

        Boolean thisProvinceAlreadyExist = false;
        String newProvinceName = textFieldAddProvince.getText();
        if (newProvinceName.equals("")) {
            textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
            textAreaProvinceDialog.setText("Operacja dodania województwa nie powiodła się.\n" +
                    "Powód: nazwa obiektu nie może być pusta.");
        } else {
            for (Province province : provinceObservableList)
                if (province.getProvince().equals(newProvinceName))
                    thisProvinceAlreadyExist = true;

            if (!thisProvinceAlreadyExist)
                try {
                    Province newProvince = new Province(newProvinceName);
                    officeService.saveProvince(newProvince);
                    refreshProvinceTableView(officeService.getProvinces());
                    textAreaProvinceDialog.setStyle("-fx-text-fill: #008000;");
                    textAreaProvinceDialog.setText("Operacja dodania województwa przebiegła pomyślnie.");
                    clearProvinceTextFields();
                } catch (DataTooLongViolationException | NameUniqueViolationException e) {
                    textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
                    textAreaProvinceDialog.setText("Operacja dodania województwa nie powiodła się.\n" +
                            "Powód: " + e.getCause().getMessage() + ".");
                }
            else {
                textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
                textAreaProvinceDialog.setText("Operacja dodania województwa nie powiodła się.\n" +
                        "Powód: obiekt o nazwie '" + newProvinceName + "' istnieje już w bazie danych.");
            }
        }
    }

    @FXML
    void buttonAddTrade_onAction() {
        clearDialogTextAreas();

        Boolean thisTradeAlreadyExist = false;
        String newTradeName = textFieldAddTrade.getText();
        if (newTradeName.equals("")) {
            textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
            textAreaTradeDialog.setText("Operacja dodania branży nie powiodła się.\n" +
                    "Powód: nazwa obiektu nie może być pusta.");
        } else {
            for (Trade trade : tradeObservableList)
                if (trade.getTrade().equals(newTradeName))
                    thisTradeAlreadyExist = true;

            if (!thisTradeAlreadyExist)
                try {
                    Trade newTrade = new Trade(newTradeName);
                    officeService.saveTrade(newTrade);
                    refreshTradeTableView(officeService.getTrades());
                    textAreaTradeDialog.setStyle("-fx-text-fill: #008000;");
                    textAreaTradeDialog.setText("Operacja dodania branży przebiegła pomyślnie.");
                    clearTradeTextFields();
                } catch (DataTooLongViolationException | NameUniqueViolationException e) {
                    textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
                    textAreaTradeDialog.setText("Operacja dodania branży nie powiodła się.\n" +
                            "Powód: " + e.getCause().getMessage() + ".");
                }
            else {
                textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
                textAreaTradeDialog.setText("Operacja dodania branży nie powiodła się.\n" +
                        "Powód: obiekt o nazwie '" + newTradeName + "' istnieje już w bazie danych.");
            }
        }
    }

    @FXML
    void buttonCancel_onAction() {
        Boolean sceneWasLoadedSuccessfully = true;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/MainFrame.fxml"));
            loader.load();
        } catch (IOException ioEcx) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ioEcx);
            sceneWasLoadedSuccessfully = false;
        }

        if (sceneWasLoadedSuccessfully) {
            MainFrameController display = loader.getController();
            display.setFrameObjects(officeService);
            Parent parent = loader.getRoot();
            Stage stage = Main.getMainStage();
            Stage currentStage = (Stage) buttonCancel.getScene().getWindow();
            stage.setScene(new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 42.5));
        }
    }

    @FXML
    void buttonDeleteProvince_onAction() {
        clearDialogTextAreas();

        if (tableViewProvinces.getSelectionModel().getSelectedItem() != null) {
            ButtonType confirmButton = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
            customMessageBox.showConfirmMessageBox(Alert.AlertType.CONFIRMATION, "Operacja wymaga potwierdzenia.",
                    "Usunięcie województwa spowoduje aktualizację listy kontaktów.",
                    "W celu potwierdzenia naciśnij przycisk.", confirmButton, cancelButton).showAndWait()
                    .ifPresent(rs -> {
                        if (rs.getText().equals("Potwierdź")) {
                            officeService.deleteProvince(tableViewProvinces.getSelectionModel().getSelectedItem().getId());
                            refreshProvinceTableView(officeService.getProvinces());
                            textAreaProvinceDialog.setStyle("-fx-text-fill: #008000;");
                            textAreaProvinceDialog.setText("Operacja usunięcia województwa przebiegła pomyślnie.");
                            clearProvinceTextFields();
                        }
                    });
        } else {
            textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
            textAreaProvinceDialog.setText("Operacja usunięcia województwa nie powiodła się.\n" +
                    "Powód: nie wybrano województwa.");
        }
    }

    @FXML
    void buttonDeleteTrade_onAction() {
        clearDialogTextAreas();

        if (tableViewTrades.getSelectionModel().getSelectedItem() != null) {
            ButtonType confirmButton = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
            customMessageBox.showConfirmMessageBox(Alert.AlertType.CONFIRMATION, "Operacja wymaga potwierdzenia.",
                    "Usunięcie branży spowoduje aktualizację listy kontaktów.",
                    "W celu potwierdzenia naciśnij przycisk.", confirmButton, cancelButton).showAndWait()
                    .ifPresent(rs -> {
                        if (rs.getText().equals("Potwierdź")) {
                            officeService.deleteTrade(tableViewTrades.getSelectionModel().getSelectedItem().getId());
                            refreshTradeTableView(officeService.getTrades());
                            textAreaTradeDialog.setStyle("-fx-text-fill: #008000;");
                            textAreaTradeDialog.setText("Operacja usunięcia branży przebiegła pomyślnie.");
                            clearTradeTextFields();
                        }
                    });
        } else {
            textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
            textAreaTradeDialog.setText("Operacja usunięcia branży nie powiodła się.\n" +
                    "Powód: nie wybrano branży.");
        }
    }

    @FXML
    void buttonModifyProvince_onAction() {
        clearDialogTextAreas();

        if (tableViewProvinces.getSelectionModel().getSelectedItem() != null) {
            Boolean thisProvinceNameAlreadyExist = false;
            String modifiedProvinceName = textFieldModifyProvince.getText();
            if (modifiedProvinceName.equals("")) {
                textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
                textAreaProvinceDialog.setText("Operacja modyfikacji województwa nie powiodła się.\n" +
                        "Powód: nazwa obiektu nie może być pusta.");
            } else {
                for (Province province : provinceObservableList)
                    if (province.getProvince().equals(modifiedProvinceName))
                        thisProvinceNameAlreadyExist = true;

                if (!thisProvinceNameAlreadyExist) {
                    ButtonType confirmButton = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
                    customMessageBox.showConfirmMessageBox(Alert.AlertType.CONFIRMATION, "Operacja wymaga potwierdzenia.",
                            "Modyfikacja województwa spowoduje aktualizację listy kontaktów.",
                            "W celu potwierdzenia naciśnij przycisk.", confirmButton, cancelButton).showAndWait()
                            .ifPresent(rs -> {
                                if (rs.getText().equals("Potwierdź")) {
                                    try {
                                        Province selectedProvince = officeService.getProvince(tableViewProvinces.getSelectionModel().getSelectedItem().getId());
                                        selectedProvince.setProvince(textFieldModifyProvince.getText());
                                        officeService.saveProvince(selectedProvince);
                                        refreshProvinceTableView(officeService.getProvinces());
                                        textAreaProvinceDialog.setStyle("-fx-text-fill: #008000;");
                                        textAreaProvinceDialog.setText("Operacja modyfikacji województwa przebiegła pomyślnie.");
                                        clearProvinceTextFields();
                                    } catch (DataTooLongViolationException | NameUniqueViolationException e) {
                                        textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
                                        textAreaProvinceDialog.setText("Operacja modyfikacji województwa nie powiodła się.\n" +
                                                "Powód: " + e.getCause().getMessage() + ".");
                                    }
                                }
                            });
                } else {
                    textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
                    textAreaProvinceDialog.setText("Operacja modyfikacji województwa nie powiodła się.\n" +
                            "Powód: obiekt o nazwie '" + modifiedProvinceName + "' istnieje już w bazie danych.");
                }
            }
        } else {
            textAreaProvinceDialog.setStyle("-fx-text-fill: #ff0000;");
            textAreaProvinceDialog.setText("Operacja modyfikacji województwa nie powiodła się.\n" +
                    "Powód: nie wybrano województwa.");
        }
    }

    @FXML
    void buttonModifyTrade_onAction() {
        clearDialogTextAreas();

        if (tableViewTrades.getSelectionModel().getSelectedItem() != null) {
            Boolean thisTradeNameAlreadyExist = false;
            String modifiedTradeName = textFieldModifyTrade.getText();
            if (modifiedTradeName.equals("")) {
                textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
                textAreaTradeDialog.setText("Operacja modyfikacji branży nie powiodła się.\n" +
                        "Powód: nazwa obiektu nie może być pusta.");
            } else {
                for (Trade trade : tradeObservableList)
                    if (trade.getTrade().equals(modifiedTradeName))
                        thisTradeNameAlreadyExist = true;

                if (!thisTradeNameAlreadyExist) {
                    ButtonType confirmButton = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
                    customMessageBox.showConfirmMessageBox(Alert.AlertType.CONFIRMATION, "Operacja wymaga potwierdzenia.",
                            "Modyfikacja branży spowoduje aktualizację listy kontaktów.",
                            "W celu potwierdzenia naciśnij przycisk.", confirmButton, cancelButton).showAndWait()
                            .ifPresent(rs -> {
                                if (rs.getText().equals("Potwierdź")) {
                                    try {
                                        Trade selectedTrade = officeService.getTrade(tableViewTrades.getSelectionModel().getSelectedItem().getId());
                                        selectedTrade.setTrade(modifiedTradeName);
                                        officeService.saveTrade(selectedTrade);
                                        refreshTradeTableView(officeService.getTrades());
                                        textAreaTradeDialog.setStyle("-fx-text-fill: #008000;");
                                        textAreaTradeDialog.setText("Operacja modyfikacji branży przebiegła pomyślnie.");
                                        clearTradeTextFields();
                                    } catch (DataTooLongViolationException | NameUniqueViolationException e) {
                                        textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
                                        textAreaTradeDialog.setText("Operacja modyfikacji branży nie powiodła się.\n" +
                                                "Powód: " + e.getCause().getMessage() + ".");
                                    }
                                }
                            });
                } else {
                    textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
                    textAreaTradeDialog.setText("Operacja modyfikacji branży nie powiodła się.\n" +
                            "Powód: obiekt o nazwie '" + modifiedTradeName + "' istnieje już w bazie danych.");
                }
            }
        } else {
            textAreaTradeDialog.setStyle("-fx-text-fill: #ff0000;");
            textAreaTradeDialog.setText("Operacja modyfikacji branży nie powiodła się.\n" +
                    "Powód: nie wybrano branży.");
        }
    }

    @FXML
    void textFieldAddTrade_onKeyPressed() {
        clearDialogTextAreas();
    }

    @FXML
    void textFieldModifyTrade_onKeyPressed() {
        clearDialogTextAreas();
    }

    @FXML
    void textFieldAddProvince_onKeyPressed() {
        clearDialogTextAreas();
    }

    @FXML
    void textFieldModifyProvince_onKeyPressed() {
        clearDialogTextAreas();
    }

    @FXML
    void tableViewProvinces_onMouseClicked() {
        clearDialogTextAreas();
        try {
            if (tableViewProvinces.getSelectionModel().getSelectedItem() != null) {
                textFieldModifyProvince.setText(tableViewProvinces.getSelectionModel().getSelectedItem().getProvince());
            }
        } catch (NullPointerException nullExc) {
            textFieldModifyProvince.setText("");
        }
    }

    @FXML
    void tableViewTrades_onMouseClicked() {
        clearDialogTextAreas();
        try {
            if (tableViewTrades.getSelectionModel().getSelectedItem() != null) {
                textFieldModifyTrade.setText(tableViewTrades.getSelectionModel().getSelectedItem().getTrade());
            }
        } catch (NullPointerException nullExc) {
            textFieldModifyTrade.setText("");
        }
    }

    private void refreshTradeTableView(List<Trade> trades) {
        tradeObservableList.clear();
        tradeObservableList.addAll(trades);
        tableViewTrades.setItems(tradeObservableList);
    }

    private void initTradeTableView() {
        tableColumnTradeName.setCellValueFactory(new PropertyValueFactory<>("trade"));
        refreshTradeTableView(officeService.getTrades());
    }

    private void refreshProvinceTableView(List<Province> provinces) {
        provinceObservableList.clear();
        provinceObservableList.addAll(provinces);
        tableViewProvinces.setItems(provinceObservableList);
    }

    private void initProvinceTableView() {
        tableColumnProvinceName.setCellValueFactory(new PropertyValueFactory<>("province"));
        refreshProvinceTableView(officeService.getProvinces());
    }

    private void clearDialogTextAreas() {
        if (!textAreaTradeDialog.getText().equals("")) {
            textAreaTradeDialog.setStyle("-fx-text-fill: #000000;");
            textAreaTradeDialog.setText("");
        }
        if (!textAreaProvinceDialog.getText().equals("")) {
            textAreaProvinceDialog.setStyle("-fx-text-fill: #000000;");
            textAreaProvinceDialog.setText("");
        }
    }

    private void clearProvinceTextFields() {
        textFieldModifyProvince.setText("");
        textFieldAddProvince.setText("");
    }

    private void clearTradeTextFields() {
        textFieldModifyTrade.setText("");
        textFieldAddTrade.setText("");
    }
}
