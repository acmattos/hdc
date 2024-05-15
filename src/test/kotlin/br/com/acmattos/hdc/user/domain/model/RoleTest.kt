package br.com.acmattos.hdc.user.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_CODE_FORMAT
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_DESCRIPTION
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.catchThrowableOfType

/**
 * @author ACMattos
 * @since 14/05/2024.
 */
class RoleTest: FreeSpec({
    "Feature: Role creation" - {
        "Scenario: Valid creation" {
            // Given a valid request
            // When
            var entity: Role? = null
            val assertion = assertThatCode {
                entity = RoleBuilder.buildCreate()
            }
            // Then
            assertion.doesNotThrowAnyException()
            assertThat(entity).isNotNull()
            assertThat(entity?.code).isEqualTo("CODE_12")
            assertThat(entity?.description).isEqualTo("DESCRIPTION")
            assertThat(entity?.enabled).isTrue()
        }

        "Scenario: invalid creation - code.length < 3 " {
            // Given
            val request = RoleRequest.buildWithCode("IN")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildCreate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid creation - code.length > 20 " {
            // Given
            val request = RoleRequest.buildWithCode(
                "INVALIDINVALIDINVALID")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildCreate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid creation - code !starting with A-Z " {
            // Given
            val request = RoleRequest.buildWithCode(
                "_INVALID")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildCreate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid creation - description.length < 3 " {
            // Given
            val request = RoleRequest.buildWithDescription("IN")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildCreate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid description: 3 <= description.length <= 255!")
            assertThat(exception.code)
                .isEqualTo(INVALID_DESCRIPTION.messageTrackerId)
        }

        "Scenario: invalid creation - description.length > 255 " {
            // Given
            val request = RoleRequest.buildWithDescription(
                "I".inflate(256)
            )
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildCreate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid description: 3 <= description.length <= 255!")
            assertThat(exception.code)
                .isEqualTo(INVALID_DESCRIPTION.messageTrackerId)
        }
    }

    "Feature: Role upsert" - {
        "Scenario: Valid upsert" {
            // Given a valid request
            // When
            var entity: Role? = null
            val assertion = assertThatCode {
                entity = RoleBuilder.buildUpsert()
            }
            // Then
            assertion.doesNotThrowAnyException()
            assertThat(entity).isNotNull()

        }

        "Scenario: invalid upsert - code.length < 3 " {
            // Given
            val request = RoleRequest.buildWithCode("IN")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpsert(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid upsert - code.length > 20 " {
            // Given
            val request = RoleRequest.buildWithCode(
                "INVALIDINVALIDINVALID")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpsert(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid upsert - code !starting with A-Z " {
            // Given
            val request = RoleRequest.buildWithCode(
                "_INVALID")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpsert(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid upsert - description.length < 3 " {
            // Given
            val request = RoleRequest.buildWithDescription("IN")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpsert(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid description: 3 <= description.length <= 255!")
            assertThat(exception.code)
                .isEqualTo(INVALID_DESCRIPTION.messageTrackerId)
        }

        "Scenario: invalid upsert - description.length > 255 " {
            // Given
            val request = RoleRequest.buildWithDescription(
                "I".inflate(256)
            )
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpsert(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid description: 3 <= description.length <= 255!")
            assertThat(exception.code)
                .isEqualTo(INVALID_DESCRIPTION.messageTrackerId)
        }
    }

    "Feature: Role update" - {
        "Scenario: Valid update" {
            // Given a valid request
            // When
            var entity: Role? = null
            val assertion = assertThatCode {
                entity = RoleBuilder.buildUpdate()
            }
            // Then
            assertion.doesNotThrowAnyException()
            assertThat(entity).isNotNull()
        }

        "Scenario: Valid update - new code" {
            // Given a valid request
            val request = RoleRequest.buildWithCode("NEW_CODE", true)
            // When
            var entity: Role? = null
            val assertion = assertThatCode {
                entity = RoleBuilder.buildUpdate(request)
            }
            // Then
            assertion.doesNotThrowAnyException()
            assertThat(entity).isNotNull()
            assertThat(entity?.code).isEqualTo("NEW_CODE")
            assertThat(entity?.description).isEqualTo("DESCRIPTION")
            assertThat(entity?.enabled).isTrue()
        }

        "Scenario: Valid update - new description" {
            // Given a valid request
            val request = RoleRequest.buildWithDescription("NEW_DESCRIPTION", true)
            // When
            var entity: Role? = null
            val assertion = assertThatCode {
                entity = RoleBuilder.buildUpdate(request)
            }
            // Then
            assertion.doesNotThrowAnyException()
            assertThat(entity).isNotNull()
            assertThat(entity?.code).isEqualTo("CODE_12")
            assertThat(entity?.description).isEqualTo("NEW_DESCRIPTION")
            assertThat(entity?.enabled).isTrue()
        }

        "Scenario: Valid update - new description" {
            // Given a valid request
            val request = RoleRequest.buildWithEnabled(false, true)
            // When
            var entity: Role? = null
            val assertion = assertThatCode {
                entity = RoleBuilder.buildUpdate(request)
            }
            // Then
            assertion.doesNotThrowAnyException()
            assertThat(entity).isNotNull()
            assertThat(entity?.code).isEqualTo("CODE_12")
            assertThat(entity?.description).isEqualTo("DESCRIPTION")
            assertThat(entity?.enabled).isFalse()
        }

        "Scenario: invalid update - code.length < 3 " {
            // Given
            val request = RoleRequest.buildWithCode("IN")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpdate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid update - code.length > 20 " {
            // Given
            val request = RoleRequest.buildWithCode(
                "INVALIDINVALIDINVALID")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpdate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid update - code !starting with A-Z " {
            // Given
            val request = RoleRequest.buildWithCode(
                "_INVALID")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpdate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid code format: A-Z, A-Z0-9_, 3 <= name.length <= 20!")
            assertThat(exception.code)
                .isEqualTo(INVALID_CODE_FORMAT.messageTrackerId)
        }

        "Scenario: invalid update - description.length < 3 " {
            // Given
            val request = RoleRequest.buildWithDescription("IN")
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpdate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid description: 3 <= description.length <= 255!")
            assertThat(exception.code)
                .isEqualTo(INVALID_DESCRIPTION.messageTrackerId)
        }

        "Scenario: invalid update - description.length > 255 " {
            // Given
            val request = RoleRequest.buildWithDescription(
                "I".inflate(256)
            )
            // When
            val exception: AssertionFailedException = catchThrowableOfType(
                { RoleBuilder.buildUpdate(request) },
                AssertionFailedException::class.java
            )
            // Then
            assertThat(exception).hasMessage(
                "Invalid description: 3 <= description.length <= 255!")
            assertThat(exception.code)
                .isEqualTo(INVALID_DESCRIPTION.messageTrackerId)
        }
    }

    "Feature: Role delete" - {
        "Scenario: Valid delete" {
            // Given a valid request
            // When
            var entity: Role? = null
            val assertion = assertThatCode {
                entity = RoleBuilder.buildDelete()
            }
            // Then
            assertion.doesNotThrowAnyException()
            assertThat(entity).isNotNull()
        }
    }
})

fun String.inflate(value: Int): String {
    var temp = this
    for (i in 2 .. value) {
        temp += this
    }
    return temp
}