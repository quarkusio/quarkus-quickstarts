package org.acme.hibernate.orm;

import io.quarkus.hibernate.orm.PersistenceUnit;

import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseResource<T> {

    protected final Class<T> entityClass;

    @Inject
    @PersistenceUnit("fruitPU")
    EntityManagerFactory entityManagerFactory;

    public BaseResource(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    T find(EntityManager entityManager, Object id, Class clazz) {
        try {
            Map<String, Object> hints = new HashMap<>();
            List<EntityGraph<? super T>> graphs = entityManager.getEntityGraphs(clazz);
            for (EntityGraph<?> graph : graphs) {
                if (graph.getName().contains(".detail")) {
                    System.out.println("Found the detail graph: " + graph.getName());
                    hints.put("javax.persistence.loadgraph", graph);
                    break;
                }
            }
            T t = (T) entityManager.find(clazz, id, hints);
            if (t == null) {
                throw new NotFoundException(
                        String.format("Unable to find '%s' with identifier '%s'", entityClass.getSimpleName(), id));
            }
            return t;
        } finally {
        }
    }

    protected EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public T find(EntityManager entityManager, Object id) {
        return find(entityManager, id, entityClass);
    }

    public List<T> findAll(EntityManager entityManager) {

        try {
            List<T> results = getFindAllQuery(entityManager).getResultList();
            return results;
        } finally {
        }
    }

    protected Query getFindAllQuery(EntityManager e) {
        Query q = e.createQuery(getFindAllCriteriaQuery(e));
        // set entity graph if applicable to improve relationship loading performance
        // Only use *.detail graph for findById type requests
        List<EntityGraph<? super T>> graphs = e.getEntityGraphs(entityClass);
        for (EntityGraph<?> graph : graphs) {
            if (graph.getName().contains(".detail")) {
                System.out.println("Found the detail graph: " + graph.getName());
                q.setHint("javax.persistence.loadgraph", graph);
                break;
            }
        }
        q.setMaxResults(30000);
        return q;
    }

    protected CriteriaQuery getFindAllCriteriaQuery(EntityManager e) {
        CriteriaQuery cq = e.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        cq.distinct(true);
        return cq;
    }
}
