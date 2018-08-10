package com.j13.poppy.util;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.RequestData;
import org.apache.commons.fileupload.FileItem;

public class JSONV2 {

    public static String toJSONString(Object o) {
        if (o instanceof RequestData) {
            RequestData r2 = new RequestData();
            RequestData r = (RequestData) o;
            for (String key : r.getData().keySet()) {
                Object v = r.getData().get(key);
                if (v instanceof FileItem) {

                } else {
                    r2.getData().put(key, v);
                }
            }
            return JSON.toJSONString(r2);
        } else {
            return JSON.toJSONString(o);
        }
    }
}
