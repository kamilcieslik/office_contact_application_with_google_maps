package database.dao;

import database.view.ViewContact;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ViewContactDAO {
    private SessionFactory sessionFactory;

    public ViewContactDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ViewContact> getEntities() {
        List<ViewContact> contacts;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<ViewContact> theQuery = currentSession.createQuery("from ViewContact", ViewContact.class);
            contacts = theQuery.getResultList();
            currentSession.getTransaction().commit();
        }
        return contacts;
    }

    public ViewContact getEntity(int id) {
        ViewContact contact;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            contact = currentSession.get(ViewContact.class, id);
            currentSession.getTransaction().commit();
        }
        return contact;
    }

    public List<ViewContact> searchEntities(String name, String trade, String email, String phone,
                                        String street, String postalCode, String city, String province) {
        List<ViewContact> contacts = null;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<ViewContact> theQuery = currentSession.createQuery("select c from ViewContact c " +
                            "where c.name like:searchedName and (:searchedTrade like 'Wszystkie' or " +
                            "c.trade=:searchedTrade) and c.email like:searchedEmail " +
                            "and c.phone like:searchedPhone and c.street like:searchedStreet " +
                            "and c.postalCode like:searchedPostalCode " +
                            "and c.city like:searchedCity " +
                            "and (:searchedProvince like 'Wszystkie' or " +
                            "c.province=:searchedProvince)"
                    , ViewContact.class).setParameter("searchedName", "%" + name + "%")
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
