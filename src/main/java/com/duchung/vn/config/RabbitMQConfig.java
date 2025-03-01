//package com.duchung.vn.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableRabbit
//public class RabbitMQConfig {
//
//    // Queue names
//    public static final String NOTIFICATION_QUEUE = "notification.queue";
//    public static final String EMAIL_QUEUE = "email.queue";
//    public static final String SMS_QUEUE = "sms.queue";
//
//    // Exchange names
//    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
//
//    // Routing keys
//    public static final String EMAIL_ROUTING_KEY = "notification.email";
//    public static final String SMS_ROUTING_KEY = "notification.sms";
//
//    @Bean
//    public Queue notificationQueue() {
//        return QueueBuilder.durable(NOTIFICATION_QUEUE)
//                .withArgument("x-dead-letter-exchange", "dlx.exchange")
//                .withArgument("x-dead-letter-routing-key", "dlx.notification")
//                .build();
//    }
//
//    @Bean
//    public Queue emailQueue() {
//        return QueueBuilder.durable(EMAIL_QUEUE)
//                .withArgument("x-dead-letter-exchange", "dlx.exchange")
//                .withArgument("x-dead-letter-routing-key", "dlx.email")
//                .build();
//    }
//
//    @Bean
//    public Queue smsQueue() {
//        return QueueBuilder.durable(SMS_QUEUE)
//                .withArgument("x-dead-letter-exchange", "dlx.exchange")
//                .withArgument("x-dead-letter-routing-key", "dlx.sms")
//                .build();
//    }
//
//    @Bean
//    public DirectExchange notificationExchange() {
//        return new DirectExchange(NOTIFICATION_EXCHANGE);
//    }
//
//    @Bean
//    public Binding emailBinding() {
//        return BindingBuilder.bind(emailQueue()).to(notificationExchange()).with(EMAIL_ROUTING_KEY);
//    }
//
//    @Bean
//    public Binding smsBinding() {
//        return BindingBuilder.bind(smsQueue()).to(notificationExchange()).with(SMS_ROUTING_KEY);
//    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter);
//        return rabbitTemplate;
//    }
//
//    // Dead Letter Exchange configuration
//    @Bean
//    public DirectExchange deadLetterExchange() {
//        return new DirectExchange("dlx.exchange");
//    }
//
//    @Bean
//    public Queue deadLetterQueue() {
//        return QueueBuilder.durable("dlx.queue").build();
//    }
//
//    @Bean
//    public Binding deadLetterBinding() {
//        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dlx.#");
//    }
//}