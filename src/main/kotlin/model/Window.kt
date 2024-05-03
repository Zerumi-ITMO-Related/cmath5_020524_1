package io.github.zerumi.model

data class Window(val window: Array<Int>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Window) return false

        if (!window.contentEquals(other.window)) return false

        return true
    }

    override fun hashCode(): Int {
        return window.contentHashCode()
    }
}
