package com.insta.my_app.rest;

import com.insta.my_app.model.NotificationDTO;
import com.insta.my_app.service.NotificationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationResource {

    private final NotificationService notificationService;

    public NotificationResource(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDTO> getNotification(
            @PathVariable(name = "notificationId") final Integer notificationId) {
        return ResponseEntity.ok(notificationService.get(notificationId));
    }

    @PostMapping
    public ResponseEntity<Integer> createNotification(
            @RequestBody @Valid final NotificationDTO notificationDTO) {
        final Integer createdNotificationId = notificationService.create(notificationDTO);
        return new ResponseEntity<>(createdNotificationId, HttpStatus.CREATED);
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<Integer> updateNotification(
            @PathVariable(name = "notificationId") final Integer notificationId,
            @RequestBody @Valid final NotificationDTO notificationDTO) {
        notificationService.update(notificationId, notificationDTO);
        return ResponseEntity.ok(notificationId);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable(name = "notificationId") final Integer notificationId) {
        notificationService.delete(notificationId);
        return ResponseEntity.noContent().build();
    }

}
