package ru.otus.core.repository;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> clazz;

    public DataTemplateHibernate(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public Optional<T> findByLogin(Session session, String login) {
        List<T> resultList = session.createQuery(String.format("from %s WHERE login =\'%s\'", clazz.getSimpleName(), login), clazz).getResultList();

        T t = resultList.stream()
                .findFirst()
                .orElse(null);
        Optional<T> t1 = Optional.ofNullable(t);
        return t1;
    }

    @Override
    public List<T> findAll(Session session) {
        return session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz).getResultList();
    }

    @Override
    public void insert(Session session, T object) {
        session.persist(object);
    }

    @Override
    public void update(Session session, T object) {
        session.merge(object);
    }
}
