package br.com.acmattos.hdc.common.context.domain.cqs

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
interface Filter<F, V> {
//    val fieldName: String // TODO IF IT CAN BE REMOVED
//    val value: V

    fun accept(translator: FilterTranslator<F>): F
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class EqFilter<F, V>(
    /*override*/ val fieldName: String,
    /*override*/ val value: V
): Filter<F, V> {
    override fun accept(translator: FilterTranslator<F>): F =
        translator.translate(this)
}

///**
// * @author ACMattos
// * @since 25/03/2022.
// */
//data class FieldFilter<V, F>(
//    val fieldName: String,
//    val value: V,
//    val operator: String
//): Filter<F>{
//    override fun accept(translator: FilterTranslator<F>): F =
//        translator.translate(this)
//}
//
/**
 * @author ACMattos
 * @since 25/03/2022.
 */
data class AndFilter<F, V>(
//    override val fieldName: String = "NOT_USED",
//    override val value: V = Any() as V,
    val filters: List<Filter<*, *>>
): Filter<F, V>{
    override fun accept(translator: FilterTranslator<F>): F =
        translator.translate(this)
}

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
interface FilterTranslator<F> {
    fun translate(filter: Filter<F, *>): F
}