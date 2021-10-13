package br.com.acmattos.hdc.common.context.domain.model

import br.com.acmattos.hdc.common.tool.uid.ULIDGen

/**
 * @author ACMattos
 * @since 13/06/2020.
 */
open class Id(
    val id: String
): Entity {
    constructor(): this(nextULID())

    init {
        ULIDGen.parseULID(id)
    }

    override fun toString() = id

    companion object: ULIDGen()
}
