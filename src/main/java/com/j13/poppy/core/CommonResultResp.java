package com.j13.poppy.core;


import com.j13.poppy.anno.Parameter;

public class CommonResultResp {
    @Parameter(desc = "操作结果成功为0，其他是失败，如果是检查存在的方法，0为存在，1位不存在")
    private int result = 0;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public static CommonResultResp success() {
        return new CommonResultResp();
    }

    public static CommonResultResp failure() {
        CommonResultResp r = new CommonResultResp();
        r.setResult(1);
        return r;
    }
}
