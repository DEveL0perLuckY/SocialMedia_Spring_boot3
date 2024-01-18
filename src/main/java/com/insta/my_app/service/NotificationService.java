package com.insta.my_app.service;

import com.insta.my_app.domain.Notification;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.NotificationDTO;
import com.insta.my_app.repos.NotificationRepository;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(final NotificationRepository notificationRepository,
            final UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<NotificationDTO> findAll() {
        final List<Notification> notifications = notificationRepository.findAll(Sort.by("notificationId"));
        return notifications.stream()
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .toList();
    }

    public NotificationDTO get(final Integer notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> mapToDTO(notification, new NotificationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final NotificationDTO notificationDTO) {
        final Notification notification = new Notification();
        mapToEntity(notificationDTO, notification);
        return notificationRepository.save(notification).getNotificationId();
    }

    public void update(final Integer notificationId, final NotificationDTO notificationDTO) {
        final Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(notificationDTO, notification);
        notificationRepository.save(notification);
    }

    public void delete(final Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private NotificationDTO mapToDTO(final Notification notification,
            final NotificationDTO notificationDTO) {
        notificationDTO.setNotificationId(notification.getNotificationId());
        notificationDTO.setContent(notification.getContent());
        notificationDTO.setCreatedAt(notification.getCreatedAt());
        notificationDTO.setIsRead(notification.getIsRead());
        notificationDTO.setUser(notification.getUser() == null ? null : notification.getUser().getUserId());
        return notificationDTO;
    }

    private Notification mapToEntity(final NotificationDTO notificationDTO,
            final Notification notification) {
        notification.setContent(notificationDTO.getContent());
        notification.setCreatedAt(notificationDTO.getCreatedAt());
        notification.setIsRead(notificationDTO.getIsRead());
        final User user = notificationDTO.getUser() == null ? null : userRepository.findById(notificationDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        notification.setUser(user);
        return notification;
    }

}
