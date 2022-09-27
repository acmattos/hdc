package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.domain.cqs.AscSort
import br.com.acmattos.hdc.common.context.domain.cqs.DescSort
import br.com.acmattos.hdc.common.context.domain.cqs.EmptySort
import br.com.acmattos.hdc.common.context.domain.cqs.Sort
import br.com.acmattos.hdc.common.context.domain.cqs.SortTranslator
import com.mongodb.client.model.Sorts
import org.assertj.core.api.Assertions.assertThat
import org.bson.BsonDocument
import org.bson.conversions.Bson
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val FIELD_NAME = "fieldName"

/**
 * @author ACMattos
 * @since 07/04/2022.
 */
object MdbSortTranslatorTest: Spek({
    Feature("${MdbSortTranslator::class.java.simpleName} usage") {
        Scenario("${AscSort::class.java.simpleName} usage") {
            lateinit var translator: SortTranslator<Bson>
            lateinit var sort: Sort<Bson>
            lateinit var fieldName: String
            lateinit var bson: Bson
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a ${AscSort::class.java.simpleName} sort""") {
                sort = AscSort(fieldName)
            }
            And("""a ${MdbSortTranslator::class.java.simpleName} sort translator""") {
                translator = MdbSortTranslator()
            }
            When("""sort#translate is executed""") {
                bson = sort.translate(translator)
            }
            Then("""the sort is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Sorts.ascending(fieldName))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("{\"fieldName\": 1}")
            }
        }

        Scenario("${DescSort::class.java.simpleName} usage") {
            lateinit var translator: SortTranslator<Bson>
            lateinit var sort: Sort<Bson>
            lateinit var fieldName: String
            lateinit var bson: Bson
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a ${DescSort::class.java.simpleName} sort""") {
                sort = DescSort(fieldName)
            }
            And("""a ${MdbSortTranslator::class.java.simpleName} sort translator""") {
                translator = MdbSortTranslator()
            }
            When("""sort#translate is executed""") {
                bson = sort.translate(translator)
            }
            Then("""the sort is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Sorts.descending(fieldName))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("{\"fieldName\": -1}")
            }
        }

        Scenario("${EmptySort::class.java.simpleName} usage") {
            lateinit var translator: SortTranslator<Bson>
            lateinit var sort: Sort<Any>
            lateinit var bson: Any
            Given("""a ${EmptySort::class.java.simpleName} sort""") {
                sort = EmptySort()
            }
            And("""a ${MdbSortTranslator::class.java.simpleName} sort translator""") {
                translator = MdbSortTranslator()
            }
            When("""sort#translate is executed""") {
                bson = sort.translate(translator as SortTranslator<Any>)
            }
            Then("""the sort is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Sorts.orderBy(BsonDocument()))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("Compound Sort{sorts=[{}]}")
            }
        }
    }
})
