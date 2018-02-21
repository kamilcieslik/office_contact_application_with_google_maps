package javafx.controller;

import app.Main;
import database.entity.Contact;
import database.entity.Province;
import database.entity.Trade;
import database.exception.DataTooLongViolationException;
import database.service.OfficeService;
import database.view.ViewExtendedContact;
import javafx.AddressGeolocation;
import javafx.CustomMessageBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

@Controller
public class MainFrameController implements Initializable {
    private Preferences pref;
    private OfficeService officeService;
    private ObservableList<ViewExtendedContact> viewContactObservableList = FXCollections.observableArrayList();
    private ObservableList<String> tradeObservableList = FXCollections.observableArrayList();
    private ObservableList<String> provinceObservableList = FXCollections.observableArrayList();
    private List<Trade> trades;
    private List<Province> provinces;
    private CustomMessageBox customMessageBox;

    @FXML
    private Label labelHeader, labelDetails, labelGeocode;
    @FXML
    private Button buttonSaveChanges;
    @FXML
    private TableView<ViewExtendedContact> tableViewContacts;
    @FXML
    private TableColumn<ViewExtendedContact, String> tableColumnName, tableColumnTrade, tableColumnEmail, tableColumnPhone,
            tableColumnStreet, tableColumnPostalCode, tableColumnCity, tableColumnProvince;
    @FXML
    private RadioButton radioButtonMapMode, radioButtonDetailsMode, radioButtonSearchMode;
    @FXML
    private VBox vBoxSearchMode, vBoxDetailsMode, vBoxMapMode, vBoxGeocode;
    @FXML
    private HBox hBoxSelectedContactButtons;
    @FXML
    private TextField textFieldName, textFieldEmail, textFieldPhone, textFieldStreet, textFieldPostalCode, textFieldCity;
    @FXML
    private ComboBox<String> comboBoxTrade;
    @FXML
    private ComboBox<String> comboBoxProvince;
    @FXML
    private CheckBox checkBoxDescription, checkBoxComments, checkBoxAddress, checkBoxGeolocation;
    @FXML
    private TextArea textAreaDescription, textAreaComments;
    @FXML
    private WebView webViewGoogleMaps;

    public void setFrameObjects(OfficeService officeService) {
        this.officeService = officeService;
        trades = officeService.getTrades();
        provinces = officeService.getProvinces();
        initTableView();
        initRadioButtons();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pref = Preferences.userRoot();
        prepareContactComponents(false);
        labelHeader.setText(pref.get("header",
                "Inter Art Marcin Rogal, ul. Wiktorowska 34, Wapiennik, 42-120 Miedźno, Polska"));
        customMessageBox = new CustomMessageBox("image/icon.png");

        WebEngine engine = webViewGoogleMaps.getEngine();
        String url = Main.class.getResource("../map.html").toExternalForm();
        engine.load(url);
    }

    @FXML
    void buttonAdd_onAction() {
        Boolean sceneWasLoadedSuccessfully = true;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/AddContact.fxml"));
            loader.load();
        } catch (IOException ioEcx) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ioEcx);
            sceneWasLoadedSuccessfully = false;
        }

        if (sceneWasLoadedSuccessfully) {
            AddContactController display = loader.getController();
            display.setFrameObjects(officeService);
            Parent parent = loader.getRoot();
            Stage stage = Main.getMainStage();
            Stage currentStage = (Stage) buttonSaveChanges.getScene().getWindow();
            Scene scene = new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 42.5);
            stage.setScene(scene);
        }
    }

    @FXML
    void buttonDelete_onAction() {
        if (tableViewContacts.getSelectionModel().getSelectedItem() != null) {
            ButtonType confirmButton = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
            customMessageBox.showConfirmMessageBox(Alert.AlertType.CONFIRMATION, "Operacja wymaga potwierdzenia.",
                    "Usunięcie kontaktu jest operacją nieodwracalną.",
                    "W celu potwierdzenia naciśnij przycisk.", confirmButton, cancelButton).showAndWait()
                    .ifPresent(rs -> {
                        if (rs.getText().equals("Potwierdź")) {
                            ViewExtendedContact deletedContact = tableViewContacts.getSelectionModel().getSelectedItem();
                            if (deletedContact.getAddressId() != null)
                                officeService.deleteAddress(deletedContact.getAddressId());
                            officeService.deleteContact(deletedContact.getContactId());
                            refreshTableView(officeService.getViewExtendedContacts());
                            prepareContactComponents(false);
                            setDefaultDetailsInformation();
                        }
                    });
        }
    }

    @FXML
    void buttonModify_onAction() {
        Boolean sceneWasLoadedSuccessfully = true;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/ModifyContact.fxml"));
            loader.load();
        } catch (IOException ioEcx) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ioEcx);
            sceneWasLoadedSuccessfully = false;
        }

        if (sceneWasLoadedSuccessfully) {
            ModifyContactController display = loader.getController();
            display.setFrameObjects(officeService, officeService.getContact(tableViewContacts.getSelectionModel().getSelectedItem().getContactId()));
            Parent parent = loader.getRoot();
            Stage stage = Main.getMainStage();
            Stage currentStage = (Stage) buttonSaveChanges.getScene().getWindow();
            Scene scene = new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 42.5);
            stage.setScene(scene);
        }
    }

    @FXML
    void buttonGeocode_onAction() {
        Contact contact = officeService.getContact((tableViewContacts.getSelectionModel().getSelectedItem().getContactId()));
        AddressGeolocation addressGeolocation = new AddressGeolocation(contact.getAddress(), FXCollections.observableArrayList(provinces));
        addressGeolocation.setAddressCoordinates();
        try {
            officeService.saveContact(contact);
            refreshTableView(officeService.getViewExtendedContacts());
            refreshComboBoxes();
            prepareContactComponents(false);
            setDefaultDetailsInformation();
            prepareGeolocationButton(false);
        } catch (DataTooLongViolationException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja modyfikacji kontaktu nie powiodła się.",
                    "Powód: " + e.getCause().getMessage() + ".").showAndWait();
        }
    }

    @FXML
    void buttonSaveChanges_onAction() {
        ViewExtendedContact contact = tableViewContacts.getSelectionModel().getSelectedItem();
        if (contact != null) {
            try {
                Integer selectedContactPosition = tableViewContacts.getSelectionModel().getSelectedIndex();
                Contact selectedContact = officeService.getContact(tableViewContacts.getSelectionModel().getSelectedItem().getContactId());
                selectedContact.setDescription(textAreaDescription.getText());
                selectedContact.setComments(textAreaComments.getText());
                officeService.saveContact(selectedContact);
                refreshTableView(officeService.getViewExtendedContacts());
                tableViewContacts.getSelectionModel().select(selectedContactPosition);
            } catch (DataTooLongViolationException e) {
                customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                        "Operacja nie powiodła się.",
                        "Powód: " + e.getCause().getMessage() + ".").showAndWait();
            }
        }
    }

    @FXML
    void buttonStandardSearch_onAction() {
        searchContacts();
    }

    @FXML
    void buttonClearSearchPreferences_onAction() {
        clearSearchPreferences();
        refreshTableView(officeService.getViewExtendedContacts());
        prepareContactComponents(false);
        setDefaultDetailsInformation();
        prepareGeolocationButton(false);
    }

    @FXML
    void menuItemNormalDataExport_onAction() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Inter Art Kontakty");
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nazwa");
        header.createCell(1).setCellValue("Branża");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Telefon");
        header.createCell(4).setCellValue("Ulica");
        header.createCell(5).setCellValue("Kod pocztowy");
        header.createCell(6).setCellValue("Miasto");
        header.createCell(7).setCellValue("Województwo");

        ObservableList<ViewExtendedContact> excelData = FXCollections.observableArrayList();
        excelData.addAll(officeService.getViewExtendedContacts());

        int index = 1;
        for (ViewExtendedContact viewExtendedContact : excelData) {
            HSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(viewExtendedContact.getName());
            row.createCell(1).setCellValue(viewExtendedContact.getTrade());
            row.createCell(2).setCellValue(viewExtendedContact.getEmail());
            row.createCell(3).setCellValue(viewExtendedContact.getPhone());
            row.createCell(4).setCellValue(viewExtendedContact.getStreet());
            row.createCell(5).setCellValue(viewExtendedContact.getPostalCode());
            row.createCell(6).setCellValue(viewExtendedContact.getCity());
            row.createCell(7).setCellValue(viewExtendedContact.getProvince());
            index++;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("inter_art_contacts_standard_data.xls");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File("inter_art_contacts_standard_data.xls"));
        } catch (IOException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja utworzenia pliku nie powiodła się.",
                    "Powód: " + e.getCause().getMessage()).showAndWait();
        } catch (UnsupportedOperationException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Plik został utworzony w domyślnej lokalizacji,\nale nie można go otworzyć.",
                    "Powód: brak wymaganej aplikacji.").showAndWait();
        }
    }

    @FXML
    void menuItemExtendedDataExport_onAction() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Inter Art Kontakty");
        HSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Nazwa");
        header.createCell(1).setCellValue("Branża");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Telefon");
        header.createCell(4).setCellValue("Ulica");
        header.createCell(5).setCellValue("Kod pocztowy");
        header.createCell(6).setCellValue("Miasto");
        header.createCell(7).setCellValue("Województwo");
        header.createCell(8).setCellValue("Opis");
        header.createCell(9).setCellValue("Uwagi");

        ObservableList<ViewExtendedContact> excelData = FXCollections.observableArrayList();
        excelData.addAll(officeService.getViewExtendedContacts());

        int index = 1;
        for (ViewExtendedContact viewExtendedContact : excelData) {
            HSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(viewExtendedContact.getName());
            row.createCell(1).setCellValue(viewExtendedContact.getTrade());
            row.createCell(2).setCellValue(viewExtendedContact.getEmail());
            row.createCell(3).setCellValue(viewExtendedContact.getPhone());
            row.createCell(4).setCellValue(viewExtendedContact.getStreet());
            row.createCell(5).setCellValue(viewExtendedContact.getPostalCode());
            row.createCell(6).setCellValue(viewExtendedContact.getCity());
            row.createCell(7).setCellValue(viewExtendedContact.getProvince());
            row.createCell(8).setCellValue(viewExtendedContact.getDescription());
            row.createCell(9).setCellValue(viewExtendedContact.getComments());
            index++;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("inter_art_contacts_extended_data.xls");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File("inter_art_contacts_extended_data.xls"));
        } catch (IOException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Operacja utworzenia pliku nie powiodła się.",
                    "Powód: " + e.getCause().getMessage()).showAndWait();
        } catch (UnsupportedOperationException e) {
            customMessageBox.showMessageBox(Alert.AlertType.WARNING, "Ostrzeżenie",
                    "Plik został utworzony w domyślnej lokalizacji,\nale nie można go otworzyć.",
                    "Powód: brak wymaganej aplikacji.").showAndWait();
        }
    }

    @FXML
    void menuItemModifyTradesAndProvinces_onAction() {
        Boolean sceneWasLoadedSuccessfully = true;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/ModifyTradesAndProvinces.fxml"));
            loader.load();
        } catch (IOException ioEcx) {
            Logger.getLogger(MainFrameController.class.getName()).log(Level.SEVERE, null, ioEcx);
            sceneWasLoadedSuccessfully = false;
        }

        if (sceneWasLoadedSuccessfully) {
            ModifyTradesAndProvincesController display = loader.getController();
            display.setFrameObjects(officeService);
            Parent parent = loader.getRoot();
            Stage stage = Main.getMainStage();
            Stage currentStage = (Stage) buttonSaveChanges.getScene().getWindow();
            Scene scene = new Scene(parent, currentStage.getWidth() - 16.0, currentStage.getHeight() - 42.5);
            if ((Double.compare(currentStage.getWidth(), 1157)) <= 0)
                display.changeProvinceLabel("Nazwa zaznaczonego woj.:");
            else
                display.changeProvinceLabel("Nazwa zaznaczonego województwa:");
            scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
                if (display.getTextOfProvinceLabel().equals("Nazwa zaznaczonego województwa:")) {
                    if ((Double.compare((Double) newSceneWidth, 1157)) <= 0)
                        display.changeProvinceLabel("Nazwa zaznaczonego woj.:");
                } else if (display.getTextOfProvinceLabel().equals("Nazwa zaznaczonego woj.:"))
                    if ((Double.compare((Double) newSceneWidth, 1157)) > 0)
                        display.changeProvinceLabel("Nazwa zaznaczonego województwa:");
            });
            stage.setScene(scene);
        }
    }

    @FXML
    void menuItemModifyHeader_onAction() {
        Boolean sceneWasLoadedSuccessfully = true;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/ModifyHeader.fxml"));
            loader.load();
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
            sceneWasLoadedSuccessfully = false;
        }

        if (sceneWasLoadedSuccessfully) {
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setTitle("Inter Art - Nagłówek");
            stage.getIcons().add(new Image("/image/icon.png"));
            stage.setScene(new Scene(root, 819, 236));
            stage.showAndWait();
            labelHeader.setText(pref.get("header",
                    "Inter Art Marcin Rogal, ul. Wiktorowska 34, Wapiennik, 42-120 Miedźno, Polska"));
        }
    }

    @FXML
    void menuItemModifyGoogleApiKey_onAction() {
        Boolean sceneWasLoadedSuccessfully = true;
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(getClass().getResource("../../fxml/ModifyGoogleApiKey.fxml"));
            loader.load();
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
            sceneWasLoadedSuccessfully = false;
        }

        if (sceneWasLoadedSuccessfully) {
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setTitle("Inter Art - Klucz Google Maps API");
            stage.getIcons().add(new Image("/image/icon.png"));
            stage.setScene(new Scene(root, 819, 270));
            stage.showAndWait();
        }
    }

    @FXML
    void radioButtonDetailsMode_onAction() {
        prepareContactModeComponents("details");
        pref.put("contact_mode", "details");
    }

    @FXML
    void radioButtonMapMode_onAction() {
        prepareContactModeComponents("map");
        pref.put("contact_mode", "map");
    }

    @FXML
    void radioButtonSearchMode_onAction() {
        prepareContactModeComponents("search");
        pref.put("contact_mode", "search");
    }

    @FXML
    void textFieldCity_onAction() {
        searchContacts();
    }

    @FXML
    void textFieldEmail_onAction() {
        searchContacts();
    }

    @FXML
    void textFieldName_onAction() {
        searchContacts();
    }

    @FXML
    void textFieldPhone_onAction() {
        searchContacts();
    }

    @FXML
    void textFieldPostalCode_onAction() {
        searchContacts();
    }

    @FXML
    void textFieldStreet_onAction() {
        searchContacts();
    }

    @FXML
    void tableViewContacts_onMouseClicked() {
        try {
            if (tableViewContacts.getSelectionModel().getSelectedItem() != null) {
                setDetailsInformation(tableViewContacts.getSelectionModel().getSelectedItem());
                prepareContactComponents(true);
            }
        } catch (NullPointerException nullExc) {
            prepareContactComponents(false);
            setDefaultDetailsInformation();
        }
    }

    private void setDefaultDetailsInformation() {
        labelDetails.setText("------");
        textAreaDescription.setText("");
        textAreaComments.setText("");
        labelGeocode.setText("---");
    }

    private void setDetailsInformation(ViewExtendedContact selectedContact) {
        labelDetails.setText(selectedContact.getName());
        textAreaDescription.setText(selectedContact.getDescription());
        textAreaComments.setText(selectedContact.getComments());
        if (selectedContact.getAddressId() != null && (selectedContact.getLongitude() != null && !selectedContact.getLongitude().equals(""))) {
            labelGeocode.setText("- zgeokodowany adres: " + selectedContact.getLatitude() + ", " + selectedContact.getLongitude() + ".");
            prepareGeolocationButton(false);
        } else if (selectedContact.getAddressId() != null && (selectedContact.getLongitude() == null || selectedContact.getLongitude().equals(""))) {
            labelGeocode.setText("- nie zgeokodowano adresu.");
            prepareGeolocationButton(true);
        } else if (selectedContact.getAddressId() == null) {
            labelGeocode.setText("- brak danych adresowych.");
            prepareGeolocationButton(false);
        }
    }

    private void refreshTableView(List<ViewExtendedContact> viewExtendedContacts) {
        viewContactObservableList.clear();
        viewContactObservableList.addAll(viewExtendedContacts);
        tableViewContacts.setItems(viewContactObservableList);
    }

    private void initTableView() {
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnTrade.setCellValueFactory(new PropertyValueFactory<>("trade"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableColumnStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
        tableColumnPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        tableColumnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        tableColumnProvince.setCellValueFactory(new PropertyValueFactory<>("province"));
        refreshTableView(officeService.getViewExtendedContacts());
    }

    private void initRadioButtons() {
        ToggleGroup toggleGroupContactModes = new ToggleGroup();
        radioButtonMapMode.setToggleGroup(toggleGroupContactModes);
        radioButtonDetailsMode.setToggleGroup(toggleGroupContactModes);
        radioButtonSearchMode.setToggleGroup(toggleGroupContactModes);

        String contactMode = pref.get("contact_mode", "search");

        switch (contactMode) {
            case "map":
                prepareContactModeComponents("map");
                radioButtonMapMode.setSelected(true);
                break;
            case "details":
                prepareContactModeComponents("details");
                radioButtonDetailsMode.setSelected(true);
                break;
            case "search":
                prepareContactModeComponents("search");
                radioButtonSearchMode.setSelected(true);
                break;
        }
    }

    private void prepareContactComponents(boolean contactIsSelected) {
        if (contactIsSelected) {
            hBoxSelectedContactButtons.setVisible(true);
            hBoxSelectedContactButtons.setDisable(false);
            hBoxSelectedContactButtons.setMinWidth(Control.USE_COMPUTED_SIZE);
            hBoxSelectedContactButtons.setMinHeight(Control.USE_COMPUTED_SIZE);
            hBoxSelectedContactButtons.setPrefWidth(Control.USE_COMPUTED_SIZE);
            hBoxSelectedContactButtons.setPrefHeight(Control.USE_COMPUTED_SIZE);
            hBoxSelectedContactButtons.setMaxWidth(Control.USE_COMPUTED_SIZE);
            hBoxSelectedContactButtons.setMaxHeight(10);

            buttonSaveChanges.setVisible(true);
            buttonSaveChanges.setDisable(false);
            buttonSaveChanges.setMinWidth(147);
            buttonSaveChanges.setMinHeight(Control.USE_COMPUTED_SIZE);
            buttonSaveChanges.setPrefWidth(147);
            buttonSaveChanges.setPrefHeight(31);
            buttonSaveChanges.setMaxWidth(Control.USE_COMPUTED_SIZE);
            buttonSaveChanges.setMaxHeight(Control.USE_COMPUTED_SIZE);

            textAreaDescription.setEditable(true);
            textAreaComments.setEditable(true);
        } else {
            hBoxSelectedContactButtons.setVisible(false);
            hBoxSelectedContactButtons.setDisable(true);
            hBoxSelectedContactButtons.setMinWidth(0);
            hBoxSelectedContactButtons.setMinHeight(0);
            hBoxSelectedContactButtons.setPrefWidth(0);
            hBoxSelectedContactButtons.setPrefHeight(0);
            hBoxSelectedContactButtons.setMaxWidth(0);
            hBoxSelectedContactButtons.setMaxHeight(0);

            buttonSaveChanges.setVisible(false);
            buttonSaveChanges.setDisable(true);
            buttonSaveChanges.setMinWidth(0);
            buttonSaveChanges.setMinHeight(0);
            buttonSaveChanges.setPrefWidth(0);
            buttonSaveChanges.setPrefHeight(0);
            buttonSaveChanges.setMaxWidth(0);
            buttonSaveChanges.setMaxHeight(0);

            textAreaDescription.setEditable(false);
            textAreaComments.setEditable(false);
        }
    }

    private void prepareContactModeComponents(String mode) {
        switch (mode) {
            case "map":
                vBoxMapMode.setVisible(true);
                vBoxMapMode.setDisable(false);
                vBoxMapMode.setMinWidth(Control.USE_COMPUTED_SIZE);
                vBoxMapMode.setMinHeight(Control.USE_COMPUTED_SIZE);
                vBoxMapMode.setPrefWidth(100);
                vBoxMapMode.setPrefHeight(200);
                vBoxMapMode.setMaxWidth(Control.USE_COMPUTED_SIZE);
                vBoxMapMode.setMaxHeight(Control.USE_COMPUTED_SIZE);

                vBoxDetailsMode.setVisible(false);
                vBoxDetailsMode.setDisable(true);
                vBoxDetailsMode.setMinWidth(0);
                vBoxDetailsMode.setMinHeight(0);
                vBoxDetailsMode.setPrefWidth(0);
                vBoxDetailsMode.setPrefHeight(0);
                vBoxDetailsMode.setMaxWidth(0);
                vBoxDetailsMode.setMaxHeight(0);

                vBoxSearchMode.setVisible(false);
                vBoxSearchMode.setDisable(true);
                vBoxSearchMode.setMinWidth(0);
                vBoxSearchMode.setMinHeight(0);
                vBoxSearchMode.setPrefWidth(0);
                vBoxSearchMode.setPrefHeight(0);
                vBoxSearchMode.setMaxWidth(0);
                vBoxSearchMode.setMaxHeight(0);
                break;
            case "details":
                vBoxMapMode.setVisible(false);
                vBoxMapMode.setDisable(true);
                vBoxMapMode.setMinWidth(0);
                vBoxMapMode.setMinHeight(0);
                vBoxMapMode.setPrefWidth(0);
                vBoxMapMode.setPrefHeight(0);
                vBoxMapMode.setMaxWidth(0);
                vBoxMapMode.setMaxHeight(0);

                vBoxDetailsMode.setVisible(true);
                vBoxDetailsMode.setDisable(false);
                vBoxDetailsMode.setMinWidth(Control.USE_COMPUTED_SIZE);
                vBoxDetailsMode.setMinHeight(Control.USE_COMPUTED_SIZE);
                vBoxDetailsMode.setPrefWidth(100);
                vBoxDetailsMode.setPrefHeight(200);
                vBoxDetailsMode.setMaxWidth(Control.USE_COMPUTED_SIZE);
                vBoxDetailsMode.setMaxHeight(Control.USE_COMPUTED_SIZE);

                vBoxSearchMode.setVisible(false);
                vBoxSearchMode.setDisable(true);
                vBoxSearchMode.setMinWidth(0);
                vBoxSearchMode.setMinHeight(0);
                vBoxSearchMode.setPrefWidth(0);
                vBoxSearchMode.setPrefHeight(0);
                vBoxSearchMode.setMaxWidth(0);
                vBoxSearchMode.setMaxHeight(0);
                break;
            case "search":
                vBoxMapMode.setVisible(false);
                vBoxMapMode.setDisable(true);
                vBoxMapMode.setMinWidth(0);
                vBoxMapMode.setMinHeight(0);
                vBoxMapMode.setPrefWidth(0);
                vBoxMapMode.setPrefHeight(0);
                vBoxMapMode.setMaxWidth(0);
                vBoxMapMode.setMaxHeight(0);

                vBoxDetailsMode.setVisible(false);
                vBoxDetailsMode.setDisable(true);
                vBoxDetailsMode.setMinWidth(0);
                vBoxDetailsMode.setMinHeight(0);
                vBoxDetailsMode.setPrefWidth(0);
                vBoxDetailsMode.setPrefHeight(0);
                vBoxDetailsMode.setMaxWidth(0);
                vBoxDetailsMode.setMaxHeight(0);

                vBoxSearchMode.setVisible(true);
                vBoxSearchMode.setDisable(false);
                vBoxSearchMode.setMinWidth(Control.USE_COMPUTED_SIZE);
                vBoxSearchMode.setMinHeight(Control.USE_COMPUTED_SIZE);
                vBoxSearchMode.setPrefWidth(100);
                vBoxSearchMode.setPrefHeight(200);
                vBoxSearchMode.setMaxWidth(Control.USE_COMPUTED_SIZE);
                vBoxSearchMode.setMaxHeight(Control.USE_COMPUTED_SIZE);

                refreshComboBoxes();
                break;
        }
    }

    @SuppressWarnings("Duplicates")
    private void prepareGeolocationButton(boolean button) {
        if (button) {
            vBoxGeocode.setVisible(true);
            vBoxGeocode.setDisable(false);
            vBoxGeocode.setMinWidth(Control.USE_COMPUTED_SIZE);
            vBoxGeocode.setMinHeight(Control.USE_COMPUTED_SIZE);
            vBoxGeocode.setPrefWidth(Control.USE_COMPUTED_SIZE);
            vBoxGeocode.setPrefHeight(Control.USE_COMPUTED_SIZE);
            vBoxGeocode.setMaxWidth(Control.USE_COMPUTED_SIZE);
            vBoxGeocode.setMaxHeight(Control.USE_COMPUTED_SIZE);
        } else {
            vBoxGeocode.setVisible(false);
            vBoxGeocode.setDisable(true);
            vBoxGeocode.setMinWidth(0);
            vBoxGeocode.setMinHeight(0);
            vBoxGeocode.setPrefWidth(0);
            vBoxGeocode.setPrefHeight(0);
            vBoxGeocode.setMaxWidth(0);
            vBoxGeocode.setMaxHeight(0);
        }
    }

    private void refreshComboBoxes() {
        tradeObservableList.clear();
        tradeObservableList.add("Wszystkie");
        for (Trade trade : trades)
            tradeObservableList.add(trade.getTrade());
        comboBoxTrade.setItems(tradeObservableList);
        comboBoxTrade.getSelectionModel().select(0);

        provinceObservableList.clear();
        provinceObservableList.add("Wszystkie");
        for (Province province : provinces)
            provinceObservableList.add(province.getProvince());
        comboBoxProvince.setItems(provinceObservableList);
        comboBoxProvince.getSelectionModel().select(0);
    }

    private void clearSearchPreferences() {
        textFieldName.setText("");
        textFieldEmail.setText("");
        textFieldPhone.setText("");
        textFieldStreet.setText("");
        textFieldCity.setText("");
        textFieldPostalCode.setText("");
        checkBoxComments.setSelected(false);
        checkBoxDescription.setSelected(false);
        checkBoxAddress.setSelected(false);
        checkBoxGeolocation.setSelected(false);
        comboBoxTrade.getSelectionModel().select(0);
        comboBoxProvince.getSelectionModel().select(0);
    }

    private void searchContacts() {
        if (tableViewContacts.getSelectionModel().getSelectedItem() != null) {
            prepareContactComponents(false);
            setDefaultDetailsInformation();
            prepareGeolocationButton(false);
        }
        refreshTableView(officeService.searchContacts(textFieldName.getText(),
                comboBoxTrade.getSelectionModel().getSelectedItem(), textFieldEmail.getText(), textFieldPhone.getText(),
                textFieldStreet.getText(), textFieldPostalCode.getText(), textFieldCity.getText(),
                comboBoxProvince.getSelectionModel().getSelectedItem(), checkBoxDescription.isSelected(),
                checkBoxComments.isSelected(), checkBoxAddress.isSelected(), checkBoxGeolocation.isSelected()));
    }
}
