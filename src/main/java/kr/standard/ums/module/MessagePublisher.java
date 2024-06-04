package kr.standard.ums.module;

import kr.standard.ums.config.RabbitMqConfig;
import kr.standard.ums.dto.message.MessageDelivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class MessagePublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    public void fallbackPublisher(MessageDelivery message, String channel) {
        try {
            String routingKey = Objects.equals(channel, "RCS") ? rabbitMqConfig.getRcsFallbackRoutingKey() : rabbitMqConfig.getKkoFallbackRoutingKey();
            log.info("Publish [{}] Fallback Message", channel);
            message.setChannel(channel);
            rabbitTemplate.convertAndSend(rabbitMqConfig.getExchangeName(), routingKey, message);
        } catch (Exception e) {
            log.error("Error Occurred while publish [{}] Fallback Message... : ", channel, e);
        }
    }

}
