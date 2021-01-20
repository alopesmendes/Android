package fr.umlv.test_confroid

data class Config(val id: Int, val version: Int, val content: String, val tag: String) {

    override fun toString(): String {
        return "Config(id=$id, version=$version, content='$content', tag='$tag')"
    }
}