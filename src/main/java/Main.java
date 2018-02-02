import database.entity.Address;
import database.entity.Contact;
import database.entity.Province;
import database.entity.Trade;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

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
        SessionFactory factory;

        factory = new Configuration()
                .configure()
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Contact.class)
                .addAnnotatedClass(Province.class)
                .addAnnotatedClass(Trade.class)
                .buildSessionFactory();
        System.out.println("Utworzono SessionFactory.");

        List<Contact> contacts = null;
        try (Session currentSession = factory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<Contact> theQuery = currentSession.createQuery("from Contact", Contact.class);
            contacts = theQuery.getResultList();
            currentSession.getTransaction().commit();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        System.out.println(contacts);

        System.out.println("Zamknięcie SessionFactory.");
        factory.close();
    }
}
