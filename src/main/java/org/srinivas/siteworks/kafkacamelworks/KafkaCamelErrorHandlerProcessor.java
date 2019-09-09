package org.srinivas.siteworks.kafkacamelworks;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.srinivas.siteworks.kafkacamelworks.KafkaCamelRoute.*;


public class KafkaCamelErrorHandlerProcessor implements Processor {
    public static final String KAFKA_CAMEL_WORKS_ERROR_WHILE_PROCESSING_REQUEST = "KafkaCamelWorks Error While Processing Request";
    private static final Logger log = LoggerFactory.getLogger(KafkaCamelErrorHandlerProcessor.class);


    @Override
    public void process(Exchange exchange) throws Exception {
        Exception kfkexception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        log.error("KafkaCamelErrorHandlerProcessor Logging:", kfkexception);
            errorSetBody(exchange, KAFKA_CAMEL_WORKS_ERROR_WHILE_PROCESSING_REQUEST);
            if (kfkexception != null) {
                errorSetBody(exchange, kfkexception.getMessage());
            }

    }

    private void errorSetBody(Exchange exchange, String newBody) {
        String body = exchange.getIn().getBody(String.class);
        if (newBody != null) {
            body = (StringUtils.isNotBlank(body)) ? body + System.getProperty("line.separator") + newBody : newBody;
        }
        exchange.getIn().setBody(body);
    }

}
