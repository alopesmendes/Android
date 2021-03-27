package fr.uge.confroid.configurations.model

import java.io.Serializable

data class Field(
    val name: String,
    val content: String?,
    val recursiveContent: ArrayList<Field>?
) : Serializable {

    override fun toString(): String {
        return "Field(name=$name, contenu=$content, reste=$recursiveContent')"
    }

    fun toJsonFormat(): String {
        val res:StringBuilder = java.lang.StringBuilder()
        res.append(name)
        res.append("=")
        if (!content.isNullOrBlank()){
            res.append(content)
        }else if (recursiveContent != null){
            res.append("{")
            for(i in recursiveContent.indices){
                res.append(recursiveContent[i].toJsonFormat())
                if (i < recursiveContent.size-1){
                    res.append(",")
                }
            }
            res.append("}")
        }
        return res.toString()
    }
}