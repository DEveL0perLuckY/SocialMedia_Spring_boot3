package com.insta.my_app.service;

import com.insta.my_app.domain.Message;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.MessageDTO;
import com.insta.my_app.repos.MessageRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(final MessageRepository messageRepository,
            final UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageDTO> findAll() {
        final List<Message> messages = messageRepository.findAll(Sort.by("messageId"));
        return messages.stream()
                .map(message -> mapToDTO(message, new MessageDTO()))
                .toList();
    }

    public MessageDTO get(final Integer messageId) {
        return messageRepository.findById(messageId)
                .map(message -> mapToDTO(message, new MessageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MessageDTO messageDTO) {
        final Message message = new Message();
        mapToEntity(messageDTO, message);
        return messageRepository.save(message).getMessageId();
    }

    public void update(final Integer messageId, final MessageDTO messageDTO) {
        final Message message = messageRepository.findById(messageId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(messageDTO, message);
        messageRepository.save(message);
    }

    public void delete(final Integer messageId) {
        messageRepository.deleteById(messageId);
    }

    private MessageDTO mapToDTO(final Message message, final MessageDTO messageDTO) {
        messageDTO.setMessageId(message.getMessageId());
        messageDTO.setMessageText(message.getMessageText());
        messageDTO.setCreatedAt(message.getCreatedAt());
        messageDTO.setSender(message.getSender() == null ? null : message.getSender().getUserId());
        messageDTO.setReceiver(message.getReceiver() == null ? null : message.getReceiver().getUserId());
        return messageDTO;
    }

    private Message mapToEntity(final MessageDTO messageDTO, final Message message) {
        message.setMessageText(messageDTO.getMessageText());
        message.setCreatedAt(messageDTO.getCreatedAt());
        final User sender = messageDTO.getSender() == null ? null : userRepository.findById(messageDTO.getSender())
                .orElseThrow(() -> new NotFoundException("sender not found"));
        message.setSender(sender);
        final User receiver = messageDTO.getReceiver() == null ? null : userRepository.findById(messageDTO.getReceiver())
                .orElseThrow(() -> new NotFoundException("receiver not found"));
        message.setReceiver(receiver);
        return message;
    }

}
