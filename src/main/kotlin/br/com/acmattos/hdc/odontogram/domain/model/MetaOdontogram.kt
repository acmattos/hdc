package br.com.acmattos.hdc.odontogram.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.odontogram.domain.cds.CreateMetaOdontogramEvent
import br.com.acmattos.hdc.odontogram.domain.cds.MetaOdontogramEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
data class MetaOdontogram(
    private var metaOdontogramIdData: MetaOdontogramId? = null,
    private var upperLeftData: MutableList<MetaTooth>? = null,
    private var upperRightData: MutableList<MetaTooth>? = null,
    private var lowerLeftData: MutableList<MetaTooth>? = null,
    private var lowerRightData: MutableList<MetaTooth>? = null,
    private var enabledData: Boolean = true,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): Entity {
    val metaOdontogramId get() = metaOdontogramIdData!!
    val upperLeft get() = upperLeftData!!
    val upperRight get() = upperRightData!!
    val lowerLeft get() = lowerLeftData!!
    val lowerRight get() = lowerRightData!!
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData

    fun apply(events: List<MetaOdontogramEvent>): MetaOdontogram {
        for (event in events) {
            apply(event)
        }
        return this
    }

    fun apply(event: MetaOdontogramEvent): MetaOdontogram {
        when(event) {
            is CreateMetaOdontogramEvent -> apply(event)
            else -> apply(event as CreateMetaOdontogramEvent)
        }
        return this
    }

    private fun apply(event: CreateMetaOdontogramEvent) {
        metaOdontogramIdData = event.metaOdontogramId
        upperLeftData = event.upperLeft
        upperRightData = event.upperRight
        lowerLeftData = event.lowerLeft
        lowerRightData = event.lowerRight
        enabledData = event.enabled
        createdAtData = event.createdAt
//        assertCodeIsValid()
//        assertDescriptionIsValid()
    }

//    private fun assertCodeIsValid() {
//        Assertion.assert(
//            "Code is out of range (81000014-87000199)!",
//            PROCEDURE.name,
//            CODE_OUT_OF_RANGE.code
//        ) {
//            codeData!! in 81000014..87000199
//        }
//    }
//
//    private fun assertDescriptionIsValid() {
//        Assertion.assert(
//            "Description length is out of range (3-120)!",
//            PROCEDURE.name,
//            DESCRIPTION_INVALID_LENGTH.code
//        ) {
//            descriptionData!!.length in 3..120
//        }
//    }

    companion object: Loggable() {
        fun apply(events: List<MetaOdontogramEvent>): MetaOdontogram = MetaOdontogram().apply(events)
        fun apply(event: MetaOdontogramEvent): MetaOdontogram = MetaOdontogram().apply(event)
    }
}

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
class MetaOdontogramId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
