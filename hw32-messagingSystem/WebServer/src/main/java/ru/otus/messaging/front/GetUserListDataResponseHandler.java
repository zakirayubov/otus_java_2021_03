package ru.otus.messaging.front;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.dto.UserListData;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUserListDataResponseHandler implements RequestHandler<UserListData> {

    private final CallbackRegistry callbackRegistry;

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("new message:{}", msg);
        try {
            MessageCallback<? extends ResultDataType> callback = callbackRegistry.getAndRemove(msg.getCallbackId());
            if (callback != null) {
                callback.accept(MessageHelper.getPayload(msg));
            } else {
                log.error("callback for Id:{} not found", msg.getCallbackId());
            }
        } catch (Exception ex) {
            log.error("msg:{}", msg, ex);
        }
        return Optional.empty();
    }
}
