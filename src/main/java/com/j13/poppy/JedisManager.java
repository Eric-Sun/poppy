package com.j13.poppy;

import com.j13.poppy.config.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
@Scope("singleton")
public class JedisManager {

    private static Logger LOG = LoggerFactory.getLogger(JedisManager.class);
    private JedisPool pool = null;

    public void init() {
        String ip = PropertiesConfiguration.getInstance().getStringValue("redis.ip");
        int port = PropertiesConfiguration.getInstance().getIntValue("redis.port");
        LOG.debug("redis ip={},port={}", ip, port);
        pool = new JedisPool(new JedisPoolConfig(), ip, port);
        LOG.info("redis is ready. ip = {}, port = {},class = {}", new Object[]{
                ip, port, this.toString()
        });
    }


    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.get(key);
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return value;
    }


    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public void set(String key, String value, int time) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
            jedis.expire(key, time);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    public void delete(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(key);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

}
