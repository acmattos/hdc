package br.com.acmattos.hdc.common.tool.page

import br.com.acmattos.hdc.common.context.domain.cqs.QueryResult


/**
 * @author ACMattos
 * @since 05/04/2022.
 */
data class Page private constructor(
    val filter: Filter<*>,
    val sort: Sort<*>,
    private val number: Int,
    val size: Int
) {
    val page: Int
        get() = if(number > FIRST_PAGE){
            ( number - 1 ) * size
        } else {
            FIRST_PAGE
        }

    companion object {
        private const val DEFAULT_SIZE = 10
        internal const val FIRST_PAGE = 0

        fun create(
            filter: Filter<*> = EmptyFilter(),
            sort: Sort<*> = EmptySort(),
            number: String? = null,
            size: String? = null
        ) = Page(
            filter,
            sort,
            number?.toIntOrDefault(FIRST_PAGE) ?: FIRST_PAGE,
            size?.toIntOrDefault(DEFAULT_SIZE) ?: DEFAULT_SIZE
        )
    }
}

/**
 * @author ACMattos
 * @since 05/04/2022.
 */
data class PageResult<R> private constructor(
    override val results: List<R>,
    val page: Page,
    val total: Long
): QueryResult<R>(results) {
    companion object {
        fun <R> create(
            results: List<R>,
            page: Page,
            total: Long
        ) = PageResult(results, page, total)
    }
}

/**
 * @author ACMattos
 * @since 13/04/2022.
 */
private fun String.toIntOrDefault(default: Int): Int =
    try {
        this.toInt()
    } catch (ex: Exception) {
        default
    }
