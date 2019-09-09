/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.kafkacamelworks.webconfig;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.kafka.KafkaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@Controller
public class KafkaCamelController {
    private static final Logger log = LoggerFactory.getLogger(KafkaCamelController.class);


    @RequestMapping(value = "/kafkaworks/message.mvc", method = RequestMethod.POST)
    public @ResponseBody
    String kafkaWorksPage(@RequestParam("producerMessage") String producerMessage) throws Exception {
        log.info("KafkaCamelController");
        String result = null;
        String resultconsumer = null;
        try {
            ProducerTemplate producerTemplate = KafkaCamelListener.getContext().createProducerTemplate();
            Map<String, Object> newHeader = new HashMap<>();
            newHeader.put(KafkaConstants.KEY, "AB");
            result = producerTemplate.requestBodyAndHeaders("seda:kafkaStart", producerMessage, newHeader, String.class);
            log.info("Successfully published event to Kafka.");
            log.info("kafkaWorksPage Result is" + result);
            resultconsumer = producerTemplate.requestBody("direct:currentmessage", null, String.class);
            log.info("kafkaWorksPage consumerResult is" + resultconsumer);
            return resultconsumer;
        } catch (Exception e) {
            log.info("Exception at KafkaCamelListener", e);
        }
        return resultconsumer;
    }

    @RequestMapping(value = "/kafkaworks.mvc", method = RequestMethod.GET)
    public String kafkaWorksPage() throws Exception {
        log.info("KafkaCamelController");
        return "kafkaworks";
    }

}
