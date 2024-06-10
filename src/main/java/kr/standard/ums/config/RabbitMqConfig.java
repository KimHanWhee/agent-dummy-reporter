package kr.standard.ums.config;

import com.rabbitmq.client.Address;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Getter
    @Value("${spring.rabbitmq.password}")
    private String rabbitMQPassword;

    @Value("${rabbitmq.queue.sms-name}")
    private String smsQueueName;

    @Value("${rabbitmq.queue.mms-name}")
    private String mmsQueueName;

    @Value("${rabbitmq.queue.rcs-name}")
    private String rcsQueueName;

    @Value("${rabbitmq.queue.rcs-file-name}")
    private String rcsFileQueueName;

    @Value("${rabbitmq.queue.kko-name}")
    private String kkoQueueName;

    @Value("${rabbitmq.queue.fallback.rcs-name}")
    private String rcsFallbackQueue;

    @Value("${rabbitmq.queue.fallback.kko-name}")
    private String kkoFallbackQueue;


    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;


    @Value("${rabbitmq.routing.sms-key}")
    private String smsRoutingKey;

    @Value("${rabbitmq.routing.mms-key}")
    private String mmsRoutingKey;

    @Value("${rabbitmq.routing.rcs-key}")
    private String rcsRoutingKey;

    @Value("${rabbitmq.routing.rcs-file-key}")
    private String rcsFileRoutingKey;

    @Value("${rabbitmq.routing.kko-key}")
    private String kkoRoutingKey;

    @Value("${rabbitmq.routing.fallback.rcs-key}")
    private String rcsFallbackRoutingKey;

    @Value("${rabbitmq.routing.fallback.kko-key}")
    private String kkoFallbackRoutingKey;

    @Getter
    private List<Address> clusterNodeList = new ArrayList<>();

    @Bean
    public Queue smsQueue() {
        return new Queue(smsQueueName);
    }

    @Bean
    public Queue mmsQueue() {
        return new Queue(mmsQueueName);
    }

    @Bean
    public Queue rcsQueue() {
        return new Queue(rcsQueueName);
    }

    @Bean
    public Queue rcsFileQueue() {
        return new Queue(rcsFileQueueName);
    }

    @Bean
    public Queue kkoQueue() {
        return new Queue(kkoQueueName);
    }

    @Bean
    public Queue rcsFallbackQueue() {
        return new Queue(rcsFallbackQueue);
    }


    @Bean
    public Queue kkoFallbackQueue() {
        return new Queue(kkoFallbackQueue);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding smsBinding(Queue smsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(smsQueue).to(exchange).with(smsRoutingKey);
    }

    @Bean
    public Binding mmsBinding(Queue mmsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mmsQueue).to(exchange).with(mmsRoutingKey);
    }

    @Bean
    public Binding rcsBinding(Queue rcsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(rcsQueue).to(exchange).with(rcsRoutingKey);
    }

    @Bean
    public Binding rcsFileBinding(Queue rcsFileQueue, DirectExchange exchange) {
        return BindingBuilder.bind(rcsFileQueue).to(exchange).with(rcsFileRoutingKey);
    }

    @Bean
    public Binding kkoBinding(Queue kkoQueue, DirectExchange exchange) {
        return BindingBuilder.bind(kkoQueue).to(exchange).with(kkoRoutingKey);
    }

    @Bean
    public Binding rcsFallbackBinding(Queue rcsFallbackQueue, DirectExchange exchange) {
        return BindingBuilder.bind(rcsFallbackQueue).to(exchange).with(rcsFallbackRoutingKey);
    }

    @Bean
    public Binding kkoFallbackBinding(Queue kkoFallbackQueue, DirectExchange exchange) {
        return BindingBuilder.bind(kkoFallbackQueue).to(exchange).with(kkoFallbackRoutingKey);
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cf = new CachingConnectionFactory();

        cf.setUsername(username);
        cf.setPassword(rabbitMQPassword);
        cf.setVirtualHost(virtualHost);


        return cf;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);

        return rabbitTemplate;
    }


}
