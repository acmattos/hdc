package br.com.acmattos.hdc.common.tool.config

import java.util.Properties

/**
 * @author ACMattos
 * @since 09/06/2020.
 */
object PropHandler {
    inline fun <reified T> getProperty(key: String): T =
        getValue<T>(key)
            ?: throw IllegalArgumentException("Property '$key' is missing!")

    inline fun <reified T> getProperty(
        key: String,
        defaultValue: T
    ): T {
        return this.getValue(key) ?: defaultValue
    }

    inline fun <reified T> getValue(key: String): T? {
        val clazzName = T::class.java.simpleName
        val value = System.getProperty(key) ?: System.getenv(key)
            ?: javaClass.classLoader.getResourceAsStream("application.properties").use {
            Properties().apply { load(it) }.getProperty(key)
        }
        return if (value is String && clazzName != "String") {
            when (clazzName) {
                "Integer" -> value.toIntOrNull()
                "Float" -> value.toFloatOrNull()
                "Boolean" -> value.toBoolean()
                else -> value
            } as T?
        } else {
            value as? T?
        }
    }
}
