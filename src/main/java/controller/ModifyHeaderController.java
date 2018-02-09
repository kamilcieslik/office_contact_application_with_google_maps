package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Controller
public class ModifyHeaderController implements Initializable {
    private Preferences pref;

    @FXML
    private TextField textFieldHeader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
    }

    @FXML
    void buttonCancel_onAction() {
        Stage stage = (Stage) textFieldHeader.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buttonModifyHeader_onAction() {
        pref.put("header", textFieldHeader.getText());
        Stage stage = (Stage) textFieldHeader.getScene().getWindow();
        stage.close();
    }

    public void setCurrentHeaderText(String headerText) {
        textFieldHeader.setText(headerText);
    }
}
