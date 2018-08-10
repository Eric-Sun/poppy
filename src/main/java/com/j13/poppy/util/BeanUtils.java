package com.j13.poppy.util;


import com.j13.poppy.SystemErrorCode;
import com.j13.poppy.exceptions.CommonException;

public class BeanUtils {

    public static void copyProperties(Object dest, Object origin) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(dest, origin);
        } catch (Exception e) {
            throw new CommonException(SystemErrorCode.System.REFLECT_ERROR, e);
        }
    }

}
