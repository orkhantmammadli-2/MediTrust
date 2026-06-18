package com.ltc.notificationservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
                String,
                Object
                > kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler errorHandler
    ) {

        ConcurrentKafkaListenerContainerFactory<
                String,
                Object
                > factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                consumerFactory
        );

        factory.setCommonErrorHandler(
                errorHandler
        );

        return factory;
    }
    @Bean
    public DefaultErrorHandler errorHandler(
        KafkaTemplate<String, Object> kafkaTemplate) {

    DeadLetterPublishingRecoverer recoverer =
            new DeadLetterPublishingRecoverer(
                    kafkaTemplate,
                    (record, ex) ->
                            new TopicPartition(
                                    record.topic() + "-dlt",
                                    record.partition()));

    return new DefaultErrorHandler( recoverer,
            new FixedBackOff(
                    2000L,
                    1));}
    @Bean
    public NewTopic appointmentCreatedDltTopic() {
        return TopicBuilder
                .name("appointment-created-v2-dlt")
                .partitions(1)
                .replicas(1)
                .build();}}

