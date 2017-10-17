package com.org.rivermanage.utils;

import com.google.gson.Gson;



public class GsonUtils {
    public static <T> T JsonToEntity(String result, Class<T> clazz){
        Gson gson = new Gson();
        return gson.fromJson(result, clazz);
    }

    public static String beanToString(Object bean){
        Gson gson = new Gson();
        return gson.toJson(bean);
    }
}
