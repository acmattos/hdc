package br.com.acmattos.hdc.user.domain.model

import br.com.acmattos.hdc.common.context.domain.cqs.CreateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpdateEvent
import br.com.acmattos.hdc.common.context.domain.cqs.UpsertEvent
import br.com.acmattos.hdc.common.context.domain.model.AppliableEntity
import br.com.acmattos.hdc.common.tool.assertion.Assertion
import br.com.acmattos.hdc.common.tool.loggable.Loggable
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_EMAIL
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_NAME
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_USERNAME
import br.com.acmattos.hdc.user.config.UserLogEnum.USER
import br.com.acmattos.hdc.user.domain.cqs.UserCreateEvent
import br.com.acmattos.hdc.user.domain.cqs.UserEvent
import br.com.acmattos.hdc.user.domain.cqs.UserUpdateEvent
import br.com.acmattos.hdc.user.domain.cqs.UserUpsertEvent
import java.time.LocalDateTime

/**
 * @author ACMattos
 * @since 26/03/2023.
 */
data class User(
    private var userIdData: RoleId? = null,
    private var nameData: String? = null,
    private var usernameData: String? = null,
    private var passwordData: String? = null,
    private var emailData: String? = null,
    private var saltData: String? = null,
    private var rolesData: Set<Role>? = null,
    private var lastLoginAtData: LocalDateTime? = null,
    private var enabledData: Boolean = true,
    override var createdAtData: LocalDateTime = LocalDateTime.now(),
    override var updatedAtData: LocalDateTime? = null,
    override var deletedAtData: LocalDateTime? = null,
): AppliableEntity {
    val userId get() = userIdData!!
    val name get() = nameData!!
    val username get() = usernameData!!
    val password get() = passwordData!!
    val salt get() = saltData!!
    val email get() = emailData!!
    val roles get() = rolesData
    val lastLoginAt get() = lastLoginAtData
    val enabled get() = enabledData

    override fun apply(event: CreateEvent, validateState: Boolean) {
        userIdData = (event as UserCreateEvent).userId
        nameData = event.name
        usernameData = event.username
        passwordData = event.password
        saltData = event.salt
        emailData = event.email
        rolesData = event.roles
        enabledData = event.enabled
        //validate()
        assertValidName()
        assertValidUsername()
//        assertValidPassword()
        assertValidEmail()
        super.apply(event as CreateEvent, validateState)
    }

    override fun apply(event: UpsertEvent, validateState: Boolean) {
        nameData = (event as UserUpsertEvent).name
        usernameData = event.username
        passwordData = event.password
        saltData = event.salt
        emailData = event.email
        rolesData = event.roles
        enabledData = event.enabled
        //validate()
        super.apply(event as UpsertEvent, validateState)
    }

    override fun apply(event: UpdateEvent, validateState: Boolean) {
        nameData = (event as UserUpdateEvent).name ?: nameData
        usernameData = event.username ?: usernameData
        passwordData = event.password ?: passwordData
        saltData = event.salt ?: saltData
        emailData = event.email ?: emailData
        rolesData = event.roles ?: rolesData
        enabledData = event.enabled ?: enabledData
        assertValidName()
        assertValidUsername()
//        assertValidPassword()
        assertValidEmail()
        super.apply(event as UpdateEvent, validateState)


//        userIdData = (event as UserCreateEvent).userId
//        nameData = event.name
//        usernameData = event.username
//        passwordData = event.password
//        saltData = event.salt
//        emailData = event.email
////        rolesData = event.roles
//        enabledData = event.enabled
    }

    private fun assertValidName() {
        Assertion.assert(
            "Invalid name: 3 <= name.length <= 100!",
            "${USER.name} $INVALID_NAME",
            INVALID_NAME
        ) {
            name.length in 3..100
        }
    }

    private fun assertValidUsername() {
        Assertion.assert(
            "Invalid username: 3 <= username.length <= 30!",
            "${USER.name} $INVALID_USERNAME",
            INVALID_USERNAME
        ) {
            username.length in 3..30
        }
        Assertion.assert(
            "Invalid username: no white space is allowed!",
            "${USER.name} $INVALID_USERNAME",
            INVALID_USERNAME
        ) {
            username.split(" ").size == 1
        }
    }
//    private fun assertValidPassword() {
//        Assertion.assert(
//            "Invalid full name: 3 <= fullName.length <= 100!",
//            "${USER.name} $INVALID_PERSON_FULL_NAME",
//            INVALID_PERSON_FULL_NAME
//        ) {
//            fullName.length in 5..255
//        }
//    }
//    val password get() = passwordData!!
//    val salt get() = saltData!!

    private fun assertValidEmail() {
        Assertion.assert(
            "Invalid email: 6 <= email.length <= 50!",
            "${USER.name} $INVALID_EMAIL",
            INVALID_EMAIL
        ) {
            email.length in 6..50
        }
        //TODO REGEX
    }

//    //    val roles get() = rolesData!!

//    val enabled get() = enabledData
//    private fun assertLastLoginAt() {
//        Assertion.assert(
//            "Invalid full name: 3 <= lastLoginAt.length <= 100!",
//            "${USER.name} $INVALID_PERSON_FULL_NAME",
//            INVALID_PERSON_FULL_NAME
//        ) {
//            lastLoginAt.length in 3..100
//        }
//    }


//    private fun validateUpperLeft() {
//        validateUpperLowerSize("Upper left", upperLeftData!!, 8)
//        validateUpperLowerRange(upperLeftData!!, 11 .. 18)
//    }
//
//    private fun validateUpperLowerSize(
//        context: String,
//        teeth: List<Tooth>,
//        size: Int
//    ) {
//        val messageTracker: MessageTracker = if (size == 8) {
//            SIZE_DIFFERENT_THAN_EIGHT
//        } else {
//            SIZE_DIFFERENT_THAN_FIVE
//        }
//        Assertion.assert(
//            "$context must contain exactly $size teeth!",
//            ODONTOGRAM.name,
//            messageTracker
//        ) {
//            teeth.size == size
//        }
//    }
//
//    private fun validateUpperLowerRange(
//        teeth: List<Tooth>,
//        range: IntRange
//    ) {
//        teeth.stream().forEach { tooth ->
//            Assertion.assert(
//                "Tooth code [${tooth.code}] does not belong to the range: $range!",
//                ODONTOGRAM.name,
//                CODE_OUT_OF_RANGE
//            ) {
//                tooth.code in range
//            }
//        }
//    }

    companion object: Loggable() {
        fun apply(events: List<UserEvent>, validateState: Boolean = false): User =
            User().apply(events, validateState) as User
        fun apply(event: UserEvent, validateState: Boolean = true): User =
            User().apply(event, validateState) as User
    }

}

///**
// * @author ACMattos
// * @since 27/03/2023.
// */
//class RoleId: Id {
//    constructor(id: String): super(id)
//    constructor(): super()
//}
