package com.j13.poppy;

import com.j13.poppy.config.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service("singleton")
public class TokenManager {

    private static String PREFIX = "v10:";
    private Random random = new Random();
    private int s;

    @Autowired
    JedisManager jedisManager;
    @Autowired
    PropertiesConfiguration propertiesConfiguration;

    @PostConstruct
    public void init() {
        s = propertiesConfiguration.getIntValue("token.expire.s");
    }

    public int checkTicket(String t) {
        String value = jedisManager.get(PREFIX + t);
        return value == null ? 0 : new Integer(value);
    }

    public void setTicket(String t, long userId) {
//        int min = PropertiesConfiguration.getInstance().getIntValue("t.expire.min");
//        int time = min * 60;

        jedisManager.set(PREFIX + t, userId + "", s);
    }

    public String genT() {
        int i = random.nextInt(10000);
        return System.currentTimeMillis() + i + "";
    }


}
