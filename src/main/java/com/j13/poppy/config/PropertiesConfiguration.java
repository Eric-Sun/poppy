package com.j13.poppy.config;


import com.j13.poppy.exceptions.RequestFatalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class PropertiesConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(PropertiesConfiguration.class);
    private static Map<String, String> map = new HashMap();


    @PostConstruct
    public void init() throws RequestFatalException {
        InputStream is = PropertiesConfiguration.class.getResourceAsStream("/poppy.properties");
        Properties properties = new Properties();

        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RequestFatalException("configPath=/poppy.properties", e);
        }

        Set keys = properties.stringPropertyNames();
        Iterator iter = keys.iterator();

        while(iter.hasNext()) {
            String key = (String)iter.next();
            map.put(key, properties.getProperty(key));
        }
        LOG.info("load all properties");

    }

    public String getStringValue(String key) {
        return (String)map.get(key);
    }

    public int getIntValue(String key) {
        return (new Integer((String)map.get(key) + "")).intValue();
    }
}
