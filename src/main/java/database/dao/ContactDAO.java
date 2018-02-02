package database.dao;

import database.EntityCRUD;
import database.entity.Contact;
import org.hibernate.SessionFactory;

import java.util.List;

public class ContactDAO implements EntityCRUD<Contact> {
    private final SessionFactory sessionFactory;

    public ContactDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Contact> getEntities() {
        return null;
    }

    public void saveEntity(Contact entity) {

    }

    public Contact getEntity(int id) {
        return null;
    }

    public void deleteEntity(int id) {

    }
}
