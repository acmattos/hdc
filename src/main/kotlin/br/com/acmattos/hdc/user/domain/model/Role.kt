package br.com.acmattos.hdc.user.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
import br.com.acmattos.hdc.common.context.domain.model.Id
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_CODE_FORMAT
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_DESCRIPTION
import br.com.acmattos.hdc.user.config.UserLogEnum.ROLE
import br.com.acmattos.hdc.user.config.UserLogEnum.USER
import br.com.acmattos.hdc.user.domain.cqs.RoleCreateEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleUpdateEvent
import br.com.acmattos.hdc.user.domain.cqs.RoleUpsertEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
data class Role(
    private var roleIdData: RoleId? = null,
    private var codeData: String? = null,
    private var descriptionData: String? = null,
    private var enabledData: Boolean = true,
    override var createdAtData: LocalDateTime = LocalDateTime.now(),
    override var updatedAtData: LocalDateTime? = null,
    override var deletedAtData: LocalDateTime? = null,
): AppliableEntity {
    val roleId get() = roleIdData!!
    val code get() = codeData!!
    val description get() = descriptionData!!
    val enabled get() = enabledData

    override fun apply(event: CreateEvent, validateState: Boolean) {
        roleIdData = (event as RoleCreateEvent).roleId
        codeData = event.code.uppercase()
        descriptionData = event.description
        enabledData = event.enabled
        if(validateState) {
            assertValidCode()
            assertValidDescription()
        }
        super.apply(event as CreateEvent, validateState)
    }

    override fun apply(event: UpsertEvent, validateState: Boolean) {
        codeData = (event as RoleUpsertEvent).code
        descriptionData = event.description
        enabledData = event.enabled
        if(validateState) {
            assertValidCode()
            assertValidDescription()
        }
        super.apply(event as UpsertEvent, validateState)
    }

    override fun apply(event: UpdateEvent, validateState: Boolean) {
        codeData = (event as RoleUpdateEvent).code?.uppercase() ?: codeData
        descriptionData = event.description ?: descriptionData
        enabledData = event.enabled ?: enabledData
        if(validateState) {
            assertValidCode()
            assertValidDescription()
        }
        super.apply(event as UpdateEvent, validateState)
    }

    private fun assertValidCode(){
        Assertion.assert(
            "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!",
            "${ROLE.name} $INVALID_CODE_FORMAT",
            INVALID_CODE_FORMAT
        ) {
            code.matches(Regex("[A-Z][A-Z0-9_]{2,19}"))
        }
    }

    private fun assertValidDescription(){
        Assertion.assert(
            "Invalid description: 3 <= description.length <= 255!",
            "${USER.name} $INVALID_DESCRIPTION",
            INVALID_DESCRIPTION
        ) {
            description.length in 3..255
        }
    }

    companion object: Loggable() {
        fun apply(events: List<RoleEvent>, validateState: Boolean = false): Role = Role().apply(events, validateState) as Role
        fun apply(event: RoleEvent, validateState: Boolean = true): Role = Role().apply(event, validateState) as Role
    }
}

/**
 * @author ACMattos
 * @since 04/12/2023.
 */
class RoleId: Id {
    constructor(id: String): super(id)
    constructor(): super()
}
