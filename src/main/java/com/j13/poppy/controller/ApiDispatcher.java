package com.j13.poppy.controller;

import com.google.common.collect.Lists;
import com.j13.poppy.ErrorResponse;
import com.j13.poppy.RequestData;
import com.j13.poppy.SystemErrorCode;
import com.j13.poppy.TokenCacheInterface;
import com.j13.poppy.core.ActionMethodInfo;
import com.j13.poppy.core.ActionServiceLoader;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.ParameterInfo;
import com.j13.poppy.exceptions.CommonException;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Service
public class ApiDispatcher {
    private static Logger LOG = LoggerFactory.getLogger(ApiDispatcher.class);

    private static String T_KEY = "t";
    private static String UID_KEY = "uid";
    private static String DEVICE_KEY = "deviceId";

    @Autowired
    ActionServiceLoader actionServiceLoader;

    @Autowired
    TokenCacheInterface tokenCacheInterface;


    public Object dispatch(String act, RequestData requestData, String postData) {

        Map<String, ActionMethodInfo> maps = actionServiceLoader.getActionInfoMap();
        ActionMethodInfo ami = maps.get(act);
        if (ami == null) {
            return new ErrorResponse(SystemErrorCode.System.NOT_FOUND_ACTION);
        }

        Method actionMethod = ami.getActionMethod();
        LOG.debug("Founded. method = {}", actionMethod.getName());
        Object beanObject = ami.getServiceObject();
        List<ParameterInfo> parameterInfoList = ami.getParamList();
        List<Object> inputParams = Lists.newLinkedList();
        CommandContext ctxt = null;

        for (ParameterInfo pi : parameterInfoList) {
            LOG.debug(" type = {}, name = {}", pi.getClazz(), pi.getName());
            if (pi.getClazz().equals(CommandContext.class)) {
                // get and set context
                ctxt = genCommandContextObject(requestData, postData);
                inputParams.add(ctxt);
            } else {
                // request object
                try {
                    Object obj = pi.getClazz().newInstance();
                    BeanUtils.populate(obj, requestData.getData());
                    inputParams.add(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }


        }


        try {
            if (ami.isNeedToken()) {
                // 增加是否要对token过期的错误码的判断，首页之类的如果token过期的话是不需要抛出16的

                LOG.debug("begin to check t.");
                if (requestData.getData().get(T_KEY) != null && !requestData.getData().get(T_KEY).toString().equals("0")) {
                    // 检查这个t
                    String t = requestData.getData().get(T_KEY).toString();
                    LOG.debug("t = {}", t);
                    int userId = tokenCacheInterface.getToken2UserId(t);
                    if (userId == 0) {
                        if(ami.isTokenExpireDontThrow16()){
                            ctxt.setUid(0);
                        }else {
                            LOG.debug("t expire. t = {}", t);
                            // 不存在这个token
                            return new ErrorResponse(SystemErrorCode.Common.T_EXPIRE);
                        }
                    } else {
                        LOG.debug("t is ok. t = {},userId = {}", t, userId);
                        ctxt.setUid(userId);
                    }
                } else {
                    ctxt.setUid(0);
//                    return new ErrorResponse(SystemErrorCode.Common.NEED_T);
                }

            } else {
                LOG.debug("not need token");
            }


            return actionMethod.invoke(beanObject, inputParams.toArray());
        } catch (IllegalAccessException e) {
            return new ErrorResponse(SystemErrorCode.System.ACTION_REFLECT_ERROR);
        } catch (InvocationTargetException e) {
//            LOG.error("", e.getTargetException());
            if (e.getTargetException().getClass().equals(CommonException.class)) {
                LOG.error("", e);
                return new ErrorResponse(((CommonException) e.getTargetException()).getErrorCode());
            }
            LOG.error("", e);
            return new ErrorResponse(SystemErrorCode.System.ACTION_REFLECT_ERROR);
        } catch (Exception e) {
            LOG.error("", e);
            return new ErrorResponse(SystemErrorCode.System.SYSTEM_ERROR);
        }
    }

    private CommandContext genCommandContextObject(RequestData requestData, String postData) {
        CommandContext ctxt = new CommandContext();
        if (requestData.getData().get(T_KEY) != null) {
            ctxt.setT(requestData.getData().get(T_KEY).toString());
        }
        if (requestData.getData().get(UID_KEY) != null) {
            ctxt.setUid(new Integer(requestData.getData().get(UID_KEY).toString()));
        }
        if (requestData.getData().get(DEVICE_KEY) != null) {
            ctxt.setDeviceId(requestData.getData().get(DEVICE_KEY).toString());
        }
        if (!StringUtils.isEmpty(postData)) {
            ctxt.setPostData(postData);
        }

        return ctxt;
    }

}
