package database.dao;

import database.EntityCRUD;
import org.hibernate.SessionFactory;

import java.util.List;

public class Address implements EntityCRUD<Address> {
    private final SessionFactory sessionFactory;

    public Address(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Address> getEntities() {
        return null;
    }

    public void saveEntity(Address entity) {

    }

    public Address getEntity(int id) {
        return null;
    }

    public void deleteEntity(int id) {

    }
}
