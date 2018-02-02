package database.dao;

import database.EntityCRUD;
import database.entity.Province;
import org.hibernate.SessionFactory;

import java.util.List;

public class ProvinceDAO implements EntityCRUD<Province> {
    private final SessionFactory sessionFactory;

    public ProvinceDAO(SessionFactory sessionFactory) {
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
