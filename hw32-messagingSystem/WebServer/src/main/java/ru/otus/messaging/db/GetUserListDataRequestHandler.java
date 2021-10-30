package ru.otus.messaging.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.dto.UserListData;
import ru.otus.service.UserService;

import java.util.Optional;

import static ru.otus.dto.UserListData.toUserListData;

@Component
@RequiredArgsConstructor
public class GetUserListDataRequestHandler implements RequestHandler<UserListData> {

    private final UserService service;

    @Override
    public Optional<Message> handle(Message msg) {
        UserListData data = toUserListData(service.findAll());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data, MessageType.GET_USER_LIST_DATA));
    }
}
