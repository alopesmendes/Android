package fr.umlv.test_calculator.utils

import android.content.Context
import android.content.Intent
import java.util.function.Consumer

object ConfroidUtils {

    fun saveConfiguration(context: Context, name: String, value: Any, versionName: String) {
        val reflected = deepGetFields(mutableMapOf(), value)
        val content = convertToBundle(reflected)

        Intent().apply {
            action = Intent.ACTION_SEND

            putExtra("app", name)
            putExtra("version", versionName.toInt())
            putExtra("content", content)
            putExtra("tag", "TAG")

            putExtra("request", 0)

            context.startActivity(this)
        }
    }

    fun <T> loadConfiguration(
        context: Context,
        name: String,
        version: String,
        callback: Consumer<T>?
    ) {
        Intent().apply {
            action = Intent.ACTION_SEND

            putExtra("app", name)
            putExtra("version", version.toInt())

            putExtra("request", 1)

            context.startActivity(this)
        }
    }


    fun <T> subscribeConfiguration(context: Context, name: String, callback: Consumer<T>?) {
        Intent().apply {
            action = Intent.ACTION_SEND

            putExtra("app", name)

            putExtra("request", 3)

            context.startActivity(this)
        }
    }

    //
//    fun <T> cancelConfigurationSubscription(context: Context, callback: Consumer<T>) {}
//
    fun getConfigurationVersions(
        context: Context,
        name: String,
        callback: Consumer<List<Version>>?
    ) {
        Intent().apply {
            action = Intent.ACTION_SEND

            putExtra("app", name)

            putExtra("request", 2)

            context.startActivity(this)
        }
    }

    ////////////////////////////////

    fun deepGetFields(acc: MutableMap<String, Any>, obj: Any): Map<String, Any> {
        val clazz = obj::class.java
        val fields = clazz.declaredFields
        acc["class"] = clazz.name

        for (field in fields) {
            field.isAccessible = true
            val otherObj = field.get(obj)

            if (otherObj != null) {

                if (field.type.isPrimitive || field.type == String::class.java) {
                    acc[field.name] = otherObj
                } else if (field.type == Map::class.java) {
                    (otherObj as Map<*, *>).entries.forEach {
                        acc[it.key.toString()] =
                            it.value?.let { it1 -> deepGetFields(mutableMapOf(), it1) }!!
                    }
                } else {
                    acc[field.name] = deepGetFields(mutableMapOf(), otherObj)
                }
            }
        }
        return acc
    }

    /////////////////////////////////////////////

    fun convertToBundle(reflected: Any): HashMap<String, Any> {
        return deepFlat(reflected, 0) as HashMap<String, Any>
    }

    private fun deepFlat(data: Any, level: Int): Map<String, Any> {
        val res = HashMap<String, Any>()

        when (data) {
            is List<*> -> res.putAll(deepFlatArray(data as List<Any>, level))
            is Map<*, *> -> res.putAll(deepFlatMap(data as Map<Any, Any>, level))
            else -> return res
        }

        return res
    }

    private fun deepFlatArray(data: List<Any>, level: Int): Map<String, Any> {
        val res = HashMap<String, Any>()

        for (elem in data) {
            when (elem) {
                is List<*> -> res["" + level + "" + data.indexOf(elem)] =
                    deepFlatArray(elem as List<Any>, level + 1)
                is Map<*, *> -> res["" + level + "" + data.indexOf(elem)] =
                    deepFlatMap(elem as Map<Any, Any>, level + 1)
                else -> res["" + level + "" + data.indexOf(elem)] = elem.toString()
            }
        }

        return res
    }

    private fun deepFlatMap(data: Map<Any, Any>, level: Int): Map<String, Any> {
        val res = HashMap<String, Any>()

        for ((key, value) in data) {
            when (value) {
                is Map<*, *> -> res[key.toString()] =
                    deepFlatMap(value as Map<Any, Any>, level + 1)
                is List<*> -> res[key.toString()] = deepFlatArray(value as List<Any>, level + 1)
                else -> res[key.toString()] = value
            }
        }

        return res
    }
}