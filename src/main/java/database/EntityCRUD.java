package database;

import java.util.List;

/**
 * Created by Kamil Cieślik on 01.02.2018.
 */
public interface EntityCRUD<T> {
    List<T> getEntities();

    void saveEntity(T entity) throws Exception;

    T getEntity(int id);

    void deleteEntity(int id) throws Exception;
}
