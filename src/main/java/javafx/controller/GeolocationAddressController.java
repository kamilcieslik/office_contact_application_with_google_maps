package javafx.controller;

import com.google.maps.model.GeocodingResult;
import database.entity.Address;
import database.entity.Province;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class GeolocationAddressController implements Initializable {
    private Integer indexOfSelectedAddress;
    private GeocodingResult[] geocodingResults;
    private Address address;
    private ObservableList<Province> provinceObservableList;

    @FXML
    private Label labelNumberOfAddresses;
    @FXML
    private TextField textFieldProvince, textFieldCounty, textFieldPostalCode, textFieldCity, textFieldDistrict,
            textFieldStreet, textFieldGeocodedAddress;
    @FXML
    private CheckBox checkBoxProvince, checkBoxPostalCode, checkBoxCity, checkBoxStreet, checkBoxGeocodedAddress;
    @FXML
    private VBox vBoxDistrict, vBoxStreet, vBoxProvince, vBoxCity, vBoxCounty, vBoxPostalCode;

    public void setAddressComponents(GeocodingResult[] geocodingResults, Address address, ObservableList<Province> provinceObservableList) {
        this.address = address;
        indexOfSelectedAddress = 0;
        this.geocodingResults = geocodingResults;
        this.provinceObservableList = provinceObservableList;
        setAddressData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableAllVBoxes();
    }

    @FXML
    void buttonSave_onAction() {
        if (checkBoxPostalCode.isSelected())
            address.setPostalCode(textFieldPostalCode.getText());
        if (checkBoxStreet.isSelected())
            address.setStreet(textFieldStreet.getText());
        if (checkBoxCity.isSelected())
            address.setCity(textFieldCity.getText());
        if (checkBoxProvince.isSelected())
            for (Province province : provinceObservableList) {
                if (textFieldProvince.getText().equals(province.getProvince())) {
                    address.setProvince(province);
                    break;
                }
                address.setProvince(new Province(textFieldProvince.getText()));
            }
        if (checkBoxGeocodedAddress.isSelected()) {
            String[] splitedGeocodedAddress = textFieldGeocodedAddress.getText().split(" ");
            address.setLatitude(splitedGeocodedAddress[0].substring(0, splitedGeocodedAddress[0].length() - 1));
            address.setLongitude(splitedGeocodedAddress[1]);
        } else {
            address.setLatitude(null);
            address.setLongitude(null);
        }

        Stage stage = (Stage) textFieldStreet.getScene().getWindow();
        stage.close();
    }

    @FXML
    void imageViewPrevious_onMouseClicked() {
        if (indexOfSelectedAddress - 1 >= 0) {
            disableAllVBoxes();
            indexOfSelectedAddress--;
            setAddressData();
        }
    }

    @FXML
    void imageViewNext_onMouseClicked() {
        if (indexOfSelectedAddress + 1 <= geocodingResults.length - 1) {
            disableAllVBoxes();
            indexOfSelectedAddress++;
            setAddressData();
        }
    }

    private void setAddressData() {
        labelNumberOfAddresses.setText("Nr wyświetlonego adresu: " + (indexOfSelectedAddress + 1) + "/" + String.valueOf(geocodingResults.length));
        String addressType;
        for (int i = 0; i < geocodingResults[indexOfSelectedAddress].addressComponents.length; i++) {
            addressType = geocodingResults[indexOfSelectedAddress].addressComponents[i].types[0].toCanonicalLiteral();
            switch (addressType) {
                case "street_number":
                    changeVBoxVisible(vBoxStreet, true);
                    if (!textFieldStreet.getText().equals("")) {
                        String currentStreetText = textFieldStreet.getText();
                        textFieldStreet.setText(currentStreetText + " " + geocodingResults[indexOfSelectedAddress].addressComponents[i].longName);
                    } else
                        textFieldStreet.setText(geocodingResults[indexOfSelectedAddress].addressComponents[i].longName);
                    checkBoxStreet.setSelected(true);
                    break;
                case "route":
                    changeVBoxVisible(vBoxStreet, true);
                    if (!textFieldStreet.getText().equals("")) {
                        String currentStreetText = textFieldStreet.getText();
                        textFieldStreet.setText(geocodingResults[indexOfSelectedAddress].addressComponents[i].longName + " " + currentStreetText);
                    } else
                        textFieldStreet.setText(geocodingResults[indexOfSelectedAddress].addressComponents[i].longName);
                    checkBoxStreet.setSelected(true);
                    break;
                case "political":
                    changeVBoxVisible(vBoxDistrict, true);
                    textFieldDistrict.setText(geocodingResults[indexOfSelectedAddress].addressComponents[i].longName);
                    break;
                case "locality":
                    changeVBoxVisible(vBoxCity, true);
                    textFieldCity.setText(geocodingResults[indexOfSelectedAddress].addressComponents[i].longName);
                    checkBoxCity.setSelected(true);
                    break;
                case "administrative_area_level_2":
                    changeVBoxVisible(vBoxCounty, true);
                    textFieldCounty.setText(geocodingResults[indexOfSelectedAddress].addressComponents[i].longName);
                    break;
                case "administrative_area_level_1":
                    changeVBoxVisible(vBoxProvince, true);
                    String province = geocodingResults[indexOfSelectedAddress].addressComponents[i].shortName;
                    if (province.contains("Województwo") || province.contains("województwo"))
                        province = province.substring(12);
                    textFieldProvince.setText(province);
                    checkBoxProvince.setSelected(true);
                    break;
                case "postal_code":
                    changeVBoxVisible(vBoxPostalCode, true);
                    textFieldPostalCode.setText(geocodingResults[indexOfSelectedAddress].addressComponents[i].longName);
                    checkBoxPostalCode.setSelected(true);
                    break;
            }
        }
        textFieldGeocodedAddress.setText(String.valueOf(geocodingResults[indexOfSelectedAddress].geometry.location.lat)
                + ", " + String.valueOf(geocodingResults[indexOfSelectedAddress].geometry.location.lng));
        checkBoxGeocodedAddress.setSelected(true);
    }

    private void disableAllVBoxes() {
        changeVBoxVisible(vBoxCity, false);
        changeVBoxVisible(vBoxCounty, false);
        changeVBoxVisible(vBoxDistrict, false);
        changeVBoxVisible(vBoxPostalCode, false);
        changeVBoxVisible(vBoxProvince, false);
        changeVBoxVisible(vBoxStreet, false);

        checkBoxCity.setSelected(false);
        checkBoxStreet.setSelected(false);
        checkBoxProvince.setSelected(false);
        checkBoxPostalCode.setSelected(false);
        checkBoxGeocodedAddress.setSelected(false);

        textFieldStreet.setText("");
        textFieldProvince.setText("");
        textFieldPostalCode.setText("");
        textFieldDistrict.setText("");
        textFieldCounty.setText("");
        textFieldCity.setText("");
        textFieldGeocodedAddress.setText("");
    }

    private void changeVBoxVisible(VBox vBox, Boolean visible) {
        if (!visible) {
            vBox.setVisible(false);
            vBox.setDisable(true);
            vBox.setMinWidth(0);
            vBox.setMinHeight(0);
            vBox.setPrefWidth(0);
            vBox.setPrefHeight(0);
            vBox.setMaxWidth(0);
            vBox.setMaxHeight(0);
        } else {
            vBox.setVisible(true);
            vBox.setDisable(false);
            vBox.setMinWidth(Control.USE_COMPUTED_SIZE);
            vBox.setMinHeight(Control.USE_COMPUTED_SIZE);
            vBox.setPrefWidth(Control.USE_COMPUTED_SIZE);
            vBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
            vBox.setMaxWidth(Control.USE_COMPUTED_SIZE);
            vBox.setMaxHeight(Control.USE_COMPUTED_SIZE);
        }
    }
}
