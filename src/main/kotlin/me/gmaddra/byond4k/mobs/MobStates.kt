package me.gmaddra.byond4k.mobs

/**
 * For handling mob.stat data returned by a SS13 server
 * @author Gregory Maddra
 * @see me.gmaddra.byond4k.ByondResponsePacket
 * 2017-05-31
 */
enum class MobStates(val stat: Int) {
    Conscious(0),
    Unconscious(1),
    Dead(2),
    Invalid(-1);

    companion object {
        fun getByStateInt(state: Int?): MobStates? {
            if (state == null) return Invalid
            values().forEach {
                if (it.stat == state) {
                    return it
                }
            }
            return Invalid
        }
    }
}