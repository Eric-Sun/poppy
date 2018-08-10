package com.j13.poppy.core;

import com.google.common.collect.Lists;

import java.util.List;

public class ParameterInfo {
    private String name;
    private Class clazz;
    private String desc;

    private List<ParameterInfo> innerList = Lists.newLinkedList();

    public List<ParameterInfo> getInnerList() {
        return innerList;
    }

    public void setInnerList(List<ParameterInfo> innerList) {
        this.innerList = innerList;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
