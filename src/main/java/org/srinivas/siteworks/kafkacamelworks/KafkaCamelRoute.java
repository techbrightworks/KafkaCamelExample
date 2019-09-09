package org.srinivas.siteworks.kafkacamelworks;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.component.kafka.KafkaComponent;


public class KafkaCamelRoute extends RouteBuilder {

    public static final String CAMEL_ENDPOINT_DIRECT_ERROR = "direct:error";

    public String getCurrentMessage() {
        return currentMessage;
    }

    public void setCurrentMessage(String currentMessage) {
        this.currentMessage = currentMessage;
    }

    private String currentMessage;

    @Override
    public void configure() throws Exception {

        onException(Exception.class).to(CAMEL_ENDPOINT_DIRECT_ERROR).handled(true).end();

        PropertiesComponent pc = getContext().getComponent("properties", PropertiesComponent.class);
        pc.setLocation("classpath:application.properties");

        KafkaComponent kafka = new KafkaComponent();
        kafka.setBrokers("{{kafka.host}}:{{kafka.port}}");
        getContext().addComponent("kafka", kafka);

        from("seda:kafkaStart").routeId("DirectToKafka")
                .to("kafka:{{producer.topic}}").log("${headers}");

        from("kafka:{{consumer.topic}}?brokers={{kafka.host}}:{{kafka.port}}"
                + "&maxPollRecords={{consumer.maxPollRecords}}"
                + "&consumersCount={{consumer.consumersCount}}"
                + "&seekTo={{consumer.seekTo}}"
                + "&groupId={{consumer.group}}")
                .routeId("FromKafka")
                .to("seda:transform")
                .log("${body}");

        from("seda:transform")
                .routeId("Transform")
                .log("${body}")
                .log("${headers}")
                .transform(
                        simple("The message consumed is: ${body}"))
                .convertBodyTo(String.class).process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                log.info("Processing setting current message from topic");
                String message = exchange.getIn().getBody().toString();
                setCurrentMessage(message);
                exchange.getIn().setBody(message);
            }
        }).log("Message ${body}");


        from("direct:currentmessage").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                log.info("Processing getting current message from topic");
                exchange.getIn().setBody(getCurrentMessage());
            }
        }).log("Message ${body}");


        from(CAMEL_ENDPOINT_DIRECT_ERROR).process(new KafkaCamelErrorHandlerProcessor())
                .log("Message ${body}");


    }

}
