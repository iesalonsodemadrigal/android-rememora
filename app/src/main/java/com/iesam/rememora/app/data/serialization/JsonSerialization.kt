package com.iesam.rememora.app.data.serialization

interface JsonSerialization {
    fun <T> toJson(obj: T, typeClass: Class<T>): String
    fun <T> fromJson(json: String, typeClass: Class<T>): T
}