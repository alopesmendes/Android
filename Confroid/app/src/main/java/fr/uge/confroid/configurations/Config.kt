package fr.uge.confroid.configurations

import java.io.Serializable

/**
 * Dataclass that represents a config.
 * It is the main object saved in the SQL database and its content can be send to an App.
 *
 * @author Ailton Lopes Mendes
 * @author Jonathan CHU
 * @author Fabien LAMBERT--DELAVAQUERIE
 * @author Akram MALEK
 * @author Gérald LIN
 */
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