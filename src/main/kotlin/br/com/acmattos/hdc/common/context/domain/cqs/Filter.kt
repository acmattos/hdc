package br.com.acmattos.hdc.common.context.domain.cqs

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
interface Filter<F> {
    fun accept(translator: FilterTranslator<F>): F
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class EqFilter<F, V>(
    val fieldName: String,
    val value: V
): Filter<F> {
    override fun accept(translator: FilterTranslator<F>): F =
        translator.translate(this)
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class FieldFilter<V, F>(
    val fieldName: String,
    val value: V,
    val operator: String
): Filter<F>{
    override fun accept(translator: FilterTranslator<F>): F =
        translator.translate(this)
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class AndFilter<F>(
    val filterA: Filter<F>,
    val filterB: Filter<F>
): Filter<F> {
    override fun accept(translator: FilterTranslator<F>): F =
        translator.translate(this)
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
interface FilterTranslator<F> {
    fun translate(filter: Filter<F>): F
}