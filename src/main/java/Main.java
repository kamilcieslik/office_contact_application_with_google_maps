import database.entity.Address;
import database.entity.Contact;
import database.entity.Province;
import database.entity.Trade;
import database.service.OfficeService;
import exception.DataTooLongViolationException;
import exception.NameUniqueViolationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class Main {
    private static void setupLog4J() {
        System.setProperty("log4j.configuration", new File(".", File.separatorChar +
                "src/main/resources/log4j.properties").toURI().toString());
    }

    public static void main(String[] args) {
        setupLog4J();
        SessionFactory sessionFactory;

        sessionFactory = new Configuration()
                .configure()
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Contact.class)
                .addAnnotatedClass(Province.class)
                .addAnnotatedClass(Trade.class)
                .buildSessionFactory();
        System.out.println("Utworzono SessionFactory.");

        OfficeService officeService = new OfficeService(sessionFactory);
        try {
            officeService.saveTrade(new Trade("aaaaaaaaaab123"));
        } catch (NameUniqueViolationException | DataTooLongViolationException e) {
            System.out.println(e.getMessage() + ", powód: " + e.getCause().getMessage());
        }

        System.out.println("Zamknięcie SessionFactory.");
        sessionFactory.close();
    }
}
