package br.com.acmattos.hdc.common.tool.oneof

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
interface MatchFirst<DECORATED, FIRST> {
    fun orTheFirst(
        firstLambda: (FIRST) -> DECORATED
    ): DECORATED
}

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
interface MatchSecond<DECORATED, FIRST, SECOND> {
    fun orTheSecond(
        secondLambda: (SECOND) -> DECORATED
    ): MatchFirst<DECORATED, FIRST>
}

/**
 * @author ACMattos
 * @since 07/01/2023.
 */
interface MatchThird<DECORATED, FIRST, SECOND, THIRD> {
    fun orTheThird(
        thirdLambda: (THIRD) -> DECORATED
    ): MatchSecond<DECORATED, FIRST, SECOND>
}
