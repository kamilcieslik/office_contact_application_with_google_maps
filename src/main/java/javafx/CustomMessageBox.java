package javafx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomMessageBox {
    String iconImagePath;

    public CustomMessageBox(String iconImagePath) {
        this.iconImagePath=iconImagePath;
    }

    public Alert showMessageBox(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(iconImagePath));
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    public Alert showConfirmMessageBox(Alert.AlertType alertType, String title, String header, String content, ButtonType confirmButton, ButtonType cancelButton) {
        Alert alert = new Alert(alertType, content, confirmButton, cancelButton);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(iconImagePath));
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }
}
