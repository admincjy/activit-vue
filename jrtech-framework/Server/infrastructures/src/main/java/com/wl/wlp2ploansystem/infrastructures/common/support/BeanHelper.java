package com.wl.wlp2ploansystem.infrastructures.common.support;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * bean 操作工具类
 */
public class BeanHelper {
    private static Lock initLock =new ReentrantLock();

    private static Map<String,BeanCopier> beanCopierMap =new HashMap<String,BeanCopier>();

    private static ObjectMapper objectMapper = (ObjectMapper)SpringContextUtil.getBean("ObjectMapper");

    /**
     * 初始化 BeanCopier
     */
    private static BeanCopier initCopier(Class source,Class target){
        initLock.lock();
        BeanCopier find = beanCopierMap.get(source.getName()+"_"+target.getName());
        if(find!=null){
            initLock.unlock();
            return find;
        }
        BeanCopier beanCopier = BeanCopier.create(source,target,false);
        beanCopierMap.put(source.getName()+"_"+target.getName(),beanCopier);
        initLock.unlock();
        return beanCopier;
    }


    /**
     * 获取BeanCopier
     */
    private static  BeanCopier getBeanCopier(Class source,Class target){
        BeanCopier beanCopier = beanCopierMap.get(source.getClass().getName()+"_"+target.getName());
        if(beanCopier!=null){
            return beanCopier;
        }
        return initCopier(source,target);
    }


    /**
     * 类型转换（浅复制，字段名&类型相同则被复制）
     */
    public static <T> T map(Object source, Class<T> targetClass, Converter converter){
        if(source==null){
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(),targetClass);
        try {
            T target = targetClass.newInstance();
            beanCopier.copy(source,target,converter);
            return target;

        }catch (Exception e){
            throw new UserFriendlyException("对象拷贝失败"+source+"_"+targetClass);
        }
    }

    /**
     * 类型转换（浅复制，字段名&类型相同则被复制）
     */
    public static <E> List<E> map(List source, Class<E> targetClass, Converter converter){
        if(source==null){
            return null;
        }
        try {
            if(source.isEmpty()){
                return source.getClass().newInstance();
            }
            List result = source.getClass().newInstance();

            for(Object each: source){
                result.add(map(each,targetClass,converter));
            }
            return result;
        }catch (Exception e){
            throw new UserFriendlyException("对象拷贝失败"+source+"_"+targetClass);
        }
    }

    /**
     * https://blog.csdn.net/a123demi/article/details/43112547
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     * (1)转换为普通JavaBean：readValue(json,Student.class)
     * (2)转换为List,如List<Student>,将第二个参数传递为Student
     * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
     *
     * @param jsonStr
     * @param valueType
     * @return
     */
    public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef){

        try {
            return objectMapper.readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * json数组转List
     * @param jsonStr
     * @param valueTypeRef
     * @return
     */
    public static <T> T readValue(String jsonStr, Class<T> valueType) {

        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 把JavaBean转换为json字符串
     *
     * @param object
     * @return
     */
    public static String toJSon(Object object) {

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map objectToMap(Object obj) {
        if(obj == null)
            return null;

        return new org.apache.commons.beanutils.BeanMap(obj);
    }


    /**
     * 获取java bean中用Display注解标注的字段名称
     */
    public static Map<String,String> getClassDislpayMetadatas(Class onwClass) {
        Map<String,String> metadataMap =new HashMap<>();

        Field[] fields = onwClass.getDeclaredFields();
        for (Field f : fields) {
            // 过滤 static、 final、private static final字段
            if (f.getModifiers() == 16 || f.getModifiers() == 8
                    || f.getModifiers() == 26) {
                continue;
            }
            Display annotationColumn = f.getAnnotation(Display.class);
            if (annotationColumn == null) {
                metadataMap.put(f.getName(),f.getName());
            }else{
                metadataMap.put(f.getName(),annotationColumn.value());
            }

        }
        return  metadataMap;
    }
}
