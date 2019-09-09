/**
 * @author SrinivasJasti
 */
package org.srinivas.siteworks.kafkacamelworks.webconfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


public class KafkaCamelWebApplicationInitializer implements WebApplicationInitializer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCamelWebApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext container) {
        logger.info("Started to pickup the annotated classes at KafkaCamelWebApplicationInitializer");
        startServlet(container);
    }

    private void startServlet(final ServletContext container) {
        WebApplicationContext dispatcherContext = registerContext(KafkaCamelMvcContextConfiguration.class);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        container.addListener(new ContextLoaderListener(dispatcherContext));
        container.addListener(new KafkaCamelListener());
        ServletRegistration.Dynamic dispatcher;
        dispatcher = container.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("*.mvc");
    }

    private WebApplicationContext registerContext(final Class<?>... annotatedClasses) {
        logger.info("Using AnnotationConfigWebApplicationContext createContext");
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(annotatedClasses);
        return context;
    }

}
