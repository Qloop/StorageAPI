package com.fanruan.utils;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.validation.constraints.Null;
import java.util.*;

/**
 * 针对未知key的json解析
 * Created by Qloop on 2017/1/12.
 * <p>
 * {
 * "location":"数据库表名",
 * "value" : {"列名1":"值1", "列名2":"值2"}
 * }
 * <p>
 * {
 * "location":"数据库表名",
 * "values" : [{"列名1":"值1", "列名2":"值2"}]
 * }
 */
public class JsonParserUtils {

    /**
     * 根据已知的key值解析简单的key-value
     */
    public static List<String> parserWithKey(String jsonStr, String[] keyArray) {
        List<String> resultList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        for (String key : keyArray) {
            String value = jsonObject.get(key).toString();
            resultList.add(value);
        }
        return resultList;
    }

    /**
     * 不知key的json解析(不含数组)
     */
    public static Map<String, String> parserWithoutKey(String jsonStr, boolean hasArray) {
        if (!hasArray) {
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            Map<String, String> map = new HashMap<>();
            for (Iterator<?> keyIterator = jsonObject.keys(); keyIterator.hasNext(); ) {
                String key = (String) keyIterator.next();
                String value = jsonObject.get(key).toString();
                map.put(key, value);
            }
            return map;
        } else {
            return parserWithoutKey(jsonStr);
        }
    }

    /**
     * 不知key的json解析(包含数组的json)
     */
    private static Map<String, String> parserWithoutKey(String jsonStr) {
        Map<String, String> map = new LinkedHashMap<>();
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            for (Iterator<?> keyIterator = jsonObject.keys(); keyIterator.hasNext(); ) {
                String key = (String) keyIterator.next();
                String value = jsonObject.get(key).toString();
                System.out.println("key: " + key);
                map.put(key, value);
            }
        }
        return map;
    }
}