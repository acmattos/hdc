package br.com.acmattos.hdc.common.tool.page

import br.com.acmattos.hdc.common.tool.page.Order.ASC
import br.com.acmattos.hdc.common.tool.page.Order.DESC

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
interface Sort<TRANSLATION> {
    fun translate(translator: SortTranslator<TRANSLATION>): TRANSLATION
}

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
interface SortTranslator<TRANSLATION> {
    fun createTranslation(sort: Sort<TRANSLATION>): TRANSLATION
}

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
enum class Order {
    ASC, DESC, NONE;

    companion object {
        fun convert(term: String?): Order =
            if(equalsToAsc(term)) {
                ASC
            } else if(equalsToDesc(term)) {
                DESC
            } else {
                NONE
            }

        private fun equalsToAsc(term: String?) = term != null
            && term.equals("a", true)
                ||  term.equals("asc", true)
                || term == "+"

        private fun equalsToDesc(term: String?) = term != null
            && term.equals("d", true)
            ||  term.equals("desc", true)
            || term == "-"
    }
}

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
class EmptySort<TRANSLATION>: Sort<TRANSLATION> {
    override fun translate(
        translator: SortTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
open class FieldSort<TRANSLATION>(
    open val fieldName: String,
    open val value: Order,
): Sort<TRANSLATION> {
    override fun translate(
        translator: SortTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
data class AscSort<TRANSLATION>(
    override val fieldName: String,
    override val value: Order = ASC,
): FieldSort<TRANSLATION>(fieldName, ASC)

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
data class DescSort<TRANSLATION>(
    override val fieldName: String,
    override val value: Order = DESC,
): FieldSort<TRANSLATION>(fieldName, DESC)

/**
 * @author ACMattos
 * @since 24/11/2023.
 */
class SingleSortBuilder{
    companion object {
        fun build(
            fieldName: String,
            value: String?
        ): Sort<String> = when(Order.convert(value)) {
            ASC -> AscSort(fieldName)
            DESC -> DescSort(fieldName)
            Order.NONE -> EmptySort()
        }
    }
}

/**
 * @author ACMattos
 * @since 25/09/2022.
 */
data class CollectionSort<TRANSLATION>(
    val sorts: List<Sort<TRANSLATION>> = listOf()
): Sort<TRANSLATION> {
    override fun translate(
        translator: SortTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}
