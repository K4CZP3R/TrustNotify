package xyz.k4czp3r.trustnotify.models

class Page(
    private val name: String,
    private val icon: Int,
    private val position: Int
) {

    fun getPageName(): String {
        return name
    }

    fun getPageIcon(): Int {
        return icon
    }

    fun getPagePosition(): Int {
        return position
    }

}