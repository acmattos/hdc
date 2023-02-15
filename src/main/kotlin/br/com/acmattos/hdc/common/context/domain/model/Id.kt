package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.tool.uid.ULIDGen

/**
 * @author ACMattos
 * @since 13/06/2020.
 */
open class Id(
    val id: String
) {
    constructor(): this(nextULID())

    init {
        ULIDGen.parseULID(id)
    }

    override fun toString() = id

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (javaClass != other?.javaClass) {
            return false
        }

        other as Id
        if (id != other.id) {
            return false
        }

        return true
    }

    override fun hashCode() = id.hashCode()

    companion object: ULIDGen() {
        const val INVALID = "7ZZZZZZZZZZZZZZZZZZZZZZZZ7"
    }
}
