/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.kafkacamelworks.webconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class
KafkaCamelViewConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCamelViewConfiguration.class);

    @Bean
    public ViewResolver kafkacamelviewResolver() {
        logger.info("ViewConfiguration kafkacamelviewResolver()");
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setPrefix("/WEB-INF/displays/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
