package com.example.testwebservice

/***
 * The content of a request to see ...
 */
data class Content(private val data : Any) {
    lateinit var content: Map<String, Any>

    fun convertToBundle() {
        content = deepFlat(data, 0)
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
                is Map<*, *> -> res[key.toString()] = deepFlatMap(value as Map<Any, Any>, level + 1)
                is List<*> -> res[key.toString()] = deepFlatArray(value as List<Any>, level + 1)
                else -> res[key.toString()] = value
            }
        }

        return res
    }

}