package javafx.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@Controller
public class ModifyGoogleApiKeyController implements Initializable {
    private Preferences pref;

    @FXML
    private TextField textFieldKey;
    @FXML
    private Label labelKey;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
        textFieldKey.setText(pref.get("google_api_key", ""));

        try {
            GeoApiContext context = new GeoApiContext.Builder().apiKey(textFieldKey.getText()).build();
            GeocodingApi.geocode(context, "Polska").await();
            labelKey.setStyle("-fx-text-fill: #008000;");
            labelKey.setText("Wprowadzony klucz Google Maps API jest poprawny.");
        } catch (InterruptedException | IOException | ApiException | IllegalStateException e) {
            switch (e.getMessage()) {
                case "No route to host: connect":
                    labelKey.setStyle("-fx-text-fill: #ff0000;");
                    labelKey.setText("Brak połączenia z Internetem.");
                    break;
                case "The provided API key is invalid.":
                    labelKey.setStyle("-fx-text-fill: #ff0000;");
                    labelKey.setText("Wprowadzony klucz Google Maps API jest niepoprawny.");
                    break;
                case "You have exceeded your daily request quota for this API.":
                    labelKey.setStyle("-fx-text-fill: #008000;");
                    labelKey.setText("Wprowadzony klucz Google Maps API jest poprawny.");
                    break;
            }
        }
    }

    @FXML
    void buttonCancel_onAction() {
        Stage stage = (Stage) textFieldKey.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buttonCheckKey_onAction() {
        try {
            GeoApiContext context = new GeoApiContext.Builder().apiKey(textFieldKey.getText()).build();
            GeocodingApi.geocode(context, "Polska").await();
            labelKey.setStyle("-fx-text-fill: #008000;");
            labelKey.setText("Wprowadzony klucz Google Maps API jest poprawny.");
        } catch (InterruptedException | IOException | ApiException | IllegalStateException e) {
            switch (e.getMessage()) {
                case "No route to host: connect":
                    labelKey.setStyle("-fx-text-fill: #ff0000;");
                    labelKey.setText("Brak połączenia z Internetem.");
                    break;
                case "The provided API key is invalid.":
                    labelKey.setStyle("-fx-text-fill: #ff0000;");
                    labelKey.setText("Wprowadzony klucz Google Maps API jest niepoprawny.");
                    break;
                case "You have exceeded your daily request quota for this API.":
                    labelKey.setStyle("-fx-text-fill: #008000;");
                    labelKey.setText("Wprowadzony klucz Google Maps API jest poprawny.");
                    break;
            }
        }
    }

    @FXML
    void buttonSaveKey_onAction() {
        pref.put("google_api_key", textFieldKey.getText());
        Stage stage = (Stage) textFieldKey.getScene().getWindow();
        stage.close();
    }
}
