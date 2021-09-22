package br.com.acmattos.hdc.common.tool.uid

import br.com.acmattos.hdc.common.tool.loggable.Loggable
import de.huxhorn.sulky.ulid.ULID
import java.security.SecureRandom

/**
 * @author ACMattos
 * @since 12/06/2020.
 */
open class ULIDGen {
    private val ulid: ULID = ULID(
        SecureRandom(
            (this.javaClass.name.substringBefore("\$Companion")
                + System.currentTimeMillis()).toByteArray()
        )
    )

    init {
        logger.trace(
            "ULIDGen created for: [{}]",
            this.javaClass.name.substringBefore("\$Companion")
        )
    }

    fun nextULID(): String = ulid.nextULID()

    companion object: Loggable() {
        fun parseULID(candidate: String):String {
            try {
                return ULID.parseULID(candidate).toString()
            } catch(ex: IllegalArgumentException) {
                if("ulidString must be exactly 26 chars long." === ex.message ) {
                    throw IllegalArgumentException(
                        "[$candidate] does not match the expected size: 26 chars long!"
                    )
                } else {
                    throw IllegalArgumentException(
                        "[$candidate] must not exceed '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'!"
                    )
                }
            }
        }
    }
}
