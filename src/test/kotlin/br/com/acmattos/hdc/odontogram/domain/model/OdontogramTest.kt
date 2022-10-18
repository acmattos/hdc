package br.com.acmattos.hdc.odontogram.domain.model

import br.com.acmattos.hdc.common.tool.assertion.AssertionFailedException
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.CODE_OUT_OF_RANGE
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SIZE_DIFFERENT_THAN_EIGHT
import br.com.acmattos.hdc.odontogram.config.MessageTrackerIdEnum.SIZE_DIFFERENT_THAN_FIVE
import org.assertj.core.api.AbstractThrowableAssert
import org.assertj.core.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val MESSAGE_1 = "Code is out of range: 11-18 | 21-28 | 31-38 | 41-48 | 51-55 | 61-65 | 71-75 | 81-85!"
private const val MESSAGE_2 = "Upper left must contain exactly 8 teeth!"
private const val MESSAGE_3 = "Tooth code [28] does not belong to the range: 11..18!"
private const val MESSAGE_4 = MESSAGE_1
private const val MESSAGE_5 = "Upper right must contain exactly 8 teeth!"
private const val MESSAGE_6 = "Tooth code [18] does not belong to the range: 21..28!"
private const val MESSAGE_7 = MESSAGE_1
private const val MESSAGE_8 = "Lower left must contain exactly 8 teeth!"
private const val MESSAGE_9 = "Tooth code [38] does not belong to the range: 41..48!"
private const val MESSAGE_10 = MESSAGE_1
private const val MESSAGE_11 = "Lower right must contain exactly 8 teeth!"
private const val MESSAGE_12 = "Tooth code [48] does not belong to the range: 31..38!"

private const val MESSAGE_13 = MESSAGE_1
private const val MESSAGE_14 = "Upper left child must contain exactly 5 teeth!"
private const val MESSAGE_15 = "Tooth code [65] does not belong to the range: 51..55!"
private const val MESSAGE_16 = MESSAGE_1
private const val MESSAGE_17 = "Upper right child must contain exactly 5 teeth!"
private const val MESSAGE_18 = "Tooth code [55] does not belong to the range: 61..65!"
private const val MESSAGE_19 = MESSAGE_1
private const val MESSAGE_20 = "Lower left child must contain exactly 5 teeth!"
private const val MESSAGE_21 = "Tooth code [75] does not belong to the range: 81..85!"
private const val MESSAGE_22 = MESSAGE_1
private const val MESSAGE_23 = "Lower right child must contain exactly 5 teeth!"
private const val MESSAGE_24 = "Tooth code [85] does not belong to the range: 71..75!"

/**
 * @author ACMattos
 * @since 29/08/2022.
 */
object OdontogramTest: Spek({
    Feature("${Odontogram::class.java} usage") {
        Scenario("a valid ${Odontogram::class.java.simpleName} creation") {
            lateinit var entity: Odontogram
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            When("a successful ${Odontogram::class.java.simpleName} instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.build()
                }
            }
            Then("no exception is raised") {
                assertion.doesNotThrowAnyException()
            }
            And("entity is not null") {
                Assertions.assertThat(entity).isNotNull()
            }
        }

        Scenario("upper left with more than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 19
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperLeftWithMoreThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_1, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_1""") {
                assertion.hasMessage(MESSAGE_1)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper left with less than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 17
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperLeftWithLessThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_2, SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId)
            }
            And("""exception has message $MESSAGE_2""") {
                assertion.hasMessage(MESSAGE_2)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper left with eight teeth but one is 28") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(28)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperLeftWithEightTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_3, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_3""") {
                assertion.hasMessage(MESSAGE_3)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper right with more than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 29
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperRightWithMoreThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_4, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_4""") {
                assertion.hasMessage(MESSAGE_4)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper right with less than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 27
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperRightWithLessThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_5, SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId)
            }
            And("""exception has message $MESSAGE_5""") {
                assertion.hasMessage(MESSAGE_5)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper right with eight teeth but one is 18") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(18)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperRightWithEightTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_6, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_6""") {
                assertion.hasMessage(MESSAGE_6)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower left with more than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 49
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerLeftWithMoreThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_7, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_7""") {
                assertion.hasMessage(MESSAGE_7)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower left with less than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 47
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerLeftWithLessThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_8, SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId)
            }
            And("""exception has message $MESSAGE_8""") {
                assertion.hasMessage(MESSAGE_8)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower left with eight teeth but one is 38") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(38)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerLeftWithEightTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_3, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_9""") {
                assertion.hasMessage(MESSAGE_9)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower right with more than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 39
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerRightWithMoreThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_10, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_10""") {
                assertion.hasMessage(MESSAGE_10)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower right with less than eight teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 37
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerRightWithLessThanEightTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_11, SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId)
            }
            And("""exception has message $MESSAGE_11""") {
                assertion.hasMessage(MESSAGE_11)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower right with eight teeth but one is 48") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(48)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerRightWithEightTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_12, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_12""") {
                assertion.hasMessage(MESSAGE_12)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper left child with more than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 56
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperLeftChildWithMoreThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_13, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_13""") {
                assertion.hasMessage(MESSAGE_13)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper left child with less than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 54
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperLeftChildWithLessThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_14, SIZE_DIFFERENT_THAN_FIVE.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_FIVE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_FIVE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_14""") {
                assertion.hasMessage(MESSAGE_14)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper left child with five teeth but one is 65") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(65)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperLeftChildWithFiveTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_15, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_15""") {
                assertion.hasMessage(MESSAGE_15)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper right child with more than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 66
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperRightChildWithMoreThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_16, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_16""") {
                assertion.hasMessage(MESSAGE_16)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper right child with less than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 64
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperRightChildWithLessThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_17, SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_FIVE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_FIVE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_17""") {
                assertion.hasMessage(MESSAGE_17)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("upper right child with five teeth but one is 55") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(55)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildUpperRightChildWithFiveTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_18, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_18""") {
                assertion.hasMessage(MESSAGE_18)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower left child with more than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 86
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerLeftChildWithMoreThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_19, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_19""") {
                assertion.hasMessage(MESSAGE_19)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower left child with less than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 84
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerLeftChildWithLessThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_20, SIZE_DIFFERENT_THAN_FIVE.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_FIVE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_FIVE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_20""") {
                assertion.hasMessage(MESSAGE_20)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower left child with five teeth but one is 75") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(75)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerLeftChildWithFiveTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_21, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_21""") {
                assertion.hasMessage(MESSAGE_21)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower right child with more than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 76
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerRightChildWithMoreThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_22, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_22""") {
                assertion.hasMessage(MESSAGE_22)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower right child with less than five teeth") {
            var invalid: Int? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = 74
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerRightChildWithLessThanFiveTeeth(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_23, SIZE_DIFFERENT_THAN_EIGHT.messageTrackerId))
            }
            And("""exception has code ${SIZE_DIFFERENT_THAN_FIVE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", SIZE_DIFFERENT_THAN_FIVE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_23""") {
                assertion.hasMessage(MESSAGE_23)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }

        Scenario("lower right child with five teeth but one is 85") {
            var invalid: Tooth? = null
            var entity: Odontogram? = null
            lateinit var assertion: AbstractThrowableAssert<*, out Throwable>
            Given("an invalid tooth code") {
                invalid = Tooth.create(85)
            }
            When("an unsuccessful odontogram instantiation is done") {
                assertion = Assertions.assertThatCode {
                    entity = OdontogramBuilder.buildLowerRightChildWithFiveTeethWrongLast(invalid!!)
                }
            }
            Then("""instantiation throws exception""") {
                assertion.hasSameClassAs(AssertionFailedException(MESSAGE_24, CODE_OUT_OF_RANGE.messageTrackerId))
            }
            And("""exception has code ${CODE_OUT_OF_RANGE.messageTrackerId}""") {
                assertion.hasFieldOrPropertyWithValue("code", CODE_OUT_OF_RANGE.messageTrackerId)
            }
            And("""exception has message $MESSAGE_24""") {
                assertion.hasMessage(MESSAGE_24)
            }
            And("""no entity was instantiated""") {
                Assertions.assertThat(entity).isNull()
            }
        }
    }
})
