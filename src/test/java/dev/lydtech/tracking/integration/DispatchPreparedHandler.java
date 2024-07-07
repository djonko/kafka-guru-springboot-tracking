package dev.lydtech.tracking.integration;

import dev.lydtech.tracking.TrackingConfiguration;
import dev.lydtech.tracking.message.DispatchPrepared;
import dev.lydtech.tracking.message.TrackingStatusUpdated;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static dev.lydtech.tracking.TrackingConfiguration.TRACKING_STATUS_TOPIC;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@SpringBootTest(classes = {TrackingConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(controlledShutdown = true)
public class DispatchPreparedHandler {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private KafkaTestListener kafkaTestListener;

    @BeforeEach
    public void setUp(@Autowired EmbeddedKafkaBroker broker, @Autowired KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        kafkaTestListener.trackingStatusCounter.set(0);
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(container -> {
            ContainerTestUtils.waitForAssignment(container, broker.getPartitionsPerTopic());
        });

    }

    @Test
    public void testListenDispatchPrepared() throws Exception {
        DispatchPrepared dispatchPrepared = DispatchPrepared.builder().orderId(UUID.randomUUID()).build();
        sendMessage(dispatchPrepared);
        await().atMost(3, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS).until(kafkaTestListener.trackingStatusCounter::get, equalTo(1));
    }

    private void sendMessage(Object data) throws Exception {
        kafkaTemplate.send(MessageBuilder.withPayload(data).setHeader(KafkaHeaders.TOPIC, TrackingConfiguration.DISPATCH_TRACKING_TOPIC).build()).get();
    }

    @Configuration
    static class TestConfig {

        @Bean
        public KafkaTestListener testListener() {
            return new KafkaTestListener();
        }
    }


    public static class KafkaTestListener {

        AtomicInteger trackingStatusCounter = new AtomicInteger(0);

        /**
         * To test the tracking update status producer
         * @param  payload DispatchPrepared
         */
        @KafkaListener(groupId = "KafkaIntegrationTest", topics = TRACKING_STATUS_TOPIC)
        void receiveTrackingStatus(@Payload TrackingStatusUpdated payload) {
            log.debug("Received updated tracking status: {}", payload);
            trackingStatusCounter.incrementAndGet();
        }

    }
}
