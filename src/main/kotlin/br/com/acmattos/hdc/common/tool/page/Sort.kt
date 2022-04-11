package br.com.acmattos.hdc.common.tool.page

/**
 * @author ACMattos
 * @since 07/04/2022.
 */
interface Sort<TRANSLATION> {
    fun translate(translator: SortTranslator<TRANSLATION>): TRANSLATION
}

/**
 * @author ACMattos
 * @since 07/04/2022.
 */
interface SortTranslator<TRANSLATION> {
    fun createTranslation(sort: Sort<TRANSLATION>): TRANSLATION
}

/**
 * @author ACMattos
 * @since 05/04/2022.
 */
data class FieldSort<TRANSLATION>(
    val fieldName: String,
    val order: SortOrder
): Sort<TRANSLATION> {
    override fun translate(
        translator: SortTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}

/**
 * @author ACMattos
 * @since 05/04/2022.
 */
data class CollectionSort<TRANSLATION>(
    val sorts: List<Sort<TRANSLATION>> = listOf()
): Sort<TRANSLATION> {
    override fun translate(
        translator: SortTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}

/**
 * @author ACMattos
 * @since 05/04/2022.
 */
enum class SortOrder {
    ASC, DESC;
}
