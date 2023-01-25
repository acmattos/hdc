package br.com.acmattos.hdc.common.tool.oneof

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
internal class Decorator<DECORATED, FIRST, SECOND, THIRD>(
    private val decorated: DECORATED
): MatchFirst<DECORATED, FIRST>,
   MatchSecond<DECORATED, FIRST, SECOND>,
   MatchThird<DECORATED, FIRST, SECOND, THIRD> {
    override fun orTheFirst(
        firstLambda: (FIRST) -> DECORATED
    ): DECORATED {
        return decorated
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
}
