package com.ttalkak.user.common.util;

import com.nimbusds.jose.shaded.gson.Gson;

public class Json {
    private static final Gson gson = new Gson();
    public static <T> String serialize(T object) {
        return gson.toJson(object);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
