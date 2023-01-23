package br.com.acmattos.hdc.procedure.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.DeleteEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.DESCRIPTION_INVALID_LENGTH
import br.com.acmattos.hdc.procedure.config.MessageTrackerIdEnum.Id_OUT_OF_RANGE
import br.com.acmattos.hdc.procedure.config.ProcedureLogEnum.PROCEDURE
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpdateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpsertEvent
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
    override var createdAtData: LocalDateTime = LocalDateTime.now(),
    override var updatedAtData: LocalDateTime? = null,
    override var deletedAtData: LocalDateTime? = null,
): AppliableEntity {
    val procedureId get() = procedureIdData!!
    val code get() = codeData!!
    val description get() = descriptionData!!
    val enabled get() = enabledData
    val createdAt get() = createdAtData
    val updatedAt get() = updatedAtData
    val deletedAt get() = deletedAtData


    override fun apply(event: CreateEvent) {
        procedureIdData = (event as ProcedureCreateEvent).procedureId
        codeData = event.code
        descriptionData = event.description
        enabledData = event.enabled
        createdAtData = event.createdAt
        assertCodeIsValid()
        assertDescriptionIsValid()
        super.apply(event as CreateEvent)
    }

    override fun apply(event: UpsertEvent) {
        procedureIdData = (event as ProcedureUpsertEvent).procedureId
        codeData = event.code
        descriptionData = event.description
        enabledData = event.enabled
        updatedAtData = event.updatedAt
        deletedAtData = event.deletedAt
        assertCodeIsValid()
        assertDescriptionIsValid()
        super.apply(event as UpsertEvent)
    }

    override fun apply(event: UpdateEvent) {
        procedureIdData = (event as ProcedureUpdateEvent).procedureId
        codeData = event.code
        descriptionData = event.description
        enabledData = event.enabled
        updatedAtData = event.updatedAt
        assertCodeIsValid()
        assertDescriptionIsValid()
        super.apply(event as UpdateEvent)
    }

    override fun apply(event: DeleteEvent) {
        deletedAtData = LocalDateTime.now()
        super.apply(event)
    }

    private fun assertCodeIsValid() {
        Assertion.assert(
            "Code is out of range (81000014-87000199)!",
            PROCEDURE.name,
            Id_OUT_OF_RANGE
        ) {
            codeData!! in 81000014..87000199
        }
    }

    private fun assertDescriptionIsValid() {
        Assertion.assert(
            "Description length is out of range (3-120)!",
            PROCEDURE.name,
            DESCRIPTION_INVALID_LENGTH
        ) {
            descriptionData!!.length in 3..120
        }
    }

    companion object: Loggable() {
        fun apply(events: List<ProcedureEvent>): Procedure =
            Procedure().apply(events) as Procedure
        fun apply(event: ProcedureEvent): Procedure =
            Procedure().apply(event) as Procedure
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
