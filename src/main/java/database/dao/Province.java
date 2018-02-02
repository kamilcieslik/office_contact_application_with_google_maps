package database.dao;

import database.EntityCRUD;
import org.hibernate.SessionFactory;

import java.util.List;

public class Province implements EntityCRUD<Province> {
    private final SessionFactory sessionFactory;

    public Province(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Province> getEntities() {
        return null;
    }

    public void saveEntity(Province entity) {

    }

    public Province getEntity(int id) {
        return null;
    }

    public void deleteEntity(int id) {

    }
}
