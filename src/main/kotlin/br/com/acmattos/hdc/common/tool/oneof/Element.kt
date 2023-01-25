package br.com.acmattos.hdc.common.tool.oneof

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
internal abstract class Element<DECORATED, FIRST, SECOND, THIRD>:
    MatchFirst<DECORATED, FIRST>,
    MatchSecond<DECORATED, FIRST, SECOND>,
    MatchThird<DECORATED, FIRST, SECOND, THIRD>,
    OneOfTwo<SECOND, FIRST>,
    OneOfThree<THIRD, SECOND, FIRST> {
    override fun orTheFirst(
        firstLambda: (FIRST) -> DECORATED
    ): DECORATED {
        throw IllegalStateException("No decorated returned here!")
    }

    override fun orTheSecond(
        secondLambda: (SECOND) -> DECORATED
    ): MatchFirst<DECORATED, FIRST> {
        return this
    }

    override fun orTheThird(
        thirdLambda: (THIRD) -> DECORATED
    ): MatchSecond<DECORATED, FIRST, SECOND> {
        return this
    }

    override fun <NEW_DECORATED> theSecond(
        secondLambda: (SECOND) -> NEW_DECORATED
    ): MatchFirst<NEW_DECORATED, FIRST> {
        return (this as Element<NEW_DECORATED, FIRST, SECOND, THIRD>)
            .orTheSecond(secondLambda)
    }

    override fun <NEW_DECORATED> theThird(
        thirdLambda: (THIRD) -> NEW_DECORATED
    ): MatchSecond<NEW_DECORATED, FIRST, SECOND> {
        return (this as Element<NEW_DECORATED, FIRST, SECOND, THIRD>)
            .orTheThird(thirdLambda)
    }
}

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
internal class FirstElement<DECORATED, FIRST, SECOND, THIRD>(
    private val one: FIRST
): Element<DECORATED, FIRST, SECOND, THIRD>() {
    override fun orTheFirst(
        firstLambda: (FIRST) -> DECORATED
    ): DECORATED {
        return firstLambda(one)
    }
}

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
internal class SecondElement<DECORATED, FIRST, SECOND, THIRD>(
    private val two: SECOND
): Element<DECORATED, FIRST, SECOND, THIRD>() {
    override fun orTheSecond(
        secondLambda: (SECOND) -> DECORATED
    ): MatchFirst<DECORATED, FIRST> =
        Decorator<DECORATED, FIRST, Any, Any>(secondLambda(two))
}

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
internal class ThirdElement<DECORATED, FIRST, SECOND, THIRD>(
    private val three: THIRD
): Element<DECORATED, FIRST, SECOND, THIRD>() {
    override fun orTheThird(
        thirdLambda: (THIRD) -> DECORATED
    ): MatchSecond<DECORATED, FIRST, SECOND> =
        Decorator<DECORATED,FIRST,SECOND,THIRD>(thirdLambda(three))
}
