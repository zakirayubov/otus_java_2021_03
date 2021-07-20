package ru.otus;

import java.util.Map;

public class Storage {
    private  Map<Banknote, Integer> container;

    public Storage(Map<Banknote, Integer> container) {
        this.container = container;
    }

    public Map<Banknote, Integer> getContainer() {
        return container;
    }
}
