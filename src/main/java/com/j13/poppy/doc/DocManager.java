package com.j13.poppy.doc;

import com.google.common.collect.Lists;
import com.j13.poppy.core.ActionMethodInfo;
import com.j13.poppy.core.ActionServiceLoader;
import com.j13.poppy.core.ParameterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
public class DocManager {


    @Autowired
    ActionServiceLoader actionServiceLoader;

    public List<MethodDoc> getAllMothods() {

        List<MethodDoc> list = Lists.newLinkedList();
        Map<String, ActionMethodInfo> map = actionServiceLoader.getActionInfoMap();
        for (String methodName : map.keySet()) {
            MethodDoc methodDoc = new MethodDoc();
            ActionMethodInfo ami = map.get(methodName);
            methodDoc.setName(ami.getActionName());
            methodDoc.setDesc(ami.getDesc());
            list.add(methodDoc);
        }
        return list;
    }


    public MethodDoc getDocByMethodName(String methodName) throws NoSuchFieldException {
        MethodDoc methodDoc = new MethodDoc();
        Map<String, ActionMethodInfo> map = actionServiceLoader.getActionInfoMap();
        ActionMethodInfo ami = map.get(methodName);
        methodDoc.setDesc(ami.getDesc());
        methodDoc.setName(ami.getActionName());

        // request
        ParameterInfo reqParameterInfo = ami.getParamList().get(1);
        ReqAndRespDoc requestDoc = new ReqAndRespDoc();
        String className = reqParameterInfo.getClazz().getName();
        requestDoc.setClassName(className.substring(className.lastIndexOf(".") + 1));
        for (ParameterInfo pi : ami.getInnerParamList()) {
            ParamDoc pDoc = new ParamDoc();
            pDoc.setName(pi.getName());
            if (pi.getClazz().toString().indexOf("String") > 0) {
                pDoc.setType("String");
            } else {
                pDoc.setType(pi.getClazz().toString());
            }
            pDoc.setDesc(pi.getDesc());
            requestDoc.getParams().add(pDoc);
        }
        methodDoc.setReq(requestDoc);

//        // response
        String responseClassName = ami.getResponse().getName();
        parseResponse(methodDoc, ami.getResponse(), responseClassName.substring(responseClassName.lastIndexOf(".") + 1),
                ami.getResponseParamList());

        List<ReqAndRespDoc> tmpRespList = methodDoc.getRespList();
        List<ReqAndRespDoc> respList = Lists.newLinkedList();
        methodDoc.setRespList(respList);
        for (ReqAndRespDoc doc : tmpRespList) {
            if (!respList.contains(doc)) {
                respList.add(doc);
            }
        }

        return methodDoc;
    }

    private void parseResponse(MethodDoc methodDoc, Class clazz,
                               String responseClassName, List<ParameterInfo> responseParamList) throws NoSuchFieldException {

        ReqAndRespDoc doc = new ReqAndRespDoc();
        methodDoc.getRespList().add(doc);
        doc.setClassName(responseClassName);
        for (ParameterInfo pi : responseParamList) {
            ParamDoc pDoc = new ParamDoc();
            pDoc.setName(pi.getName());
            if (pi.getClazz().toString().indexOf("String") > 0) {
                pDoc.setType("String");
            } else {
                pDoc.setType(pi.getClazz().toString());
            }
            pDoc.setDesc(pi.getDesc());
            doc.getParams().add(pDoc);

            //
            if (pi.getClazz().equals(List.class)) {
                // if List<Object>
                Type genType = clazz.getDeclaredField(pi.getName()).getGenericType();
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                Class classInList = (Class) params[0];
                String className = toClassName(classInList);
                pDoc.setType("List<" + className + ">");
                parseResponse(methodDoc, classInList, className, pi.getInnerList());
            } else if (pi.getInnerList().size() != 0) {
                // if object
                String cn = toClassName(pi.getClazz());
                pDoc.setType(cn);
                parseResponse(methodDoc, pi.getClazz(), cn, pi.getInnerList());
            }
        }

    }


    public String toClassName(Class clazz) {
        String cn = clazz.getName();
        return cn.substring(cn.lastIndexOf(".") + 1);
    }

}
