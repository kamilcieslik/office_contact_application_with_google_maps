package database.dao;

import database.EntityCRUD;
import database.entity.Address;
import org.hibernate.SessionFactory;

import java.util.List;

public class AddressDAO implements EntityCRUD<Address> {
    private final SessionFactory sessionFactory;

    public AddressDAO(SessionFactory sessionFactory) {
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
