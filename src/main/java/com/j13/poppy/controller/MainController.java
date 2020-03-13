package com.j13.poppy.controller;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.ErrorResponse;
import com.j13.poppy.RequestData;
import com.j13.poppy.SystemErrorCode;
import com.j13.poppy.doc.DocManager;
import com.j13.poppy.doc.MethodDoc;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.JSONV2;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    private static Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Autowired
    ApiDispatcher dispatcher;
    @Autowired
    DocManager docManager;

    private static int requestCount = 0;


    @RequestMapping("/api")
    public String api(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin","*");

//        String postData = parseRequestPostData(request);
//        if (!StringUtils.isEmpty(postData)) {
//            LOG.info("post data . {}", postData);
//        }
        RequestData requestData = parseRequest(request);
        LOG.info("request({}) : {}", requestCount, JSONV2.toJSONString(requestData));
        String act = requestData.getData().get("act").toString();
        String postData = "";
        long timeStart = System.currentTimeMillis();

        Object obj = null;
        try {
            obj = dispatcher.dispatch(act, requestData, postData);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            obj = new ErrorResponse(SystemErrorCode.System.SYSTEM_ERROR);
        }
        String responseJson = null;
        if (obj instanceof String) {
            responseJson = (String) obj;
        } else {
            responseJson = JSON.toJSONString(obj);
        }
        LOG.info("requestCost={}, count={}", System.currentTimeMillis() - timeStart, requestCount);
        requestCount++;
        LOG.debug("response : {}", responseJson);
        response.getWriter().write(responseJson);
        response.flushBuffer();
        return null;
    }


//    @RequestMapping("/wechat")
//    public String wechat(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException {
//        response.setContentType("text/html;charset=UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        String postData = parseRequestPostData(request);
//        if (!StringUtils.isEmpty(postData)) {
//            LOG.info("post data . {}", postData);
//        }
//        RequestData requestData = parseRequest(request);
//        String act = requestData.getData().get("act").toString();
//
//        LOG.info("request : {}", JSONV2.toJSONString(requestData));
//        Object obj = null;
//        try {
//            obj = dispatcher.dispatch(act, requestData, postData);
//        } catch (Exception e) {
//            LOG.error(e.getMessage());
//            obj = new ErrorResponse(SystemErrorCode.System.SYSTEM_ERROR);
//        }
//        String responseJson = null;
//        if (obj instanceof String) {
//            responseJson = (String) obj;
//        } else {
//            responseJson = JSON.toJSONString(obj);
//        }
//        LOG.info("response : {}", responseJson);
//        response.getWriter().write(responseJson);
//        response.flushBuffer();
//        return null;
//    }

    @RequestMapping("/doc")
    public String doc(HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws NoSuchFieldException {
        String method = request.getParameter("method");
        if (method == null) {
            List<MethodDoc> methodList = docManager.getAllMothods();
            model.put("methodList", methodList);
            return "/list";
        } else {
            MethodDoc doc = docManager.getDocByMethodName(method);
            model.put("doc", doc);
            return "/method";
        }
    }


    private RequestData parseRequest(HttpServletRequest request) throws FileUploadException {
        RequestData requestData = new RequestData();
        if (ServletFileUpload.isMultipartContent(request)) {
            LOG.info("multipart content-type.");
            LOG.info("act="+request.getParameter("act"));
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            List<FileItem> list = upload.parseRequest(request);
            LOG.info("list.size={}",list.size());
            for (FileItem item : list) {
                if (!item.isFormField()) {
                    requestData.getData().put(item.getFieldName(), item);
                    LOG.info("item is fileItem object. key={}", item.getFieldName());
                } else {
                    requestData.getData().put(item.getFieldName(), item.getString());
                    LOG.info("item is common field . key={},value={}", item.getFieldName(), item.getString());
                }
            }
            return requestData;
        } else {
            LOG.info("common content-type.");
            Enumeration<String> enumKeys = request.getParameterNames();
            while (enumKeys.hasMoreElements()) {
                String key = enumKeys.nextElement();
                String value = request.getParameter(key);
                requestData.getData().put(key, value);
                LOG.debug("item is common field . key={},value={}", key, value);
            }

            return requestData;
        }


    }


    public String parseRequestPostData(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                sb.append(line);
            return sb.toString();
        } catch (Exception e) {
            LOG.error("parse request post data error.", e);
            throw new CommonException(SystemErrorCode.System.PARSE_REQUEST_POST_DATA_ERROR);
        }
    }

}
