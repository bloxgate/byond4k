package me.gmaddra.byond4k

import me.gmaddra.byond4k.extensions.get
import me.gmaddra.byond4k.extensions.unpackFloat
import me.gmaddra.byond4k.extensions.unpackShort
import java.io.DataInputStream
import java.net.URLDecoder

/**
 * Converts a topic response from BYOND into several useful formats
 * @author Gregory Maddra
 * @constructor Constructs a [ByondResponsePacket] from a [ByteArray] containing a topic response
 * @version 2017-05-30
 */
class ByondResponsePacket(response: ByteArray) {

    companion object {
        /**
         * Creates a [ByondResponsePacket] from a DataInputStream
         * @param input a DataInputStream receiving a topic response from BYOND
         * @param size Optional parameter for the size of the response in bytes. Defaults to 10kB
         * @return a [ByondResponsePacket] representing the topic response
         */
        fun fromInputStream(input: DataInputStream, size: Int = 10000): ByondResponsePacket {
            val array = ByteArray(size)
            input.read(array)
            return ByondResponsePacket(array)
        }
    }

    /**
     * The response as a String. May contain control characters
     */
    private val responseString: String = response.toString(Charsets.ISO_8859_1)

    /**
     * The size of the response
     */
    var size: Int = 0

    /**
     * If this packet is a valid BYOND packet
     */
    val validPacket = responseString[0].toString() == "\u0000" && responseString[1].toString() == "\u0083"

    /**
     * What type of data have we received
     */
    var type =
        PacketTypes.INVALID

    /**
     * The data itself
     */
    var data: Any? = null

    init {
        if (validPacket) {
            //Packet is valid. Its size will be a short packed in the next two bytes
            size = (responseString[2].toString() + responseString[3].toString()).unpackShort() - 1

            //What type of packet is this?
            val identifier = response[4].toChar()
            if (identifier == PacketTypes.FLOAT.identifier) {
                //the packet contains a packed float
                type =
                    PacketTypes.FLOAT
                data = String(responseString[5, 8]).unpackFloat()
            } else if (identifier == PacketTypes.STRING.identifier) {
                //The packet contains a string in URL format
                type =
                    PacketTypes.STRING
                //Remove control codes, null characters, etc.
                data = URLDecoder.decode(responseString.substring(5, size + 5), Charsets.ISO_8859_1.name()).replace(Regex("[^\u000a+\u0020-\u007f]+"), "")
            }

        }

    }

    override fun toString(): String {
        return data.toString()
    }

    /**
     * Converts a topic response into a map.
     * Each parameter is a key, and each parameter's value is the value.
     * Essentially the same as BYOND's param2list proc.
     * @return the data as a Map of parameters to values
     */
    fun toMap(): Map<String, String> {
        val parts = (data.toString()).split("&")
        val pairs = mutableMapOf<String, String>()
        parts
                .map { it.split("=") }
                .forEach { if (it.size > 1) pairs[it[0]] = it[1] + "\n" else pairs[it[0]] = "Error" }
        return pairs
    }


    /**
     * Converts the data in the response to a float
     * @see [isFloat]
     * @return the data as a [Float]
     */
    fun toFloat(): Float {
        return data as Float
    }

    /**
     * Is the data returned by BYOND a String?
     * @return Whether or not the data returned is a String
     */
    fun isString(): Boolean {
        return this.type == PacketTypes.STRING
    }

    /**
     * Is the data returned by BYOND a Float?
     * @return Whether or not the data returned is a float
     */
    fun isFloat(): Boolean {
        return this.type == PacketTypes.FLOAT
    }

    /**
     * Represents the identifiers a BYOND packet uses to represent its data type
     */
    enum class PacketTypes(val identifier: Char?) {
        FLOAT('\u002a'),
        STRING('\u0006'),
        INVALID(null)
    }

}