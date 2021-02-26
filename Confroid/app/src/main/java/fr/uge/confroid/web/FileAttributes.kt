package fr.uge.confroid.web

data class FileAttributes(val name : String, val url : String) {
    override fun toString(): String {
        return name
    }
}
