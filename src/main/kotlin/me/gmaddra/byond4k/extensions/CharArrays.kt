package me.gmaddra.byond4k.extensions

/**
 * CharArrays
 * @author Gregory Maddra
 * 2017-05-30
 */

/**
 * Gets a substring from index i to j
 * @param i Starting point
 * @param j End point (inclusive)
 */
operator fun String.get(i: Int, j: Int): CharArray {
    if (i >= j)
        throw IllegalArgumentException("Inputs must form a valid ascending range")
    val array = CharArray(j - i)
    for (k in i..j) {
        array[k - i] = this[k]
    }
    return array
}


