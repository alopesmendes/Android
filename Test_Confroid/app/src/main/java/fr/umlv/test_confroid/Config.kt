package fr.umlv.test_confroid

data class Config(val id: Int, val content: String) {

    override fun toString(): String {
        return "Config(id=$id, content='$content')"
    }
}