package com.ecommerce.notification_service.service;

import com.ecommerce.notification_service.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @KafkaListener(topics = "notificationTopic", groupId = "notificationId")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        // This simulates the time it takes to send an actual email
        log.info("========================================");
        log.info("🔔 NEW MESSAGE RECEIVED FROM KAFKA!");
        log.info("Sending confirmation email for Order Number: {}", orderPlacedEvent.getOrderNumber());
        log.info("========================================");
    }
}
