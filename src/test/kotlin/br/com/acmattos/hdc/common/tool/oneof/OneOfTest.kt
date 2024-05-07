package br.com.acmattos.hdc.common.tool.oneof

import io.kotest.core.spec.style.FreeSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail

/**
 * @author ACMattos
 * @since 23/03/2023.
 */
class OneOfTest: FreeSpec({
    "Feature: ${OneOfThree::class.java.simpleName} usage - Holders of different T types" - {
        "Scenario: handling ${OneOfThree::class.java.simpleName}#first - different T" - {
            lateinit var oneOfThree: OneOfThree<Holder<String>, Holder<Int>, Holder<Float>>
            lateinit var one: Any
            "Given: a ${OneOfThree::class.java.simpleName}#first - different T" {
                oneOfThree = OneOfThreeBuilder.first()
            }
            "When: gfsfdgdfr#theThird is executed - different T" {
                one = oneOfThree
                    .theThird { string -> string.value() }
                    .orTheSecond { fail("Should not be an int but a string") }
                    .orTheFirst { fail("Should not be a float but a string") }
            }
            "Then: the value is FIRST - different T"  {
                assertThat(one).isEqualTo("FIRST")
            }
        }

        "Scenario: handling ${OneOfThree::class.java.simpleName}#second - different T" - {
            lateinit var oneOfThree: OneOfThree<Holder<String>, Holder<Int>, Holder<Float>>
            lateinit var one: Any
            "Given: a ${OneOfThree::class.java.simpleName}#second - different T"  {
                oneOfThree = OneOfThreeBuilder.second()
            }
            "When: #orTheSecond is executed - different T"  {
                one = oneOfThree
                    .theThird<Int> { fail("shouldn't match string, expecting int") }
                    .orTheSecond { int -> int.value() }
                    .orTheFirst { fail("shouldn't match float, expecting int") }
            }
            "Then: the value is 2 - different T"  {
                assertThat(one).isEqualTo(2)
            }
        }

        "Scenario: handling ${OneOfThree::class.java.simpleName}#third - different T" - {
            lateinit var oneOfThree: OneOfThree<Holder<String>, Holder<Int>, Holder<Float>>
            lateinit var one: Any
            "Given: a ${OneOfThree::class.java.simpleName}#third - different T"  {
                oneOfThree = OneOfThreeBuilder.third()
            }
            "When: #orTheFirst is executed - different T"  {
                one = oneOfThree
                    .theThird<Float>{ fail("shouldn't match string, expecting float") }
                    .orTheSecond { fail("shouldn't match int, expecting float") }
                    .orTheFirst { float -> float.value() }
            }
            "Then: the value is 3.0f - different T"  {
                assertThat(one).isEqualTo(3.0f)
            }
        }
    }

    "Feature: ${OneOfThree::class.java.simpleName} usage - Holders of the same T type" - {
        "Scenario: handling ${OneOfThree::class.java.simpleName}#first - same T" - {
            lateinit var oneOfThree: OneOfThree<Holder<String>, Holder<String>, Holder<String>>
            lateinit var one: String
            "Given: a ${OneOfThree::class.java.simpleName}#first - same T"  {
                oneOfThree = OneOfThreeBuilder.firstSameT()
            }
            "When: #theThird is executed - same T"  {
                one = oneOfThree
                    .theThird(Holder<String>::value)
                    .orTheSecond(Holder<String>::value)
                    .orTheFirst(Holder<String>::value)
            }
            "Then: the value is FIRST - same T"  {
                assertThat(one).isEqualTo("FIRST")
            }
        }

        "Scenario: handling ${OneOfThree::class.java.simpleName}#second - same T" - {
            lateinit var oneOfThree: OneOfThree<Holder<String>, Holder<String>, Holder<String>>
            lateinit var one: String
            "Given: a ${OneOfThree::class.java.simpleName}#second - same T"  {
                oneOfThree = OneOfThreeBuilder.secondSameT()
            }
            "When: #orTheSecond is executed - same T"  {
                one = oneOfThree
                    .theThird(Holder<String>::value)
                    .orTheSecond(Holder<String>::value)
                    .orTheFirst(Holder<String>::value)
            }
            "Then: the value is SECOND - same T"  {
                assertThat(one).isEqualTo("SECOND")
            }
        }

        "Scenario: handling ${OneOfThree::class.java.simpleName}#third - same T" - {
            lateinit var oneOfThree: OneOfThree<Holder<String>, Holder<String>, Holder<String>>
            lateinit var one: String
            "Given: a ${OneOfThree::class.java.simpleName}#third - same T"  {
                oneOfThree = OneOfThreeBuilder.thirdSameT()
            }
            "When: #orTheFirst is executed - same T"  {
                one = oneOfThree
                    .theThird(Holder<String>::value)
                    .orTheSecond(Holder<String>::value)
                    .orTheFirst(Holder<String>::value)
            }
            "Then: the value is THIRD - same T"  {
                assertThat(one).isEqualTo("THIRD")
            }
        }
    }

    "Feature: ${OneOfTwo::class.java.simpleName} usage - Holders of different T types" - {
        "Scenario: handling ${OneOfTwo::class.java.simpleName}#first - different T" - {
            lateinit var oneOfTwo: OneOfTwo<Holder<String>, Holder<Int>>
            lateinit var one: Any
            "Given: a ${OneOfTwo::class.java.simpleName}#first - different T"  {
                oneOfTwo = OneOfTwoBuilder.first()
            }
            "When: #theSecond is executed - different T"  {
                one = oneOfTwo
                    .theSecond { string -> string.value() }
                    .orTheFirst { fail("Should not be an int but a string") }
            }
            "Then: the value is FIRST - different T"  {
                assertThat(one).isEqualTo("FIRST")
            }
        }

        "Scenario: handling ${OneOfTwo::class.java.simpleName}#second - different T" - {
            lateinit var oneOfTwo: OneOfTwo<Holder<String>, Holder<Int>>
            lateinit var one: Any
            "Given: a ${OneOfTwo::class.java.simpleName}#second - different T"  {
                oneOfTwo = OneOfTwoBuilder.second()
            }
            "When: #orTheFirst is executed - different T"  {
                one = oneOfTwo
                    .theSecond<Int> { fail("shouldn't match string, expecting int") }
                    .orTheFirst { int -> int.value() }
            }
            "Then: the value is 2 - different T"  {
                assertThat(one).isEqualTo(2)
            }
        }
    }

    "Feature: ${OneOfTwo::class.java.simpleName} usage - Holders of the same T type" - {
        "Scenario: handling ${OneOfTwo::class.java.simpleName}#first - same T" - {
            lateinit var oneOfTwo: OneOfTwo<Holder<String>, Holder<String>>
            lateinit var one: String
            "Given: a ${OneOfTwo::class.java.simpleName}#first - same T"  {
                oneOfTwo = OneOfTwoBuilder.firstSameT()
            }
            "When: #orTheFirst is executed - same T"  {
                one = oneOfTwo
                    .theSecond(Holder<String>::value)
                    .orTheFirst(Holder<String>::value)
            }
            "Then: the value is FIRST - same T"  {
                assertThat(one).isEqualTo("FIRST")
            }
        }

        "Scenario: handling ${OneOfTwo::class.java.simpleName}#second - same T" - {
            lateinit var oneOfTwo: OneOfTwo<Holder<String>, Holder<String>>
            lateinit var one: String
            "Given: a ${OneOfTwo::class.java.simpleName}#second - same T"  {
                oneOfTwo = OneOfTwoBuilder.secondSameT()
            }
            "When: #orTheSecond is executed - same T"  {
                one = oneOfTwo
                    .theSecond(Holder<String>::value)
                    .orTheFirst(Holder<String>::value)
            }
            "Then: the value is SECOND - same T"  {
                assertThat(one).isEqualTo("SECOND")
            }
        }
    }
})

class Holder<T>(private val v: T) {
    fun value() = v
}
object OneOfThreeBuilder {
    fun first() = OneOfThree
        .first<Holder<String>, Holder<Int>, Holder<Float>>(Holder("FIRST"))

    fun firstSameT() = OneOfThree
        .first<Holder<String>, Holder<String>, Holder<String>>(Holder("FIRST"))

    fun second() = OneOfThree
        .second<Holder<String>, Holder<Int>, Holder<Float>>(Holder(2))

    fun secondSameT() = OneOfThree
        .second<Holder<String>, Holder<String>, Holder<String>>(Holder("SECOND"))

    fun third() = OneOfThree
        .third<Holder<String>, Holder<Int>, Holder<Float>>(Holder(3F))

    fun thirdSameT() = OneOfThree
        .third<Holder<String>, Holder<String>, Holder<String>>(Holder("THIRD"))
}
object OneOfTwoBuilder {
    fun first() = OneOfTwo
        .first<Holder<String>, Holder<Int>>(Holder("FIRST"))

    fun firstSameT() = OneOfTwo
        .first<Holder<String>, Holder<String>>(Holder("FIRST"))

    fun second() = OneOfTwo
        .second<Holder<String>, Holder<Int>>(Holder(2))

    fun secondSameT() = OneOfTwo
        .second<Holder<String>, Holder<String>>(Holder("SECOND"))
}
