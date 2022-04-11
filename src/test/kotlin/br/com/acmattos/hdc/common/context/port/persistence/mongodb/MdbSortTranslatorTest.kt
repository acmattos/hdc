package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.tool.page.CollectionSort
import br.com.acmattos.hdc.common.tool.page.FieldSort
import br.com.acmattos.hdc.common.tool.page.Sort
import br.com.acmattos.hdc.common.tool.page.SortOrder
import br.com.acmattos.hdc.common.tool.page.SortOrder.ASC
import br.com.acmattos.hdc.common.tool.page.SortTranslator
import com.mongodb.client.model.Sorts
import org.assertj.core.api.Assertions.assertThat
import org.bson.conversions.Bson
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val FIELD_NAME = "fieldName"
private val VALUE = ASC

/**
 * @author ACMattos
 * @since 07/04/2022.
 */
object MdbSortTranslatorTest: Spek({
    Feature("${MdbSortTranslator::class.java.simpleName} usage") {
        Scenario("${FieldSort::class.java.simpleName} usage") {
            lateinit var translator: SortTranslator<Bson>
            lateinit var sort: Sort<Bson>
            lateinit var fieldName: String
            lateinit var bson: Bson
            lateinit var value: SortOrder
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $VALUE""") {
                value = VALUE
            }
            And("""a ${FieldSort::class.java.simpleName} sort""") {
                sort = FieldSort(fieldName, value)
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
                assertThat(bson.toString())
                    .isEqualTo("{\"fieldName\": 1}")
            }
        }

        Scenario("${CollectionSort::class.java.simpleName} usage") {
            lateinit var translator: SortTranslator<Bson>
            lateinit var sort: Sort<Bson>
            lateinit var fieldName: String
            lateinit var bson: Bson
            lateinit var value: SortOrder
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $VALUE""") {
                value = VALUE
            }
            And("""a ${FieldSort::class.java.simpleName} sort""") {
                sort = FieldSort(fieldName, ASC)
            }
            And("""a ${CollectionSort::class.java.simpleName} sort""") {
                sort = CollectionSort(listOf(sort, sort, sort))
            }
            And("""a ${MdbSortTranslator::class.java.simpleName} sort translator""") {
                translator = MdbSortTranslator()
            }
            When("""sort#translate is executed""") {
                bson = sort.translate(translator)
            }
            Then("""the sort is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(
                    Sorts.orderBy(
                        listOf(
                            Sorts.ascending(fieldName),
                            Sorts.ascending(fieldName),
                            Sorts.ascending(fieldName)
                        )
                    )
                )
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo(
                    "Compound Sort{sorts=[{\"fieldName\": 1}, {\"fieldName\": 1}, {\"fieldName\": 1}]}"
                )
            }
        }



        Scenario("${CollectionSort::class.java.simpleName} usage2") {
            lateinit var translator: SortTranslator<Bson>
            lateinit var sort: Sort<Bson>
            lateinit var fieldName: String
            lateinit var bson: Bson
            lateinit var value: SortOrder
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $VALUE""") {
                value = VALUE
            }
            And("""a ${FieldSort::class.java.simpleName} sort""") {
                sort = FieldSort(fieldName, ASC)
            }
            And("""a ${CollectionSort::class.java.simpleName} sort""") {
                sort = CollectionSort()
            }
            And("""a ${MdbSortTranslator::class.java.simpleName} sort translator""") {
                translator = MdbSortTranslator()
            }
            When("""sort#translate is executed""") {
                bson = sort.translate(translator)
            }
            Then("""the sort is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(
                    Sorts.orderBy(
                        listOf(
                            Sorts.ascending(fieldName),
                            Sorts.ascending(fieldName),
                            Sorts.ascending(fieldName)
                        )
                    )
                )
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo(
                    "Compound Sort{sorts=[{\"fieldName\": 1}, {\"fieldName\": 1}, {\"fieldName\": 1}]}"
                )
            }
        }
    }
})
