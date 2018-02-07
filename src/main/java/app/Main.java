package app;

import controller.MainFrameController;
import database.entity.Address;
import database.entity.Contact;
import database.entity.Province;
import database.entity.Trade;
import database.service.OfficeService;
import database.view.ViewContact;
import database.view.ViewExtendedContact;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.GenericJDBCException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Main extends Application {
    private static Stage mainStage;
    private SessionFactory sessionFactory;
    private Preferences pref;
    private Boolean mainFrameIsCurrentStage = true;

    private static void setupLog4J() {
        System.setProperty("log4j.configuration", new File(".", File.separatorChar +
                "src/main/resources/log4j.properties").toURI().toString());
    }

    private void initSessionFactory() {
        try {
            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Address.class)
                    .addAnnotatedClass(Contact.class)
                    .addAnnotatedClass(Province.class)
                    .addAnnotatedClass(Trade.class)
                    .addAnnotatedClass(ViewContact.class)
                    .addAnnotatedClass(ViewExtendedContact.class)
                    .buildSessionFactory();
        } catch (GenericJDBCException e) {
            System.out.println("Błąd połączenia z bazą danych.\nPowód: " + e.getCause().getMessage());
            System.exit(0);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Boolean sceneWasLoadedSuccessfully = true;
        setupLog4J();
        initSessionFactory();
        OfficeService officeService = new OfficeService(sessionFactory);
        pref = Preferences.userRoot();

        FXMLLoader loader = new FXMLLoader();
        try {
            setMainStage(primaryStage);
            loader.setLocation(getClass().getResource("../fxml/MainFrame.fxml"));
            loader.load();
        } catch (IOException ioEcx) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ioEcx);
            sceneWasLoadedSuccessfully = false;
        }

        if (sceneWasLoadedSuccessfully) {
            Parent root = loader.getRoot();
            primaryStage.setTitle("Inter Art - Kontakty");
            primaryStage.getIcons().add(new Image("/image/icon.png"));
            primaryStage.setMinWidth(970);
            primaryStage.setMinHeight(850);
            primaryStage.setScene(new Scene(root, pref.getDouble("scene_width", 1600),
                    pref.getDouble("scene_height", 900)));
            MainFrameController display = loader.getController();
            display.setFrameObjects(officeService);
            primaryStage.show();
        }
    }

    @Override
    public void stop() {
        sessionFactory.close();
        pref = Preferences.userRoot();
        if (mainFrameIsCurrentStage) {
            pref.putDouble("scene_width", getMainStage().getWidth() - 16.0);
            pref.putDouble("scene_height", getMainStage().getHeight() - 42.5);
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setMainStage(Stage mainStage) {
        Main.mainStage = mainStage;
    }
}
