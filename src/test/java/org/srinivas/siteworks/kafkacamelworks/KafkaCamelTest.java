package org.srinivas.siteworks.kafkacamelworks;

import org.apache.camel.*;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaEndpoint;
import org.apache.camel.component.kafka.KafkaProducer;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultHeadersMapFactory;
import org.apache.camel.impl.DefaultMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KafkaCamelTest {
    private KafkaEndpoint endpoint;
    private KafkaProducer producer;
    private KafkaComponent kafka;
    @Mock
    private TypeConverter converter;
    @Mock
    private Exchange exchange;

    @Mock
    private ConsumerRecord<String, String> mockRecord;
    @Mock
    private CamelContext camelContext;
    private Message in;

    @Before
    public void setup() {
        endpoint = new KafkaEndpoint("kafka:mytopic?brokers=localhost", new KafkaComponent(new DefaultCamelContext()));
        in = new DefaultMessage(camelContext);
    }

    @After
    public void tidyup() {
        kafka = null;
    }


    @Test
    public void kafkaExchangeTest() {
        when(mockRecord.key()).thenReturn("relevantkey");
        when(mockRecord.topic()).thenReturn("sometopic");
        when(mockRecord.partition()).thenReturn(8);
        when(mockRecord.offset()).thenReturn(100L);
        when(mockRecord.timestamp()).thenReturn(2618126577394L);

        Exchange exchange = endpoint.createKafkaExchange(mockRecord);
        Message inMessage = exchange.getIn();
        assertNotNull(inMessage);
        assertEquals("relevantkey", inMessage.getHeader(KafkaConstants.KEY));
        assertEquals("sometopic", inMessage.getHeader(KafkaConstants.TOPIC));
        assertEquals(8, inMessage.getHeader(KafkaConstants.PARTITION));
        assertEquals(100L, inMessage.getHeader(KafkaConstants.OFFSET));
        assertEquals(2618126577394L, inMessage.getHeader(KafkaConstants.TIMESTAMP));
    }

    @Test
    public void sendsMessageTest() throws Exception {
        kafka = new KafkaComponent(new DefaultCamelContext());
        kafka.setBrokers("brokertest:6789,brokertest2:4567");

        endpoint = (KafkaEndpoint) kafka.createEndpoint("kafka:sometopic");
        producer = new KafkaProducer(endpoint);

        RecordMetadata record = new RecordMetadata(null, 0, 0, 0, 0L, 0, 0);
        Future future = Mockito.mock(Future.class);
        Mockito.when(future.get()).thenReturn(record);
        org.apache.kafka.clients.producer.KafkaProducer kafkaProducer = Mockito.mock(org.apache.kafka.clients.producer.KafkaProducer.class);
        Mockito.when(kafkaProducer.send(any(ProducerRecord.class))).thenReturn(future);

        Mockito.when(exchange.getContext()).thenReturn(camelContext);
        Mockito.when(camelContext.getTypeConverter()).thenReturn(converter);
        Mockito.when(converter.tryConvertTo(String.class, exchange, null)).thenReturn(null);
        Mockito.when(camelContext.getHeadersMapFactory()).thenReturn(new DefaultHeadersMapFactory());

        producer.setKafkaProducer(kafkaProducer);
        producer.setWorkerPool(Executors.newFixedThreadPool(1));

        endpoint.getConfiguration().setTopic("sometopic");
        Mockito.when(exchange.getIn()).thenReturn(in);

        in.setHeader(KafkaConstants.PARTITION_KEY, 4);
        in.setHeader(KafkaConstants.TOPIC, "testTopic");
        in.setHeader(KafkaConstants.KEY, "relevantKey");

        producer.process(exchange);

        checkSendMessage(4, "testTopic", "relevantKey");
        assertMetadata();
    }


    private void checkSendMessage(Integer partitionKey, String topic, String messageKey) {
        ArgumentCaptor<ProducerRecord> captor = ArgumentCaptor.forClass(ProducerRecord.class);
        Mockito.verify(producer.getKafkaProducer()).send(captor.capture());
        assertEquals(partitionKey, captor.getValue().partition());
        assertEquals(messageKey, captor.getValue().key());
        assertEquals(topic, captor.getValue().topic());
    }

    private void assertMetadata() {
        List<RecordMetadata> metaData = (List<RecordMetadata>) in.getHeader(KafkaConstants.KAFKA_RECORDMETA);
        assertTrue(metaData != null);
        assertEquals("one record", metaData.size(), 1);
        assertTrue(metaData.get(0) != null);
    }

    @Test
    public void isSingletonShoudlReturnTrue() {
        assertTrue(endpoint.isSingleton());
    }

}
