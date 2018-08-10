package com.j13.poppy;

import com.google.common.collect.Maps;

import java.util.Map;

public class RequestParams {
    private Map<String, Object> data = Maps.newHashMap();


    public static RequestParams getInstance() {
        return new RequestParams();
    }

    public RequestParams add(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Map<String, Object> end() {
        return data;
    }

}
