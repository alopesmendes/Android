package fr.uge.confroid.configurations

import java.io.Serializable

data class Config(
    val app: String,
    val id: Int,
    val version: Int,
    val content: String,
    val tag: String
) : Serializable {

    override fun toString(): String {
        return "Config(app=$app, id=$id, version=$version, content='$content', tag='$tag')"
    }
}