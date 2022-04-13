package br.com.acmattos.hdc.common.tool.page

import br.com.acmattos.hdc.common.context.domain.cqs.EmptyFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter

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
        private const val FIRST_PAGE = 0

        fun create(
            filter: Filter<*> = EmptyFilter(),
            sort: Sort<*> = CollectionSort<Any>(),
            number: String? = null,
            size: Int = DEFAULT_SIZE
        ) = Page(filter, sort, number?.toInt() ?: FIRST_PAGE, size)
    }
}

/**
 * @author ACMattos
 * @since 05/04/2022.
 */
data class PageResult<R> private constructor(
    val results: List<R>,
    val page: Page,
    val total: Long
) {
    companion object {
        fun <R> create(
            results: List<R>,
            page: Page,
           total: Long
        ) = PageResult(results, page, total)
    }
}
