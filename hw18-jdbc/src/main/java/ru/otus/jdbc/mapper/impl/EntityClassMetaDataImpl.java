package ru.otus.jdbc.mapper.impl;

import ru.otus.crm.annotation.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private Class<T> clazz;


    public EntityClassMetaDataImpl(Class<T>  clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            Class<?>[] types = getAllFields().stream()
                    .map(Field::getType)
                    .toArray(Class[]::new);
            return clazz.getConstructor(types);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return getAllFields().stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst().orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream().filter(field -> !field.isAnnotationPresent(Id.class)).collect(Collectors.toList());
    }
}
