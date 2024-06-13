package kr.standard.ums.module;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import kr.standard.ums.dto.message.ImageDelivery;
import kr.standard.ums.dto.message.MessageDelivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Component
public class MessageConsumer {
    @Autowired
    private ReportSender reportSender;

    @Autowired
    private MessagePublisher messagePublisher;


    @RabbitListener(queues = {"#{'${rabbitmq.queue.sms-name}'}",
                              "#{'${rabbitmq.queue.mms-name}'}",
                              "#{'${rabbitmq.queue.rcs-name}'}",
                              "#{'${rabbitmq.queue.kko-name}'}"},
                    ackMode = "MANUAL")
    public void consume(Message message, Channel channel, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {
        String msgType = queueName.split("msg.")[1].toUpperCase();

        parseMessage(message)
                .flatMap(messageDelivery -> {
                    if(messageDelivery.isFallbackFg()) {
                        log.info("[{}] Send Fallback Message", msgType);
                        messagePublisher.fallbackPublisher(messageDelivery, msgType);
                    }
                    return reportSender.send(messageDelivery);
                })
                .doOnSuccess(isSend -> {
                    if(isSend) {
                        log.info("[{}] msg send Success...", msgType);
                        consumeAck(message, channel);
                    } else {
                        log.error("[{}] msg send Failed...", msgType);
                        consumeNack(message, channel);
                    }
                }).onErrorResume(e -> {
                    log.error("[{}] Error Occurred while SendMessage... : ", msgType, e);
                    consumeNack(message, channel);
                    return Mono.empty();
                })
                .subscribe();
    }

    @RabbitListener(queues = {"#{'${rabbitmq.queue.fallback.rcs-name}'}",
                              "#{'${rabbitmq.queue.fallback.kko-name}'}"},
                    ackMode = "MANUAL")
    public void fallbackConsume(Message message, Channel channel, @Header(AmqpHeaders.CONSUMER_QUEUE) String queueName) {
        String msgType = queueName.split("msg.")[1].toUpperCase();

        parseMessage(message)
                .flatMap(reportSender::fallbackSend)
                .doOnSuccess(isSend -> {
                    if(isSend) {
                        log.info("[{}] fallback msg send Success...", msgType);
                        consumeAck(message, channel);
                    } else {
                        log.error("[{}] fallback msg send Failed...", msgType);
                        consumeNack(message, channel);
                    }
                }).onErrorResume(e -> {
                    log.error("[{}] Error Occurred while Send Fallback Message... : ", msgType, e);
                    consumeNack(message, channel);
                    return Mono.empty();
                })
                .subscribe();
    }

    @RabbitListener(queues = {"${rabbitmq.queue.rcs-file-name}"}, ackMode = "MANUAL")
    public void fileConsume(Message message, Channel channel) {
        String msgType = "RCS FILE";

        parseImage(message)
                .flatMap(reportSender::imageSend)
                .doOnSuccess(isSend -> {
                    if(isSend) {
                        log.info("[{}] image msg send Success...", msgType);
                        consumeAck(message, channel);
                    } else {
                        log.error("[{}] image msg send Failed...", msgType);
                        consumeNack(message, channel);
                    }
                }).onErrorResume(e -> {
                    log.error("[{}] Error Occurred while Send Image Message... : ", msgType, e);
                    consumeNack(message, channel);
                    return Mono.empty();
                })
                .subscribe();
    }



    public Mono<MessageDelivery> parseMessage(Message message) {
        try {
            MessageDelivery messageDelivery = new Gson().fromJson(new String(message.getBody()), MessageDelivery.class);
            return Mono.just(messageDelivery);
        } catch (Exception e) {
            return Mono.error(new IllegalStateException());
        }
    }

    public Mono<ImageDelivery> parseImage(Message message) {
        try {
            ImageDelivery imageDelivery = new Gson().fromJson(new String(message.getBody()), ImageDelivery.class);
            return Mono.just(imageDelivery);
        } catch (Exception e) {
            return Mono.error(new IllegalStateException());
        }
    }


    public void consumeAck(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void consumeNack(Message message, Channel channel) {
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
