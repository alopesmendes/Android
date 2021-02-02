package fr.umlv.test_confroid.test.reflect

class Content {

    lateinit var data: Any
    lateinit var content: HashMap<String, Any>

    fun init(value: Any) {
        data = value
    }

    fun convertToBundle() {
        content = deepFlat(data, 0) as HashMap<String, Any>
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