package com.j13.poppy;

import com.j13.poppy.config.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class PoppyListener implements ServletContextListener {
    private static Logger LOG = LoggerFactory.getLogger(PoppyListener.class);

    private ApplicationContext applicationContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        PropertiesConfiguration.getInstance().addResource("/poppy.properties");
        LOG.info("load all properties.");
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
        JedisManager jedisManager = applicationContext.getBean(JedisManager.class);
        jedisManager.init();

        TokenManager tokenManager = applicationContext.getBean(TokenManager.class);
        tokenManager.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
