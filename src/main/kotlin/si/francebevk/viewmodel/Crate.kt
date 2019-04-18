package si.francebevk.viewmodel

/**
 * A crate that can contain a single value. Used in Rocker templates because we can't declare instance variables
 * quite so simply.
 */
class Crate<T>(var value: T? = null) {
    val isEmpty get() = value == null
    val isNotEmpty get() = value != null


    /**
     * Returns true if the given value will change the current register when set.
     */
    fun willChange(other: T?): Boolean {
        if (value == null && other == null) {
            return false
        }
        else if ((value == null) xor (other == null)) {
            return true
        } else {
            return value != other
        }
    }

    /**
     * If the new value is not equal to the old: changes stored value and returns true.
     * Otherwise: returns false.
     */
    fun hasChanged(other: T?): Boolean {
        return if (willChange(other)) {
            value = other
            true
        } else {
            false
        }
    }
}