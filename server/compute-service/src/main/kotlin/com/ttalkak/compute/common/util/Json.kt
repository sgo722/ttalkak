package com.ttalkak.compute.common.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Json {
    fun <T> serialize(instance: T): String {
        return Gson().toJson(instance)
    }

    fun <T> deserialize(json: String, clazz: Class<T>): T {
        return Gson().fromJson(json, clazz)
    }

    inline fun <reified T> deserialize(json: String): T {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson(json, type)
    }
}