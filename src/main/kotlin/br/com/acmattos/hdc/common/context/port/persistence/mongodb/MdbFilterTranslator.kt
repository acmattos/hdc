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
            Filters.eq(
                filter.fieldName,
                "/^${filter.value.toString().replace("*", "")}/"
            )
        } else if(filter.value.toString().matches(X_TERM)) {
            Filters.eq(
                filter.fieldName,
                "/${filter.value.toString().replace("*", "")}\$/"
            )
        } else if(filter.value.toString().matches(X_TERM_X)) {
            Filters.eq(
                filter.fieldName,
                "/.*${filter.value.toString().replace("*", "")}.*/"
            )
        } else {
           Filters.empty()
        }
//        MySQL - SELECT * FROM users  WHERE name LIKE '%m%'
//        MongoDb
//        1) db.users.find({ "name": { "$regex": "m", "$options": "i" } })
//        2) db.users.find({ "name": { $regex: new RegExp("m", 'i') } })
//        3) db.users.find({ "name": { $regex:/m/i } })
//        4) db.users.find({ "name": /mail/ })
//        5) db.users.find({ "name": /.*m.*/ })
//
//        MySQL - SELECT * FROM users  WHERE name LIKE 'm%'
//        MongoDb Any of Above with /^String/
//        6) db.users.find({ "name": /^m/ })
//
//        MySQL - SELECT * FROM users  WHERE name LIKE '%m'
//        MongoDb Any of Above with /String$/
//        7) db.users.find({ "name": /m$/ })

    private fun translate(filter: AndFilter<Bson>): Bson =
        Filters.and(
            filter
                .filters
                .map { filter ->  createTranslation(filter) }
        )

    private fun translate(filter: EmptyFilter): Bson = Filters.empty()
}