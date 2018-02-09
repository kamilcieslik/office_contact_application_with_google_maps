package controller;

import app.Main;
import database.entity.Province;
import database.entity.Trade;
import database.service.OfficeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class AddContactController implements Initializable {
    private OfficeService officeService;
    @FXML
    private Label labelHeader, labelName, labelTrade, labelEmail, labelPhone, labelStreet, labelPostalCode,
            labelCity, labelProvince;
    @FXML
    private TextField textFieldName, textFieldEmail, textFieldPhoneNumber, textFieldStreet, textFieldPostalCode,
            textFieldCity;
    @FXML
    private ComboBox<Trade> comboBoxTrade;
    @FXML
    private ComboBox<Province> comboBoxProvince;

    public void setFrameObjects(OfficeService officeService) {
        this.officeService = officeService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void buttonAdd_onAction() {

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
            Stage currentStage = (Stage) comboBoxProvince.getScene().getWindow();
            stage.setScene(new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 42.5));
        }
    }

    @FXML
    void buttonReset_onAction() {

    }

    @FXML
    void comboBoxProvince_onMouseReleased() {

    }

    @FXML
    void comboBoxTrade_onMouseReleased() {

    }

    @FXML
    void textFieldCity_onAction() {

    }

    @FXML
    void textFieldEmail_onAction() {

    }

    @FXML
    void textFieldName_onAction() {

    }

    @FXML
    void textFieldPhoneNumber_onAction() {

    }

    @FXML
    void textFieldPostalCode_onAction() {

    }

    @FXML
    void textFieldStreet_onAction() {

    }
}
