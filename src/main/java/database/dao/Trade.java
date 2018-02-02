package database.dao;

import database.EntityCRUD;
import org.hibernate.SessionFactory;

import java.util.List;

public class Trade implements EntityCRUD<Trade> {
    private final SessionFactory sessionFactory;

    public Trade(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Trade> getEntities() {
        return null;
    }

    public void saveEntity(Trade entity) {

    }

    public Trade getEntity(int id) {
        return null;
    }

    public void deleteEntity(int id) {

    }
}
