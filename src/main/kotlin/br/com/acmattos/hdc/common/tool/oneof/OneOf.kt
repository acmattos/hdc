package br.com.acmattos.hdc.common.tool.oneof

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
interface OneOfThree<FIRST, SECOND, THIRD> {
    fun <DECORATED> theThird(
        thirdLambda: (FIRST) -> DECORATED
    ): MatchSecond<DECORATED, THIRD, SECOND>

    companion object {
        @JvmStatic
        fun <FIRST, SECOND, THIRD> first(
            first: FIRST
        ): OneOfThree<FIRST, SECOND, THIRD> {
            return ThirdElement<Any, THIRD, SECOND, FIRST>(first)
        }

        @JvmStatic
        fun <FIRST, SECOND, THIRD> second(
            second: SECOND
        ): OneOfThree<FIRST, SECOND, THIRD> {
            return SecondElement<Any, THIRD, SECOND, FIRST>(second)
        }

        @JvmStatic
        fun <FIRST, SECOND, THIRD> third(
            third: THIRD
        ): OneOfThree<FIRST, SECOND, THIRD> {
            return FirstElement<Any, THIRD, SECOND, FIRST>(third)
        }
    }
}

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
interface OneOfTwo<FIRST, SECOND> {
    fun <DECORATED> theSecond(
        secondLambda: (FIRST) -> DECORATED
    ): MatchFirst<DECORATED, SECOND>

    companion object {
        @JvmStatic
        fun <FIRST, SECOND> first(first: FIRST): OneOfTwo<FIRST, SECOND> {
            return SecondElement<Any, SECOND, FIRST, Any>(first)
        }

        @JvmStatic
        fun <FIRST, SECOND> second(second: SECOND): OneOfTwo<FIRST, SECOND> {
            return FirstElement<Any, SECOND, FIRST, Any>(second)
        }
    }
}
