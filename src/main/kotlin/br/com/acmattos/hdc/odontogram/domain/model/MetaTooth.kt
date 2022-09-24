package br.com.acmattos.hdc.odontogram.domain.model

import br.com.acmattos.hdc.common.context.domain.model.ValueObject
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.odontogram.config.MessageTrackerCodeEnum.CODE_OUT_OF_RANGE
import br.com.acmattos.hdc.odontogram.config.MetaOdontogramLogEnum.META_TOOTH
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 24/08/2022.
 */
data class MetaTooth(
    val code: String,
    val distal: String,
    val mesial: String,
    val buccal: String,
    val labial: String,
    val incisalOcclusal: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null
): ValueObject {
//    private fun apply(event: CreateMetaToothEvent) {
//        metaToothIdData = event.metaToothId
//        codeData = event.code
//        distalData = event.distal
//        mesialData = event.mesial
//        buccalData = event.buccal
//        labialData = event.labial
//        incisalOcclusalData = event.incisalOcclusal
//        createdAtData = event.createdAt
    init {
        assertCodeIsValid()
    }

    private fun assertCodeIsValid() {
        Assertion.assert(
            "Code is out of range (11-18 | 21-28 | 31-38 | 41-48 | 51-55 | 61-65 | 71-75 ! 81-85",
            META_TOOTH.name,
            CODE_OUT_OF_RANGE.code
        ) {
            code in 11..18
                || code in 21..28
                || code in 31..38
                || code in 41..48
                || code in 51..55
                || code in 61..65
                || code in 71..75
                || code in 81..85
        }
    }

//    companion object: Loggable() {
//        fun apply(events: List<MetaToothEvent>): MetaTooth = MetaTooth().apply(events)
//        fun apply(event: MetaToothEvent): MetaTooth = MetaTooth().apply(event)
//    }
}
