package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.tool.page.AscSort
import br.com.acmattos.hdc.common.tool.page.DescSort
import br.com.acmattos.hdc.common.tool.page.EmptySort
import br.com.acmattos.hdc.common.tool.page.Sort
import br.com.acmattos.hdc.common.tool.page.SortTranslator
import com.mongodb.client.model.Sorts
import org.bson.BsonDocument
import org.bson.conversions.Bson

/**
 * @author ACMattos
 * @since 07/04/2022.
 */
class MdbSortTranslator: SortTranslator<Bson> {
    override fun createTranslation(sort: Sort<Bson>): Bson =
        when(sort){
            is AscSort<Bson> -> translate(sort)
            is DescSort<Bson> -> translate(sort)
            else -> translate(sort as EmptySort)
//            else -> translate(sort as CollectionSort<Bson>) // TODO Verify how to do it
        }

    private fun translate(sort: AscSort<Bson>): Bson =
        Sorts.ascending(sort.fieldName)

    private fun translate(sort: DescSort<Bson>): Bson =
        Sorts.descending(sort.fieldName)

    private fun translate(sort: EmptySort<Bson>): Bson =
        Sorts.orderBy(BsonDocument())

//    private fun translate(sort: CollectionSort<Bson>): Bson =
//        Sorts.orderBy(
//            sort
//                .sorts
//                .map { sort -> createTranslation(sort) }
//        )
}
