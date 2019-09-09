/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.kafkacamelworks.webconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.srinivas.siteworks.kafkacamelworks.webconfig"})
public class KafkaCamelMvcContextConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCamelMvcContextConfiguration.class);


    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        logger.info("KafkaCamelMvcContextConfiguration WebMvcContextConfiguration: configureDefaultServletHandling Method");
        configurer.enable();
    }

}
