package me.gmaddra.byond4k

import me.gmaddra.byond4k.extensions.packShort
import java.io.IOException
import java.io.OutputStream

/**
 * Represents a packet in BYOND's topic format
 * @author Gregory Maddra
 * @param message The topic to send. Should be formatted as a URL
 * @see [java.net.URLEncoder]
 * @see [ByondResponsePacket]
 * @constructor Creates a packet with the given message
 * 2017-05-30
 */
class ByondPacket(val message: String) {

    companion object {
        const val header = "\u0000\u0083"
        val padding = "\u0000".repeat(5) //Functions can't be used in consts
        const val terminator = "\u0000"
    }

    val length = (message.length + 6).packShort()
    val packet: String

    init {
        packet = header + length + padding + message + terminator
    }

    /**
     * @return The packet as a ISO 8859-1 formatted [ByteArray], ready for sending over the network
     */
    fun toByteArray(): ByteArray {
        return packet.toByteArray(Charsets.ISO_8859_1)
    }

    /**
     * Writes the packet to the given [OutputStream]
     * @param sock An [OutputStream]. Will not be closed in this function
     * @return Whether or not the write succeeded
     */
    fun sendPacket(sock: OutputStream): Boolean {
        return try {
            sock.write(this.toByteArray())
            true
        } catch (ex: IOException) {
            println("Error writing packet")
            ex.printStackTrace()
            false
        }
    }

}