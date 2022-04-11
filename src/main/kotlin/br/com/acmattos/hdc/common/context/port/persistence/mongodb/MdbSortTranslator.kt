package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.tool.page.CollectionSort
import br.com.acmattos.hdc.common.tool.page.FieldSort
import br.com.acmattos.hdc.common.tool.page.Sort
import br.com.acmattos.hdc.common.tool.page.SortOrder
import br.com.acmattos.hdc.common.tool.page.SortTranslator
import com.mongodb.client.model.Sorts
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 07/04/2022.
 */
class MdbSortTranslator: SortTranslator<Bson> {
    override fun createTranslation(sort: Sort<Bson>): Bson =
        when(sort){
            is FieldSort<Bson> -> translate(sort)
            else -> translate(sort as CollectionSort<Bson>)
        }

    private fun translate(sort: FieldSort<Bson>): Bson = if(SortOrder.ASC == sort.order) {
        Sorts.ascending(sort.fieldName)
    } else {
        Sorts.descending(sort.fieldName)
    }

    private fun translate(sort: CollectionSort<Bson>): Bson =
        Sorts.orderBy(
            sort
                .sorts
                .map { sort -> createTranslation(sort) }
        )
}
