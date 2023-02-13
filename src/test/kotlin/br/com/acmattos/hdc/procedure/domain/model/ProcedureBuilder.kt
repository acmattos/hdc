package br.com.acmattos.hdc.procedure.domain.model

import br.com.acmattos.hdc.common.tool.server.javalin.ContextBuilder
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureCreateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureDeleteCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureDeleteEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpdateCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpdateEvent
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpsertCommand
import br.com.acmattos.hdc.procedure.domain.cqs.ProcedureUpsertEvent
import br.com.acmattos.hdc.procedure.domain.model.EventBuilder.buildCreateEvent
import br.com.acmattos.hdc.procedure.domain.model.EventBuilder.buildDeleteEvent
import br.com.acmattos.hdc.procedure.domain.model.EventBuilder.buildUpdateEvent
import br.com.acmattos.hdc.procedure.domain.model.EventBuilder.buildUpsertEvent
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.ICODE
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.IDESC
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.IPRID
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.VCODE
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.VDESC
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.VEBLD
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.VPRID
import br.com.acmattos.hdc.procedure.domain.model.ProcedureAttributes.VUDESC
import br.com.acmattos.hdc.procedure.port.rest.ProcedureCreateRequest
import br.com.acmattos.hdc.procedure.port.rest.ProcedureDeleteRequest
import br.com.acmattos.hdc.procedure.port.rest.ProcedureUpdateRequest
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 08/02/2023.
 */
object ProcedureAttributes{
    const val VPRID: String = "01GQNYQ7TY57RMXBZDKHVT7ZGJ"
    const val VCODE: Int = 81000015
    const val VDESC: String = "Procedure Description"
    const val VUDESC: String = "Upsert Procedure Description"
    const val VEBLD: Boolean = true
    const val IPRID: String = "01GQNYQ7TY57RMXBZDKHVT7ZGJA"
    const val ICODE: Int = 8
    const val IDESC: String = "In"
}

/**
 * @author ACMattos
 * @since 08/02/2023.
 */
object ProcedureContextBuilder{
    fun getContext(
        matchedPath: String = "procedure_ids",
        matchedPathValue: String = "01GQNYQ7TY57RMXBZDKHVT7ZGJ;81000015"
    ) = ContextBuilder().mockContext(matchedPath, matchedPathValue)
}

/**
 * @author ACMattos
 * @since 26/01/2023.
 */
object RequestBuilder {
    fun buildCreateRequest(
        code: Int = VCODE,
        description: String = VDESC
    ) = ProcedureCreateRequest(code = code, description = description)

    fun buildUpdateRequest(
        procedureId: String = VPRID,
        code: Int = VCODE,
        description: String = VDESC,
        enabled: Boolean = VEBLD
    ) = ProcedureUpdateRequest(
        procedureId = procedureId,
        code = code,
        description = description,
        enabled = enabled
    )

    fun buildDeleteRequest() = ProcedureDeleteRequest(ProcedureContextBuilder.getContext())
}

/**
 * @author ACMattos
 * @since 08/02/2023.
 */
object CommandBuilder {
    fun buildCreateCommand(
        code: Int = VCODE,
        description: String = VDESC
    ) = (
        RequestBuilder.buildCreateRequest(code, description).toType() as ProcedureCreateCommand
    ).copy(
        procedureId = ProcedureId("01FK96GENJKTN1BYZW6BRHFZFJ"),
        createdAt = LocalDateTime.of(2022, 3, 30, 11, 2, 0),
    )

    fun buildInvalidCreateCommand(
        code: Int = ICODE,
        description: String = IDESC
    ) = buildCreateCommand(code, description)

    fun buildUpsertCommand(
        code: Int = VCODE,
        description: String = VUDESC,
    ) = ProcedureUpsertCommand(
        buildCreateCommand(code, description),
        listOf(buildCreateEvent(), buildDeleteEvent())
    )

    fun buildUpdateCommand(
        procedureId: String = VPRID,
        code: Int = VCODE,
        description: String = VDESC,
        enabled: Boolean = VEBLD
    ) = RequestBuilder.buildUpdateRequest(
        procedureId, code, description, enabled
    ).toType() as ProcedureUpdateCommand

    fun buildInvalidUpdateCommand(
        procedureId: String = IPRID,
        code: Int = ICODE,
        description: String = IDESC
    ) = buildUpdateCommand(procedureId, code, description, VEBLD)

    fun buildDeleteCommand() = (
        RequestBuilder.buildDeleteRequest().toType() as ProcedureDeleteCommand
    ).copy(
        procedureId = ProcedureId("01FK96GENJKTN1BYZW6BRHFZFJ"),
        deletedAt = LocalDateTime.of(2023, 2, 8, 11, 2, 0),
    )
}

/**
 * @author ACMattos
 * @since 08/02/2023.
 */
object EventBuilder {
    fun buildCreateEvent(
        code: Int = VCODE,
        description: String = VDESC
    ) = ProcedureCreateEvent(
        CommandBuilder.buildCreateCommand(code, description)
    )

    fun buildUpsertEvent(
        code: Int = VCODE,
        description: String = VUDESC
    ) = ProcedureUpsertEvent(
        CommandBuilder.buildUpsertCommand(code, description)
    )

    fun buildUpdateEvent(
        code: Int = VCODE,
        description: String = VUDESC
    ) = ProcedureUpdateEvent(
        CommandBuilder.buildUpdateCommand(code = code, description = description)
    )

    fun buildDeleteEvent() = ProcedureDeleteEvent(
        CommandBuilder.buildDeleteCommand()
    )
}

/**
 * @author ACMattos
 * @since 29/03/2022.
 */
class ProcedureBuilder {
    companion object {
        fun buildCreate(
            code: Int = VCODE,
            description: String = VDESC
        ) = Procedure.apply(buildCreateEvent(code, description))

        fun buildInvalidCreate(
            code: Int = ICODE,
            description: String = IDESC
        ) = buildCreate(code, description)

        fun buildUpsert(
            code: Int = VCODE,
            description: String = VUDESC
        ) = Procedure.apply(listOf(
            buildCreateEvent(),
            buildDeleteEvent(),
            buildUpsertEvent(code, description)
        ))

        fun buildInvalidUpsert(
            code: Int = ICODE,
            description: String = IDESC
        ) = buildUpsert(code, description)

        fun buildUpdate(
            code: Int = VCODE,
            description: String = VUDESC
        ) = Procedure.apply(listOf(
            buildCreateEvent(),
            buildUpdateEvent(code, description)
        ))

        fun buildInvalidUpdate(
            code: Int = ICODE,
            description: String = IDESC
        ) = buildUpdate(code, description)

        fun buildDelete() = Procedure.apply(listOf(
            buildCreateEvent(),
            buildDeleteEvent()
        ))
    }
}
