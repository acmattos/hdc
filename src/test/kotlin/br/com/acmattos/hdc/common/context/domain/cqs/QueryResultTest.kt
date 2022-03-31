package br.com.acmattos.hdc.common.context.domain.cqs

import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

private const val RESULTS = "Result"

/**
 *
 * @author ACMattos
 * @since 31/03/2022.
 */
object QueryResultTest: Spek({
    Feature("${QueryResult::class.java} usage") {
        Scenario("adding an empty list") {
            lateinit var result: List<String>
            lateinit var queryResult: QueryResult<String>
            Given("""an empty list result""") {
                result = listOf()
            }
            When("""#build is executed""") {
                queryResult = QueryResult.build(result)
            }
            Then("""the queryResult.results#size is equal to 0""") {
                assertThat(queryResult.results.size).isEqualTo(0)
            }
        }

        Scenario("adding an optional result") {
            lateinit var result: Optional<String>
            lateinit var queryResult: QueryResult<String>
            Given("""an optional result""") {
                result = Optional.of(RESULTS)
            }
            When("""#build is executed""") {
                queryResult = QueryResult.build(result)
            }
            Then("""the queryResult.results#size is equal to 1""") {
                assertThat(queryResult.results.size).isEqualTo(1)
            }
            And("the queryResult.results[0] is equal to $RESULTS") {
                assertThat(queryResult.results[0]).isEqualTo(RESULTS)
            }
        }

        Scenario("adding an optional#empty result") {
            lateinit var result: Optional<String>
            lateinit var queryResult: QueryResult<String>
            Given("""an optional#empty result""") {
                result = Optional.empty()
            }
            When("""#build is executed""") {
                queryResult = QueryResult.build(result)
            }
            Then("""the queryResult.results#size is equal to 0""") {
                assertThat(queryResult.results.size).isEqualTo(0)
            }
        }
    }
})
