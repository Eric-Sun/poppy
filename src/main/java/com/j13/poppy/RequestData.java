package com.j13.poppy;

import com.google.common.collect.Maps;

import java.util.Map;

public class RequestData {

    private Map<String,Object> data = Maps.newLinkedHashMap();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
