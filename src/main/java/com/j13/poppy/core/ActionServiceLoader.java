package com.j13.poppy.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedTicket;
import com.j13.poppy.anno.Parameter;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
public class ActionServiceLoader implements ApplicationContextAware {

    private static String FACADE = "Facade";

    private static Logger LOG = LoggerFactory.getLogger(ActionServiceLoader.class);

    private ApplicationContext applicationContext;

    public Map<String, ActionMethodInfo> getActionInfoMap() {
        return actionInfoMap;
    }

    private Map<String, ActionMethodInfo> actionInfoMap = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void loadActionInfos() {
        try {
            LOG.info("start loading components.");

            Map<String, Object> beansMap = applicationContext.getBeansWithAnnotation(Component.class);
            for (String componentName : beansMap.keySet()) {

                if (componentName.indexOf(FACADE) < 0)
                    continue;
                Class componentClazz = beansMap.get(componentName).getClass();
                Object componentObject = beansMap.get(componentName);
                Method[] componentMethods = componentClazz.getDeclaredMethods();
                LOG.info("Loading component . name = {}", componentName);
                for (int mIndex = 0; mIndex < componentMethods.length; mIndex++) {
                    Method actionMethod = componentMethods[mIndex];
                    ActionMethodInfo ami = new ActionMethodInfo();

                    // parse all anno(eg. Action NeedTicket)
                    Action actionAnno = (Action) actionMethod.getAnnotation(Action.class);
                    NeedTicket ticketAnno = (NeedTicket) actionMethod.getAnnotation(NeedTicket.class);

                    if (ticketAnno != null) {
                        ami.isNeedTicket();
                    }

                    if (actionAnno != null) {
                        String name = actionAnno.name();
                        String desc = actionAnno.desc();

                        ami.setActionMethod(actionMethod);
                        ami.setActionName(name);
                        ami.setDesc(desc);
                        ami.setServiceObject(componentObject);

                        // parse parameters
                        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
                        String[] parameterNames = pnd.getParameterNames(actionMethod);
                        Class[] types = actionMethod.getParameterTypes();

                        String[] paramNames = new String[parameterNames.length];
                        LOG.info("Loading method . action = {},name = {}, params size = {}",
                                new Object[]{name, actionMethod.getName(), paramNames.length});

                        for (int pIndex = 0; pIndex < paramNames.length; pIndex++) {
                            String pn = parameterNames[pIndex];
                            Class pClazz = types[pIndex];
                            ParameterInfo pi = new ParameterInfo();
                            pi.setName(pn);
                            pi.setClazz(types[pIndex]);
                            ami.getParamList().add(pi);
                            if (!pClazz.equals(CommandContext.class)) {
                                List<ParameterInfo> pList = parse(pClazz);
                                ami.setInnerParamList(pList);
                            }

                            LOG.info("loadding params. name={}, type={}, pos={}", new Object[]{pn, types[pIndex], pIndex});
                        }

                        if (actionInfoMap.get(name) != null) {
                            LOG.info("action has been existed. name={}", name);
                        }
                        actionInfoMap.put(name, ami);


                    }

                    // parse response
                    Class responseClass = actionMethod.getReturnType();
                    ami.setResponse(responseClass);
                    List<ParameterInfo> responseParamList = parse(responseClass);
                    ami.setResponseParamList(responseParamList);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private List<ParameterInfo> parse(Class clazz) throws NoSuchFieldException {
        List<ParameterInfo> list = Lists.newLinkedList();
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(clazz);
        for (int pdsIndex = 0; pdsIndex < pds.length; pdsIndex++) {
            ParameterInfo innerPi = new ParameterInfo();
            PropertyDescriptor pd = pds[pdsIndex];
            if (pd.getName().toLowerCase().equals("class")) {
                continue;
            }
            innerPi.setClazz(pd.getPropertyType());

            Parameter pAnno = null;
            try {
                pAnno = clazz.getDeclaredField(pd.getName()).getAnnotation(Parameter.class);
            } catch (NoSuchFieldException e) {
                LOG.error("class = {},fileName = {}", new Object[]{clazz.getName(), pd.getName()}, e);
            }
            if (pAnno == null)
                continue;
            innerPi.setDesc(pAnno.desc());
            if (StringUtils.hasText(pAnno.name())) {
                innerPi.setName(pAnno.name());
            } else {
                innerPi.setName(pd.getName());
            }
            list.add(innerPi);

            if (!innerPi.getClazz().isPrimitive() &&
                    innerPi.getClazz().getPackage().getName().indexOf("java") < 0) {
                List<ParameterInfo> innerList = parse(innerPi.getClazz());
                innerPi.setInnerList(innerList);
            }

            // if list.
            if (innerPi.getClazz().equals(List.class)) {

                Type genType = clazz.getDeclaredField(innerPi.getName()).getGenericType();
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                Class classInList = (Class) params[0];
                List<ParameterInfo> innerList = parse(classInList);
                innerPi.setInnerList(innerList);
            }

        }
        return list;
    }

}


