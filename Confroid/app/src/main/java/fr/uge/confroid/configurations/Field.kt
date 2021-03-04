package fr.uge.confroid.configurations

import java.io.Serializable

data class Field(
    val name: String,
    val content: String?,
    val recursiveContent: List<Field>?
) : Serializable {

    override fun toString(): String {
        return "Field(name=$name, contenu=$content, reste=$recursiveContent')"
    }
}