package com.j13.poppy;

import com.j13.poppy.config.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenManager {

    private static String PREFIX = "v10:";

    @Autowired
    JedisManager jedisManager;

    public int checkTicket(String t) {
        String value = jedisManager.get(PREFIX + t);
        return value == null ? 0 : new Integer(value);
    }

    public void setTicket(long userId, String t) {
        int min = PropertiesConfiguration.getInstance().getIntValue("t.expire.min");
        int time = min * 60;

        jedisManager.set(PREFIX + t, userId + "", time);
    }


}
