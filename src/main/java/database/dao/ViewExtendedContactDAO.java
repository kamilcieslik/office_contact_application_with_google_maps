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
}
