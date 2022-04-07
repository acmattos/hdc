package br.com.acmattos.hdc.common.context.port.persistence.mongodb

import br.com.acmattos.hdc.common.context.domain.cqs.AndFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EmptyFilter
import br.com.acmattos.hdc.common.context.domain.cqs.EqFilter
import br.com.acmattos.hdc.common.context.domain.cqs.Filter
import br.com.acmattos.hdc.common.context.domain.cqs.FilterTranslator
import br.com.acmattos.hdc.common.context.domain.cqs.RegexFilter
import com.mongodb.client.model.Filters
import org.assertj.core.api.Assertions.assertThat
import org.bson.conversions.Bson
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val FIELD_NAME = "fieldName"
private const val VALUE = 10
private const val X_VALUE_X = "*value*"
private const val X_VALUE = "*value"
private const val VALUE_X = "value*"

/**
 * @author ACMattos
 * @since 01/04/2022.
 */
object MdbFilterTranslatorTest: Spek({
    Feature("${MdbFilterTranslator::class.java.simpleName} usage") {
        Scenario("${EqFilter::class.java.simpleName} usage") {
            lateinit var translator: FilterTranslator<Bson>
            lateinit var filter: Filter<Bson>
            lateinit var fieldName: String
            lateinit var bson: Bson
            var value = 0
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $VALUE""") {
                value = VALUE
            }
            And("""a ${EqFilter::class.java.simpleName} filter""") {
                filter = EqFilter<Bson, Int>(fieldName, value)
            }
            And("""a ${MdbFilterTranslator::class.java.simpleName} filter translator""") {
                translator = MdbFilterTranslator()
            }
            When("""filter#translate is executed""") {
                bson = filter.translate(translator)
            }
            Then("""the filter is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Filters.eq(fieldName, value))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("Filter{fieldName='fieldName', value=10}")
            }
        }

        Scenario("${AndFilter::class.java.simpleName} usage") {
            lateinit var translator: FilterTranslator<Bson>
            lateinit var filter: Filter<Bson>
            lateinit var fieldName: String
            lateinit var bson: Bson
            var value = 0
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $VALUE""") {
                value = VALUE
            }
            And("""a ${EqFilter::class.java.simpleName} filter""") {
                filter = EqFilter(fieldName, value)
            }
            And("""a ${AndFilter::class.java.simpleName} filter""") {
                filter = AndFilter(listOf(filter, filter, filter))
            }
            And("""a ${MdbFilterTranslator::class.java.simpleName} filter translator""") {
                translator = MdbFilterTranslator()
            }
            When("""filter#translate is executed""") {
                bson = filter.translate(translator)
            }
            Then("""the filter is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(
                    Filters.and(
                        Filters.eq(fieldName, value),
                        Filters.eq(fieldName, value),
                        Filters.eq(fieldName, value)
                    )
                )
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo(
                    "And Filter{filters=[Filter{fieldName='fieldName', value=10}, Filter{fieldName='fieldName', value=10}, Filter{fieldName='fieldName', value=10}]}"
                )
            }
        }

        Scenario("${EmptyFilter::class.java.simpleName} usage") {
            lateinit var translator: FilterTranslator<Bson>
            lateinit var filter: Filter<Any>
            lateinit var bson: Any
            var value = 0
            Given("""a ${EmptyFilter::class.java.simpleName} filter""") {
                filter = EmptyFilter()
            }
            And("""a ${MdbFilterTranslator::class.java.simpleName} filter translator""") {
                translator = MdbFilterTranslator()
            }
            When("""filter#translate is executed""") {
                bson = filter.translate(translator as FilterTranslator<Any>)
            }
            Then("""the filter is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Filters.empty())
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("{}")
            }
        }

        Scenario("${RegexFilter::class.java.simpleName} usage: $X_VALUE_X match") {
            lateinit var translator: FilterTranslator<Bson>
            lateinit var filter: Filter<Any>
            lateinit var fieldName: String
            lateinit var bson: Any
            lateinit var value: String
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $X_VALUE_X""") {
                value = X_VALUE_X
            }
            And("""a ${RegexFilter::class.java.simpleName} filter""") {
                filter = RegexFilter(fieldName, value)
            }
            And("""a ${MdbFilterTranslator::class.java.simpleName} filter translator""") {
                translator = MdbFilterTranslator()
            }
            When("""filter#translate is executed""") {
                bson = filter.translate(translator as FilterTranslator<Any>)
            }
            Then("""the filter is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Filters.eq("fieldName", "/.*value.*/"))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("Filter{fieldName='fieldName', value=/.*value.*/}")
            }
        }

        Scenario("${RegexFilter::class.java.simpleName} usage: $X_VALUE match") {
            lateinit var translator: FilterTranslator<Bson>
            lateinit var filter: Filter<Any>
            lateinit var fieldName: String
            lateinit var bson: Any
            lateinit var value: String
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $X_VALUE""") {
                value = X_VALUE
            }
            And("""a ${RegexFilter::class.java.simpleName} filter""") {
                filter = RegexFilter(fieldName, value)
            }
            And("""a ${MdbFilterTranslator::class.java.simpleName} filter translator""") {
                translator = MdbFilterTranslator()
            }
            When("""filter#translate is executed""") {
                bson = filter.translate(translator as FilterTranslator<Any>)
            }
            Then("""the filter is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Filters.eq("fieldName", "/value\$/"))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("Filter{fieldName='fieldName', value=/value\$/}")
            }
        }

        Scenario("${RegexFilter::class.java.simpleName} usage: $VALUE_X match") {
            lateinit var translator: FilterTranslator<Bson>
            lateinit var filter: Filter<Any>
            lateinit var fieldName: String
            lateinit var bson: Any
            lateinit var value: String
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $VALUE_X""") {
                value = VALUE_X
            }
            And("""a ${RegexFilter::class.java.simpleName} filter""") {
                filter = RegexFilter(fieldName, value)
            }
            And("""a ${MdbFilterTranslator::class.java.simpleName} filter translator""") {
                translator = MdbFilterTranslator()
            }
            When("""filter#translate is executed""") {
                bson = filter.translate(translator as FilterTranslator<Any>)
            }
            Then("""the filter is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Filters.eq("fieldName", "/^value/"))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("Filter{fieldName='fieldName', value=/^value/}")
            }
        }

        Scenario("${RegexFilter::class.java.simpleName} usage: exact match") {
            lateinit var translator: FilterTranslator<Bson>
            lateinit var filter: Filter<Any>
            lateinit var fieldName: String
            lateinit var bson: Any
            lateinit var value:String
            Given("""a field named $FIELD_NAME""") {
                fieldName = FIELD_NAME
            }
            And("""a value equals to $VALUE""") {
                value = "" + VALUE
            }
            And("""a ${RegexFilter::class.java.simpleName} filter""") {
                filter = RegexFilter(fieldName, value+"")
            }
            And("""a ${MdbFilterTranslator::class.java.simpleName} filter translator""") {
                translator = MdbFilterTranslator()
            }
            When("""filter#translate is executed""") {
                bson = filter.translate(translator as FilterTranslator<Any>)
            }
            Then("""the filter is equal to a ${Bson::class.java.simpleName} equivalent""") {
                assertThat(bson).isEqualTo(Filters.eq(fieldName, "" + VALUE))
            }
            And("""the #toString representation matches the expected""") {
                assertThat(bson.toString()).isEqualTo("Filter{fieldName='fieldName', value=10}")
            }
        }
    }
})
