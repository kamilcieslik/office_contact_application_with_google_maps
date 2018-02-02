package database.dao;

import database.EntityCRUD;
import database.entity.Trade;
import org.hibernate.SessionFactory;

import java.util.List;

public class TradeDAO implements EntityCRUD<Trade> {
    private final SessionFactory sessionFactory;

    public TradeDAO(SessionFactory sessionFactory) {
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
