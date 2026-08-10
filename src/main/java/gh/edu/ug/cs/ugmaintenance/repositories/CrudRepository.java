package gh.edu.ug.cs.ugmaintenance.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
     /**
     * Inserts a new record.
     */
    boolean save(T entity);

    /**
     * Updates an existing record.
     */
    boolean update(T entity);

    /**
     * Deletes a record by its ID.
     */
    boolean delete(ID id);

    /**
     * Finds a record by its ID.
     */
    Optional<T> findById(ID id);

    /**
     * Returns all records.
     */
    List<T> findAll();
}
