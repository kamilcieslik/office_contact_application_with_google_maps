package database.dao;

import database.EntityCRUD;
import database.entity.Province;
import exception.DataTooLongViolationException;
import exception.NameUniqueViolationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.List;

public class ProvinceDAO implements EntityCRUD<Province> {
    private final SessionFactory sessionFactory;

    public ProvinceDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Province> getEntities() {
        List<Province> provinces;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<Province> theQuery = currentSession.createQuery("from Province", Province.class);
            provinces = theQuery.getResultList();
            currentSession.getTransaction().commit();
        }
        return provinces;
    }

    public void saveEntity(Province entity) throws NameUniqueViolationException, DataTooLongViolationException {
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            currentSession.saveOrUpdate(entity);
            currentSession.getTransaction().commit();
        } catch (PersistenceException e) {
            Throwable eCause = e.getCause();
            while ((eCause != null) && !((eCause instanceof ConstraintViolationException) || (eCause instanceof DataException)))
                eCause = eCause.getCause();
            if (eCause instanceof ConstraintViolationException) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("obiekt o nazwie '" + entity.getProvince() + "' istnieje już w bazie danych");
                throw new NameUniqueViolationException("Błąd bazy danych", exceptionCause);
            } else if (eCause != null) {
                Throwable exceptionCause;
                exceptionCause = new Throwable("nazwa '" + entity.getProvince() + "' jest za długa");
                throw new DataTooLongViolationException("Błąd bazy danych", exceptionCause);
            }
        }
    }

    public Province getEntity(int id) {
        Province province;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            province = currentSession.get(Province.class, id);
            currentSession.getTransaction().commit();
        }
        return province;
    }

    public void deleteEntity(int id) {
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            currentSession.createQuery("delete from Province where id=:provinceId")
                    .setParameter("provinceId", id).executeUpdate();
            currentSession.getTransaction().commit();
        }
    }
}
