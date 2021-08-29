package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.otus.core.sessionmanager.TransactionRunnerJdbc.wrapException;
import static java.util.Collections.emptyList;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id),
                rs -> {
                    try {
                        if (rs.next()) {
                            Object[] values = entityClassMetaData.getAllFields().stream()
                                    .map(field -> wrapException(() -> rs.getObject(field.getName(), field.getType())))
                                    .toArray();
                            return entityClassMetaData.getConstructor().newInstance(values);
                        }
                    } catch (SQLException | ReflectiveOperationException e) {
                        throw new DataTemplateException(e);
                    }
                    return null;
                });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(),
                rs -> {
                    try {
                        List<T> list = new ArrayList<>();
                        while (rs.next()) {
                            Object[] values = entityClassMetaData.getAllFields().stream()
                                    .map(field -> wrapException(() -> rs.getObject(field.getName(), field.getType())))
                                    .toArray();
                            list.add(entityClassMetaData.getConstructor().newInstance(values));
                        }
                        return list;
                    } catch (SQLException | ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).orElse(emptyList());
    }

    @Override
    public long insert(Connection connection, T entity) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldValue(entity));
    }

    @Override
    public void update(Connection connection, T entity) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getFieldValue(entity));
    }

    private List<Object> getFieldValue(T entity) {
        return entityClassMetaData.getFieldsWithoutId().stream().map(field -> getFieldValue(field, entity)).collect(Collectors.toList());
    }

    private Object getFieldValue(Field field, T entity) {
        try {
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
