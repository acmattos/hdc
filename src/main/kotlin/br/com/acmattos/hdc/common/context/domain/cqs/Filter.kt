package br.com.acmattos.hdc.common.context.domain.cqs

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
interface Filter<TRANSLATION> {
    fun translate(translator: FilterTranslator<TRANSLATION>): TRANSLATION
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
interface FilterTranslator<TRANSLATION> {
    fun createTranslation(filter: Filter<TRANSLATION>): TRANSLATION
}

/**
 * @author ACMattos
 * @since 01/04/2022.
 */
class EmptyFilter: Filter<Any> {
    override fun translate(
        translator: FilterTranslator<Any>
    ): Any = translator.createTranslation(this)
}

/**
 * @author ACMattos
 * @since 01/04/2022.
 */
open class FieldFilter<TRANSLATION, TYPE>(
    open val fieldName: String,
    open val value: TYPE
): Filter<TRANSLATION> {
    override fun translate(
        translator: FilterTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}

/**
 * @author ACMattos
 * @since 01/04/2022.
 */
open class CollectionFilter<TRANSLATION>(
    open val filters: List<Filter<TRANSLATION>>
): Filter<TRANSLATION> {
    override fun translate(
        translator: FilterTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class EqFilter<TRANSLATION, TYPE>(
    override val fieldName: String,
    override val value: TYPE
): FieldFilter<TRANSLATION, TYPE>(fieldName, value)

/**
 * @author ACMattos
 * @since 05/04/2022.
 */
data class RegexFilter<TRANSLATION, TYPE>(
    override val fieldName: String,
    override val value: TYPE
): FieldFilter<TRANSLATION, TYPE>(fieldName, value)

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class AndFilter<TRANSLATION>(
    override val filters: List<Filter<TRANSLATION>>
): CollectionFilter<TRANSLATION>(filters)
