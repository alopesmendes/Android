package fr.umlv.test_confroid

import java.io.Serializable

data class Field(
    val name: String,
    val content: String?,
    val recursiveContent: Field?
) : Serializable {

}