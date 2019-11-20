package me.gmaddra.byond4k.extensions

import java.nio.ByteBuffer

/**
 * Packing
 * @author Gregory Maddra
 * 2017-05-25
 */

/**
 * Packs an integer into a binary string representing a short
 */
fun Int.packShort(): String {
    val buf = ByteBuffer.allocate(2)
    buf.putShort(this.toShort())
    return buf.array().toString(Charsets.ISO_8859_1)
}

/**
 * Unpacks a short into an int from a binary string
 * @return The packed short as an int
 */
fun String.unpackShort(): Int {
    if (this.length > 2) {
        throw IllegalArgumentException("Not a packed short!")
    }
    val array = this.toByteArray(Charsets.ISO_8859_1)
    val buf = ByteBuffer.wrap(array)
    return buf.short.toInt()
}

/**
 * Unpacks a float from a binary string
 * @return The packed float
 */
fun String.unpackFloat(): Float {
    if (this.length > 4) {
        throw IllegalArgumentException("Not a packed float")
    }
    val buf = ByteBuffer.wrap(this.toByteArray(Charsets.ISO_8859_1))
    return buf.float
}
