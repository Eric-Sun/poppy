package com.j13.poppy.doc;

import com.google.common.collect.Lists;

import java.util.List;

public class ReqAndRespDoc {
    private String className;
    private List<ParamDoc> params = Lists.newLinkedList();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ParamDoc> getParams() {
        return params;
    }

    public void setParams(List<ParamDoc> params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReqAndRespDoc)) return false;

        ReqAndRespDoc that = (ReqAndRespDoc) o;

        if (className != null ? !className.equals(that.className) : that.className != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = className != null ? className.hashCode() : 0;
        return result;
    }
}
