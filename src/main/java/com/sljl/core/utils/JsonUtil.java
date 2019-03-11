package com.sljl.core.utils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Json工具类 基于Jackson封装的Json高效工具类
 *
 * @author L.Y.F
 */
public class JsonUtil {

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(Inclusion.NON_NULL);
        OBJECT_MAPPER.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES, false);
        OBJECT_MAPPER.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * 将对象转换为json字符串
     *
     * @param obj
     *
     * @return
     */
    public static String toJson(Object obj) {
        String jsonstr = null;
        try {
            jsonstr = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonstr;
    }

    /**
     * 将json串转换成指定的对象 T
     *
     * @param json
     * @param objclass
     *
     * @return
     */
    public static <T> T jsonToT(String json, Class<T> objclass) {
        try {
            return OBJECT_MAPPER.readValue(json, objclass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json串转换成对象 Map
     *
     * @param json
     * @param kClass
     * @param vClass
     *
     * @return
     */
    public static <K, V> Map<K, V> jsonToMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(Map.class, kClass, vClass);
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json串转换成对象 Map
     *
     * @param json
     * @param kClass
     * @param vClass
     *
     * @return
     */
    public static <K, V> List<Map<K, V>> jsonToList_Map(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<List<Map<K, V>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json串转换成集合 List
     *
     * @param json
     * @param tClass
     *
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> tClass) {
        try {
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, tClass);
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getJsonData(String json, String fieldname) {
        JsonNode rootNode = null;
        try {
            rootNode = OBJECT_MAPPER.readTree(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rootNode == null) {
            return null;
        }
        String value = null;
        Iterator<String> names = rootNode.getFieldNames();
        while (names.hasNext()) {
            String name = names.next();
            if (!fieldname.equals(name)) {
                continue;
            }
            JsonNode node = rootNode.path(name);
            if (node.isObject() || node.isArray() || node.isPojo()) {
                value = node.toString();
            } else if (node.isNumber()) {
                value = node.getNumberValue().toString();
            } else {
                value = node.getTextValue();
            }
            break;
        }
        return value;
    }

}
