package org.srinivas.siteworks.kafkacamelworks.webconfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.srinivas.siteworks.kafkacamelworks.KafkaCamelRoute;


public final class KafkaCamelListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(KafkaCamelListener.class);
    private static final CamelContext context = new DefaultCamelContext();


    public static CamelContext getContext() {
        return context;
    }


    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        log.info("Camel Context being Stopped for KafkaCamelWorks");
        try {
            getContext().stop();
        } catch (Exception e) {
            log.info("Exception at KafkaCamelListener", e);
        }
    }


    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        log.info("Camel context being started at KafkaCamelExample" +
                "");
        try {
            context.addRoutes(new KafkaCamelRoute());
            getContext().start();
        } catch (Exception e) {
            log.info("Exception at KafkaCamelListener", e);
        }

    }

}
