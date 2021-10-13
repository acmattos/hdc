package br.com.acmattos.hdc.common.tool.uid

import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

const val ULID_ID = "01EAWYQD4T72NVTW5DAWSXESPB"
const val INVALID_ULID_ID_1 = "01EAWYQD4T72NVTW5DAWSXESP"
const val INVALID_ULID_ID_2 = "8ZZZZZZZZZZZZZZZZZZZZZZZZZ"

/**
 * @author ACMattos
 * @since 12/06/2020.
 */
object ULIDGenTest: Spek({
    Feature("${ULIDGen::class.java} usage") {
        Scenario("next ULID generation") {
            lateinit var ulidGen: ULIDGen
            lateinit var ulid: String
            Given("""a successful ${ULIDGen::class.java} instantiation""") {
                ulidGen = ULIDGen()
            }
            When("""#nextULID is executed""") {
                ulid = ulidGen.nextULID()
            }
            Then("""ulid is not blank""") {
                assertThat(ulid).isNotBlank()
            }
            And("ulid length is equal to ${ULID_ID.length} (26)") {
                assertThat(ulid.length).isEqualTo(ULID_ID.length)
            }
        }

        Scenario("parse ULID successfully") {
            lateinit var ulidCandidate: String
            lateinit var ulid: String
            Given("""a valid ULID candidate""") {
                ulidCandidate = ULID_ID
            }
            When("""#parseULID is executed""") {
                ulid = ULIDGen.parseULID(ulidCandidate)
            }
            Then("""ulid is equal to ulidCandidate""") {
                assertThat(ulid).isEqualTo(ulidCandidate)
            }
        }

        Scenario("parse ULID fails - 26 chars long") {
            lateinit var ulidCandidate: String
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            val message = "[$INVALID_ULID_ID_1] does not match the expected size: 26 chars long!"

            Given("""an invalid ULID candidate""") {
                ulidCandidate = INVALID_ULID_ID_1
            }
            When("""#parseULID is executed""") {
                assertion = assertThatCode {
                    ULIDGen.parseULID(ulidCandidate)
                }
            }
            Then("""${IllegalArgumentException::class.java} is raised""") {
                assertion.hasSameClassAs(IllegalArgumentException(message))
            }
            And("""message is $message""") {
                assertion.hasMessage(message)
            }
        }

        Scenario("parse ULID fails - must not exceed '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'") {
            lateinit var ulidCandidate: String
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            val message = "[$INVALID_ULID_ID_2] must not exceed '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'!"

            Given("""an invalid ULID candidate""") {
                ulidCandidate = INVALID_ULID_ID_2
            }
            When("""#parseULID is executed""") {
                assertion = assertThatCode {
                    ULIDGen.parseULID(ulidCandidate)
                }
            }
            Then("""${IllegalArgumentException::class.java} is raised""") {
                assertion.hasSameClassAs(IllegalArgumentException(message))
            }
            And("""message is $message""") {
                assertion.hasMessage(message)
            }
        }
    }
})
