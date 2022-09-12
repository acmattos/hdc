package br.com.acmattos.hdc.procedure.domain.model

import br.com.acmattos.hdc.common.context.domain.model.Entity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.CODE_OUT_OF_RANGE
import br.com.acmattos.hdc.procedure.config.MessageTrackerCodeEnum.DESCRIPTION_INVALID_LENGTH
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.CreateDentalProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.UpdateDentalProcedureEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
data class Procedure(
    private var procedureIdData: ProcedureId? = null,
    private var codeData: Int? = null,
    private var descriptionData: String? = null,
    private var enabledData: Boolean = true,
    private var createdAtData: LocalDateTime = LocalDateTime.now(),
    private var updatedAtData: LocalDateTime? = null
): Entity {
    val procedureId get() = procedureIdData!!
    val code get() = codeData!!
    val description get() = descriptionData!!
    val enabled get() = enabledData!!
    val createdAt get() = createdAtData!!
    val updatedAt get() = updatedAtData

    fun apply(events: List<ProcedureEvent>): Procedure {
        for (event in events) {
            apply(event)
        }
        return this
    }

    fun apply(event: ProcedureEvent): Procedure {
        when(event) {
            is CreateDentalProcedureEvent -> apply(event)
            else -> apply(event as UpdateDentalProcedureEvent)
        }
        return this
    }

    private fun apply(event: CreateDentalProcedureEvent) {
        procedureIdData = event.procedureId
        codeData = event.code
        descriptionData = event.description
        enabledData = event.enabled
        createdAtData = event.createdAt
        assertCodeIsValid()
        assertDescriptionIsValid()
    }

    private fun apply(event: UpdateDentalProcedureEvent) {
        procedureIdData = event.procedureId
        codeData = event.code
        descriptionData = event.description
        enabledData = event.enabled
        updatedAtData = event.updatedAt
        assertCodeIsValid()
        assertDescriptionIsValid()
    }

    private fun assertCodeIsValid() {
        Assertion.assert(
            "Code is out of range (81000014-87000199)!",
            PROCEDURE.name,
            CODE_OUT_OF_RANGE.code
        ) {
            codeData!! in 81000014..87000199
        }
    }

    private fun assertDescriptionIsValid() {
        Assertion.assert(
            "Description length is out of range (3-120)!",
            PROCEDURE.name,
            DESCRIPTION_INVALID_LENGTH.code
        ) {
            descriptionData!!.length in 3..120
        }
    }

    companion object: Loggable() {
        fun apply(events: List<ProcedureEvent>): Procedure = Procedure().apply(events)
        fun apply(event: ProcedureEvent): Procedure = Procedure().apply(event)
    }
}

/**
 * @author ACMattos
 * @since 19/03/2022.
 */
class ProcedureId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
