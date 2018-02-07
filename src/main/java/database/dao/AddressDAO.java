package database.dao;

import database.entity.Address;
import database.EntityCRUD;
import exception.DataTooLongViolationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.DataException;
import org.hibernate.query.Query;

import java.util.List;

public class AddressDAO implements EntityCRUD<Address> {
    private final SessionFactory sessionFactory;

    public AddressDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Address> getEntities() {
        List<Address> addresses;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<Address> theQuery = currentSession.createQuery("from Address", Address.class);
            addresses = theQuery.getResultList();
            currentSession.getTransaction().commit();
        }
        return addresses;
    }

    public void saveEntity(Address entity) throws DataTooLongViolationException {
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            currentSession.saveOrUpdate(entity);
            currentSession.getTransaction().commit();
        } catch (DataException exc) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("przynajmniej jedna z głównych wartości obiektu jest za długa");
            throw new DataTooLongViolationException("Błąd bazy danych", exceptionCause);
        }
    }

    public Address getEntity(int id) {
        Address address;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            address = currentSession.get(Address.class, id);
            currentSession.getTransaction().commit();
        }
        return address;
    }

    public void deleteEntity(int id) {
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            currentSession.createQuery("delete from Address where id=:addressId")
                    .setParameter("addressId", id).executeUpdate();
            currentSession.getTransaction().commit();
        }
    }
}
