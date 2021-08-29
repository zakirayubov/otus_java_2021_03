package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;


public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String SELECT_ALL = "select * from %s;";
    private final String SELECT_ID = "select * from %s where %s = ?;";
    private final String INSERT = "insert into %s (%s) values (%s);";
    private final String UPDATE = "update %s set (%s) where %s = ?";

    private final EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return String.format(SELECT_ALL, entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(SELECT_ID, entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        String valuesTemplate = generateInsertValuesTemplate(entityClassMetaData.getFieldsWithoutId().size());
        String columnNames = generateInsertColumnNames(entityClassMetaData.getFieldsWithoutId());

        return String.format(INSERT, entityClassMetaData.getName(), columnNames, valuesTemplate);

    }

    @Override
    public String getUpdateSql() {
        String columnsTemplate = generateUpdateColumnsTemplate(entityClassMetaData.getFieldsWithoutId());

        return String.format(UPDATE, entityClassMetaData.getName(), columnsTemplate, entityClassMetaData.getIdField().getName());
    }

    private String generateInsertValuesTemplate(int amount) {
        String[] values = new String[amount];
        Arrays.fill(values, "?");
        return String.join(", ", values);
    }

    private String generateUpdateColumnsTemplate(List<Field> fields) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Field field : fields) {
            joiner.add(String.format("%s = ?", field.getName()));
        }
        return joiner.toString();
    }

    private String generateInsertColumnNames(List<Field> fields) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Field field : fields) {
            joiner.add(field.getName());
        }
        return joiner.toString();
    }
}
