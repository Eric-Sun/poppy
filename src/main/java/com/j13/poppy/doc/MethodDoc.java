package com.j13.poppy.doc;

import com.google.common.collect.Lists;

import java.util.List;

public class MethodDoc {
    private String name;
    private String desc;
    private ReqAndRespDoc req;
    private List<ReqAndRespDoc> respList = Lists.newLinkedList();

    public ReqAndRespDoc getReq() {
        return req;
    }

    public void setReq(ReqAndRespDoc req) {
        this.req = req;
    }

    public List<ReqAndRespDoc> getRespList() {
        return respList;
    }

    public void setRespList(List<ReqAndRespDoc> respList) {
        this.respList = respList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
