package com.j13.poppy.core;

import com.google.common.collect.Lists;

import java.lang.reflect.Method;
import java.util.List;

public class ActionMethodInfo {
    private Object serviceObject;
    private Method actionMethod;
    private String actionName;
    private boolean needToken = false;
    private String desc;
    // request
    private List<ParameterInfo> paramList = Lists.newLinkedList();
    private List<ParameterInfo> innerParamList = Lists.newLinkedList();
    // response
    private Class response;
    private List<ParameterInfo> responseParamList = Lists.newLinkedList();
    private boolean tokenExpireDontThrow16 = false;

    public boolean isTokenExpireDontThrow16() {
        return tokenExpireDontThrow16;
    }

    public void setTokenExpireDontThrow16(boolean tokenExpireDontThrow16) {
        this.tokenExpireDontThrow16 = tokenExpireDontThrow16;
    }

    public Class getResponse() {
        return response;
    }

    public void setResponse(Class response) {
        this.response = response;
    }

    public List<ParameterInfo> getResponseParamList() {
        return responseParamList;
    }

    public void setResponseParamList(List<ParameterInfo> responseParamList) {
        this.responseParamList = responseParamList;
    }

    public List<ParameterInfo> getInnerParamList() {
        return innerParamList;
    }

    public void setInnerParamList(List<ParameterInfo> innerParamList) {
        this.innerParamList = innerParamList;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isNeedToken() {
        return needToken;
    }

    public void setNeedToken(boolean needToken) {
        this.needToken = needToken;
    }

    public List<ParameterInfo> getParamList() {
        return paramList;
    }

    public void setParamList(List<ParameterInfo> paramList) {
        this.paramList = paramList;
    }

    public Object getServiceObject() {
        return serviceObject;
    }

    public void setServiceObject(Object serviceObject) {
        this.serviceObject = serviceObject;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
