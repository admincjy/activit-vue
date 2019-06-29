package com.wl.wlp2ploansystem.infrastructures.common.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * JSON 处理工具类
 */
public class JsonHelper {

    /**
     *JSON 转换为对象
     */
    public static Object json2Object(String json, Class<?> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     *对象转换为JSON
     */
    public static String object2Json(Object obj) {
        try {
            return object2Json(obj, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     *对象转换为JSON
     * @param  indent JSON内容是否缩进
     */
    public static String object2Json(Object obj, boolean indent) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (indent) {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }
        return mapper.writeValueAsString(obj);
    }
    /**
     *JSON转换为Map
     */
    public static Map<String, Object> json2Map(String jsonStr) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonStr, Map.class);
        return map;

    }
    /**
     *Map转换为JSON
     * @param  indent JSON内容是否缩进
     */
    public static String map2Json(Map<String, Object> map, boolean indent) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (indent) {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }
        return mapper.writeValueAsString(map);

    }
    /**
     * 在属性中使用该注解：
     * eg：(yyyy-MM-dd HH:mm:ss)
     *
     * @JsonSerialize(using = JsonHelper.DatetimeSerializer.class)
     * private Date birthday;
     */
    public static class DatetimeSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator jsonGen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jsonGen.writeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        }
    }

    /**
     * 在属性中使用该注解：
     * eg：(yyyy-MM-dd HH:mm:ss)
     *
     * @JsonDeserialize(using = JsonHelper.DatetimeDeserializer.class)
     * private Date birthday;
     */
    public static class DatetimeDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jp.getText());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 在属性中使用该注解：
     * eg：(yyyy-MM-dd)
     *
     * @JsonSerialize(using = JsonHelper.DateSerializer.class)
     * private Date birthday;
     */
    public static class DateSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator jsonGen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jsonGen.writeString(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
    }

    /**
     * 在属性中使用该注解：
     * eg：(yyyy-MM-dd)
     *
     * @JsonDeserialize(using = JsonHelper.DateDeserializer.class)
     * private Date birthday;
     */
    public static class DateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(jp.getText());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
