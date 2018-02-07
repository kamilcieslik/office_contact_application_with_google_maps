package controller;

import app.Main;
import database.entity.Province;
import database.entity.Trade;
import database.service.OfficeService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private Preferences pref;
    private OfficeService officeService;
    private ObservableList<Trade> tradeObservableList = FXCollections.observableArrayList();
    private ObservableList<Province> provinceObservableList = FXCollections.observableArrayList();
    private Trade selectedTrade = null;
    private Province selectedProvince = null;

    @FXML
    private Label labelHeader, labelProvince;
    @FXML
    private TextField textFieldAddTrade, textFieldModifyTrade, textFieldAddProvince, textFieldModifyProvince;
    @FXML
    private Button buttonAddTrade, buttonDeleteProvince, buttonAddProvince, buttonModifyProvince, buttonCancel,
            buttonModifyTrade, buttonDeleteTrade;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
        labelHeader.setText(pref.get("header",
                "Inter Art Marcin Rogal, ul. Wiktorowska 34, Wapiennik, 42-120 Miedźno, Polska"));
    }

    public void setFrameObjects(OfficeService officeService) {
        this.officeService = officeService;
        initTradeTableView();
        initProvinceTableView();
    }

    public String getTextOfProvinceLabel() {
        return labelProvince.getText();
    }

    public void changeProvinceLabel(String province) {
        labelProvince.setText(province);
    }

    @FXML
    void buttonAddProvince_onAction() {

    }

    @FXML
    void buttonAddTrade_onAction() {

    }

    @FXML
    void buttonCancel_onAction() {
        Boolean sceneWasLoadedSuccessfully = true;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../fxml/MainFrame.fxml"));
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

    }

    @FXML
    void buttonDeleteTrade_onAction() {

    }

    @FXML
    void buttonModifyProvince_onAction() {

    }

    @FXML
    void buttonModifyTrade_onAction() {

    }

    @FXML
    void tableViewProvinces_onMouseClicked() {

    }

    @FXML
    void tableViewTrades_onMouseClicked() {

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

}
