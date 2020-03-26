package com.j13.poppy;

import com.j13.poppy.config.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class JedisManager {

    private static Logger LOG = LoggerFactory.getLogger(JedisManager.class);
    private JedisPool pool = null;

    @Autowired
    PropertiesConfiguration propertiesConfiguration;

    @PostConstruct
    public void init() {
        String ip = propertiesConfiguration.getStringValue("redis.ip");
        int port = propertiesConfiguration.getIntValue("redis.port");
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

    public void sadd(String key,String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.sadd(key,member);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }
    public void sadd(String key,String... member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.sadd(key,member);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }


    public void srem(String key,String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.srem(key,member);
        } finally {
            if (jedis != null)
                jedis.close();

        }
    }

    public List<String> srandmember(String key, int count){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srandmember(key,count);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

}
