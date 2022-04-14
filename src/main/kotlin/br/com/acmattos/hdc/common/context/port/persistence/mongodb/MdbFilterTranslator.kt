package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.domain.cqs.AndFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EmptyFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.domain.cqs.FilterTranslator
import br.com.acmattos.hdc.common.context.domain.cqs.RegexFilter
import com.mongodb.client.model.Filters
import java.util.regex.Pattern
import org.bson.conversions.Bson

private val X_TERM_X = Pattern.compile("(.*)\\w(.*)").toRegex()
private val X_TERM = Pattern.compile("(.*)\\w").toRegex()
private val TERM_X = Pattern.compile("\\w(.*)").toRegex()

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
class MdbFilterTranslator: FilterTranslator<Bson> {
    override fun createTranslation(filter: Filter<Bson>): Bson =
        when(filter){
            is EqFilter<Bson, *> -> translate(filter)
            is RegexFilter<Bson, *> -> translate(filter)
            is AndFilter<Bson> -> translate(filter)
            else -> translate(filter as EmptyFilter)
        }

    private fun translate(filter: EqFilter<Bson, *>): Bson =
        Filters.eq(filter.fieldName, filter.value)

    private fun translate(filter: RegexFilter<Bson, *>): Bson =
       if(!filter.value.toString().contains("*")) {
            Filters.eq(filter.fieldName, filter.value.toString())
       } else if(filter.value.toString().matches(TERM_X)) {
           Filters.regex(
               filter.fieldName,
               Pattern.compile(
                   "^" + "${filter.value.toString().replace("*", "")}"
                       + ".*$",
                   Pattern.CASE_INSENSITIVE
               )
            )
        } else if(filter.value.toString().matches(X_TERM)) {
           Filters.regex(
               filter.fieldName,
               Pattern.compile(
                   "^.*" + "${filter.value.toString().replace("*", "")}"
                       + "$",
                   Pattern.CASE_INSENSITIVE
               )
           )
        } else if(filter.value.toString().matches(X_TERM_X)) {
           Filters.regex(
               filter.fieldName,
               Pattern.compile(
                   "^.*" + "${filter.value.toString().replace("*", "")}"
                       + ".*$",
                   Pattern.CASE_INSENSITIVE
               )
           )
        } else {
           Filters.empty()
        }

    private fun translate(filter: AndFilter<Bson>): Bson =
        Filters.and(
            filter
                .filters
                .map { filter ->  createTranslation(filter) }
        )

    private fun translate(filter: EmptyFilter): Bson = Filters.empty()
}