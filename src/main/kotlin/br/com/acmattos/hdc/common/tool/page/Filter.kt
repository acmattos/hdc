package br.com.acmattos.hdc.common.tool.page

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
class EmptyFilter<T>: Filter<T> {
    override fun translate(
        translator: FilterTranslator<T>
    ): T = translator.createTranslation(this)
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
 * @since 24/11/2023.
 */
class EqFilterBuilder{
    companion object {
        fun build(
            fieldName: String,
            value: String?
        ): EqFilter<String, String>? =
            if(!value.isNullOrEmpty()) {
                EqFilter(fieldName, value)
            } else {
                null
            }
    }
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class EqNullFilter<TRANSLATION>(
    val fieldName: String
): Filter<TRANSLATION> {
    override fun translate(
        translator: FilterTranslator<TRANSLATION>
    ): TRANSLATION = translator.createTranslation(this)
}

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
 * @since 24/11/2023.
 */
class RegexFilterBuilder{
    companion object {
        fun build(
            fieldName: String,
            value: String?
        ): RegexFilter<String, String>? =
            if (!value.isNullOrEmpty()) {
                RegexFilter(fieldName, value)
            } else {
                null
            }
    }
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class AndFilter<TRANSLATION>(
    override val filters: List<Filter<TRANSLATION>>
): CollectionFilter<TRANSLATION>(filters)

/**
 * @author ACMattos
 * @since 24/11/2023.
 */
class AndFilterBuilder{
    companion object {
        fun <T> build(vararg clauses: Filter<T>?): Filter<T> =
            mutableListOf<Filter<T>>().run {
                clauses.filterNotNull().forEach { filter: Filter<T> ->
                    this.add(filter)
                }
                return if (this.size >= 2) {
                    AndFilter(this)
                } else if (this.size == 1) {
                    this[0]
                } else {
                    EmptyFilter()
                }
            }
    }
}

/**
 * @author ACMattos
 * @since 26/05/2022.
 */
data class OrFilter<TRANSLATION>(
    override val filters: List<Filter<TRANSLATION>>
): CollectionFilter<TRANSLATION>(filters)

/**
 * @author ACMattos
 * @since 24/11/2023.
 */
class OrFilterBuilder{
    companion object {
        fun build(
            fieldName: String,
            csv: String
        ): OrFilter<String> = OrFilter(
            csv.split(",")
               .map {
                   EqFilter(fieldName, it)
               }
        )
    }
}