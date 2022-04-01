package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.domain.cqs.AndFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.domain.cqs.FilterTranslator
import com.mongodb.client.model.Filters
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 25/03/2022.
 */
class MdbFilterTranslator: FilterTranslator<Bson> {
    override fun translate(filter: Filter<Bson, *>): Bson =
        when(filter){
            is EqFilter<Bson, *> -> translate(filter)
            is AndFilter<Bson, *> -> translate(filter)
            else -> translate(filter as EqFilter<Bson, *>)
        }

    private fun translate(filter: EqFilter<Bson, *>): Bson =
        Filters.eq(filter.fieldName, filter.value)
// TODO TEST THIS METHOD
    private fun translate(filter: AndFilter<Bson, *>): Bson =
        Filters.and(
            filter
                .filters
                .map { filter ->  translate(filter as Filter<Bson, *>) }
        )
//        Filters.and(
//            Filters.eq("event.schedule_id.id", scheduleId.id), // TODO TRACK THIS FIELD
//            Filters.eq("event.date", date),// TODO TRACK THIS FIELD
//            Filters.eq("event.time", time),// TODO TRACK THIS FIELD
//        )
        //Filters.eq(filter.fieldName, filter.value)
//    }

}