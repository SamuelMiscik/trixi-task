package cz.miscik.DAO;

import jakarta.persistence.EntityManager;

public abstract class DAO<T> {
    protected EntityManager em;
    private Class<T> entityClass;

    public DAO(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    public void create(T entity) { em.persist(entity); }
    public T find(Object id) { return em.find(entityClass, id); }
    public void update(T entity) { em.merge(entity); }
    public void delete(T entity) { em.remove(em.merge(entity)); }
}