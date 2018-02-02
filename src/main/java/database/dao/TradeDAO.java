package database.dao;

import database.EntityCRUD;
import database.entity.Trade;
import exception.DataTooLongViolationException;
import exception.NameUniqueViolationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.query.Query;

import java.util.List;

public class TradeDAO implements EntityCRUD<Trade> {
    private final SessionFactory sessionFactory;

    public TradeDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Trade> getEntities() {
        List<Trade> trades;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            Query<Trade> theQuery = currentSession.createQuery("from Trade", Trade.class);
            trades = theQuery.getResultList();
            currentSession.getTransaction().commit();
        }
        return trades;
    }

    public void saveEntity(Trade entity) throws NameUniqueViolationException, DataTooLongViolationException {
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            currentSession.saveOrUpdate(entity);
            currentSession.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("obiekt o nazwie '" + entity.getTrade() + "' istnieje już w bazie danych");
            throw new NameUniqueViolationException("Błąd bazy danych", exceptionCause);
        } catch (DataException exc) {
            Throwable exceptionCause;
            exceptionCause = new Throwable("nazwa '" + entity.getTrade() + "' obiektu jest za długa");
            throw new DataTooLongViolationException("Błąd bazy danych", exceptionCause);
        }
    }

    public Trade getEntity(int id) {
        Trade trade;
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            trade = currentSession.get(Trade.class, id);
            currentSession.getTransaction().commit();
        }
        return trade;
    }

    public void deleteEntity(int id) {
        try (Session currentSession = sessionFactory.getCurrentSession()) {
            currentSession.beginTransaction();
            currentSession.createQuery("delete from Trade where id=:tradeId")
                    .setParameter("tradeId", id).executeUpdate();
            currentSession.getTransaction().commit();
        }
    }
}
