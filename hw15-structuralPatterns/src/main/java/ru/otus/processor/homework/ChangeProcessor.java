package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

//todo: 2. Сделать процессор, который поменяет местами значения field11 и field12
public class ChangeProcessor implements Processor {
    @Override
    public Message process(Message message) {
        return message.toBuilder().field11(message.getField12()).field12(message.getField11()).build();
    }
}