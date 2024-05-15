package br.com.acmattos.hdc.user.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_CODE_FORMAT
import br.com.acmattos.hdc.user.config.MessageTrackerIdEnum.INVALID_DESCRIPTION
import br.com.acmattos.hdc.user.domain.cqs.RoleBuilder
import br.com.acmattos.hdc.user.domain.cqs.RoleRequest
import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.*

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

        }

        "Scenario: invalid creation - code.length < 3 " {
            // Given
            val request = RoleRequest.buildWithInvalidCode("IN")
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
            val request = RoleRequest.buildWithInvalidCode(
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
            val request = RoleRequest.buildWithInvalidCode(
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
            val request = RoleRequest.buildWithInvalidDescription("IN")
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
            val request = RoleRequest.buildWithInvalidDescription(
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
            val request = RoleRequest.buildWithInvalidCode("IN")
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
            val request = RoleRequest.buildWithInvalidCode(
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
            val request = RoleRequest.buildWithInvalidCode(
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
            val request = RoleRequest.buildWithInvalidDescription("IN")
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
            val request = RoleRequest.buildWithInvalidDescription(
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

        "Scenario: invalid update - code.length < 3 " {
            // Given
            val request = RoleRequest.buildWithInvalidCode("IN")
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
            val request = RoleRequest.buildWithInvalidCode(
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
            val request = RoleRequest.buildWithInvalidCode(
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
            val request = RoleRequest.buildWithInvalidDescription("IN")
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
            val request = RoleRequest.buildWithInvalidDescription(
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