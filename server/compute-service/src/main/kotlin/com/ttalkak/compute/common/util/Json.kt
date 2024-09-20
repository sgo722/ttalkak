package com.ttalkak.compute.common.util

import com.google.gson.Gson

object Json {
    fun <T> serialize(instance: T): String {
        return Gson().toJson(instance)
    }

    fun <T> deserialize(json: String, clazz: Class<T>): T {
        return Gson().fromJson(json, clazz)
    }
}