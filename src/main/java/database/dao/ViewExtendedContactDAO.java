package database.dao;

import database.view.ViewExtendedContact;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ViewExtendedContactDAO {
    private SessionFactory sessionFactory;

    public ViewExtendedContactDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ViewExtendedContact> getEntities() {
        List<ViewExtendedContact> contacts;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<ViewExtendedContact> theQuery = currentSession.createQuery("from ViewExtendedContact", ViewExtendedContact.class);
            contacts = theQuery.getResultList();
            currentSession.getTransaction().commit();
        }
        return contacts;
    }

    public ViewExtendedContact getEntity(int id) {
        ViewExtendedContact contact;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            contact = currentSession.get(ViewExtendedContact.class, id);
            currentSession.getTransaction().commit();
        }
        return contact;
    }

    public List<ViewExtendedContact> searchEntities(String name, String trade, String email, String phone,
                                                    String street, String postalCode, String city, String province) {
        List<ViewExtendedContact> contacts = null;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<ViewExtendedContact> theQuery = currentSession.createQuery("select c from ViewExtendedContact c " +
                            "where c.name like:searchedName and (:searchedTrade like 'Wszystkie' or " +
                            "c.trade=:searchedTrade) and c.email like:searchedEmail " +
                            "and c.phone like:searchedPhone and c.street like:searchedStreet " +
                            "and c.postalCode like:searchedPostalCode " +
                            "and c.city like:searchedCity " +
                            "and (:searchedProvince like 'Wszystkie' or " +
                            "c.province=:searchedProvince)"
                    , ViewExtendedContact.class).setParameter("searchedName", "%" + name + "%")
                    .setParameter("searchedTrade", trade)
                    .setParameter("searchedEmail", "%" + email + "%")
                    .setParameter("searchedPhone", "%" + phone + "%")
                    .setParameter("searchedStreet", "%" + street + "%")
                    .setParameter("searchedPostalCode", "%" + postalCode + "%")
                    .setParameter("searchedCity", "%" + city + "%")
                    .setParameter("searchedProvince", province);
            contacts = theQuery.getResultList();
            currentSession.getTransaction().commit();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return contacts;
    }
}
